package com.github.ldaniels528.v88.opcodes.bitwise

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

case class ROR(dest: MutableOperand, src: Operand) extends Instruction:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???
