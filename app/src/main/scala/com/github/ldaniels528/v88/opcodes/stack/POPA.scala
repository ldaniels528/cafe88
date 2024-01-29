package com.github.ldaniels528.v88.opcodes.stack

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.*

/**
 * Pops doublewords (POPAD) or words (POPA) from the stack into the general-purpose registers.
 * The registers are loaded in the following order: EDI, ESI, EBP, EBX, EDX, ECX, and EAX
 * (if the operand-size attribute is 32) and DI, SI, BP, BX, DX, CX, and AX (if the operand-size attribute is 16).
 *
 * (These instructions reverse the operation of the PUSHA/PUSHAD instructions.)
 *
 * The value on the stack for the ESP or SP register is ignored. Instead, the ESP or SP register is
 * incremented after each register is loaded.
 *
 * The POPA (pop all) and POPAD (pop all double) mnemonics reference the same opcode.
 * The POPA instruction is intended for use when the operand-size attribute is 16 and
 * the POPAD instruction for when the operand-size attribute is 32. Some assemblers
 * may force the operand size to 16 when POPA is used and to 32 when POPAD is used
 * (using the operand-size override prefix [66H] if necessary). Others may treat these
 * mnemonics as synonyms (POPA/POPAD) and use the current setting of the operand-size
 * attribute to determine the size of values to be popped from the stack, regardless
 * of the mnemonic used. (The D flag in the current code segment’s segment descriptor
 * determines the operand-size attribute.)
 *
 * This instruction executes as described in non-64-bit modes. It is not valid in 64-bit mode.
 */
case object POPA extends Instruction:
  override def encode: Array[Byte] = i2ba(0x61)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    val memory = vpc.memory
    Seq(DI, SI, BP, BX, DX, CX, AX).foreach(_.set(memory.pop()))


