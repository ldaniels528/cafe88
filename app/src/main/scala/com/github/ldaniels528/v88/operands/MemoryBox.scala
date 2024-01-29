package com.github.ldaniels528.v88.operands

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.decoders.OpCode

/**
 * Represents a decode-time reference to a Memory reference
 * <pre>
 * code | tt...mmm
 * --------------------------
 * 0x00 | 00...000 [bx+si]
 * 0x01 | 00...001 [bx+di]
 * 0x02 | 00...010 [bp+si]
 * 0x03 | 00...011 [bp+di]
 * 0x04 | 00...100 [si]
 * 0x05 | 00...101 [di]
 * 0x06 | 00...110 [nnnn]
 * 0x07 | 00...111 [bx]
 * 0x40 | 01...000 [bx+si+nn]
 * 0x41 | 01...001 [bx+di+nn]
 * 0x42 | 01...010 [bp+si+nn]
 * 0x43 | 01...011 [bp+di+nn]
 * 0x44 | 01...100 [si+nn]
 * 0x45 | 01...101 [di+nn]
 * 0x46 | 01...110 [bp+nn]
 * 0x47 | 01...111 [bx+nn]
 * 0x80 | 10...000 [bx+si+nnnn]
 * 0x81 | 10...001 [bx+di+nnnn]
 * 0x82 | 10...010 [bp+si+nnnn]
 * 0x83 | 10...011 [bp+di+nnnn]
 * 0x84 | 10...100 [si+nnnn]
 * 0x85 | 10...101 [di+nnnn]
 * 0x86 | 10...110 [bp+nnnn]
 * 0x87 | 10...111 [bx+nnnn]
 * </pre>
 */
sealed trait MemoryBox extends MutableOperand:
  def self(implicit state: CPUState): Int

  def offset: Int

  final def encode: Int = offset match //| tt rrr mmm
    case n if n == 0 => 0x00 | mask //   | 00 000 000
    case n if is8Bit => 0x40 | mask //   | 01 000 000
    case n => 0x80 | mask //             | 10 000 000

  protected def mask: Int

  override def get(implicit state: CPUState, vpc: VirtualPC): Int =
    vpc.memory.getByteOrWord(self, is8Bit)

  override def set(value: Int)(implicit state: CPUState, vpc: VirtualPC): Unit =
    vpc.memory.setByteOrWord(self, value, is8Bit)

  override def set(operand: Operand)(implicit state: CPUState, vpc: VirtualPC): Unit =
    vpc.memory.setByteOrWord(self, operand.get, is8Bit)

/**
 * MemoryBox Companion
 */
object MemoryBox:
  private val factory: Seq[(Int, Int, Boolean) => MemoryBox] = Seq(
    (t, offset, is8Bit) => Box_BX_SI(offset, is8Bit),
    (t, offset, is8Bit) => Box_BX_DI(offset, is8Bit),
    (t, offset, is8Bit) => Box_BP_SI(offset, is8Bit),
    (t, offset, is8Bit) => Box_BP_DI(offset, is8Bit),
    (t, offset, is8Bit) => Box_SI(offset, is8Bit),
    (t, offset, is8Bit) => Box_DI(offset, is8Bit),
    (t, offset, is8Bit) => (if (t == 0) BoxNear.apply else Box_BP.apply)(offset, is8Bit),
    (t, offset, is8Bit) => Box_BX(offset, is8Bit))
  
  /**
   * Decodes a memory reference operand
   * @param ip  the [[MemoryCursor memory cursor]]
   * @param op  the [[OpCode opcode]]
   * @param vpc the implicit [[VirtualPC]]
   * @return a [[MemoryBox memory reference]]
   */
  def apply(ip: MemoryCursor, op: OpCode)(implicit vpc: VirtualPC): MemoryBox =
    // get the memory offset
    val memory = vpc.memory
    val offset = (op.t, op.m) match
      case (0, 6) => ip.nextWord(memory)
      case (1, _) => ip.nextByte(memory)
      case (2, _) => ip.nextWord(memory)
      case _ => 0 // no offset

    // produce the opcode
    factory(op.m)(op.t, offset, op.is8Bit)

