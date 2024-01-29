package com.github.ldaniels528.v88

import org.scalatest.funspec.AnyFunSpec

import java.io.FileInputStream

class VirtualPCSpec extends AnyFunSpec with VerificationTools {

  describe(classOf[VirtualPC].getSimpleName) {

    it("should execute Willy the Worm") {
      // retrieve the application image to decode
      val image: Array[Byte] =
        val fis = new FileInputStream("./temp/joust/JOUST.COM")
        try fis.readAllBytes() finally fis.close()

      // create a virtual PC
      val vpc = VirtualPC()
      vpc.init()

      // define the entry-point for the image's location within RAM
      val (segment, offset) = (0x1000, 0x0100)

      // load the application image
      val entryPoint = vpc.loadImage(image, segment, offset)

      // execute the application
      var state = entryPoint
      var limit = 20
      while (limit > 0 && state.isRunning)
        state = vpc.update(entryPoint)
        limit -= 1
    }

  }

}
