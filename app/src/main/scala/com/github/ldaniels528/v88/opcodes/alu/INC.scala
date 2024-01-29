package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.getRegisterID
import com.github.ldaniels528.v88.opcodes.{Instruction, OneMutableOperand}
import com.github.ldaniels528.v88.operands.{MutableOperand, Register}

/**
 * Increment by 1
 * <p/>Syntax:	inc	op
 * <p/>op: register or memory
 * <p/>Action: op = op + 1
 * <p/>Flags Affected: OF, SF, ZF, AF, PF
 */
case class INC(dest: MutableOperand) extends OneMutableOperand with Lockable:
  override def encode: Array[Byte] = dest match
      case r: Register => i2ba(0x40 | getRegisterID(r))
      case x => throw IllegalOperandException(x)

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = dest.add(1)

