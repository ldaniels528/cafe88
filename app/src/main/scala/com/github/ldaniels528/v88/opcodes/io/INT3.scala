package com.github.ldaniels528.v88.opcodes.io

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case object INT3 extends Instruction:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = 
    vpc.raise(interruptNum = 3)


