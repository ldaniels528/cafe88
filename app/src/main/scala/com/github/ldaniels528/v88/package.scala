package com.github.ldaniels528

package object v88 {

  val i2ba: Int => Array[Byte] = i => Array(i.toByte)

  /**
   * It indicates when an arithmetic carry or borrow has been generated out of the four least significant bits,
   * or lower nibble.<p/>
   * The Auxiliary flag is set (to 1) if there is a carry from the low nibble (lowest four bits)
   * to the high nibble (upper four bits), or a borrow from the high nibble to the low nibble, in the low-order
   * 8-bit portion of an addition or subtraction operation. Otherwise, if no such carry or borrow occurs,
   * the flag is cleared (reset to 0).
   * @param value0 the given primary data
   * @param value1 the given secondary data
   * @return the state of the auxiliary (adjust) flag
   */
  private def determineAuxiliaryState(value0: Int, value1: Int) = {
    val valueA = value0 & 0x0F
    val valueB = value1 & 0x0F
    val sum = valueA + valueB
    val _or = valueA | valueB
    sum == _or
  }

  /**
   * The parity flag reflects the parity only of the least significant byte of the result,
   * and is set if the number of set bits of ones is even.
   * @param diff the difference of the primary and secondary values
   * @return the state of the parity bit
   */
  def determineParityState(diff: Int): Boolean = {
    val lsb = diff & 0xFF
    var count = 0
    for (n <- 0 until 8) {
      val mask = 1 << n
      if ((lsb & mask) > 0) count += 1
    }
    count % 2 == 0
  }

  /**
   * Indicates whether the bit is set for the given value and specified bit number
   * @param value the given value to check
   * @param nth   the specified bit number (starts at zero)
   * @return true, if the nth bit is set.
   */
  def isBitSet(value: Int, nth: Int): Boolean = (value & turnBitOnMask(nth)) > 0

  /**
   * Generates a masking for retrieving (ANDing) the status
   * of the given bit number
   * @param nth the given bit number
   * @return the generated AND mask
   */
  def turnBitOnMask(nth: Int): Int = 1 << nth

  /**
   * Generates a masking for retrieving (ORing) the status
   * of the given bit number
   * @param size the size (in bits) of the value being operated on
   * @param nth  the given bit number
   * @return the generated OR mask
   */
  def turnBitOffMask(size: Int, nth: Int): Int = ((1 << size) - 1) - (1 << nth)

}
