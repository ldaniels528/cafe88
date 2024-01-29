package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.operands.{AL, AX, DI, ES}

/**
 * Store String
 * <pre>
 * Usage:  STOS    dest
 * STOSB
 * STOSW
 * STOSD
 * Modifies flags: None
 *
 * Stores value in accumulator to location at ES:(E)DI (even if operand
 * is given).  (E)DI is incremented/decremented based on the size of
 * the operand (or instruction format) and the state of the Direction
 * Flag.   Use with REP prefixes.
 *
 * Clocks                 Size
 * Operands         808x  286   386   486          Bytes
 *
 * dest              11    3     4     5             1  (W88=15)
 * </pre>
 * @author lawrence.daniels@gmail.com
 * @see [[REPZ]]
 * @see [[REPNZ]]
 */
sealed trait STOS extends MassDataInstruction

/**
 * Store String Byte (STOSB)
 */
case object STOSB extends STOS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // put byte from AL into ES:[DI]
    vpc.memory.setByte(ES, DI, AL.get)

    // increment/decrement DI
    state.di.value += state.flags.direction

/**
 * Store String Byte (STOSW)
 */
case object STOSW extends STOS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // put word from AX into ES:[DI]
    vpc.memory.setWord(ES, DI, AX.get)

    // increment/decrement DI
    state.di.value += 2 * state.flags.direction
