package com.github.ldaniels528.v88.services.msdos

import com.github.ldaniels528.v88.VirtualBIOS.*
import com.github.ldaniels528.v88.VirtualMemory.toAddress
import com.github.ldaniels528.v88.VirtualPC.*
import com.github.ldaniels528.v88.services.VideoServices
import com.github.ldaniels528.v88.services.msdos.MsDosServices
import com.github.ldaniels528.v88.{CPUState, VirtualPC}
import org.slf4j.{Logger, LoggerFactory}

import java.awt.Graphics
import java.awt.event.*

/**
 * Microsoft DOS Services
 */
object MsDosServices {
  private val logger = LoggerFactory.getLogger(getClass)
  
  def execute(mode: Int)(implicit state: CPUState, vpc: VirtualPC): Unit =
    val memory = vpc.memory
    
    def getMsDosString(from: Int): String =
      var to = from
      while (to < memory.length && memory(to) != 13) to += 1
      new String(memory.slice(from, to))

    mode match
      case 0x09 => println(getMsDosString(from = state.dx.value))
      case z => logger.error(f"MS-DOS Service (INT 21h, mode $z%xh) was unhandled")
  
}
