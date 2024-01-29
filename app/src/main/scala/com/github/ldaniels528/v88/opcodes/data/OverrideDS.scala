package com.github.ldaniels528.v88.opcodes.data

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.getRegisterID
import com.github.ldaniels528.v88.opcodes.data.OverrideDS.conversions
import com.github.ldaniels528.v88.opcodes.{Instruction, Orchestration}
import com.github.ldaniels528.v88.operands.SegmentRegister

/**
 * Represents a Data Segment Override Instruction (e.g. "CS:", "DS:", "ES:" or "SS:")
 * @param register the provided [[SegmentRegister segment register]]
 * @param host     the provided [[Instruction instruction]]
 */
case class OverrideDS(register: SegmentRegister, host: Instruction) extends Instruction with Orchestration:
  override def encode: Array[Byte] =
    i2ba(conversions(getRegisterID(register))) concat host.encode

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // set the DS-override register
    val oldDS = state.ds.value
    state.ds.value = register.get

    // interpret the next instruction
    host.execute()

    // reset the DS override
    state.ds.value = oldDS

  override def toCode: String = f"${register.toCode}: ${host.toCode}"

object OverrideDS:
  private val conversions = Seq(0x2E, 0x3E, 0x26, 0x36)
