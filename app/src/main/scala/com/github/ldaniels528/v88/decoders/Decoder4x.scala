package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.IllegalOperandException
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.alu.*
import com.github.ldaniels528.v88.operands.MutableOperand

/**
 * Decodes instruction codes between 40h and 4Fh 
 * <pre>
 * ---------------------------------------------------------------------------
 * type	size		description 			comments
 * ---------------------------------------------------------------------------
 * i		5-bit 	instruction type  inc=01000,dec=01001
 * r		3-bit		register index		ax=000, cx=001
 *
 * Instruction code layout
 * -----------------------
 * 7654 3210 (8 bits)
 * iiii irrr 
 *
 * ---------------------------------------------------------------------------
 * instruction	code 	iiiii rrr 
 * ---------------------------------------------------------------------------
 * inc ax				40		01000 000
 * inc cx				41		01000 001
 * inc dx				42		01000 010
 * inc bx				43		01000 011
 * inc sp				44		01000 100
 * inc bp				45		01000 101
 * inc si				46		01000 110
 * inc di				47		01000 111
 * 
 * dec ax				48		01001 000
 * dec cx				49		01001 001
 * dec dx				4A		01001 010
 * dec bx				4B		01001 011
 * dec sp				4C		01001 100
 * dec bp				4D		01001 101
 * dec si				4E		01001 110
 * dec di				4F		01001 111
 * </pre>
 */
class Decoder4x extends BaseDecoderTypeA:
  override def channels: Seq[MutableOperand => Instruction] = Seq(INC.apply, DEC.apply)
  

