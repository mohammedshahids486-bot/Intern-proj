package com.paryavarankavalu.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {

    private const val MAX_FILE_SIZE_BYTES = 500 * 1024 // 500KB
    private const val MAX_IMAGE_DIMENSION = 1024

    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir("Images") ?: context.filesDir
        return File.createTempFile("WASTE_${timeStamp}_", ".jpg", storageDir)
    }

    /**
     * Compresses image to stay under 500KB while maintaining acceptable quality.
     */
    fun compressImage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // Decode bounds first
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()

            // Calculate sample size
            options.inSampleSize = calculateInSampleSize(options, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)
            options.inJustDecodeBounds = false

            val inputStream2 = context.contentResolver.openInputStream(uri) ?: return null
            var bitmap = BitmapFactory.decodeStream(inputStream2, null, options) ?: return null
            inputStream2.close()

            // Fix rotation using EXIF
            bitmap = fixRotation(context, uri, bitmap)

            // Save with progressive compression to stay under 500KB
            val outputFile = createImageFile(context)
            var quality = 90
            do {
                val fos = FileOutputStream(outputFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
                fos.flush()
                fos.close()
                quality -= 10
            } while (outputFile.length() > MAX_FILE_SIZE_BYTES && quality > 20)

            bitmap.recycle()
            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun compressFromFilePath(filePath: String): String {
        val file = File(filePath)
        if (!file.exists()) return filePath
        if (file.length() <= MAX_FILE_SIZE_BYTES) return filePath

        return try {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(filePath, options)

            options.inSampleSize = calculateInSampleSize(options, MAX_IMAGE_DIMENSION, MAX_IMAGE_DIMENSION)
            options.inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeFile(filePath, options) ?: return filePath

            var quality = 90
            do {
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos)
                fos.flush()
                fos.close()
                quality -= 10
            } while (file.length() > MAX_FILE_SIZE_BYTES && quality > 20)

            bitmap.recycle()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            filePath
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun fixRotation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return bitmap
            val exif = ExifInterface(inputStream)
            inputStream.close()
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                else -> return bitmap
            }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: Exception) {
            bitmap
        }
    }

    fun getFileSizeKB(path: String): Long = File(path).length() / 1024
}
