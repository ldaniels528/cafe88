package com.github.ldaniels528.v88.opcodes

import com.github.ldaniels528.v88.operands.MutableOperand

/**
 * Represents a Single operand argument instruction
 */
trait OneMutableOperand extends Instruction:

  def dest: MutableOperand

  override def toCode: String = f"$name ${dest.toCode}"


