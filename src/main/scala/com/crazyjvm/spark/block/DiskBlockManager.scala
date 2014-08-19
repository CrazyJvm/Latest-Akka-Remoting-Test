package com.crazyjvm.spark.block

import java.io.File
import java.text.SimpleDateFormat
import java.util.{Date, Random}

/**
 * Created by chenchao on 14-8-19.
 */
object DiskBlockManager {

  def main(args : Array[String]){
    getFile("xyz")
  }

  def getFile(filename: String): File = {
    val subDirsPerLocalDir = 64
    val localDirs: Array[File] = createLocalDirs()
    val subDirs = Array.fill(localDirs.length)(new Array[File](subDirsPerLocalDir))

    // Figure out which local directory it hashes to, and which subdirectory in that
    val hash = nonNegativeHash("xxx")
    val dirId = hash % localDirs.length
    val subDirId = (hash / localDirs.length) % subDirsPerLocalDir

    // Create the subdirectory if it doesn't already exist
    var subDir = subDirs(dirId)(subDirId)
    if (subDir == null) {
      subDir = subDirs(dirId).synchronized {
        val old = subDirs(dirId)(subDirId)
        if (old != null) {
          old
        } else {
          val newDir = new File(localDirs(dirId), "%02x".format(subDirId))
          newDir.mkdir()
          subDirs(dirId)(subDirId) = newDir
          newDir
        }
      }
    }

    new File(subDir, filename)
  }

  def nonNegativeHash(obj: AnyRef): Int = {
    if (obj eq null) return 0
    val hash = obj.hashCode
    val hashAbs = if (Int.MinValue != hash) math.abs(hash) else 0
    hashAbs
  }

  private def createLocalDirs(): Array[File] = {
    val rootDirs = "/Users/chenchao/code/shuffle,/Users/chenchao/code/shuffle2"
    val MAX_DIR_CREATION_ATTEMPTS = 10
    println(s"Creating local directories at root dirs '$rootDirs'")
    val dateFormat = new SimpleDateFormat("yyyyMMddHHmmss")
    rootDirs.split(",").flatMap { rootDir =>
      var foundLocalDir = false
      var localDir: File = null
      var localDirId: String = null
      var tries = 0
      val rand = new Random()
      while (!foundLocalDir && tries < MAX_DIR_CREATION_ATTEMPTS) {
        tries += 1
        try {
          localDirId = "%s-%04x".format(dateFormat.format(new Date), rand.nextInt(65536))
          localDir = new File(rootDir, s"spark-local-$localDirId")
          if (!localDir.exists) {
            foundLocalDir = localDir.mkdirs()
          }
        } catch {
          case e: Exception =>
            println(s"Attempt $tries to create local dir $localDir failed", e)
        }
      }
      if (!foundLocalDir) {
        println(s"Failed $MAX_DIR_CREATION_ATTEMPTS attempts to create local dir in $rootDir." +
          " Ignoring this directory.")
        None
      } else {
        println(s"Created local directory at $localDir")
        Some(localDir)
      }
    }
  }
}


