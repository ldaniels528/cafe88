package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.TEST
import com.github.ldaniels528.v88.opcodes.data.{LEA, MOV, XCHG}
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.POP
import com.github.ldaniels528.v88.operands.{MemoryBox, MutableOperand, Operand}
import org.slf4j.LoggerFactory

/**
 * Processes instruction codes from 80h to 8Fh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits	description 				      comments
 * ---------------------------------------------------------------------------
 * i		5	 	  instruction type          
 * s		1		  source/signed				      register=0, reference=1 / 0=negative(-), 1=positive(+)
 * v    1     direct/indirect value     direct=1, indirect=0
 * w		1	 	  memory width  				    8-bit=0, 16-bit=1
 *
 * t		2		  register/reference type		see [[Decoder.decodeBoxReference]]
 * r		3		  register index				    see [[registerByID()]]
 * m		3		  memory reference info		  see [[Decoder.decodeBoxReference]]
 * n		8/16	offset or valye						optional 8- or 16-bit quantity
 *
 * ------------------------------------------------------------------
 * Type B
 * instruction				code 		iiiii v s w   tt rrr mmm	
 * ------------------------------------------------------------------
 * add cx,nnnn				81C1    10000 0 0 1   11 000 001	nnnn
 * or  cx,nnnn				81C9    10000 0 0 1  	11 001 001	nnnn
 * adc cx,nnnn				81D1    10000 0 0 1  	11 010 001	nnnn
 * sbb cx,nnnn				81D9    10000 0 0 1   11 011 001	nnnn
 * and cx,nnnn				81E1    10000 0 0 1  	11 100 001	nnnn
 * sub cx,nnnn				81E9    10000 0 0 1  	11 101 001	nnnn
 * xor cx,nnnn				81F1    10000 0 0 1  	11 110 001	nnnn
 * cmp cx,nnnn				81F9    10000 0 0 1  	11 111 001	nnnn
 *
 * add bx,+nn				  83C3   	10000 0 1 1 	11 000 011	nn
 * or  bx,+nn				  83CB   	10000 0 1 1 	11 001 011	nn
 * adc bx,+nn				  83D3   	10000 0 1 1 	11 010 011 	nn
 * sbb bx,+nn				  83DB   	10000 0 1 1 	11 011 011 	nn
 * and bx,+nn				  83E3   	10000 0 1 1 	11 100 011 	nn
 * sub bx,+nn				  83EB   	10000 0 1 1 	11 101 011 	nn
 * xor bx,+nn				  83F3   	10000 0 1 1 	11 110 011 	nn
 * cmp bx,+nn				  83FB   	10000 0 1 1 	11 111 011 	nn
 *
 * ------------------------------------------------------------------
 * Type A
 * instruction				code 		iiiii v s w   tt rrr mmm	
 * ------------------------------------------------------------------
 * test	al,[bx]				8407		10000 1 0 0   00 000 111 	
 * test	ax,[bx]				8507		10000 1 0 1   00 000 111 	
 * xchg	al,[bx]				8607		10000 1 1 0   00 000 111 	
 * xchg	ax,[bx]				8707		10000 1 1 1   00 000 111
 *
 * test al,cl				  84C1		10000 1 0 0   11 000 001 	
 * test ax,cx				  85C1		10000 1 0 1   11 000 001 	
 * xchg al,cl				  86C1		10000 1 1 0   11 000 001 	
 * xchg ax,cx				  87C1		10000 1 1 1   11 000 001 	
 *
 * ------------------------------------------------------------------
 * Type C
 * instruction				code 		iiiii v s w   tt rrr mmm	
 * ------------------------------------------------------------------
 * mov al,al				  88C0		10001 0 0 0   11 000 000	
 * mov cl,al				  88C1		10001 0 0 0   11 000 001	
 * mov al,cl				  88C8		10001 0 0 0   11 001 000
 *
 * mov ax,ax				  89C0	  10001 0 0 1 	11 000 000	
 * mov ax,cx				  89C8		10001 0 0 1   11 001 000	
 *
 * mov al,[nnnn]			8A0E		10001 0 1 0   00 001 110
 * mov [bx],ax				8907		10001 0 0 1   00 000 111	
 * mov ax,[bx]				8B07		10001 0 1 1   00 000 111
 *
 * mov ax,cs				  8CC8		10001 1 0 0   11 001 000	
 * lea ax,[bx]				8D07		10001 1 0 1   00 000 111 	
 * mov cs,ax				  8EC8		10001 1 1 0   11 001 000	
 * pop [bx]					  8F07		10001 1 1 1   00 000 111	
 * </pre>
 */
class Decoder8x extends Decoder:
  private val factory: Seq[(MutableOperand, Operand) => Instruction] = Seq(
    ADD.apply, OR.apply, ADC.apply, SBB.apply,
    AND.apply, SUB.apply, XOR.apply, CMP.apply)
  private val channels = Seq[(VirtualMemory, MemoryCursor, Int, VirtualPC) => Instruction](
    (m, c, b, v) => decodeA(m, c, b)(v),
    (m, c, b, v) => decodeA(m, c, b)(v),
    (m, c, b, v) => decodeA(m, c, b)(v),
    (m, c, b, v) => decodeA(m, c, b)(v),
    (m, c, b, v) => decodeB(m, c, b, TEST.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, TEST.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, XCHG.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, XCHG.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, MOV.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, MOV.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, MOV.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, MOV.apply)(v),
    (m, c, b, v) => decodeC(m, c, b, MOV.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, LEA.apply)(v),
    (m, c, b, v) => decodeC(m, c, b, MOV.apply)(v),
    (m, c, b, v) => decodeB(m, c, b, {
      case (_, box: MemoryBox) => POP(box)
      case (_, x) => throw IllegalOperandException(x)
    })(v))

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val m = vpc.memory
    val b = ip.nextByte(m)
    channels(b & 0xF)(m, ip, b, vpc)

  private def decodeA(m: VirtualMemory, c: MemoryCursor, b: Int)(implicit v: VirtualPC): Instruction =
    decodeTwoOperands(c, codeI8 = b, codeR8 = c.nextByte(m), forceDirect = true): (i, r, m) =>
      factory(r)

  private def decodeB(m: VirtualMemory, c: MemoryCursor, b: Int, f: (MutableOperand, Operand) => Instruction)(implicit v: VirtualPC): Instruction =
    decodeTwoOperands(c, codeI8 = b, codeR8 = c.nextByte(m))((i, r, m) => f)

  private def decodeC(m: VirtualMemory, c: MemoryCursor, b: Int, f: (MutableOperand, Operand) => Instruction)(implicit v: VirtualPC): Instruction =
    decodeTwoOperands(c, codeI8 = b, codeR8 = c.nextByte(m), forceSegReg = true)((i, r, m) => f)