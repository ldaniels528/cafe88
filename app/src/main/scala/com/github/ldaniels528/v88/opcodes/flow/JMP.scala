package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.VirtualMemory.toAddress
import com.github.ldaniels528.v88.opcodes.{Instruction, OneImmutableOperand}
import com.github.ldaniels528.v88.operands.{DS, Operand}

/**
 * Unconditionally transfers control to "label". Jumps by default
 * are within -32768 to 32767 bytes from the instruction following
 * the jump.  NEAR and SHORT jumps cause the IP to be updated while FAR
 * jumps cause CS and IP to be updated. Modifies flags: None.
 * @example JMP(0x0156)
 */
sealed trait JMP extends JumpComputation

object JMP:

  /**
   * JMP FAR segment:offset
   * @param segment the provided memory segment
   * @param offset  the provided memory offset
   */
  case class Far(segment: Int, offset: Int) extends JMP with FarJumpOffsetComputation:
    override def name = "jmp far"

  /**
   * JMP FAR reference
   * @param src the [[Operand operand]]
   */
  case class FarRx(src: Operand) extends JMP with FarJumpOperandComputation:
    override def name = "jmp far"

  /**
   * JMP NEAR delta15
   * @param delta the signed (15-bit) offset delta
   */
  case class Near(delta: Int) extends JMP with NearJumpComputation:
    override def name = "jmp near"

  /**
   * JMP NEAR reference
   * @param src the [[Operand operand]]
   */
  case class NearRx(src: Operand) extends JMP with NearJumpOperandComputation:
    override def name: String = "jmp near"

  /**
   * JMP SHORT delta7
   * @param delta the signed (7-bit) offset delta
   */
  case class Small(delta: Int) extends JMP with ShortJumpComputation:
    override def name = "jmp short"
