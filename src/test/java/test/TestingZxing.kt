package test

import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.common.HybridBinarizer
import org.junit.Test
import java.io.File
import javax.imageio.ImageIO

class TestingZxing {

    @Test
    fun test() {
        println(decode("qrcode.png"))
        println(decode("qrcode-photo.png"))
        println(decode("barcode.jpg"))
    }


    fun decode(filename: String): String {
        val path = ClassLoader.getSystemResource(filename).file
        val image = ImageIO.read(File(path))

        val source = BufferedImageLuminanceSource(image)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        return MultiFormatReader().decode(bitmap).text
    }
}