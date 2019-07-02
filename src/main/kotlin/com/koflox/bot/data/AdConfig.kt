package com.koflox.bot.data

data class AdConfig(
    val highRiskFlags: List<String> = listOf(),
    val safeFlags: List<Any> = listOf(),
    val showsAds: Boolean = false,
    val unsafeFlags: List<String> = listOf()
)