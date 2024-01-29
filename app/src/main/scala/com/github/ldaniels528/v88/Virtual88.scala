package com.github.ldaniels528.v88

import org.slf4j.LoggerFactory

import java.awt.Color.*
import java.awt.{Dimension, Graphics, Image}
import java.io.FileInputStream
import javax.swing.{JFrame, JPanel, WindowConstants}

/**
 * Virtual 88 - Intel-8086 PC emulator
 */
object Virtual88:
  private val logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit =
    val (vpc, entryPoint) = createVirtualPC(args)
    val contentPane = new VirtualScreenPane(vpc)
    val frame = new JFrame("Virtual-88")
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setContentPane(contentPane)
    frame.pack()
    frame.addKeyListener(vpc.bios)
    frame.addMouseListener(vpc.bios)
    frame.addWindowListener(vpc.bios)
    frame.setVisible(true)

    // main loop
    val theScreen = contentPane.getGraphics
    var state: CPUState = entryPoint
    while (state.isRunning) {
      state = vpc.update(state)
      vpc.render(theScreen)
    }

  private def createVirtualPC(args: Array[String]): (VirtualPC, CPUState) =
    // optionally retrieve the application image to decode
    val image: Array[Byte] =
      if (args.nonEmpty) {
        logger.info(s"Loading '${args(0)}'...")
        val fis = new FileInputStream(args.head)
        try fis.readAllBytes() finally fis.close()
      } else Array.emptyByteArray

    // create a virtual PC and load the application image into memory at 1000:0000
    val (vpc, segment, offset) = (VirtualPC(), 0x1000, 0x0100)
    vpc.init()
    (vpc, vpc.loadImage(image, segment, offset))

  /**
   * Virtual Screen Pane
   * @param vpc the [[VirtualPC virtual PC]]
   */
  private class VirtualScreenPane(vpc: VirtualPC) extends JPanel(true):
    private var buffer: Image = _
    private var offScreen: Graphics = _
    private var (myWidth, myHeight) = (0, 0)
    resizeViewport(baseWidth = 320, baseHeight = 200, scale = 1)

    def resizeViewport(baseWidth: Int, baseHeight: Int, scale: Int): Unit =
      val (_width, _height) = (baseWidth << scale, baseHeight << scale)
      myWidth = _width
      myHeight = _height
      setPreferredSize(new Dimension(myWidth, myHeight))

    override def update(g: Graphics): Unit =
      offScreen.setColor(BLACK)
      offScreen.fillRect(0, 0, myWidth, myHeight)
      vpc.render(g)

      // ensure the buffers are initialized
      if (buffer == null || offScreen == null) {
        buffer = createImage(myWidth, myHeight)
        offScreen = buffer.getGraphics
      }
      g.drawImage(buffer, 0, 0, this)
