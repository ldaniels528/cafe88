package com.github.ldaniels528.v88.opcodes.io

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, TwoOperands}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * Output Data to Port
 * <p>Transfers byte in AL, word in AX, or dword in EAX to the specified
 * hardware port address.  If the port number is in the range of 0-255
 * it can be specified as an immediate.  If greater than 255 then the
 * port number must be specified in DX.  Since the PC only decodes 10
 * bits of the port address, values over 1023 can only be decoded by
 * third party vendor equipment and also map to the port range 0-1023.</p>
 * <pre>
 * Usage:  OUT     port,accum
 * Modifies flags: None
 * </pre>
 * @see [[IN]]
 */
case class OUT(src: Operand, dest: MutableOperand) extends Instruction with TwoOperands:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    // get the port number and the data value
    val (portNum, value) = (dest.get, src.get)

    // output the value to the port/
    if (src.is8Bit) vpc.write8(portNum, value) else vpc.write16(portNum, value)

