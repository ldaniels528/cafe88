package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.{decodeNext, decodeTwoOperands, nextValue16, nextValue8}
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.{DAA, DAS}
import com.github.ldaniels528.v88.opcodes.data.OverrideDS
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.operands.{AL, AX, CS, ES}

/**
 * Decodes instruction codes between 20h and 2Fh
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
 * .... .... iiii iisc ( 8 bits)
 * ttrr rmmm iiii iisc (16 bits)
 *
 * ------------------------------------------------------------------
 * instruction				code 		iiiii v s w   tt rrr mmm
 * ------------------------------------------------------------------
 * and	[bx],al				2007		00100 0 0 0   00 000 111 	
 * and	[bx],ax				2107		00100 0 0 1   00 000 111 	
 * and	al,[bx]				2207		00100 0 1 0   00 000 111 	
 * and	ax,[bx]				2307		00100 0 1 1   00 000 111
 * 
 * and	al,nn				  24			00100 1 0 0 nn
 * and	ax,nnnn				25			00100 1 0 1 nnnn
 * es:						    26			00100 1 1 0
 * daa						    27			00100 1 1 1
 * 
 * sub	[bx],al				2807		00101 0 0 0   00 000 111 	
 * sub	[bx],ax				2907		00101 0 0 1   00 000 111 	
 * sub	al,[bx]				2A07		00101 0 1 0   00 000 111 	
 * sub	ax,[bx]				2B07		00101 0 1 1   00 000 111 	
 * 
 * sub	al,nn				  2C			00101 1 0 0 nn
 * sub	ax,nnnn				2D			00101 1 0 1 nnnnn
 * cs:						    2E			00101 1 1 0
 * das						    2F			00101 1 1 1
 * </pre>
 */
class Decoder2x extends Decoder:
  private val channels = Seq(AND.apply, SUB.apply)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    ip.nextByte(memory) match
      case 0x24 => AND(AL, nextValue8(ip))
      case 0x25 => AND(AX, nextValue16(ip))
      case 0x26 => OverrideDS(ES, decodeNext(ip))
      case 0x27 => DAA
      case 0x2C => SUB(AL, nextValue8(ip))
      case 0x2D => SUB(AX, nextValue16(ip))
      case 0x2E => OverrideDS(CS, decodeNext(ip))
      case 0x2F => DAS
      case codeI8 =>
        decodeTwoOperands(ip, codeI8, codeR8 = ip.nextByte(memory))((i, r, m) => channels(i & 0x01))