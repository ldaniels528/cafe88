package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.getRegisterID
import com.github.ldaniels528.v88.opcodes.alu.Lockable
import com.github.ldaniels528.v88.opcodes.{IllegalInstructionException, Instruction, Orchestration}

/**
 * LOCK restricts the access to all or part of a file that has been opened by another process.
 * This is used in a multi-device environment,
 * often referred to as a network or network environment.
 * @example LOCK [#]n [,[record number] [TO record number]]
 */
case class LOCK(host: Instruction) extends Instruction with Orchestration:
  override def encode: Array[Byte] = i2ba(0xF0) concat host.encode

  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if (!host.isLockable) throw IllegalInstructionException(host) else host.execute()
  