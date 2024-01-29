package com.github.ldaniels528.v88.opcodes.io

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

/**
 * CPU enters wait state until the coor signals it has finished
 * its operation.  This instruction is used to prevent the CPU from
 * accessing memory that may be temporarily in use by the coor.
 * WAIT and FWAIT are identical
 * <pre>
 * Usage:  WAIT
 *         FWAIT
 * Modifies flags: None
 * </pre>
 */
case object WAIT extends Instruction:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = ???

