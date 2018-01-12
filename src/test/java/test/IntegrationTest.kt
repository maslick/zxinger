package test

import com.maslick.zxinger.App
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = DEFINED_PORT, classes = [(App::class)])
@TestPropertySource("classpath:test.properties")
class IntegrationTest {

    val file = File(ClassLoader.getSystemResource("qrcode.png").file)

    @Test
    fun dummyTest() {
        val rb = RequestBody.create(MediaType.parse("image/*"), file)
        val data = MultipartBody.Part.createFormData("picture", file.name, rb)
        val result = getRetrofitUploader().decodeCode(data).execute().body()
        Assert.assertEquals("https://www.shopify.com", result)
    }

    private fun getRetrofitUploader(): RetrofitCode {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        return retrofit.create<RetrofitCode>(RetrofitCode::class.java)
    }

    interface RetrofitCode {
        @Multipart
        @POST("decode")
        fun decodeCode(@Part file: MultipartBody.Part): Call<String>
    }
}