package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.operands.Operand

/**
 * Illegal Operand Exception
 * @param operand the illegal [[Operand operand]]
 */
case class IllegalOperandException(operand: Operand) 
  extends RuntimeException(s"Illegal operand '${operand.toCode}'")
