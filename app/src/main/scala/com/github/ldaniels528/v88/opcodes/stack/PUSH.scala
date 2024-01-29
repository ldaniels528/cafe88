package com.github.ldaniels528.v88.opcodes.stack

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.getRegisterID
import com.github.ldaniels528.v88.opcodes.{Instruction, OneImmutableOperand}
import com.github.ldaniels528.v88.operands.*

case class PUSH(src: Operand) extends Instruction with OneImmutableOperand:
  override def encode: Array[Byte] =
    src match
      case CS => i2ba(0x0E)
      case DS => i2ba(0x1E)
      case ES => i2ba(0x06)
      case SS => i2ba(0x16)
      case m: MemoryBox => Array(0xFF, m.encode).map(_.toByte)
      case r: Register => i2ba(0x50 | getRegisterID(r))

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = vpc.memory.push(src.get)

