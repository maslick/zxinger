package com.maslick.zxinger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
class RestController {

    @Autowired lateinit var controller: Logic

    @GetMapping(value = ["bar/{string}"], produces = [MediaType.IMAGE_PNG_VALUE])
    fun getBarcode(@PathVariable(name = "string") string: String): ByteArray {
        return controller.encodeAsBarcode(string)!!
    }

    @GetMapping(value = ["qr/{string}"], produces = [MediaType.IMAGE_PNG_VALUE])
    fun getQRcode(@PathVariable(name = "string") string: String): ByteArray {
        return controller.encodeAsQRcode(string)!!
    }

    @GetMapping(value = ["pdf417/{string}"], produces = [MediaType.IMAGE_PNG_VALUE])
    fun getPDF417code(@PathVariable(name = "string") string: String): ByteArray {
        return controller.encodeAsPDF417(string)!!
    }

    @PostMapping("decode")
    fun decodeImage(@RequestParam("picture") file: MultipartFile): String {
        return controller.decodeCode(file.bytes)!!
    }
}