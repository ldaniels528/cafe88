package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.operands.*

/**
 * Scan String
 * <pre>
 * Usage:  SCAS    string
 * SCASB
 * SCASW
 * SCASD   (386+)
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Compares value at ES:DI (even if operand is specified) from the
 * accumulator and sets the flags similar to a subtraction.  DI is
 * incremented/decremented based on the instruction format (or
 * operand size) and the state of the Direction Flag.  Use with REP
 * prefixes.
 * </pre>
 */
sealed trait SCAS extends MassDataInstruction

/**
 * Scan String Byte (SCASB)
 */
case object SCASB extends SCAS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // compare the byte value at ES:[DI] to AL
    ByteValue(vpc.memory.getByte(ES, DI)) - AL

    // increment/decrement DI
    state.di.value += state.flags.direction

/**
 * Scan String Word (SCASW)
 */
case object SCASW extends SCAS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // compare the word value at ES:[DI] to AX
    WordValue(vpc.memory.getWord(ES, DI)) - AX

    // increment/decrement DI
    state.di.value += 2 * state.flags.direction