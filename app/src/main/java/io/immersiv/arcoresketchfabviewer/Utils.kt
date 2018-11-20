package io.immersiv.arcoresketchfabviewer

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream


object Utils {

    @JvmStatic
    fun unpackZip(path: String, zipname: String, outputFolder: String): Boolean {
        val inputStream: InputStream
        val zis: ZipInputStream
        try {
            var filename: String
            inputStream = FileInputStream("$path/$zipname")
            zis = ZipInputStream(BufferedInputStream(inputStream))
            var ze: ZipEntry?
            val buffer = ByteArray(1024)
            var count: Int

            ze = zis.nextEntry
            while (ze != null) {
                filename = ze.name

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory) {
                    val fmd = File("$path/$outputFolder/$filename")
                    fmd.mkdirs()
                    ze = zis.nextEntry
                    continue
                }

                val outputFile = File("$path/$outputFolder")
                outputFile.mkdirs()

                val fout = FileOutputStream("$path/$outputFolder/$filename")

                count = zis.read(buffer)
                while (count != -1) {
                    fout.write(buffer, 0, count)
                    count = zis.read(buffer)
                }

                fout.close()
                zis.closeEntry()
                ze = zis.nextEntry
            }

            zis.close()

            File("$path/$zipname").delete()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true
    }
}