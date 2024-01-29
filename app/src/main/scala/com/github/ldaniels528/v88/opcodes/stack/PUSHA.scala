package com.github.ldaniels528.v88.opcodes.stack

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.*

/**
 * Pushes the contents of the general-purpose registers onto the stack. The registers are stored
 * on the stack in the following order: EAX, ECX, EDX, EBX, ESP (original value), EBP, ESI,
 * and EDI (if the current operand-size attribute is 32) and AX, CX, DX, BX, SP (original value),
 * BP, SI, and DI (if the operand-size attribute is 16). These instructions perform the reverse
 * operation of the POPA/POPAD instructions. The value pushed for the ESP or SP register is its
 * value before prior to pushing the first register (see the “Operation” section below).
 *
 * The PUSHA (push all) and PUSHAD (push all double) mnemonics reference the same opcode.
 * The PUSHA instruction is intended for use when the operand-size attribute is 16 and the PUSHAD
 * instruction for when the operand-size attribute is 32. Some assemblers may force the operand size
 * to 16 when PUSHA is used and to 32 when PUSHAD is used. Others may treat these mnemonics as
 * synonyms (PUSHA/PUSHAD) and use the current setting of the operand-size attribute to determine
 * the size of values to be pushed from the stack, regardless of the mnemonic used.
 *
 * In the real-address mode, if the ESP or SP register is 1, 3, or 5 when PUSHA/PUSHAD executes:
 * an #SS exception is generated but not delivered (the stack error reported prevents #SS delivery).
 * Next, the processor generates a #DF exception and enters a shutdown state as described in
 * the #DF discussion in Chapter 6 of the Intel® 64 and IA-32 Architectures Software Developer’s Manual, Volume 3A.
 *
 * This instruction executes as described in compatibility mode and legacy mode. It is not valid in 64-bit mode.
 */
case object PUSHA extends Instruction:
  override def encode: Array[Byte] = i2ba(0x60)
  
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    val memory = vpc.memory
    Seq(DI, SI, BP, BX, DX, CX, AX).foreach(r => memory.push(r.get))

