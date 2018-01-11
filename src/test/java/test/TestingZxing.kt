package test

import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.HybridBinarizer
import org.junit.Assert.assertEquals
import org.junit.Test
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


class TestingZxing {

    val testFile = System.getProperty("java.io.tmpdir") + "/test.png"

    @Test
    fun testDecode() {
        assertEquals("https://www.shopify.com", decode(getFileFromResources("qrcode.png")))
        assertEquals("MEBKM:URL:http\\://en.wikipedia.org/wiki/Main_Page;;", decode(getFileFromResources("qrcode-photo.png")))
        assertEquals("123456", decode(getFileFromResources("barcode.jpg")))
    }

    @Test
    fun testEncode() {
        encodeAsQRcode("haha")
        assertEquals("haha", decode(testFile))

        encodeAsBarcode("0123456789")
        assertEquals("0123456789", decode(testFile))
    }

    fun getFileFromResources(filename: String) = ClassLoader.getSystemResource(filename).file

    fun decode(filename: String): String {
        val image = ImageIO.read(File(filename))

        val source = BufferedImageLuminanceSource(image)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        return MultiFormatReader().decode(bitmap).text
    }

    fun encodeAsQRcode(string: String) {
        val matrix = MultiFormatWriter().encode(string, BarcodeFormat.QR_CODE, 200, 200)
        val bufferedImage = MatrixToImageWriter.toBufferedImage(matrix)
        val outputfile = File(testFile)
        ImageIO.write(bufferedImage, "png", outputfile)
    }

    fun encodeAsBarcode(string: String) {
        val width = 270
        val heightCode = 120
        val heightCaption = 30

        val matrix = MultiFormatWriter().encode(string, BarcodeFormat.ITF, width, heightCode)
        val codeImage = MatrixToImageWriter.toBufferedImage(matrix)
        val outputfile = File(testFile)

        // Merge code and caption images
        val combined = BufferedImage(width, heightCode + heightCaption, BufferedImage.TYPE_INT_ARGB)
        val g2 = combined.graphics;
        g2.drawImage(codeImage, 0, 0, null);
        g2.drawImage(createCaptionImage(width, heightCaption, string), 0, heightCode, null);

        // Save to file
        ImageIO.write(combined, "png", outputfile)
    }

    fun createCaptionImage(w: Int, h: Int, string: String): BufferedImage {
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

    fun drawCenteredString(g: Graphics, text: String, rect: Rectangle, font: Font) {
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