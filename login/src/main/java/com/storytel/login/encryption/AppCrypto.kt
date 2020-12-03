package com.storytel.login.encryption

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AppCrypto() : PasswordCrypto {

    override fun encryptPassword(password: String): String {
        val byteArray = encrypt(KEY, password.toByteArray(charset("utf-8")))
        return byteArray.toHex()
    }

    @Throws(Exception::class)
    private fun encrypt(key: ByteArray, clear: ByteArray): ByteArray {
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(IV))
        return cipher.doFinal(clear)

    }
}

private val KEY = byteArrayOf(86, 81, 90, 66, 74, 54, 84, 68, 56, 77, 57, 87, 66, 85, 87, 84)
private val IV = byteArrayOf(106, 111, 105, 119, 101, 102, 48, 56, 117, 50, 51, 106, 51, 52, 49, 97)