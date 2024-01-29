package com.github.ldaniels528.v88.opcodes.data

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * Moves data from the source to destination
 * @param dest the destination [[MutableOperand operand]]
 * @param src  the source [[Operand operand]]
 */
case class MOV(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = dest.set(src)

