package com.github.ldaniels528.v88.decoders

import com.github.ldaniels528.v88.*
import com.github.ldaniels528.v88.VirtualCPU.decoders8086
import com.github.ldaniels528.v88.opcodes.Instruction
import com.github.ldaniels528.v88.operands.*
import org.slf4j.LoggerFactory

/**
 * Intel 8086 Machine Code Decoder
 */
trait Decoder:
  
  def decode(ip: MemoryCursor)(implicit vpc: VirtualPC): Instruction

  def nextBytePtr(ip: MemoryCursor)(implicit vpc: VirtualPC): BoxNear =
    BoxNear(ip.nextByte(vpc.memory), is8Bit = true)

  def nextByteValue(ip: MemoryCursor)(implicit vpc: VirtualPC): ByteValue =
    ByteValue(ip.nextByte(vpc.memory))

  def nextValue(ip: MemoryCursor, registerID: Int)(implicit vpc: VirtualPC): DirectValue =
    if (registerID < 8) nextByteValue(ip) else nextWordValue(ip)

  def nextWordValue(ip: MemoryCursor)(implicit vpc: VirtualPC): WordValue =
    WordValue(ip.nextWord(vpc.memory))

  def nextWordPtr(ip: MemoryCursor)(implicit vpc: VirtualPC): BoxNear =
    BoxNear(ip.nextByte(vpc.memory), is8Bit = false)

object Decoder:
  // define the memory class constants
  private val MEM_CLASS_8BIT: Int = 0
  private val MEM_CLASS_16BIT: Int = 1

  // define the 8086 register code
  private val registersGP: Seq[Register] = Seq(AL, CL, DL, BL, AH, CH, DH, BH, AX, CX, DX, BX, SP, BP, SI, DI)
  private val registersSeg: Seq[SegmentRegister] = Seq(CS, DS, ES, SS)

  def decodeNext(ip: MemoryCursor, decoders: Seq[Decoder] = decoders8086)(implicit vpc: VirtualPC): Instruction = 
    // determine the decoder for the next opcode
    val address = ip.address
    val byteCode = vpc.memory(address)
    val decoderIndex = (byteCode & 0xF0) / 0x10
    val decoder = decoders(decoderIndex)
    val instruction = decoder.decode(ip)
    //logger.info(f"[$address%04X] $instruction")
    instruction

  /**
   * Decodes 16-bit instruction codes containing the i, m, r, s, t, v and w bits
   * @param ip          the [[MemoryCursor memory cursor]]
   * @param codeI8      the 8-bit instruction code portion (containing i, s, v and w bits)
   * @param codeR8      the 8-bit memory reference code portion (containing m, r and t bits)
   * @param forceSegReg indicates whether segment registers are tobe used                 
   * @param transform   the transformation function
   * @param vpc         the implicit [[VirtualPC virtual PC]]
   * @return the resultant [[Instruction opCode]]
   */
  def decodeTwoOperands(ip: MemoryCursor, codeI8: Int, codeR8: Int, forceSegReg: Boolean = false, forceDirect: Boolean = false)
                       (transform: (Int, Int, Int) => (MutableOperand, Operand) => Instruction)
                       (implicit vpc: VirtualPC): Instruction =
    val op = OpCode(ip, codeI8, codeR8)
    val (dest, src) = op.decodeOperands(ip, forceSegReg, forceDirect)
    transform(op.i, op.r, op.m)(dest, src)

  /**
   * Returns the register based on the given register code
   * @param regCode the given 3-bit register code
   * @return the appropriate [[Register operand]]
   */
  def getRegister(regCode: Int)(implicit vpc: VirtualPC): Register = registerByID(regCode)

  /**
   * Returns an 8-bit NEAR data address (offset only)
   * @return the [[BytePtr operand]]
   */
  def nextAddressByte(ip: MemoryCursor)(implicit vpc: VirtualPC): MemoryBox =
    BoxNear(ip.nextWord(vpc.memory), is8Bit = true)

  /**
   * Returns a 16-bit NEAR data address (offset only)
   * @return the [[MemoryBox operand]]
   */
  def nextAddressWord(ip: MemoryCursor)(implicit vpc: VirtualPC): MemoryBox =
    BoxNear(ip.nextWord(vpc.memory), is8Bit = false)

  /**
   * Returns the 16-bit NEAR address
   * @return the [[WordValue operand]]
   */
  def nextAddressNear(ip: MemoryCursor)(implicit vpc: VirtualPC): WordValue =
    val memory = vpc.memory
    val relOffset = ip.nextSignedWord(memory)
    val curOffset = ip.nextByte(memory)
    WordValue(curOffset + relOffset)

  /**
   * Returns the 8-bit SHORT address
   * @return the [[WordValue word value operand]]
   */
  def nextAddressShort(ip: MemoryCursor)(implicit vpc: VirtualPC): WordValue =
    val memory = vpc.memory
    val relOffset = ip.nextSignedByte(memory)
    val curOffset = ip.nextByte(memory)
    WordValue(curOffset + relOffset)

  /**
   * Returns the 8-bit value
   * @param memClass the given memory class
   * @return the [[DirectValue direct value]] depending on the memory class.
   * @see [[MEM_CLASS_16BIT]]
   * @see [[MEM_CLASS_8BIT]]
   */
  def nextValue(ip: MemoryCursor, memClass: Int)(implicit vpc: VirtualPC): DirectValue =
    nextValue(ip, memClass == MEM_CLASS_8BIT)

  /**
   * Returns the 8-bit value
   * @param is8Bit indicates an 8-bit quantity
   * @return the [[DirectValue direct value]] depending on the memory class.
   * @see [[MEM_CLASS_16BIT]]
   * @see [[MEM_CLASS_8BIT]]
   */
  def nextValue(ip: MemoryCursor, is8Bit: Boolean)(implicit vpc: VirtualPC): DirectValue =
    val memory = vpc.memory
    if (is8Bit) ByteValue(ip.nextByte(memory)) else WordValue(ip.nextWord(memory))

  /**
   * Returns the unsigned 8-bit value
   * @return the 8-bit value
   */
  def nextValue8(ip: MemoryCursor)(implicit vpc: VirtualPC): ByteValue =
    ByteValue(ip.nextByte(vpc.memory))

  /**
   * Returns the 16-bit value
   * @return the 16-bit value
   */
  def nextValue16(ip: MemoryCursor)(implicit vpc: VirtualPC): WordValue =
    WordValue(ip.nextWord(vpc.memory))

  def registerByID(index: Int): Register = registersGP(index)

  def registerByID(index: Int, is8Bit: Boolean): Register =
    val n = index & 0x07
    if (is8Bit) registersGP(n) else registersGP(n | 0x08) // 0000.1000

  def getRegisterID(r: Register): Int =
    registersGP.indexOf(r) match
      case n if n >= 8 => n & 0x07
      case n if n >= 0 => n
      case n => registersSeg.indexOf(r)

  def segmentRegisterByID(index: Int): SegmentRegister = registersSeg(index)
  