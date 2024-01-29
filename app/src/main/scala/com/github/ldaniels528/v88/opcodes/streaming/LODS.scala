package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.operands.{AL, AX, DS, SI}

/**
 * Transfers string element addressed by DS:SI (even if
 * an operand is supplied) to the accumulator. SI is incremented
 * based on the size of the operand or based on the instruction
 * used. If the Direction Flag is set SI is decremented, if
 * the Direction Flag is clear SI is incremented. Use with REP
 * prefixes.
 * <pre>
 * Usage: LODS src
 * LODSB
 * LODSW
 * LODSD (386+)
 *
 * Modifies Flags: None
 * </pre>
 * @see [[REPNZ]]
 * @see [[REPZ]]
 */
sealed trait LODS extends MassDataInstruction

/**
 * Load String Byte
 */
case object LODSB extends LODS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // load byte-value at DS:[SI] into AL
    AL.set(vpc.memory.getByte(DS, SI))

    // increment/decrement SI
    state.si.value += state.flags.direction

/**
 * Load String Word
 */
case object LODSW extends LODS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // load word-value at DS:[SI] into AX
    AX.set(vpc.memory.getWord(DS, SI))

    // increment/decrement SI
    state.si.value += 2 * state.flags.direction
