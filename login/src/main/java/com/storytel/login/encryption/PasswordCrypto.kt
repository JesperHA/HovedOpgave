package com.storytel.login.encryption

interface PasswordCrypto {
    fun encryptPassword(password : String) : String
}