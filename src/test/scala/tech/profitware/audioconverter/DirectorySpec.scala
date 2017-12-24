package tech.profitware.audioconverter

import org.scalatest._
import java.io.File

class DirectorySpec extends FlatSpec with Matchers {
  val currentDirectory = new Directory(".") with MusicDestinationDirectory with SimpleLogger

  "Current directory" should "be absolute" in {
    currentDirectory.absoluteSourceDirectory.getAbsolutePath shouldEqual new File(".").getAbsolutePath
  }

  "Music destination directory" should "end with Music" in {
    currentDirectory.absoluteDestinationDirectory.getName shouldEqual "Music"
  }

  "Artist destination directory" should "be equal to current source directory name" in {
    currentDirectory.destinationArtistDirectory.getName shouldEqual new File(".").getName
  }
}
