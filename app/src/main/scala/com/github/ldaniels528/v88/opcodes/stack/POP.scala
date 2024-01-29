package com.github.ldaniels528.v88.opcodes.stack

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.getRegisterID
import com.github.ldaniels528.v88.opcodes.{Instruction, OneMutableOperand}
import com.github.ldaniels528.v88.operands.*

case class POP(dest: MutableOperand) extends Instruction with OneMutableOperand:
  override def encode: Array[Byte] =
    dest match
      case CS => i2ba(0x0F)
      case DS => i2ba(0x1F)
      case ES => i2ba(0x07)
      case SS => i2ba(0x17)
      case m: MemoryBox => Array(0x8F, m.encode).map(_.toByte)
      case r: Register => i2ba(0x58 | getRegisterID(r))

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    dest.set(vpc.memory.pop())

