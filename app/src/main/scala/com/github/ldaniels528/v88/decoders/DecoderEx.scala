package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.operands.{AL, AX, DX}

/**
 * Decodes instruction codes between E0h and EFh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits		description 			comments
 * ---------------------------------------------------------------------------
 * i		4	 		instruction type
 * v		1			constant value			0=constant,1=non-constant
 * x		1			??unknown??
 * d		1			data direction			0=IN, 1=OUT
 * c		1	 		m class  			8-bit=0, 16-bit=1
 *
 * Instruction code layout
 * -----------------------------
 * 7654 3210 (8 bits)
 * iiii vxdc
 *
 * ---------------------------------------------------------------------------
 * instruction				code 	iiii v x d c
 * ---------------------------------------------------------------------------
 * loopnz nn				E0 		1110 0 0 0 0 nn
 * loopz nn					E1 		1110 0 0 0 1 nn
 * loop nn					E2 		1110 0 0 1 0 nn
 * jcxz nn					E3 		1110 0 0 1 1 nn
 * in  al,nn				E4 		1110 0 1 0 0 nn
 * in  ax,nn				E5 		1110 0 1 0 1 nn
 * out nn,al				E6 		1110 0 1 1 0 nn
 * out nn,ax				E7 		1110 0 1 1 1 nn
 * call nnnn				E8 		1110 1 0 0 0 nnnn
 * jmp nnnn					E9 		1110 1 0 0 1 nnnn
 * jmp nnnn:nnnn		EA 		1110 1 0 1 0 nnnn nnnn
 * jmp nn					  EB 		1110 1 0 1 1 nn
 * in  al,dx				EC		1110 1 1 0 0
 * in  ax,dx				ED		1110 1 1 0 1
 * out dx,al				EE		1110 1 1 1 0
 * out dx,ax				EF		1110 1 1 1 1
 * </pre>
 */
class DecoderEx extends Decoder:
  private val channels = Seq[(VirtualMemory, MemoryCursor, VirtualPC) => Instruction](
    (m, c, v) => LOOPNZ(delta = c.nextByte(m)),
    (m, c, v) => LOOPZ(delta = c.nextByte(m)),
    (m, c, v) => LOOP(delta = c.nextByte(m)),
    (m, c, v) => JCXZ(delta = c.nextByte(m)),
    { implicit (m, c, v) => IN(AL, nextByteValue(c)) },
    { implicit (m, c, v) => IN(AX, nextByteValue(c)) },
    { implicit (m, c, v) => OUT(nextByteValue(c), AL) },
    { implicit (m, c, v) => OUT(nextByteValue(c), AX) },
    (m, c, v) => CALL.Near(delta = c.nextWord(m)),
    (m, c, v) => JMP.Near(delta = c.nextWord(m)),
    (m, c, v) => JMP.Far(segment = c.nextWord(m), offset = c.nextWord(m)),
    (m, c, v) => JMP.Small(delta = c.nextByte(m)),
    (m, c, v) => IN(AL, DX),
    (m, c, v) => IN(AX, DX),
    (m, c, v) => OUT(DX, AL),
    (m, c, v) => OUT(DX, AX))

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    channels(ip.nextByte(memory) & 0x0F)(memory, ip, vpc)
