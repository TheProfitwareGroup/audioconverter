package tech.profitware.audioconverter

import java.io.File

trait DestinationDirectory {
  def absoluteDestinationDirectory: File
}

trait MusicDestinationDirectory extends DestinationDirectory {
  def absoluteDestinationDirectory = new File(
    System.getProperty("user.home"),
    "Music"
  )
}

abstract class Directory(val sourceDirectory: String) extends DestinationDirectory with Logger {
  val absoluteSourceDirectory = new File(sourceDirectory).getAbsoluteFile

  log(s"Using ${absoluteSourceDirectory.getAbsolutePath} directory as a source")

  val destinationArtistDirectory = new File(
    absoluteDestinationDirectory.getAbsolutePath,
    absoluteSourceDirectory.getName
  )

  // Source: https://stackoverflow.com/a/7264833
  def getFileTree(f: File): Stream[File] = f #:: {
    if (f.isDirectory)
      f.listFiles().toStream.flatMap(getFileTree)
    else
      Stream.empty
  }

  def getSourceDirectoryTree = getFileTree(absoluteSourceDirectory)
}
