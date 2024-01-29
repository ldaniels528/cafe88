package com.github.ldaniels528.v88.opcodes.io

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.*
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * Input Byte or Word From Port
 * <p>A byte, word, or dword is read from "port" and placed in AL, AX or
 * EAX respectively.  If the port number is in the range of 0-255
 * it can be specified as an immediate, otherwise the port number
 * must be specified in DX.  Valid port ranges on the PC are 0-1024,
 * though values through 65535 may be specified and recognized by
 * third party vendors and PS/2's.</p>
 * <pre>
 * Usage:  IN      accum,port
 * Modifies flags: None
 * </pre>
 * @see [[OUT]]
 */
case class IN(dest: MutableOperand, src: Operand) extends Instruction with TwoOperands:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // get the port number
    val portNum = src.get

    // place the value into the accumulator
    dest.set(value = if (dest.is8Bit) vpc.read8(portNum) else vpc.read16(portNum))

