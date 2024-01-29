package com.github.ldaniels528.v88

import com.github.ldaniels528.v88.VirtualMemory.toAddress
import com.github.ldaniels528.v88.VirtualPC.*
import org.slf4j.LoggerFactory

import java.awt.Graphics
import java.awt.event.*

/**
 * Represents a virtual computer system
 * @param cpu    the [[VirtualCPU central processing unit]]
 * @param gpu    the [[VirtualGPU graphics processing unit]]
 * @param memory the [[VirtualMemory random access memory]]
 * @param disk   the [[VirtualDisk storage manager]]
 */
case class VirtualPC(cpu: VirtualCPU = VirtualCPU(),
                     gpu: VirtualGPU = ColorGraphicsAdapter(),
                     memory: VirtualMemory = VirtualMemory(),
                     disk: VirtualDisk = DefaultDisk())
  extends VirtualBIOS:
  
  def bios: VirtualBIOS = this

  /**
   * Initializes the virtual machine
   */
  def init(): Unit =
    // create the Interrupt vector table: (256 x 4-byte values from 0000:0000 to 0000:03FC)
    bios.createDefaultVectorTableEntries()
    // vector 1F points to ASCII characters 128 thru 255 [at C000:2520]
    // TODO set this up
    //memory.setBytes(0xC000, 0x2520, [...])

  /**
   * Loads an application image (.COM file) into memory
   * @param image   the application image
   * @param segment the destination memory segment
   * @param offset  the destination memory offset
   * @return a new [[CPUState CPU state]] ready to execute the opcodes
   */
  def loadImage(image: Array[Byte], segment: Int, offset: Int): CPUState =
    memory.loadImage(image, segment, offset)
    CPUState(segment, offset)

  /**
   * Allows the GPU an opportunity to render the PC's output
   * @param g the [[Graphics]]
   */
  def render(g: Graphics): Unit = gpu.render(g)

  /**
   * Starts execution at CS:IP
   * @param state the [[CPUState CPU state]]
   * @return the updated [[CPUState CPU state]]
   */
  def run(state: CPUState): CPUState = cpu.run(state)(this)

  /**
   * Starts execution at CS:IP
   * @param state the [[CPUState CPU state]]
   * @return the updated [[CPUState CPU state]]
   */
  def start(state: CPUState): CPUState =
    var myState = state
    while (myState.isRunning && myState.ip.address < memory.length) myState = update(myState)
    myState

  /**
   * virtual computer system update function
   */
  val update: CPUState => CPUState =
    var limiter: Int = 20
    (stateIn: CPUState) =>
      var stateOut: CPUState = stateIn
      if (stateOut.isRunning && stateOut.ip.address < memory.length) {
        val start = stateOut.ip.address
        val opCode_? = cpu.executeNext(stateOut)(this)
        stateOut = stateOut.copy(isRunning = opCode_?.nonEmpty)
        opCode_?.foreach: (opCode, state) =>
          val limit = if (state.ip.address - start > 10) state.ip.address min start + 3 else state.ip.address
          val bytes = memory.slice(start, limit).map(b => f"$b%02X").mkString(".")
          logger.info(f"$limiter%02d [$start%04X] $bytes%12s ${opCode.toCode}")
          limiter -= 1
          stateOut.isRunning = limiter > 0
      }
      stateOut

