package com.mnenmenth.camclient.core

import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import java.awt.image.BufferedImage
import java.awt.{Dimension, Graphics, GraphicsEnvironment}
import java.io._
import java.net.{Socket, SocketAddress}
import javax.imageio.ImageIO
import javax.swing.{JButton, JFrame, JPanel}

import scala.collection.mutable

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
object CamClient {

  private val winSize = new Dimension(640, 480)

  var imgQueue: mutable.Queue[BufferedImage] = mutable.Queue[BufferedImage]()

  val gfx = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration

  def queueImg(data: Array[Byte]): Unit = {
    //val img = new BufferedImage(winSize.width, winSize.height, BufferedImage.TYPE_3BYTE_BGR)
    //img.setData(Raster.createRaster(img.getSampleModel, new DataBufferByte(buff, buff.length), new Point()))

    val img = new BufferedImage(winSize.width, winSize.height, BufferedImage.TYPE_3BYTE_BGR)
    img.getRaster.setDataElements(0, 0, winSize.width, winSize.height, data)
    //println(img == null)
    //ImageIO.write(img, "png", new File(s"./ey$data"))
    imgQueue.enqueue(img)
    //SwingUtilities.invokeLater(() => imgQueue += img)
  }

  def main(args: Array[String]): Unit = {

    val queueThread = new Thread(() => {
      var socket = new Socket("192.168.1.125", 2223)
      val stream = socket.getInputStream
      val reader = new BufferedReader(new InputStreamReader(stream))
      val datastream = new DataInputStream(stream)
      while (socket.isConnected) {
        try {
          val len = reader.readLine()
          val buff = new Array[Byte](len.toInt)
          datastream.readFully(buff)
          //val data = new ByteArrayInputStream(buff)
          queueImg(buff)
        } catch {
          case _: java.lang.NumberFormatException =>
            socket.close()
            socket = new Socket("192.168.1.125", 2223)
        }
      }
    }
    )

    val frame = new JFrame("CamClient")
    val panel = new JPanel {
      override def paintComponent(g: Graphics): Unit = {
        super.paintComponent(g)
        if (imgQueue.length >= 30) {
          imgQueue.drop(imgQueue.length)
        }
        if (imgQueue.nonEmpty) {
          g.drawImage(imgQueue.dequeue(), 0, 0, winSize.getWidth.toInt, winSize.getHeight.toInt, null)
          println("pls")
        }
        repaint()
      }
    }
    frame.add(panel)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(winSize)
    frame.setMinimumSize(winSize)
    frame.setVisible(true)
    frame.addWindowListener(new WindowAdapter {
      override def windowClosing(e: WindowEvent): Unit = {
        super.windowClosing(e)
        queueThread.interrupt()
      }
    })
    queueThread.start()

  }
}