/**
 * Represents a BX+SI-relative memory reference (e.g. `MOV AX,[BX+SI]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[BX+SI]
 */
case class Box_BX_SI(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.bx.value + state.si.value + offset

  override protected def mask: Int = 0x00

  override def toCode: String =
    if (offset == 0) "[bx+si]"
    else if (is8Bit) f"[bx+si+$offset%02X]"
    else f"[bx+si+$offset%04X]"

/**
 * Represents a BX+DI-relative memory reference (e.g. `MOV AX,[BX+DI]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[BX+DI]
 */
case class Box_BX_DI(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.bx.value + state.di.value + offset

  override protected def mask: Int = 0x01

  override def toCode: String =
    if (offset == 0) "[bx+di]"
    else if (is8Bit) f"[bx+di+$offset%02X]"
    else f"[bx+di+$offset%04X]"

/**
 * Represents a BP+SI-relative memory reference (e.g. `MOV AX,[BP+SI]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[BP+SI]
 */
case class Box_BP_SI(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.bp.value + state.si.value + offset

  override protected def mask: Int = 0x02

  override def toCode: String =
    if (offset == 0) "[bp+si]"
    else if (is8Bit) f"[bp+si+$offset%02X]"
    else f"[bp+si+$offset%04X]"

/**
 * Represents a BP+DI-relative memory reference (e.g. `MOV AX,[BP+DI]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[BP+DI]
 */
case class Box_BP_DI(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.bp.value + state.di.value + offset

  override protected def mask: Int = 0x03

  override def toCode: String =
    if (offset == 0) "[bp+di]"
    else if (is8Bit) f"[bp+di+$offset%02X]"
    else f"[bp+di+$offset%04X]"

/**
 * Represents a SI-relative memory reference (e.g. `MOV AX,[SI]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[SI]
 */
case class Box_SI(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.si.value + offset

  override protected def mask: Int = 0x04

  override def toCode: String =
    if (offset == 0) "[si]"
    else if (is8Bit) f"[si+$offset%02X]"
    else f"[si+$offset%04X]"

/**
 * Represents a DI-relative memory reference (e.g. `MOV AX,[DI]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[DI]
 */
case class Box_DI(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.di.value + offset

  override protected def mask: Int = 0x05

  override def toCode: String =
    if (offset == 0) "[di]"
    else if (is8Bit) f"[di+$offset%02X]"
    else f"[di+$offset%04X]"

/**
 * Represents a 16-bit relative memory offset (e.g. `MOV AX,[0128]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[0128]
 */
case class BoxNear(offset: Int, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = offset

  override protected def mask: Int = 0x06

  override def toCode: String = f"[$offset%04X]"

/**
 * Represents a BP-relative memory reference (e.g. `MOV WORD PTR AX, [BP]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV WORD PTR AX,[BP+1234]
 */
case class Box_BP(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.bp.value + offset

  override protected def mask: Int = 0x06

  override def toCode: String =
    if (offset == 0) "[bp]"
    else if (is8Bit) f"[bp+$offset%02X]"
    else f"[bp+$offset%04X]"

/**
 * Represents a BX-relative memory reference (e.g. `MOV AX,[BX]`)
 * @param offset the offset portion of the 8-bit memory offset
 * @param is8Bit indicates whether the memory location is 8-bit or conversely 16-bit
 * @example MOV AX,[BX]
 */
case class Box_BX(offset: Int = 0, is8Bit: Boolean) extends MemoryBox:
  override def self(implicit state: CPUState): Int = state.bx.value + offset

  override protected def mask: Int = 0x07 

  override def toCode: String =
    if (offset == 0) "[bx]"
    else if (is8Bit) f"[bx+$offset%02X]"
    else f"[bx+$offset%04X]"
