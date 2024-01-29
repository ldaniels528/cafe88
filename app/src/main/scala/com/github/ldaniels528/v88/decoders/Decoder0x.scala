package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.{AL, AX, CS, ES}

/**
 * Decodes instructions code between 00h and 0Fh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits	description 				      comments
 * ---------------------------------------------------------------------------
 * i		5	 	  instruction type          add=00000, or=00001
 * s		1		  source/signed				      register=0, reference=1 / 0=negative(-), 1=positive(+)
 * v    1     direct/indirect value     direct=1, indirect=0
 * w		1	 	  memory width  				    8-bit=0, 16-bit=1
 *
 * t		2		  register/reference type		see [[Decoder.decodeBoxReference]]
 * r		3		  register index				    see [[registerByID()]]
 * m		3		  memory reference info		  see [[Decoder.decodeBoxReference]]
 * n		8/16	offset or valye						optional 8- or 16-bit quantity
 *
 * Instruction code layout
 * ------------------------------
 * fedc ba98 7654 3210
 * iiii ivsw           (8 bits)
 * iiii ivsw ttrr rmmm (16 bits)
 *
 * ------------------------------------------------------------------
 * instruction				code 		iiiii v s w   tt rrr mmm
 * ------------------------------------------------------------------
 * add [bx],al				0007		00000 0 0 0   00 000 111
 * add [bx],ax				0107		00000 0 0 1   00 000 111
 * add al, [bx]			  0207		00000 0 1 0   00 000 111	
 * add ax, [bx]			  0307		00000 0 1 1   00 000 111
 * 
 * add al,nn				  04   	  00000 1 0 0 nn
 * add ax,nnnn				05    	00000 1 0 1 nnnn
 * push es					  06			00000 1 1 0
 * pop es					    07			00000 1 1 1
 * 
 * or	[bx],al				  0807		00001 0 0 0   00 000 111 	
 * or	[bx],ax				  0907		00001 0 0 1   00 000 111 	
 * or	al, [bx]			  0A07		00001 0 1 0   00 000 111 	
 * or	ax, [bx]			  0B07		00001 0 1 1   00 000 111
 * 
 * or al,nn				    0C			00001 1 0 0 nn
 * or ax,nnnn				  0D			00001 1 0 1 nnnn
 * push cs					  0E			00001 1 1 0
 * pop cs					    0F			00001 1 1 1
 */
class Decoder0x extends Decoder:
  private val channels = Seq(ADD.apply, OR.apply)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    ip.nextByte(memory) match
      case 0x04 => ADD(AL, nextValue8(ip))
      case 0x05 => ADD(AX, nextValue16(ip))
      case 0x06 => PUSH(ES)
      case 0x07 => POP(ES)
      case 0x0C => OR(AL, nextValue8(ip))
      case 0x0D => OR(AX, nextValue16(ip))
      case 0x0E => PUSH(CS)
      case 0x0F => POP(CS)
      case codeI8 =>
        decodeTwoOperands(ip, codeI8, codeR8 = ip.nextByte(memory))((i, r, m) => channels(i & 0x01))
