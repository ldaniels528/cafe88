package com.github.ldaniels528.v88.opcodes.flow

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

/**
 * Transfers control from a procedure back to the instruction address
 * saved on the stack.  "n bytes" is an optional number of bytes to
 * release.  Far returns pop the IP followed by the CS, while near
 * returns pop only the IP register.
 * <pre>
 * Usage:  RET
 *         RET  nBytes
 *         RETF
 *         RETF nBytes
 * Modifies flags: None
 * </pre>
 * @see [[RET]]
 * @see [[RETn]]
 * @see [[RETF]]
 * @see [[RETFn]]
 */
case object RET extends Redirect:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = 
    state.ip.address = vpc.memory.pop()


