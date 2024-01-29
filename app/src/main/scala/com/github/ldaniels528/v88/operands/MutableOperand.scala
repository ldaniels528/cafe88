package com.github.ldaniels528.v88.operands

import com.github.ldaniels528.v88.{CPUState, VirtualPC}

/**
 * Represents a mutable operand that can be read via [[get]] or written to via [[set()]]
 */
trait MutableOperand extends Operand:
  def add(delta: Int)(implicit state: CPUState, vpc: VirtualPC): Unit = set(get + delta)

  def set(value: Int)(implicit state: CPUState, vpc: VirtualPC): Unit

  def set(operand: Operand)(implicit state: CPUState, vpc: VirtualPC): Unit
  
