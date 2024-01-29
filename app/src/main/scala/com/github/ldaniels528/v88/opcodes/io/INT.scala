package com.github.ldaniels528.v88.opcodes.io

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction

/**
 * Interrupt - Initiates a software interrupt by pushing the flags, clearing the
 * Trap and Interrupt Flags, pushing CS followed by IP and loading
 * CS:IP with the value found in the interrupt vector table.  Execution
 * then begins at the location addressed by the new CS:IP
 *
 * See <a href="http://www.shsu.edu/~csc_tjm/spring2001/cs272/interrupt.html">Interrupt Detail</a>
 * <pre>
 * Usage:  INT     num
 * Modifies flags: TF IF
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
case class INT(interruptNum: Int) extends Instruction:
  override def encode: Array[Byte] = Array(0xCD, interruptNum).map(_.toByte)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = vpc.raise(interruptNum)

  override def toCode = f"int $interruptNum%02Xh"


