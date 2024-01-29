package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * ADC - Add With Carry
 * @param dest the destination [[MutableOperand operand]]
 * @param src  the source [[Operand operand]]
 */
case class ADC(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands with Lockable:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    dest.set((if (state.flags.CF) 1 else 0) + (dest + src))


