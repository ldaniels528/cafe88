package com.github.ldaniels528.v88.opcodes.bitwise

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case object AAS extends Instruction:
  override def encode: Array[Byte] = i2ba(0x3F)

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ()

