package com.maslick.zxinger

import com.google.zxing.*
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.HybridBinarizer
import org.springframework.stereotype.Controller
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream



@Controller
class Logic {

    fun encodeAsBarcode(string: String): ByteArray? {
        val width = 270
        val heightCode = 120
        val heightCaption = 30

        val matrix = MultiFormatWriter().encode(string, BarcodeFormat.ITF, width, heightCode)
        val codeImage = MatrixToImageWriter.toBufferedImage(matrix)

        // Merge code and caption images
        val combined = BufferedImage(width, heightCode + heightCaption, BufferedImage.TYPE_INT_ARGB)
        val g2 = combined.graphics;
        g2.drawImage(codeImage, 0, 0, null);
        g2.drawImage(createCaptionImage(width, heightCaption, string), 0, heightCode, null);

        // Return byte array
        val baos = ByteArrayOutputStream()
        ImageIO.write(combined, "png", baos)
        return baos.toByteArray()
    }

    fun encodeAsQRcode(string: String): ByteArray? {
        val matrix = MultiFormatWriter().encode(string, BarcodeFormat.QR_CODE, 200, 200)
        val bufferedImage = MatrixToImageWriter.toBufferedImage(matrix)

        // Return byte array
        val baos = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", baos)
        return baos.toByteArray()
    }

    fun decodeCode(img: ByteArray): String? {
        val bais = ByteArrayInputStream(img)
        val source = BufferedImageLuminanceSource(ImageIO.read(bais))
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            return MultiFormatReader().decode(bitmap).text
        }
        catch (e: NotFoundException) {
            return "not found"
        }
        catch (e: Exception) {
            return "error!"
        }
    }

    private fun createCaptionImage(w: Int, h: Int, string: String): BufferedImage {
        val textImage = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        val g1 = textImage.graphics

        // Fill image with white
        g1.color = Color.white
        g1.fillRect(0, 0, textImage.width, textImage.height)

        // Create rectangle with text
        val rectangle = Rectangle(0, 0, w, h)
        val font = Font("Dialog", Font.PLAIN, 20)
        g1.color = Color.black
        drawCenteredString(g1, string, rectangle, font)

        return textImage
    }

    private fun drawCenteredString(g: Graphics, text: String, rect: Rectangle, font: Font) {
        // Get the FontMetrics
        val metrics = g.getFontMetrics(font)
        // Determine the X coordinate for the text
        val x = rect.x + (rect.width - metrics.stringWidth(text)) / 2
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        val y = rect.y + (rect.height - metrics.height) / 2 + metrics.ascent
        // Set the font
        g.font = font
        // Draw the String
        g.drawString(text, x, y)
    }
}