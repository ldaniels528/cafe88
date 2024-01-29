package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.VirtualMemory.toAddress
import com.github.ldaniels528.v88.opcodes.flow.FarJumpComputation.callTo
import com.github.ldaniels528.v88.opcodes.{Instruction, OneImmutableOperand}
import com.github.ldaniels528.v88.operands.{DS, Operand}

/**
 * Pushes Instruction Pointer (and Code Segment for far calls) onto
 * stack and loads Instruction Pointer with the address of proc-name.
 * Code continues with execution at CS:IP.
 */
sealed trait CALL extends JumpComputation

object CALL:

  /**
   * CALL FAR segment:offset
   * @param segment the provided memory segment
   * @param offset  the provided memory offset 
   */
  case class Far(segment: Int, offset: Int) extends CALL with FarJumpOffsetComputation:
    override def name = "call far"
    
    override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = callTo(segment, offset)

  /**
   * CALL FAR reference
   * @param src the [[Operand operand]]
   */
  case class FarRx(src: Operand) extends CALL with FarJumpOperandComputation:
    override def name = "call far"
    
    override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
      val (segment, offset) = getSegmentAndOffset
      callTo(segment, offset)

  /**
   * CALL NEAR offset
   * @param delta the signed (15-bit) offset delta
   */
  case class Near(delta: Int) extends CALL with NearJumpComputation:
    override def name = "call near"

    override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
      vpc.memory.push(state.ip.address)
      super.execute()

  /**
   * CALL NEAR reference
   * @param src the [[Operand operand]]
   */
  case class NearRx(src: Operand) extends CALL with NearJumpOperandComputation:
    override def name = "call near"
    
    override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
      vpc.memory.push(state.ip.address)
      super.execute()
      