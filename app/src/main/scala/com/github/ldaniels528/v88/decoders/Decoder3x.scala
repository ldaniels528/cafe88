package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.opcodes.bitwise.{AAA, AAS}
import com.github.ldaniels528.v88.opcodes.data.OverrideDS
import com.github.ldaniels528.v88.operands.{AL, AX, DS, SS}

/**
 * Decodes instruction codes between 30h and 3Fh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	bits	description 				      comments
 * ---------------------------------------------------------------------------
 * i		5	 	  instruction type          xor=00110, cmp=00111
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
 * .... .... iiii iisc (8 bits)
 * ttrr rmmm iiii iisc (16 bits)
 *
 * ------------------------------------------------------------------
 * instruction				code 		iiiii v s w   tt rrr mmm
 * ------------------------------------------------------------------
 * xor [bx],al				3007		00110 0 0 0   00 000 111
 * xor [bx],ax				3107		00110 0 0 1   00 000 111
 * xor al,[bx]				3207		00110 0 1 0   00 000 111
 * xor ax,[bx]				3307		00110 0 1 1   00 000 111
 *
 * xor al,nn				  34			00110 1 0 0 nn
 * xor ax,nnnn				35			00110 1 0 1 nnnn
 * ss:						    36			00110 1 1 0
 * aaa						    37			00110 1 1 1
 *
 * cmp [bx],al				3807		00111 0 0 0   00 000 111
 * cmp [bx],ax				3907		00111 0 0 1   00 000 111
 * cmp al,[bx]				3A07		00111 0 1 0   00 000 111
 * cmp ax,[bx]				3B07		00111 0 1 1   00 000 111
 *
 * cmp al,nn				  3C		  00111 1 0 0 nn
 * cmp ax,nnnn				3D	  	00111 1 0 1 nnnnn
 * ds:						    3E	  	00111 1 1 0
 * aas						    3F  		00111 1 1 1
 * </pre>
 */
class Decoder3x extends Decoder:
  private val channels = Seq(XOR.apply, CMP.apply)

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val memory = vpc.memory
    ip.nextByte(memory) match
      case 0x34 => XOR(AL, nextValue8(ip))
      case 0x35 => XOR(AX, nextValue16(ip))
      case 0x36 => OverrideDS(SS, decodeNext(ip))
      case 0x37 => AAA
      case 0x3C => CMP(AL, nextValue8(ip))
      case 0x3D => CMP(AX, nextValue16(ip))
      case 0x3E => OverrideDS(DS, decodeNext(ip))
      case 0x3F => AAS
      case codeI8 =>
        decodeTwoOperands(ip, codeI8, codeR8 = ip.nextByte(memory))((i, r, m) => channels(i & 0x01))