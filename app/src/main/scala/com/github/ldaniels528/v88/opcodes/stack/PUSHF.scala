package com.github.ldaniels528.v88.opcodes.stack

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case object PUSHF extends Instruction {
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = 
    vpc.memory.push(state.flags.value)
}
