package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.MutableOperand

case class NEG(dest: MutableOperand) extends Instruction with Lockable:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???
