package com.github.ldaniels528.v88.opcodes.alu

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.opcodes.{OneImmutableOperand, OneMutableOperand}
import com.github.ldaniels528.v88.operands.Operand

/**
 * Signed divide.
 * <pre>
 * Algorithm:
 *
 * when operand is a byte:
 * AL = AX / operand
 * AH = remainder (modulus)
 *
 * when operand is a word:
 * AX = (DX AX) / operand
 * DX = remainder (modulus)
 *
 * Example:
 * MOV AX, -203 ; AX = 0FF35h
 * MOV BL, 4
 * IDIV BL      ; AL = -50 (0CEh), AH = -3 (0FDh)
 * RET
 * </pre>
 * @see [[http://home.comcast.net/~fbui/intel/i.html#idiv]]
 */
case class IDIV(src: Operand) extends OneImmutableOperand:
  override def execute()(implicit state: CPUState, vpc: VirtualPC): Unit =
    if (src.is8Bit)
      // get the divisor and operand
      val (valueA, valueB) = (state.ax.value, src.get)
      // put the values in AL and AH
      if (valueB != 0)
        state.al.value = valueA / valueB
        state.ah.value = valueA % valueB
      else
        // call interrupt 0 (divide by zero)
        vpc.bios.raise(interruptNum = 0)
    else
      // get the divisor and operand
      val (valueA, valueB) = ((state.dx.value << 16) | state.ax.value, src.get)
      // put the values in AL and AH// put the values in AL and AH
      if (valueB != 0)
        state.dx.value = valueA / valueB
        state.ax.value = valueA % valueB
      else
        // call interrupt 0 (divide by zero)
        vpc.bios.raise(interruptNum = 0)
      