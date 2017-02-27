package com.mnenmenth.cvclient.util

import java.io.{File, FileOutputStream}
import java.nio.channels.Channels

/**
  * Created by Mnenmenth Alkaborin
  * Please refer to LICENSE file if included
  * for licensing information
  * https://github.com/Mnenmenth
  */
object Util {

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
