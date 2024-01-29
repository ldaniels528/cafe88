package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.{decodeTwoOperands, nextValue16, nextValue8}
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.{AL, AX, DS, SS}

/**
 * Decodes instruction codes between 10h adc 1Fh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits	description 				comments
 * ---------------------------------------------------------------------------
 * t		2		register/reference type		see X86AddressReferenceTypes
 * r		3		register info				see X86RegisterReferences
 * m		3		memory reference info		see X86AddressReferenceTypes
 * i		6	 	instruction type
 * s		1		source/signed				register=0,reference=1 / 1='+',0='-'
 * c		1	 	memory class  				8-bit=0, 16-bit=1
 * d		16/32	offset						(optional)
 *
 * Instruction code layout
 * ------------------------------
 * fedc ba98 7654 3210
 *           iiii iisc ( 8 bits)
 * ttrr rmmm iiii iisc (16 bits)
 *
 * ------------------------------------------------------------------
 * instruction				code 		iiiii v s w   tt rrr mmm	
 * ------------------------------------------------------------------
 * adc	[bx],al				1007	  00010 0 0 0   00 000 111 	
 * adc	[bx],ax				1107	  00010 0 0 1   00 000 111 	
 * adc	al,[bx]				1207	  00010 0 1 0   00 000 111 	
 * adc	ax,[bx]				1307	  00010 0 1 1   00 000 111 	
 * 
 * adc	al,nn				  14		  00010 1 0 0 nn
 * adc	ax,nnnn				15		  00010 1 0 1 nnnnn
 * push ss					  16		  00010 1 1 0
 * pop ss					    17			00010 1 1 1
 * 
 * sbb	[bx],al				1807		00011 0 0 0   00 000 111 	
 * sbb	[bx],ax				1907		00011 0 0 1   00 000 111 	
 * sbb	al,[bx]				1A07		00011 0 1 0   00 000 111 	
 * sbb	ax,[bx]				1B07		00011 0 1 1   00 000 111 	
 * 
 * sbb	al,nn			  	1C			00011 1 0 0 nn
 * sbb	ax,nnnn				1D			00011 1 0 1 nnnnn
 * push ds					  1E			00011 1 1 0
 * pop ds					    1F			00011 1 1 1
 * </pre>
 */
class Decoder1x extends Decoder:
  private val channels = Seq(ADC.apply, SBB.apply)
  
  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    ip.nextByte(memory) match
      case 0x14 => ADC(AL, nextValue8(ip))
      case 0x15 => ADC(AX, nextValue16(ip))
      case 0x16 => PUSH(SS)
      case 0x17 => POP(SS)
      case 0x1C => SBB(AL, nextValue8(ip))
      case 0x1D => SBB(AX, nextValue16(ip))
      case 0x1E => PUSH(DS)
      case 0x1F => POP(DS)
      case codeI8 =>
        decodeTwoOperands(ip, codeI8, codeR8 = ip.nextByte(memory))((i, r, m) => channels(i & 0x01))
