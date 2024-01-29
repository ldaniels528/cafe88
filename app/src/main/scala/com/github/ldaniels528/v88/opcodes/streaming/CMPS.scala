package com.github.ldaniels528.v88.opcodes.streaming

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.data.*
import com.github.ldaniels528.v88.opcodes.io.*
import com.github.ldaniels528.v88.operands.*

/**
 * Compare String
 * <pre>
 * Usage:  CMPS    dest,src
 * CMPSB
 * CMPSW
 * CMPSD   (386+)
 * Modifies flags: AF CF OF PF SF ZF
 *
 * Subtracts destination value from source without saving results.
 * Updates flags based on the subtraction and  the index registers
 * (E)[[SI]] and (E)[[DI]] are incremented or decremented depending on the
 * state of the Direction Flag.  [[CMPSB]] inc/decrements the index
 * registers by 1, [[CMPSW]] inc/decrements by 2, while CMPSD increments
 * or decrements by 4.  The `REP` prefixes can be used to process
 * entire data items.
 *
 * Clocks                 Size
 * Operands         808x  286   386   486          Bytes
 *
 * dest,src          22    8     10    8             1  (W88=30)
 * </pre>
 * @example {{{
 *  IF (byte comparison)
 *  THEN IF DF = 0
 *      THEN
 *          (E)SI := (E)SI + 1;
 *          (E)DI := (E)DI + 1;
 *      ELSE
 *          (E)SI := (E)SI – 1;
 *          (E)DI := (E)DI – 1;
 *      FI;
 *  ELSE IF (Word comparison)
 *      THEN IF DF = 0
 *          (E)SI := (E)SI + 2;
 *          (E)DI := (E)DI + 2;
 *      ELSE
 *          (E)SI := (E)SI – 2;
 *          (E)DI := (E)DI – 2;
 *      FI;
 *  ELSE (* Double-word comparison *)
 *      THEN IF DF = 0
 *          (E)SI := (E)SI + 4;
 *          (E)DI := (E)DI + 4;
 *      ELSE
 *          (E)SI := (E)SI – 4;
 *          (E)DI := (E)DI – 4;
 *      FI;
 *  FI;
 * }}}
 * @author lawrence.daniels@gmail.com
 * @see [[CMPSB]]
 * @see [[REPZ]]
 * @see [[REPNZ]]
 */
sealed trait CMPS extends MassDataInstruction

/**
 * Compare String Byte (CMPSB)
 */
case object CMPSB extends CMPS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // compare byte from DS:[SI] to ES:[DI]
    val memory = vpc.memory
    val src = ByteValue(memory.getByte(DS, SI))
    val dst = ByteValue(memory.getByte(ES, DI))

    // perform the comparison (update flags)
    dst - src

    // increment/decrement index registers
    val delta = state.flags.direction
    state.si.value += delta
    state.di.value += delta

/**
 * Compare String Word (CMPSW)
 */
case object CMPSW extends CMPS:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // compare word from DS:[SI] to ES:[DI]
    val memory = vpc.memory
    val src = WordValue(memory.getWord(DS, SI))
    val dst = WordValue(memory.getWord(ES, DI))

    // perform the comparison (update flags)
    dst - src

    // increment/decrement index registers
    val delta = 2 * state.flags.direction
    state.si.value += delta
    state.di.value += delta
