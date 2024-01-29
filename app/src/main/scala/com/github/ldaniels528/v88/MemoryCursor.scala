package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.VirtualMemory.{toAddress, toWord}

/**
 * Represents a Memory Cursor
 * @param address the provided physical memory address
 */
case class MemoryCursor(var address: Int):

  @inline def nextByte(memory: VirtualMemory): Int =
    val value = memory(address)
    address += 1
    value

  @inline def nextByteOrWord(memory: VirtualMemory, is8Bit: Boolean): Int =
    if (is8Bit) nextByte(memory) else nextWord(memory)

  @inline def nextSignedByte(memory: VirtualMemory): Int = nextByte(memory) & 0x7F

  @inline def nextSignedWord(memory: VirtualMemory): Int = nextWord(memory) & 0x7FFF

  @inline def nextWord(memory: VirtualMemory): Int = toWord(lo = nextByte(memory), hi = nextByte(memory))

/**
 * Memory Cursor Companion
 */
object MemoryCursor:

  /**
   * Creates a new memory cursor
   * @param segment the provided memory segment
   * @param offset  the provided memory offset 
   * @return a new [[MemoryCursor memory cursor]]
   */
  def apply(segment: Int, offset: Int): MemoryCursor =
    MemoryCursor(address = toAddress(segment, offset))

