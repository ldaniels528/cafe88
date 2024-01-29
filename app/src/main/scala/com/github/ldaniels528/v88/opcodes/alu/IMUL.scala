package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{OneImmutableOperand, OneMutableOperand}
import com.github.ldaniels528.v88.operands.{MutableOperand, Operand}

/**
 * Signed multiply.
 * <pre>
 * Algorithm:
 *
 * when operand is a byte:
 * AX = AL * operand.
 *
 * when operand is a word:
 * (DX AX) = AX * operand.
 *
 * Example:
 * MOV AL, -2
 * MOV BL, -4
 * IMUL BL      ; AX = 8
 * RET
 * </pre>
 */
case class IMUL(src: Operand) extends OneImmutableOperand:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if (src.is8Bit)
      // get the divisor and operand
      val (valueA, valueB) = (state.al.value, src.get)
      // put the values in AL and AH
      state.ax.value = valueA * valueB
    else
      // get the divisor and operand
      val (valueA, valueB) = (state.ax.value, src.get)
      // compute the product
      val product = valueA * valueB
      // put the value in DX:AX
      state.dx.value = (product & 0xFFFF0000) >> 16
      state.ax.value = product & 0x0000FFFF
