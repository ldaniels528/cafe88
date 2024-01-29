package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.MutableOperand

/**
 * Processes instruction codes between 50h and 5Fh
 * <pre>
 * ---------------------------------------------------------------------------
 * type	size		description 			comments
 * ---------------------------------------------------------------------------
 * i		5-bit 	instruction type  push=01010, pop=01011
 * r		3-bit		register index		see [[registerByID()]]
 *
 * Instruction code layout
 * -----------------------
 * 7654 3210 (8 bits)
 * iiii irrr
 *
 * ---------------------------------------------------------------------------
 * instruction		code 	iiiii rrr
 * ---------------------------------------------------------------------------
 * push ax				50		01010 000
 * push cx				51		01010 001
 * push dx				52		01010 010
 * push bx				53		01010 011
 * push sp				54		01010 100
 * push bp				55		01010 101
 * push si				56		01010 110
 * push di				57		01010 111
 *
 * pop ax			  	58		01011 000
 * pop cx				  59		01011 001
 * pop dx				  5A		01011 010
 * pop bx				  5B		01011 011
 * pop sp				  5C		01011 100
 * pop bp				  5D		01011 101
 * pop si				  5E		01011 110
 * pop di				  5F		01011 111
 * </pre>
 */
class Decoder5x extends BaseDecoderTypeA:
  override def channels: Seq[MutableOperand => Instruction] = Seq(PUSH.apply, POP.apply)


