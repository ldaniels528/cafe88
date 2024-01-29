package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.TEST
import com.github.ldaniels528.v88.opcodes.data.MOV
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.streaming.*
import com.github.ldaniels528.v88.operands.{AL, AX, BoxNear}

/**
 * Decodes instruction codes A0 thru AF.
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits	description 				  comments
 * ---------------------------------------------------------------------------
 * i		4	 	  instruction type
 * j		3		  instruction sub type  000=mov, 001=mov, 010=movs, 011=cmps, 100=test, 101=stos, 110=lods, 111=scas
 * w		1	 	  data width  				  8-bit=0, 16-bit=1
 * d		8/16	data value					  (optional)
 *
 * ---------------------------------------------------------------------------
 * instruction				code 		  iiii jjj w dddd
 * ---------------------------------------------------------------------------
 * mov al,[nnnn]			A0 nnnn		1010 000 0 nnnn
 * mov ax,[nnnn]			A1 nnnn		1010 000 1 nnnn
 * mov [nnnn],al			A2 nnnn		1010 001 0 nnnn
 * mov [nnnn],ax			A3 nnnn		1010 001 1 nnnn
 * movsb					    A4			  1010 010 0
 * movsw					    A5			  1010 010 1
 * cmpsb					    A6			  1010 011 0
 * cmpsw					    A7			  1010 011 1
 * test al,nn				  A8 nn		  1010 100 0 nn
 * test ax,nnnn				A9 nnnn		1010 100 1 nnnn
 * stosb					    AA			  1010 101 0
 * stosw					    AB			  1010 101 1
 * lodsb					    AC			  1010 110 0
 * lodsw					    AD			  1010 110 1
 * scasb					    AE			  1010 111 0
 * scasw					    AF			  1010 111 1
 * </pre>
 */
class DecoderAx extends Decoder:
  private val channels = Seq[(VirtualMemory, MemoryCursor, VirtualPC) => Instruction](
    (m, c, v) => MOV(AL, BoxNear(c.nextByte(m), is8Bit = true)),
    (m, c, v) => MOV(AX, BoxNear(c.nextWord(m), is8Bit = false)),
    (m, c, v) => MOV(BoxNear(c.nextByte(m), is8Bit = true), AL),
    (m, c, v) => MOV(BoxNear(c.nextWord(m), is8Bit = false), AX),
    (m, c, v) => MOVSB,
    (m, c, v) => MOVSW,
    (m, c, v) => CMPSB,
    (m, c, v) => CMPSW,
    { implicit (m, c, v) => TEST(AL, nextByteValue(c)) },
    { implicit (m, c, v) => TEST(AX, nextWordValue(c)) },
    (m, c, v) => STOSB,
    (m, c, v) => STOSW,
    (m, c, v) => LODSB,
    (m, c, v) => LODSW,
    (m, c, v) => SCASB,
    (m, c, v) => SCASW)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    channels(ip.nextByte(memory) & 0x0F)(memory, ip, vpc)
