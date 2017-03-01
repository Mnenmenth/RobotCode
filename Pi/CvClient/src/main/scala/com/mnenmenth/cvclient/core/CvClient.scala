package com.mnenmenth.cvclient.core

import java.awt.{Dimension, Graphics}
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.net.Socket
import java.util.concurrent.{ExecutorService, Executors}
import javax.imageio.ImageIO
import javax.swing.{JFrame, JLabel, JPanel, SwingUtilities}

import com.mnenmenth.cvclient.util.Util
import org.opencv.core.{Mat, MatOfByte}
import org.opencv.videoio.VideoCapture
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.core.CvType
import org.opencv.videoio.Videoio

import scala.collection.mutable

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
object CvClient {

  val vidsize = new Dimension(640, 480)
  val queue: mutable.Queue[BufferedImage] = mutable.Queue[BufferedImage]()
  val pool: ExecutorService = Executors.newSingleThreadExecutor()

  def queueImg(buff: Array[Byte]): Unit = {
    val mat = new Mat(vidsize.height, vidsize.width, CvType.CV_8UC3)
    mat.put(0, 0, buff)
    val matb = new MatOfByte()
    Imgcodecs.imencode(".jpg", mat, matb)
    queue += ImageIO.read(new ByteArrayInputStream(matb.toArray))
  }

  def main(args: Array[String]): Unit = {
    Util.extractCv()

    val win = new JFrame()
    win.setSize(vidsize)
    var frame = new BufferedImage(vidsize.width, vidsize.height, BufferedImage.TYPE_3BYTE_BGR)

    val panel = new JPanel {
      override def paint(g: Graphics): Unit = {
        super.paint(g)

        if (queue.size >= 30)
          frame = queue.dequeue()


        g.drawImage(frame, 0, 0, vidsize.width, vidsize.height, null)

        repaint()
      }
    }
    panel.setSize(vidsize)
    win.add(panel)

    val sock = new Socket("192.168.1.125", 2223)
    val in = sock.getInputStream

    val sizeMat = Mat.zeros(vidsize.width, vidsize.height, CvType.CV_8UC3)
    while(sock.isConnected) {
      val buff = new Array[Byte]((sizeMat.total()*sizeMat.elemSize()).toInt)
      in.read(buff)
      pool.submit(new Runnable {
        override def run(): Unit = queueImg(buff)
      })
    }
  }

}