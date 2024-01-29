package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.FlagRegister16.*
import com.github.ldaniels528.v88.operands.{ByteSized, Operand, WordSized}

/**
 * Represents a Virtual CPU Register
 */
sealed trait VirtualRegister extends Operand:
  def name: String

  override def get(implicit state: CPUState, vpc: VirtualPC): Int = value

  def value: Int

  def value_=(newValue: Int): Unit

object VirtualRegister:
  def unapply(vr: VirtualRegister): Option[(String, Int)] = Some((vr.name, vr.value))

/**
 * Represents a Virtual 8-bit CPU Register
 * @param name the name of the register
 */
case class Register8(name: String) extends VirtualRegister with ByteSized:
  private var state: Int = 0

  def value: Int = state

  def value_=(newValue: Int): Unit = state = newValue & 0xFF

/**
 * Represents a Virtual 16-bit CPU Register
 */
sealed trait Register16 extends VirtualRegister with WordSized

/**
 * Represents a Virtual 16-bit CPU Standard Register
 * @param name the name of the register
 */
case class Register16A(name: String) extends Register16:
  private var state: Int = 0

  def value: Int = state

  def value_=(newValue: Int): Unit = state = newValue & 0xFFFF

/**
 * Represents a Virtual 16-bit CPU Composite Register
 * @param name the name of the register
 * @param hi   the embedded 8-bit high-order register
 * @param lo   the embedded 8-bit low-order register
 */
case class Register16C(name: String, hi: Register8, lo: Register8) extends Register16:
  def value: Int = (hi.value << 8) | lo.value

  def value_=(newValue: Int): Unit = {
    hi.value = (newValue & 0xFF00) >> 8
    lo.value = newValue & 0x00FF
  }

/**
 * 8086 Flags (FLAGS) Register
 * <pre>
 * Bit # (in hex)	Acronym	Description
 * 0	 			CF	 	Carry flag
 * 1		 		1       Reserved
 * 2	 			PF		Parity flag
 * 3		 		0       Reserved
 * 4				AF		Auxiliary flag
 * 5				0       Reserved
 * 6				ZF		Zero flag
 * 7				SF		Sign flag
 * 8				TF		Trap flag (single step)
 * 9				IF		Interrupt flag
 * 10				DF		Direction flag
 * 11				OF		Overflow flag
 * </pre>
 * @author lawrence.daniels@gmail.com
 */
case class FlagRegister16(name: String) extends Register16 {
  // setup the default state of the flags
  // fedc ba98 7654 3210
  // .... odit sz0a 0p1c (mask)
  private var state = 0x0202 // 0000.0010.0000.0010
  
  def direction: Int = if (DF) -1 else 1

  def CF: Boolean = isBitSet(value, CF_BIT) // 0000.0000.0001

  def CF_=(value: Boolean): Unit = state |= turnBitOnMask(CF_BIT)

  def PF: Boolean = isBitSet(value, PF_BIT) // 0000.0000.0100

  def PF_=(value: Boolean): Unit = state |= turnBitOnMask(PF_BIT)

  def AF: Boolean = isBitSet(value, AF_BIT) // 0000.0001.0000

  def AF_=(value: Boolean): Unit = setBit(AF_BIT, value)

  def ZF: Boolean = isBitSet(value, ZF_BIT) // 0000.0100.0000

  def ZF_=(value: Boolean): Unit = setBit(ZF_BIT, value)

  def SF: Boolean = isBitSet(value, SF_BIT) // 0000.1000.0000

  def SF_=(value: Boolean): Unit = setBit(SF_BIT, value)

  def TF: Boolean = isBitSet(value, TF_BIT) // 0001.0000.0000

  def TF_=(value: Boolean): Unit = setBit(TF_BIT, value)

  def IF: Boolean = isBitSet(value, IF_BIT) // 0010.0000.0000

  def IF_=(value: Boolean): Unit = setBit(IF_BIT, value)

  def DF: Boolean = isBitSet(value, DF_BIT) // 0100.0000.0000

  def DF_=(value: Boolean): Unit = setBit(DF_BIT, value)

  def OF: Boolean = isBitSet(value, OF_BIT) // 1000.0000.0000

  def OF_=(value: Boolean): Unit = setBit(OF_BIT, value)

  def value: Int = state

  def value_=(newValue: Int): Unit = state = newValue & 0xFFFF

  override def toString: String = {
    def alt(t: (Boolean, String)): Option[String] = if (t._1) Some(t._2) else None

    getClass.getSimpleName +
      Seq(AF -> "AF", CF -> "CF", IF -> "IF", PF -> "PF", OF -> "OF", SF -> "SF", TF -> "TF", ZF -> "ZF")
        .flatMap(alt).mkString("(", ", ", ")")
  }

  private def setBit(nth: Int, on: Boolean): Unit = {
    if (on) state |= turnBitOnMask(nth) else state &= turnBitOffMask(FLAG_LEN, nth)
  }
}

object FlagRegister16:
  // Flag bit number constants
  private val FLAG_LEN = 16
  private val CF_BIT = 0
  private val PF_BIT = 2
  private val AF_BIT = 4
  private val ZF_BIT = 6
  private val SF_BIT = 7
  private val TF_BIT = 8
  private val IF_BIT = 9
  private val DF_BIT = 10
  private val OF_BIT = 11
