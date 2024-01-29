package com.github.ldaniels528.v88.opcodes.data

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

case class LDS(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands:
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???

