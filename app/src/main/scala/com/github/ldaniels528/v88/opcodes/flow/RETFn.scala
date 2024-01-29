package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

case class RETFn(value: Int) extends Redirect:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ()
