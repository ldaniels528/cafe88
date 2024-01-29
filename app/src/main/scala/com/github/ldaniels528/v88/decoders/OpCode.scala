package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.Decoder.{nextValue, registerByID, segmentRegisterByID}
import com.github.ldaniels528.v88.operands.{MemoryBox, MutableOperand, Operand}
import org.slf4j.LoggerFactory

/**
 * Represents a 8086 16-bit opcode; including the 8-bit supplemental source/destination code
 * @param i the decoded instruction type (5-bits)
 * @param v the decoded direct/indirect value indicator (1-bit; 0=direct, 1=indirect)
 * @param s the decoded operand swap indicator (1-bit; 0=8-bits wide, 1=16-bits wide)
 * @param w the decoded bus width indicator (1-bit; 0=8-bits wide, 1=16-bits wide)
 * @param t the decoded operand type indicator (2-bits; 00=memory reference, 11=register)
 * @param r the decoded destination operand index (3-bits; 000=AX)
 * @param m the decoded source operand index (3-bits; 000=[BX])
 * @example {{{
 * iiii ivsw ttrr rmmm (16 bits)
 * 1111 1000 .... .... (instruction type bits; e.g. xor=00110, cmp=00111)
 * 0000 0100 .... .... (value: direct/indirect)
 * 0000 0010 .... .... (swap: dest/src bit)
 * 0000 0001 .... .... (width: 8/16-bit)
 * .... .... 1100 0000 (operand type bits)
 * .... .... 0011 1000 (dest index bits)
 * .... .... 0000 0111 (src index bits)         
 * }}}
 * @see [[MemoryBox memory reference]]
 */
case class OpCode(i: Int, v: Int, s: Int, w: Int, t: Int, r: Int, m: Int):
  @inline def is8Bit: Boolean = w == 0
  @inline def isDirectValue: Boolean = v == 0
  @inline def isSwapped: Boolean = s == 0

  def decodeOperands(ip: MemoryCursor, forceSegReg: Boolean = false, forceDirect: Boolean = false)
                    (implicit vpc: VirtualPC): (MutableOperand, Operand) =
    val (dest: MutableOperand, src: Operand) =
      val operandA: MutableOperand = registerByID(r, is8Bit = if (forceSegReg) false else is8Bit)
      val operandB: Operand = t match
        case 3 if forceDirect => nextValue(ip, is8Bit) // direct/indirect value
        case 3 if forceSegReg => segmentRegisterByID(m) // segment register
        case 3 => registerByID(m, is8Bit) // 8/16-bit register
        case _ => operands.MemoryBox(ip, this) // memory reference

      // return the appropriate operands
      if (isSwapped) operandB match
        case mutOperandB: MutableOperand => (mutOperandB, operandA)
        case _ => (operandA, operandB)
      else (operandA, operandB)
    (dest, src)

  override def toString: String = f"i = $i (${Integer.toBinaryString(i)}), v = $v, s = $s, w = $w | t = $t, r = $r, m = $m"

/**
 * OpCode Companion
 */
object OpCode:

  /**
   * Returns an object representation of an opcode
   * @param ip       the [[MemoryCursor memory cursor]]
   * @param codeIVSW the encoded primary opcode (8-bits)
   * @param codeTRM  the encoded source and destination operand code (8-bits)
   * @param vpc      the implicit [[VirtualPC]]
   * @return the [[OpCode opcode]]
   */
  def apply(ip: MemoryCursor, codeIVSW: Int, codeTRM: Int)(implicit vpc: VirtualPC): OpCode =
    // decode the opcode
    // -------------------------------
    // 7654 3210 | 7654 3210 (16 bits)
    // iiii ivsw | ttrr rmmm
    new OpCode(
      i = (codeIVSW & 0xF8) >> 3, // | 1111 1000 .... .... (instruction type bits; e.g. xor=00110, cmp=00111)
      v = (codeIVSW & 0x04) >> 2, // | 0000 0100 .... .... (value: direct/indirect)
      s = (codeIVSW & 0x02) >> 1, // | 0000 0010 .... .... (swap: dest/src bit)
      w = codeIVSW & 0x01, //        | 0000 0001 .... .... (width: 8/16-bit)
      t = (codeTRM & 0xC0) >> 6, //  | .... .... 1100 0000 (operand type bits)
      r = (codeTRM & 0x38) >> 3, //  | .... .... 0011 1000 (dest index bits)
      m = codeTRM & 0x07) //         | .... .... 0000 0111 (src index bits)
