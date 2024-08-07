package com.sampah.admin.data.model

import androidx.annotation.ColorRes

data class Statistic(
    val number: Int,
    val description: String,
    @ColorRes val backgroundColor: Int
)
