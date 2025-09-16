package com.medics.zmed.common.extension

import jakarta.servlet.http.HttpServletRequest



fun HttpServletRequest.getHeaderAccessToken()  : String?{
    val authHeader = this.getHeader("Authorization") ?: return null
    return if (authHeader.startsWith("Bearer ")) authHeader.removePrefix("Bearer ").trim() else null

}