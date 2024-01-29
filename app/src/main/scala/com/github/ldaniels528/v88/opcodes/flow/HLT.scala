package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case object HLT extends Instruction:
  override def encode: Array[Byte] = i2ba(0xF4)

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    state.isRunning = false
