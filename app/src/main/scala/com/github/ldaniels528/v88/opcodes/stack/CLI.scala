package com.github.ldaniels528.v88.opcodes.stack

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case object CLI extends Instruction:
  override def encode: Array[Byte] = i2ba(0xFA)

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = state.flags.IF = false
