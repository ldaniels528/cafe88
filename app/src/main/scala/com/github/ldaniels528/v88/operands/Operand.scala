package com.github.ldaniels528.v88.operands

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.VirtualMemory.toAddress
import com.github.ldaniels528.v88.decoders.OpCode

import scala.annotation.targetName

/**
 * Represents an Operand
 */
trait Operand:
  def bits: Int = if (is8Bit) 8 else 16

  def get(implicit state: CPUState, vpc: VirtualPC): Int

  def is8Bit: Boolean

  def toCode: String = toString.toLowerCase()

  /**
   * Sums the two operand values and updates the flags.
   * Flags Affected: OF, SF, ZF, AF, PF, CF.
   * @param src the given secondary operand
   * @return the sum of the operands
   */
  @targetName("_add")
  def +(src: Operand)(implicit state: CPUState, vpc: VirtualPC): Int =
    val (value1, value0) = (this.get, src.get)
    val (addValue, _orValue) = (value0 + value1, value0 | value1)
    updateFlagsA(value1, value0, addValue, _orValue)
    addValue

  /**
   * Computes the difference between the two operand values and updates the flags.
   * Flags Affected: OF, SF, ZF, AF, PF, CF.
   * @param src the given secondary operand
   * @return the difference of the operands
   */
  @targetName("_sub")
  def -(src: Operand)(implicit state: CPUState, vpc: VirtualPC): Int =
    val (value1, value0) = (this.get, src.get)
    val andValue = value0 & value1
    val subValue = value0 - value1
    updateFlagsA(value1, value0, andValue, subValue)
    subValue

  /**
   * Performs a bitwise OR on the two operand values and updates the flags
   * Flags Affected: OF, SF, ZF, AF, PF, CF.
   * @param src the given secondary operand
   * @return the result of the operands
   */
  @targetName("_or")
  def |(src: Operand)(implicit state: CPUState, vpc: VirtualPC): Int =
    val (value1, value0) = (this.get, src.get)
    val _orValue = value0 | value1
    updateFlagsB(value1, value0, _orValue, _orValue)
    _orValue

  /**
   * Performs a bitwise AND on the two operand values and updates the flags
   * Flags Affected: OF, SF, ZF, AF, PF, CF.
   * @param src the given secondary operand
   * @return the result of the operands
   */
  @targetName("_and")
  def &(src: Operand)(implicit state: CPUState, vpc: VirtualPC): Int =
    val (value1, value0) = (this.get, src.get)
    val andValue = value1 & value0
    updateFlagsB(value1, value0, andValue, andValue)
    andValue

  private def updateFlagsA(value1: Int, value0: Int, andValue: Int, subValue: Int)
                          (implicit state: CPUState, vpc: VirtualPC): Unit =
    state.flags.AF = determineAuxiliaryState(value0, value1)
    state.flags.CF = andValue != subValue
    updateFlagsB(value1, value0, andValue, subValue)

  private def updateFlagsB(value1: Int, value0: Int, andValue: Int, subValue: Int)
                          (implicit state: CPUState, vpc: VirtualPC): Unit =
    state.flags.OF = isBitSet(andValue, bits - 1)
    state.flags.PF = determineParityState(subValue)
    state.flags.SF = isBitSet(andValue, bits - 1)
    state.flags.ZF = subValue == 0

/**
 * Represents an 8-bit quantity
 */
trait ByteSized extends Operand:
  override def is8Bit: Boolean = true

/**
 * Represents a 16-bit quantity
 */
trait WordSized extends Operand:
  override def is8Bit: Boolean = false

