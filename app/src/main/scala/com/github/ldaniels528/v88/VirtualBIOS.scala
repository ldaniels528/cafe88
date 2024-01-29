package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.VirtualBIOS.*
import com.github.ldaniels528.v88.VirtualMemory.toAddress
import com.github.ldaniels528.v88.VirtualPC.*
import com.github.ldaniels528.v88.opcodes.flow.FarJumpComputation.callTo
import com.github.ldaniels528.v88.services.VideoServices
import com.github.ldaniels528.v88.services.msdos.MsDosServices
import org.slf4j.{Logger, LoggerFactory}

import java.awt.Graphics
import java.awt.event.*

/**
 * Virtual BIOS
 */
trait VirtualBIOS extends KeyListener with MouseListener with WindowListener:
  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  def memory: VirtualMemory

  //////////////////////////////////////////////////////////////////////
  //    INTERRUPTS
  //////////////////////////////////////////////////////////////////////

  def raise(interruptNum: Int)(implicit state: CPUState, vpc: VirtualPC): Unit =
    
    interruptNum match
      case 0x10 => VideoServices.execute(mode = state.ah.value)
      case 0x20 => state.isRunning = false
      case 0x21 => MsDosServices.execute(mode = state.ah.value)
      case z =>
        logger.info(f"system interrupt (INT $z%xh) is using system default handler...")
        val memory = vpc.memory
        val (ptrSegment, ptrOffset) = getVectorSegmentAndOffset(interruptNum)
        val (segment, offset) = (memory.getWord(ptrOffset, ptrOffset + 2), memory.getWord(ptrOffset, ptrOffset))
        logger.info(f"$ptrSegment%04X:$ptrOffset%04X ~> $segment%04X:$offset%04X")
        state.flags.IF = true
        val newState = callTo(segment, offset)
        newState.flags.IF = false
  
  /**
   * Updates a single entry in the Interrupt vector table
   * @param interruptNum the given interrupt to update
   * @param segment      the vector segment address
   * @param offset       the vector offset address
   */
  protected def setInterruptVector(interruptNum: Int, segment: Int, offset: Int): Unit =
    val (dstSegment, dstOffset) = getVectorSegmentAndOffset(interruptNum)
    memory.intraCopy(segment, offset, dstSegment, dstOffset, count = 4)

  /**
   * Creates the Interrupt vector table
   * (256 x 4-byte values from 0000:0000 to 0000:03FC)
   */
  def createDefaultVectorTableEntries(): Unit =
    // define the vector table segment and offset
    val (defaultSegment, defaultOffset) = (0xF000, 0x1000)
    // setup the address of each of 256 interrupts (0x0100)
    for {
      interruptNum <- 0 until 256
      address = getVectorAddress(interruptNum)
    } {
      memory.setWord(address + 0, value = defaultOffset)
      memory.setWord(address + 2, value = defaultSegment)
    }
    // place an [[IRET]] instruction at the default vector address
    memory.setByte(segment = defaultSegment, offset = defaultOffset, value = 0xCF)

  //////////////////////////////////////////////////////////////////////
  //    INPUT/OUTPUT PORTS
  //////////////////////////////////////////////////////////////////////

  /**
   * Reads an 8-bit data value from the given port number
   * @param port the given port number
   * @return a byte or word from the specified port
   */
  def read8(port: Int): Int = memory.getByte(BIOS_SEGMENT, port)

  /**
   * Reads a 16-bit data value from the given port number
   * @param port the given port number
   * @return a byte or word from the specified port
   */
  def read16(port: Int): Int = memory.getWord(BIOS_SEGMENT, port)

  /**
   * Writes the given value to the given port number
   * @param port  the given port number
   * @param value the given data value
   */
  def write8(port: Int, value: Int): Unit =
    // only the first 10 bits are used
    val offset = port & 0x03FF // mask = 0000 0011 1111 1111

    // get the port value
    val value0 = memory.getByte(BIOS_SEGMENT, offset)
    logger.info(f"Changed value at port $port%04X from $value0%04X to $value%04X")

    // set the new port value
    memory.setByte(BIOS_SEGMENT, offset, value)

  /**
   * Writes the given value to the given port number
   * @param port  the given port number
   * @param value the given data value
   */
  def write16(port: Int, value: Int): Unit =
    // only the first 10 bits are used
    val offset = port & 0x03FF // mask = 0000 0011 1111 1111

    // get the port value
    val value0 = memory.getWord(BIOS_SEGMENT, offset)
    logger.info(f"Changed value at port $port%04X from $value0%04X to $value%04X")

    // set the new port value
    memory.setWord(BIOS_SEGMENT, offset, value)

  //////////////////////////////////////////////////////////////////////
  //    EVENT HANDLERS
  //////////////////////////////////////////////////////////////////////

  /**
   * Updates the key status (CTRL, ALT, SHIFT, etc) area of BIOS memory
   * @param keyFlags the given key flags as an integer bit array
   */
  protected def updateKeyFlags(keyFlags: Int): Unit =
    memory.setByte(segment = BIOS_SEGMENT, offset = 0x0017, value = keyFlags)

  override def keyTyped(e: KeyEvent): Unit = ()

  override def keyPressed(e: KeyEvent): Unit = updateKeyFlags(keyFlags = e.getKeyCode)

  override def keyReleased(e: KeyEvent): Unit = updateKeyFlags(keyFlags = 0x00)

  override def mouseClicked(e: MouseEvent): Unit = ()

  override def mousePressed(e: MouseEvent): Unit = ()

  override def mouseReleased(e: MouseEvent): Unit = ()

  override def mouseEntered(e: MouseEvent): Unit = ()

  override def mouseExited(e: MouseEvent): Unit = ()

  override def windowOpened(e: WindowEvent): Unit = ()

  override def windowClosing(e: WindowEvent): Unit = ()

  override def windowClosed(e: WindowEvent): Unit = ()

  override def windowIconified(e: WindowEvent): Unit = ()

  override def windowDeiconified(e: WindowEvent): Unit = ()

  override def windowActivated(e: WindowEvent): Unit = ()

  override def windowDeactivated(e: WindowEvent): Unit = ()

object VirtualBIOS:
  // base memory segment?
  private val baseSegment = 0x0000
  private val BIOS_SEGMENT = 0x0040

  // port addresses
  private val MEM_CTRLR_STATUS = 0x060
  private val MEM_CTRLR_OUTPUT = 0x064

  // serial ports
  private val COM1 = 0x378
  private val COM2 = 0x378
  private val COM3 = 0x378
  private val COM4 = 0x378

  // parallel ports
  private val LPT1 = 0x378

  /**
   * Returns the vector address for a given interrupt
   * @param interruptNum the given interrupt number
   * @return the physical memory address
   */
  def getVectorAddress(interruptNum: Int): Int = toAddress(baseSegment, interruptNum * 4)

  /**
   * Returns the vector address for a given interrupt
   * @param interruptNum the given interrupt number
   * @return the physical memory segment and offset
   */
  def getVectorSegmentAndOffset(interruptNum: Int): (Int, Int) = (baseSegment, interruptNum * 4)