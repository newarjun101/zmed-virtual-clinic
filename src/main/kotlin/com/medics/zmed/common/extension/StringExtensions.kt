package com.medics.zmed.common.extension

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun String.encryptAES( secret: String): String {
    val key = SecretKeySpec(secret.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding") // or AES/GCM/NoPadding for more security
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val encryptedBytes = cipher.doFinal(this.toByteArray())
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

fun String.decryptAES( secret: String): String {
    val key = SecretKeySpec(secret.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key)
    val decodedBytes = Base64.getDecoder().decode(this)
    return String(cipher.doFinal(decodedBytes))
}