package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.decodeTwoOperands
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.flow.*
import com.github.ldaniels528.v88.opcodes.io.{INT, INT0, INT3}

/**
 * * Decodes instruction codes between C0h and CFh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits		description 				    comments
 * ---------------------------------------------------------------------------
 * t		2			register/reference type		see [[Decoder.decodeBoxReference]]
 * r		3			register/memory reference
 * m		3			instruction sub-code
 * i		6	 		instruction type
 * j		2			instruction sub type		  00/11=N/A, 01=8-bit, 10=16-bit
 * n		16/32	offset						        optional 8- or 16-bit offset
 *
 * Instruction code layout
 * -----------------------------
 * 7654 3210 (8 bits)
 * iiii iijj
 *
 * ---------------------------------------------------------------------------
 * instruction						        code 			iiiiii jj tt rrr mmm 
 * ---------------------------------------------------------------------------
 * (undefined)				 		          C0		  110000 00
 * (undefined)						          C1			110000 01
 * ret nnnn							            C2 			110000 10 nnnn
 * ret								              C3	 		110000 11
 * les ax,[bx]						          C407	  110001 00 00 000 111 	
 * lds ax,[si+nn]					          C544	  110001 01 01 000 100 nn
 * mov byte ptr [bx],nn				      C607	  110001 10 00 000 111 nn
 * mov word ptr [bx],nnnn			      C707	  110001 11 00 000 111 nnnn
 * mov word ptr [bx+si+nnnn],nnnn	  C780	  110001 11 10 000 000 nnnn nnnn
 * (undefined)					 		        C8			110010 00
 * (undefined)							        C9			110010 01
 * retf nnnn				 		            CA 			110010 10 nnnn
 * retf						  		            CB 			110010 11
 * int 3							              CC			110011 00
 * int nn							              CD 			110011 01 nn
 * into								              CE			110011 10
 * iret								              CF			110011 11
 * </pre>
 */
class DecoderCx extends Decoder:
  private val channels: Array[(Int, VirtualMemory, MemoryCursor, VirtualPC) => Instruction] = Array(
    (b, m, c, v) => DB(b),
    (b, m, c, v) => DB(b),
    (b, m, c, v) => RETn(c.nextByte(m)),
    (b, m, c, v) => RET,
    { implicit (b, m, c, v) => decodeTwoOperands(c, codeI8 = b, codeR8 = c.nextByte(m))((i, r, m) => LES.apply) },
    { implicit (b, m, c, v) => decodeTwoOperands(c, codeI8 = b, codeR8 = c.nextByte(m))((i, r, m) => LDS.apply) },
    { implicit (b, m, c, v) => MOV(nextBytePtr(c), nextByteValue(c)) },
    { implicit (b, m, c, v) => MOV(nextWordPtr(c), nextWordValue(c)) },
    (b, m, c, v) => DB(b),
    (b, m, c, v) => DB(b),
    (b, m, c, v) => RETFn(c.nextWord(m)),
    (b, m, c, v) => RETF,
    (b, m, c, v) => INT3,
    (b, m, c, v) => INT(c.nextByte(m)),
    (b, m, c, v) => INT0,
    (b, m, c, v) => IRET)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    val codeI8 = ip.nextByte(memory)
    channels(codeI8 & 0x0F)(codeI8, memory, ip, vpc)