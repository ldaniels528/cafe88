package com.github.ldaniels528.v88.opcodes

import com.github.ldaniels528.v88.operands.Operand

trait OneImmutableOperand extends Instruction:

  def src: Operand

  override def toCode: String = f"$name ${src.toCode}"