package com.github.ldaniels528.v88.opcodes.stack

import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.{CPUState, VirtualMemory, VirtualPC}

case object POPF extends Instruction:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    state.flags.value = vpc.memory.pop()
