package com.mnenmenth.camclient.core

import java.awt.{Graphics, Point}
import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.io.ByteArrayOutputStream
import java.net.Socket
import javax.imageio.ImageIO
import javax.swing.{JFrame, SwingUtilities}

import scala.collection.mutable

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
object CamClient {

  private val winSize = (640, 480)

  val imgQueue: mutable.Queue[BufferedImage] = mutable.Queue[BufferedImage]()

  def queueImg(bytes: ByteArrayOutputStream): Unit = {
    //val img = new BufferedImage(winSize._1, winSize._2, BufferedImage.TYPE_3BYTE_BGR)
    //img.setData(Raster.createRaster(img.getSampleModel, new DataBufferByte(buf, buf.length), new Point()))

    val img = new BufferedImage(winSize._1, winSize._2, BufferedImage.TYPE_3BYTE_BGR)
    ImageIO.write(img, "png", bytes)

    SwingUtilities.invokeLater(() => imgQueue += img)

  def main(args: Array[String]): Unit = {

    val queueThread = new Thread {
      override def run(): Unit = {
        val socket = new Socket("192.168.1.125", 2223)
        val buffer = new Array[Byte](4096)
        val data = new ByteArrayOutputStream()
        Iterator.continually(socket.getInputStream.read(buffer)).takeWhile(_ != -1).foreach(data.write(buffer, 0, _))

        queueImg(data)
      }
    }

    SwingUtilities.invokeLater { () =>
      val frame = new JFrame("CamClient") {
        override def paint(g: Graphics): Unit = {
          super.paint(g)

          if (imgQueue.length >= 30) {
            imgQueue.drop(imgQueue.length)
          }

          if (imgQueue.nonEmpty) {
            g.drawImage(imgQueue.dequeue(), 0, 0, winSize._1, winSize._2, null)
          }

          repaint()
        }
      }
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
      frame.setSize(winSize._1, winSize._2)
      frame.pack()
      frame.setVisible(true)
      queueThread.run()
      sys.addShutdownHook(() => queueThread.interrupt())
    }
  }

  }

}
