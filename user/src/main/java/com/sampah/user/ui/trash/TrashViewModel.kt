package com.sampah.user.ui.trash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sampah.user.data.model.JemputSampah
import com.sampah.user.data.repository.TrashRepository

class TrashViewModel(private val repository: TrashRepository): ViewModel() {

    private val _hargaMap = MutableLiveData<Map<String, String>>()
    val hargaMap: LiveData<Map<String, String>> get() = _hargaMap

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> get() = _isSaved


    fun loadHarga(){
        repository.getSettingHarga { data ->
            _hargaMap.value = data
        }
    }

    fun saveJemputSampah(jemputSampah: JemputSampah){
        repository.saveJemputSampah(jemputSampah){ success ->
            _isSaved.value = success
        }
    }
}