package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.VirtualMemory.toAddress
import com.github.ldaniels528.v88.opcodes.{Instruction, OneImmutableOperand}
import com.github.ldaniels528.v88.operands.{DS, Operand}

/**
 * Jump Computation logic
 */
trait JumpComputation extends Instruction:
  private val signs = Array(1, -1)

  def signed8(value: Int): Int = signs((value & 0x80) >> 8) * (value & 0x7F)

  def signed16(value: Int): Int = signs((value & 0x8000) >> 16) * (value & 0x7FFF)

/**
 * Far Jump Computation logic
 */
trait FarJumpComputation extends JumpComputation

object FarJumpComputation:
  def callTo(segment: Int, offset: Int)(implicit state: CPUState, vpc: VirtualPC): CPUState =
    // save CS:IP to the stack
    val memory = vpc.memory
    memory.push(state.cs.value)
    memory.push(state.ip.address)

    // setup the jump
    state.cs.value = segment
    state.ip.address = toAddress(segment, offset)
    state

/**
 * Far Jump (20-bit address) Computation logic
 */
trait FarJumpOffsetComputation extends FarJumpComputation:

  def segment: Int

  def offset: Int

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    state.cs.value = segment
    state.ip.address = toAddress(segment, offset)

  override def toCode: String = f"jmp far $segment%04X:$offset%04X"

/**
 * Far Jump (20-bit address) Operand Computation logic
 */
trait FarJumpOperandComputation extends FarJumpComputation with OneImmutableOperand:

  def src: Operand

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    val (segment, offset) = getSegmentAndOffset
    state.ip.address += toAddress(segment, offset) - 3

  def getSegmentAndOffset(implicit state: CPUState, vpc: VirtualPC): (Int, Int) =
    val address = toAddress(DS.get, src.get)
    val memory = vpc.memory
    val (offset, segment) = (memory.getWord(from = address), memory.getWord(from = address + 2))
    (segment, offset)

  override def toCode: String = s"$name ${src.toCode}"

/**
 * Near Jump (signed 16-bit offset) Computation logic
 */
trait NearJumpComputation extends JumpComputation:

  def delta: Int

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    state.ip.address += jumpDelta

  def jumpDelta: Int = signed16(delta) - 3

  override def toCode: String =
    val value = signed16(delta)
    val absValue = Math.abs(value)
    val text = if (value < 0) f"-$absValue%03X" else f"+$absValue%03X"
    f"$name $text <$delta%04X>"

/**
 * Near Jump (signed 16-bit offset) Operand Computation logic
 */
trait NearJumpOperandComputation extends JumpComputation with OneImmutableOperand:

  def src: Operand

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    state.ip.address += signed16(src.get) - 3

  override def toCode: String = s"$name ${src.toCode}"

/**
 * Short Jump (signed 8-bit offset) Computation logic
 */
trait ShortJumpComputation extends JumpComputation:

  def delta: Int

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    state.ip.address += jumpDelta

  def jumpDelta: Int = signed8(delta) - 2

  override def toCode: String =
    val value = signed8(delta)
    val absValue = Math.abs(value)
    val relative = if (value < 0) f"-$absValue%02X" else f"+$absValue%02X"
    f"$name $relative <$delta%02X>"