package com.v2ray.ang.dto

data class ProfileItem(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val port: Int = 0,
    val uuid: String = "",
    val alterId: Int = 0,
    val network: String = "",
    val type: String = "",
    val tls: Boolean = false
)
