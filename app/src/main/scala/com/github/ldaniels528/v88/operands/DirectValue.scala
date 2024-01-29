package com.github.ldaniels528.v88.operands

import com.github.ldaniels528.v88.{CPUState, VirtualPC}

/**
 * Represents a direct value (e.g. register or constant; non-memory read)
 */
sealed trait DirectValue extends Operand:
  def value: Int

  override def toCode: String = value.toString

/**
 * Represents a decode-time 8-bit Value
 */
case class ByteValue(value: Int) extends ByteSized with DirectValue:
  override def get(implicit state: CPUState, vpc: VirtualPC): Int = value & 0xFF

  override def toCode: String = f"$value%02Xh"

/**
 * Represents a decode-time 16-bit Value
 */
case class WordValue(value: Int) extends WordSized with DirectValue:
  override def get(implicit state: CPUState, vpc: VirtualPC): Int = value & 0xFFFF

  override def toCode: String = f"$value%04Xh"