package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.decoders.Decoder.getRegisterID
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.opcodes.{Instruction, Orchestration}
import com.github.ldaniels528.v88.operands.CX
import com.github.ldaniels528.v88.{CPUState, VirtualMemory, VirtualPC}

/**
 * Base class for REPEAT opcodes
 */
sealed trait REP extends Instruction with Orchestration

/**
 * Repeats execution of string instructions while [[CX]] != 0 and the Zero
 * Flag is clear. [[CX]] is decremented and the Zero Flag tested after
 * each string operation.   The combination of a repeat prefix and a
 * segment override on ors other than the 386 may result in
 * errors if an interrupt occurs before [[CX]] == 0.
 * <pre>
 * Usage:  [[REPNZ]]
 * Modifies flags: None
 * </pre>
 * @author lawrence.daniels@gmail.com
 * @see [[CMPSB]]
 * @see [[CMPSW]]
 * @see [[LODSB]]
 * @see [[LODSW]]
 * @see [[SCASB]]
 * @see [[SCASW]]
 * @see [[STOSB]]
 * @see [[STOSW]]
 */
case class REPNZ(host: Instruction) extends REP:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // while CX != 0 and ZF == 1
    while ((CX.get > 0) && state.flags.ZF)
      // execute the instruction
      host.execute()
      state.cx.value -= 1

    // set the Zero Flag (ZF)
    state.flags.ZF = false

/**
 * Repeats execution of string instructions while [[CX]] != 0 and the Zero
 * Flag is set. [[CX]] is decremented and the Zero Flag tested after
 * each string operation. The combination of a repeat prefix and a
 * segment override on processors other than the 386 may result in
 * errors if an interrupt occurs before [[CX]] == 0.
 * <pre>
 * Usage: [[REPZ]]
 * Modifies flags: None
 * </pre>
 * @see [[CMPSB]]
 * @see [[CMPSW]]
 * @see [[LODSB]]
 * @see [[LODSW]]
 * @see [[SCASB]]
 * @see [[SCASW]]
 * @see [[STOSB]]
 * @see [[STOSW]]
 */
case class REPZ(host: Instruction) extends REP:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // while CX != 0 and ZF == 0
    while ((CX.get > 0) && !state.flags.ZF)
      // execute the instruction
      host.execute()
      state.cx.value -= 1

    // set the Zero Flag (ZF)
    state.flags.ZF = true