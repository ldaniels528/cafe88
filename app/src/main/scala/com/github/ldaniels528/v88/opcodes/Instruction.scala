package com.github.ldaniels528.v88.opcodes

import com.github.ldaniels528.v88.{CPUState, VirtualPC}

/**
 * Represents an 8086 instruction
 */
trait Instruction:

  def name: String = this.getClass.getSimpleName.toLowerCase()

  def encode: Array[Byte] = ???

  def execute()(implicit state: CPUState, vpc: VirtualPC): Unit

  def isLockable: Boolean = false

  def toCode: String = toString.toLowerCase()

