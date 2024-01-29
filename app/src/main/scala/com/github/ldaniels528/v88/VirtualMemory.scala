package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.VirtualMemory.{toAddress, toWord}
import com.github.ldaniels528.v88.operands.{IndexRegister, SegmentRegister}
import org.slf4j.LoggerFactory

import java.lang

/**
 * Represents Virtual Random Access Memory
 * @param rawBytes the byte array representing the virtual PC's RAM
 */
class VirtualMemory(rawBytes: Array[Byte]):

  @inline def apply(address: Int): Int = getByte(address)

  @inline def getByte(address: Int): Int = rawBytes(address).toInt & 0xFF

  @inline def getByte(segment: Int, offset: Int): Int = getByte(toAddress(segment, offset))

  @inline
  def getByte(r0: SegmentRegister, r1: IndexRegister)(implicit state: CPUState, m: VirtualPC): Int =
    getByte(segment = r0.get, offset = r1.get)

  @inline
  def getByteOrWord(address: Int, is8Bit: Boolean): Int = if (is8Bit) getByte(address) else getWord(address)

  @inline def getWord(from: Int): Int = toWord(lo = apply(from), hi = apply(from + 1))

  @inline
  def getWord(r0: SegmentRegister, r1: IndexRegister)(implicit state: CPUState, m: VirtualPC): Int =
    getWord(toAddress(r0.get, r1.get))

  @inline def getWord(segment: Int, offset: Int): Int = getWord(toAddress(segment, offset))

  /**
   * Copies the specified number of bytes from the provided source segment and offset to the 
   * provided destination segment and offset within memory.
   * @param srcSegment the given source segment
   * @param srcOffset  the given source offset
   * @param dstSegment the given destination segment
   * @param dstOffset  the given destination offset
   * @param count      the specified number of bytes
   */
  def intraCopy(srcSegment: Int, srcOffset: Int, dstSegment: Int, dstOffset: Int, count: Int): Unit =
    // compute the source and destination positions within the array
    val srcPos = toAddress(srcSegment, srcOffset)
    val destPos = toAddress(dstSegment, dstOffset)
    // copy the data
    System.arraycopy(rawBytes, srcPos, rawBytes, destPos, count)

  @inline def length: Int = rawBytes.length

  def loadImage(image: Array[Byte], segment: Int, offset: Int): Unit =
    System.arraycopy(image, 0, rawBytes, toAddress(segment, offset), image.length)

  def pop()(implicit state: CPUState): Int =
    // return the value from the pointer at SS:[SP]
    val value16 = getWord(state.ss.value, state.sp.value)
    // increment the stack pointer
    state.sp.value += 2
    value16

  def push(value16: Int)(implicit state: CPUState): Unit =
    // decrement the stack pointer
    state.sp.value -= 2
    // set the value of the pointer at SS:[SP]
    setWord(state.ss.value, state.sp.value, value16)

  @inline def setByte(address: Int, value: Int): Unit = rawBytes(address) = value.toByte

  @inline def setByte(segment: Int, offset: Int, value: Int): Unit = setByte(toAddress(segment, offset), value)

  @inline
  def setByte(r0: SegmentRegister, r1: IndexRegister, value8: Int)(implicit state: CPUState, m: VirtualPC): Unit =
    setByte(segment = r0.get, offset = r1.get, value8)

  @inline
  def setByteOrWord(address: Int, value: Int, is8Bit: Boolean): Unit =
    if (is8Bit) setByte(address, value) else setWord(address, value)

  @inline
  def setWord(r0: SegmentRegister, r1: IndexRegister, value16: Int)(implicit state: CPUState, m: VirtualPC): Unit =
    setWord(segment = r0.get, offset = r1.get, value16)

  @inline
  def setWord(address: Int, value: Int): Unit =
    // split the value into two bytes (hi:lo)
    val (hi, lo) = ((value >> 8) & 0xFF, value & 0xFF)
    // set the bytes
    setByte(address + 0, lo)
    setByte(address + 1, hi)

  @inline
  def setWord(segment: Int, offset: Int, value: Int): Unit = setWord(toAddress(segment, offset), value)

  def slice(from: Int, to: Int): Array[Byte] =
    val size = to - from
    if (size < 1) Array.empty else {
      val bytes = new Array[Byte](size)
      System.arraycopy(rawBytes, from, bytes, 0, size)
      bytes
    }

object VirtualMemory:

  def apply(size: Int = toAddress(segment = 0xFFFF, offset = 0xFFFF)): VirtualMemory =
    new VirtualMemory(new Array[Byte](size))

  /**
   * Splits the 16-bit value into two 8-bit values (high, low)
   * @param value16 the 16-bit value (e.g. 0xA0C0)
   * @return a tuple containing two 8-bit values (high, low) of the 16-bit value
   */
  @inline def fromWord(value16: Int): (Int, Int) = ((value16 >> 8) & 0xFF, value16 & 0xFF)

  /**
   * Returns the 20-bit memory address that corresponds to the provided segment and offset
   * @param segment the provided segment (e.g. 0xA000)
   * @param offset  the provided offset (e.g. 0xBCDE)
   * @return the 20-bit memory address
   */
  @inline def toAddress(segment: Int, offset: Int): Int = (segment << 4) | offset

  @inline def toWord(hi: Int, lo: Int): Int = ((hi & 0xFF) << 8) | (lo & 0xFF)
