package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

case class XOR(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands with Lockable:
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???

