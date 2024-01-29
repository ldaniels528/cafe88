package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.MutableOperand

/**
 * Represents an instruction that can be used with [[LOCK]]
 */
trait Lockable extends Instruction:
  
  /**
   * @return the destination [[MutableOperand operand]] 
   */
  def dest: MutableOperand

  override def isLockable: Boolean = true