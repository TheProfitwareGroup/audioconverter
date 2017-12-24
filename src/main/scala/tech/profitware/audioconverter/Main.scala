package tech.profitware.audioconverter

import java.io.File

object Main extends App {
  val sourceDirectoryFile = new File(if (args.length > 0) args(0) else ".")
  val sourceDirectory =
    if (sourceDirectoryFile.exists)
      sourceDirectoryFile.getAbsolutePath
    else "."

  val directory = new Directory(sourceDirectory) with MusicDestinationDirectory with SimpleLogger
  val converter = new Converter(directory) with FFMpegFlacToAlacConversion with SimpleLogger

  if (converter.isPresent)
    converter.convert()
  else
    converter.alertNotInstalled()
}
