package com.maslick.zxinger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class RestController {

    @Autowired lateinit var controller: Logic

    @GetMapping(value = ["bar/{string}"], produces = arrayOf(MediaType.IMAGE_PNG_VALUE))
    fun getBarcode(@PathVariable(name = "string") string: String): ByteArray {
        return controller.encodeAsBarcode(string)!!
    }

    @GetMapping(value = ["qr/{string}"], produces = arrayOf(MediaType.IMAGE_PNG_VALUE))
    fun getQRcode(@PathVariable(name = "string") string: String): ByteArray {
        return controller.encodeAsQRcode(string)!!
    }
}