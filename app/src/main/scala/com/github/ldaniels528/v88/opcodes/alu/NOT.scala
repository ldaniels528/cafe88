package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{Instruction, OneMutableOperand}
import com.github.ldaniels528.v88.operands.MutableOperand

/**
 * Inverts the bits of the "dest" operand forming the 1s complement.
 * <pre>
 * Clocks                	Size
 * Operands         808x  286   386   486          Bytes
 *
 * reg               3     2     2     1             2
 * mem             16+EA   7     6     3            2-4  (W88=24+EA)
 *
 * Usage:  NOT     dest
 * Modifies flags: None
 * </pre>
 */
case class NOT(dest: MutableOperand) extends Instruction with OneMutableOperand with Lockable:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit = 
    // set 1's complement (e.g. 0xAB (10101011) becomes 0x54 (01010100))
    dest.set(1 - dest.get)
