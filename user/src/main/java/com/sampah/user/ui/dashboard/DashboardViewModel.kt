package com.sampah.user.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampah.user.data.model.User
import com.sampah.user.data.repository.DashboardRespository

class DashboardViewModel (private val repository: DashboardRespository) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    private val _totalPengiriman = MutableLiveData<Int>()
    val totalPengiriman: LiveData<Int> get() = _totalPengiriman

    private val _totalPendapatan = MutableLiveData<Double>()
    val totalPendapatan: LiveData<Double> get() = _totalPendapatan

    fun loadUserData(uid: String) {
        repository.getUser(uid) {
            _user.value = it
        }
    }

    fun loadTotalPengirimanDanPendapatan(uid: String) {
        repository.getTotalPengirimanDanPendapatan(uid) { totalPengiriman, totalPendapatan ->
            _totalPengiriman.value = totalPengiriman
            _totalPendapatan.value = totalPendapatan
        }
    }

}