package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.decoders.Decoder.decodeTwoOperands
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.*
import com.github.ldaniels528.v88.opcodes.data.DB
import com.github.ldaniels528.v88.{MemoryCursor, VirtualMemory, VirtualPC}

/**
 * Decodes instruction code between D0h and DFh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	size (bits)	description 				      comments
 * ---------------------------------------------------------------------------
 * t		2			      register/memory type		  register=11b
 * j		3			      instruction sub-code
 * r		3			      register/memory reference
 * i		5	 		      instruction type
 * k		1			      function Code
 * s		1			      source type					      'cl'=1, '1'=0
 * c		1	 		      memory class 	 			      8-bit=0, 16-bit=1
 * d		16/32		    offset
 *
 * ---------------------------------------------------------------------------
 * Type A
 * instruction 					      code	tt jjj rrr	iiiii v s w
 * ---------------------------------------------------------------------------
 * shl byte ptr [bx],1        D027	00 100 111	11010 0 0 0
 * rol al,1						        D0C0	11 000 000	11010 0 0 0
 * rol cl,1						        D0C1	11 000 001	11010 0 0 0
 * ror al,1						        D0C8	11 001 000	11010 0 0 0
 * ror cl,1						        D0C9	11 001 001	11010 0 0 0
 * shl al,1						        D0E0	11 100 000	11010 0 0 0
 * shl cl,1						        D0E1	11 100 001	11010 0 0 0
 * shr al,1						        D0E8	11 101 000	11010 0 0 0
 * shr cl,1						        D0E9	11 101 001	11010 0 0 0
 * sar cl,1						        D0F9	11 111 001	11010 0 0 0
 * 
 * sar word ptr [si],1			  D13C	00 111 100	11010 0 0 1
 * shl byte ptr [bx],cl			  D227	00 100 111	11010 0 1 0
 * shl byte ptr [si+nnnn],cl	D2A4	10 100 100	11010 0 1 0 nnnn
 * rcl al,cl					        D2D0	11 010 000	11010 0 1 0
 * rcl cl,cl					        D2D1	11 010 001	11010 0 1 0
 * rcr al,cl					        D2D8	11 011 000	11010 0 1 0
 * rcr cl,cl					        D2D9	11 011 001	11010 0 1 0
 * shl al,cl					        D2E0	11 100 000	11010 0 1 0
 * shl cl,cl					        D2E1	11 100 001	11010 0 1 0
 * shl ax,cl					        D3E0	11 100 000	11010 0 1 1
 *
 * ---------------------------------------------------------------------------
 * Type B
 * instruction 					      code	tt rrr mmm 	iiiii j kk
 * ---------------------------------------------------------------------------
 * aam 							          D40A	00 001 010	11010 1 00
 * aad							          D50A	00 001 010	11010 1 01
 * (undefined)					      D6				        11010 1 10
 * xlat							          D7				        11010 1 11
 * </pre>
 */
class DecoderDx extends Decoder:
  private val channels = Seq(SHL.apply, RCR.apply, ROR.apply)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    ip.nextByte(memory) match
      case 0xD4 => AAM
      case 0xD5 => AAD
      case 0xD6 => DB(0xD6)
      case 0xD7 => XLAT
      case codeI8 =>
        decodeTwoOperands(ip, codeI8, codeR8 = ip.nextByte(memory))((i, r, m) => channels(i & 0x01))
