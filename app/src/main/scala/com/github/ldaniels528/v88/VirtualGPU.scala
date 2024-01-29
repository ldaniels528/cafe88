package com.github.ldaniels528.v88

import java.awt.Color.*
import java.awt.Graphics

/**
 * Represents a virtual graphics processing unit (GPU)
 */
trait VirtualGPU:
  def render(g: Graphics): Unit

  protected val createFrameCounter: Graphics => Unit =
    var lastTime: Long = System.currentTimeMillis()
    var frames: Int = 0
    var fps: Double = 0.0
    (g: Graphics) =>
      val time = System.currentTimeMillis()
      val diff = time - lastTime
      frames += 1
      fps = if (diff >= 1000) {
        val nFrames = frames.toDouble
        frames = 0
        nFrames / diff
      } else fps
      lastTime = time
      g.setColor(if (fps >= 60) GREEN else if (fps >= 30) YELLOW else RED)
      g.drawString(f"$fps%.1f", 25, 25)

/**
 * Represents a CGA graphics processing unit
 */
case class ColorGraphicsAdapter() extends VirtualGPU:
  private val frameRate = createFrameCounter

  override def render(g: Graphics): Unit =
    g.setColor(RED)
    g.fillOval(100, 100, 100, 100)
    frameRate(g)

