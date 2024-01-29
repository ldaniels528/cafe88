package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.operands.{DI, DS, ES, SI}

/**
 * Copies data from addressed by [[DS]]:[[SI]] (even if operands are given) to
 * the location [[ES]]:[[DI]] destination and updates SI and DI based on the
 * size of the operand or instruction used.  [[SI]] and [[DI]] are incremented
 * when the Direction Flag is cleared and decremented when the Direction
 * Flag is Set.  Use with REP prefixes.
 * @see [[REPZ]]
 * @see [[REPNZ]]
 */
sealed trait MOVS extends MassDataInstruction

/**
 * Move String Byte (MOVSB)
 */
case object MOVSB extends MOVS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // get the byte value from DS:[SI] and put it into ES:[DI]
    val memory = vpc.memory
    memory.setByte(ES, DI, memory.getByte(DS, SI))

    // increment/decrement index registers
    val delta8 = state.flags.direction
    state.si.value += delta8
    state.di.value += delta8

/**
 * Move String Word (MOVSW)
 */
case object MOVSW extends MOVS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // get the word value from DS:[SI] and put it into ES:[DI]
    val memory = vpc.memory
    memory.setWord(ES, DI, memory.getWord(DS, SI))

    // increment/decrement index registers
    val delta16 = 2 * state.flags.direction
    state.si.value += delta16
    state.di.value += delta16