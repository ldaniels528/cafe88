package com.github.ldaniels528.v88.opcodes.bitwise

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

case class TEST(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands:
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???


