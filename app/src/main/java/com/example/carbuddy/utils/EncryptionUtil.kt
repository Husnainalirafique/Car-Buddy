package com.example.carbuddy.utils

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {

    // Encryption function
    fun encrypt(plainText: String, password: String): String {
        val salt = ByteArray(16)
        // Alternatively, use SecureRandom().nextBytes(salt) for a more secure random salt
        val iv = ByteArray(16)
        val random = java.security.SecureRandom()
        random.nextBytes(salt)
        random.nextBytes(iv)

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp = factory.generateSecret(spec)
        val secret = SecretKeySpec(tmp.encoded, "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec)
        val encrypted = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        val combined = ByteArray(iv.size + salt.size + encrypted.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(salt, 0, combined, iv.size, salt.size)
        System.arraycopy(encrypted, 0, combined, iv.size + salt.size, encrypted.size)

        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    // Decryption function
    fun decrypt(cipherText: String, password: String): String {
        val combined = Base64.decode(cipherText, Base64.DEFAULT)
        val iv = ByteArray(16)
        val salt = ByteArray(16)

        System.arraycopy(combined, 0, iv, 0, iv.size)
        System.arraycopy(combined, iv.size, salt, 0, salt.size)

        val encrypted = ByteArray(combined.size - iv.size - salt.size)
        System.arraycopy(combined, iv.size + salt.size, encrypted, 0, encrypted.size)

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        val tmp = factory.generateSecret(spec)
        val secret = SecretKeySpec(tmp.encoded, "AES")

        val cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivSpec = IvParameterSpec(iv)
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secret, ivSpec)
        val decrypted = cipherDecrypt.doFinal(encrypted)

        return String(decrypted, Charsets.UTF_8)
    }
}
