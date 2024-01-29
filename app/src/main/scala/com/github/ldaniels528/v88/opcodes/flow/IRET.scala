package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case object IRET extends Redirect:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ()

