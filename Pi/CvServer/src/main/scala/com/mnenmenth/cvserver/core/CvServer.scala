package com.mnenmenth.cvserver.core

import java.awt.{Dimension, Graphics}
import java.awt.image.BufferedImage
import java.io.{BufferedReader, ByteArrayInputStream, InputStreamReader}
import java.net.Socket
import java.util.concurrent.{ExecutorService, Executors}
import javax.imageio.ImageIO
import javax.swing.{JFrame, JLabel, JPanel, SwingUtilities}

import com.mnenmenth.cvserver.util.Util
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
object CvServer {

  val vidsize = new Dimension(640, 480)
  val pool: ExecutorService = Executors.newSingleThreadExecutor()

  def main(args: Array[String]): Unit = {

    val cap = new VideoCapture()
    cap.open("/dev/stdin")

    val rt = Runtime.getRuntime
    val proc = rt.exec("COMMAND")
    val isr = new InputStreamReader(proc.getInputStream)
    val br = new BufferedReader(isr)

    val mat = Mat.zeros(CvServer.vidsize.width, CvServer.vidsize.height, CvType.CV_8UC3)
    cap.retrieve(mat)

  }

}




/*
    val win = new JFrame()
    win.setUndecorated(true)
    win.setSize(vidsize)

    val cap = new VideoCapture
    cap.open(0)
    cap.set(Videoio.CV_CAP_PROP_FRAME_WIDTH, vidsize.width)
    cap.set(Videoio.CV_CAP_PROP_FRAME_HEIGHT, vidsize.height)

    while(!cap.isOpened) Thread.sleep(10)

    val frame = new Mat()
    var bufFrame = new BufferedImage(vidsize.width, vidsize.height, BufferedImage.TYPE_3BYTE_BGR)

    val panel = new JPanel {
      override def paint(g: Graphics): Unit = {
        super.paint(g)
        g.drawImage(bufFrame, 0, 0, vidsize.width, vidsize.height, null)
        repaint()
      }
    }
    panel.setSize(vidsize)
    win.add(panel)

    while(frame.empty()) cap.retrieve(frame)
    win.setVisible(true)
    while (true) {
      cap.retrieve(frame)
      mat2img(frame)
    }*/