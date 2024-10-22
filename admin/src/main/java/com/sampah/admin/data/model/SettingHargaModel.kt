package com.sampah.admin.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingHargaModel(
    val jenis_sampah: String = "",
    val harga_sampah: String = "",
    var settingHargaId: String = "",
    val sampahId: String = ""
): Parcelable
