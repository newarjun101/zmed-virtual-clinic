package com.medics.zmed.common.util

object ChatUtil {
    fun generateChatId(userA: Long, userB: Long): String {
        return if (userA < userB) {
            "${userA}_${userB}"
        } else {
            "${userB}_${userA}"
        }
    }
}
