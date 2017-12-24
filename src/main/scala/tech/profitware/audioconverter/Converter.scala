package tech.profitware.audioconverter

import scala.sys.process._
import java.io.IOException
import java.io.File

trait Conversion {
  val sourceExtension: String
  val destinationFormat: String
  val destinationExtension: String
  def isPresent: Boolean
  def alertNotInstalled(): Unit
  def convertFile(file: File, directory: Directory): Boolean
}

trait FFMpegConversion extends Conversion with Logger {
  def isPresent =
    try {
      ("ffmpeg -version" #> new File("/dev/null")).! == 0
    } catch {
      case ioe: IOException => false
    }
  def alertNotInstalled() = log("FFMpeg is not installed!")
  def convertFile(file: File, directory: Directory) = {
    val absoluteFile = file.getAbsoluteFile
    val album = absoluteFile.getParentFile.getName
    val destionationDirectory =
      new File(directory.destinationArtistDirectory.getAbsolutePath, album)
    val destinationFile = new File(destionationDirectory, s"${file.getName}.${destinationExtension}").getAbsolutePath

    val executableSeq = Seq("ffmpeg", "-i", s"${absoluteFile.getPath}", "-acodec", destinationFormat, destinationFile)
    val executable = Process(executableSeq, absoluteFile.getParentFile)

    try {
      destionationDirectory.mkdirs()
      ("echo y" #| executable).! == 0
    } catch {
      case ioe: IOException => false
    }
  }
}

trait FFMpegFlacToAlacConversion extends FFMpegConversion {
  val sourceExtension = "flac"
  val destinationFormat = "alac"
  val destinationExtension = "m4a"
}

abstract class Converter(val directory: Directory) extends Conversion with Logger {
  def convert() =
    directory.getSourceDirectoryTree.par
      .filter(_.getName().endsWith(s".${sourceExtension}"))
      .map(
        currentFile => convertFile(currentFile, directory)
      )
}
