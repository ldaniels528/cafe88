package com.github.ldaniels528.v88.opcodes.bitwise

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.TwoOperands
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

case class SHL(dest: MutableOperand, src: Operand) extends TwoOperands:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???
