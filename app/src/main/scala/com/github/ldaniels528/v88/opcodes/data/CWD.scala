package com.github.ldaniels528.v88.opcodes.data

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case object CWD extends Instruction {
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???
}

