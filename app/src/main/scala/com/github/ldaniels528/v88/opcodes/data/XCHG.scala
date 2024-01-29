package com.github.ldaniels528.v88.opcodes.data

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.alu.Lockable
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * Exchanges contents of source and destination.
 * Modifies flags: None.
 * @param dest the destination [[MutableOperand operand]]
 * @param src  the source [[Operand operand]]
 * @example XCHG dest,src
 */
case class XCHG(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands with Lockable:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = 
    val t = dest.get
    dest.set(src)
    src match
      case m: MutableOperand => m.set(t)
      case x => throw IllegalOperandException(x)
