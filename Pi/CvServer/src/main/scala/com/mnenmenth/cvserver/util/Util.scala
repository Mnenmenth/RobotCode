package com.mnenmenth.cvserver.util

import java.io._
import java.nio.channels.Channels

import com.mnenmenth.cvserver.core.CvServer
import org.opencv.core.{CvType, Mat}
import org.opencv.videoio.VideoCapture

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
object Util {

  class RaspiVidReader(cap: VideoCapture) extends Runnable {
    override def run(): Unit = {


      Stream.continually(br.read()).takeWhile(_ != null).foreach{ line =>

      }
    }
  }

  def extractCv(): Unit = { /** TODO: Multiple platforms */
    System.load(extractResource("com/mnenmenth/cvclient/cvnatives/opencv_java320.dll").getCanonicalPath)
  }

  def extractResource(resource: String, destPath: String = "."): File = {
    val source = Channels.newChannel(getClass.getClassLoader.getResourceAsStream(resource))
    val fileOut = new File(destPath, resource.split('/').last)
    val dest = new FileOutputStream(fileOut)
    dest.getChannel.transferFrom(source, 0, Long.MaxValue)
    source.close()
    dest.close()
    fileOut.deleteOnExit()
    fileOut
  }

}
