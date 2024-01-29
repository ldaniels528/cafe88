package com.github.ldaniels528.v88.services

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
 * Video Services
 */
object VideoServices {
  private val logger = LoggerFactory.getLogger(getClass)
  
  def execute(mode: Int)(implicit state: CPUState, vpc: VirtualPC): Unit =
    mode match
      case z => logger.error(f"Video Service (INT 10h, mode $z%xh) was unhandled")
  
}
