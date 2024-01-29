package com.github.ldaniels528.v88.operands

import com.github.ldaniels528.v88.*

/**
 * Represents an 8086 Register
 */
sealed trait Register extends MutableOperand:
  def self(implicit state: CPUState): VirtualRegister

  override def get(implicit state: CPUState, vpc: VirtualPC): Int = self.value

  override def set(value: Int)(implicit state: CPUState, vpc: VirtualPC): Unit = self.value = value

  override def set(operand: Operand)(implicit state: CPUState, vpc: VirtualPC): Unit = self.value = operand.get

/**
 * Marker trait for 16-bit Segment Registers (CS, ES, DS and SS)
 */
sealed trait SegmentRegister extends Register with WordSized

/**
 * Represents a decode-time reference to the Code Segment (CS) Register
 */
case object CS extends SegmentRegister:
  override def self(implicit state: CPUState): Register16 = state.cs

/**
 * Represents a decode-time reference to the Data Segment (DS) Register
 */
case object DS extends SegmentRegister:
  override def self(implicit state: CPUState): Register16 = state.ds

/**
 * Represents a decode-time reference to the Extra Segment (ES) Register
 */
case object ES extends SegmentRegister:
  override def self(implicit state: CPUState): Register16 = state.es

/**
 * Represents a decode-time reference to the Stack Segment (SS) Register
 */
case object SS extends SegmentRegister:
  override def self(implicit state: CPUState): Register16 = state.ss

/**
 * Represents a decode-time reference to the Accumulator (AX) Register
 */
case object AX extends Register with WordSized:
  override def self(implicit state: CPUState): Register16 = state.ax

/**
 * Represents a decode-time reference to the Base (BX) Register
 */
case object BX extends Register with WordSized:
  override def self(implicit state: CPUState): Register16 = state.bx

/**
 * Represents a decode-time reference to the Counter (CX) Register
 */
case object CX extends Register with WordSized:
  override def self(implicit state: CPUState): Register16 = state.cx

/**
 * Represents a decode-time reference to the Data (DX) Register
 */
case object DX extends Register with WordSized:
  override def self(implicit state: CPUState): Register16 = state.dx

/**
 * Represents a decode-time reference to the Stack Pointer (SP) Register
 */
case object SP extends Register with WordSized:
  override def self(implicit state: CPUState): Register16 = state.sp

/**
 * Represents a decode-time reference to the Base (BP) Register
 */
case object BP extends Register with WordSized:
  override def self(implicit state: CPUState): Register16 = state.bp

/**
 * Marker trait for 16-bit Index Registers (SI and DI)
 */
sealed trait IndexRegister extends Register with WordSized

/**
 * Represents a decode-time reference to the Source Index (SI) Register
 */
case object SI extends IndexRegister:
  override def self(implicit state: CPUState): Register16 = state.si

/**
 * Represents a decode-time reference to the Destination Index (DI) Register
 */
case object DI extends IndexRegister:
  override def self(implicit state: CPUState): Register16 = state.di

/**
 * Represents a decode-time reference to the Accumulator Lower-Half (AL) Register
 */
case object AL extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.al

/**
 * Represents a decode-time reference to the Counter Lower-Half (CL) Register
 */
case object CL extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.cl

/**
 * Represents a decode-time reference to the Data Lower-Half (DL) Register
 */
case object DL extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.dl

/**
 * Represents a decode-time reference to the Base Lower-Half (BL) Register
 */
case object BL extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.bl

/**
 * Represents a decode-time reference to the Accumulator Upper-Half (AH) Register
 */
case object AH extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.ah

/**
 * Represents a decode-time reference to the Counter Upper-Half (CH) Register
 */
case object CH extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.ch

/**
 * Represents a decode-time reference to the Data Upper-Half (DH) Register
 */
case object DH extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.dh

/**
 * Represents a decode-time reference to the Base Upper-Half (BH) Register
 */
case object BH extends Register with ByteSized:
  override def self(implicit state: CPUState): Register8 = state.bh
