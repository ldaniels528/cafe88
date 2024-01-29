package com.github.ldaniels528.v88.opcodes

import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * Represents a two-operand opcode
 */
trait TwoOperands extends Instruction:

  /**
   * @return the destination [[MutableOperand operand]]
   */
  def dest: MutableOperand

  /**
   * @return the source [[Operand operand]]
   */
  def src: Operand

  override def toCode: String = f"$name ${dest.toCode},${src.toCode}"