package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.decoders.Decoder.registerByID
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.opcodes.stack.{POP, PUSH}
import com.github.ldaniels528.v88.operands.MutableOperand
import com.github.ldaniels528.v88.{MemoryCursor, VirtualMemory, VirtualPC}

/**
 * Base Decoder for single-register-argument opcodes (e.g. INC, DEC, PUSH and POP)
 */
trait BaseDecoderTypeA extends Decoder:
  def channels: Seq[MutableOperand => Instruction]

  override def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction =
    val code8 = ip.nextByte(vpc.memory)
    val regIndex = code8 & 0x07 //       | 0000.0111
    val opIndex = (code8 & 0x08) >> 3 // | 0000.1000
    channels(opIndex)(registerByID(regIndex, is8Bit = false))