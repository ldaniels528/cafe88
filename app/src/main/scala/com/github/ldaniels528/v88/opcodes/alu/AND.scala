package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * Performs a bitwise AND between the source and the destination.
 * Flags Affected: OF, SF, ZF, AF, PF, CF.
 * Notes: Works for both signed and unsigned numbers.
 * @param dest the destination [[MutableOperand operand]]
 * @param src  the source [[Operand operand]]
 * @example {{{
 *   AND dest, src
 *      * dest: register or memory
 *      * src: register, memory, or immediate
 * }}}
 * @example AND AX, CX
 * @see [[FlagRegister16]]
 */
case class AND(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands with Lockable:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = dest.set(dest & src)


