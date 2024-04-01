package org.d3if3087.miniproject1.model

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

class MainViewModel : ViewModel() {
    // Variabel untuk menyimpan data catatan
    private val _data = mutableStateListOf<Notes>()
    private val _notes = mutableStateListOf<Notes>()
    val notes: List<Notes> get() = _notes
    val data: List<Notes>
        get() = _data

    // Method untuk menambahkan catatan baru
    fun addData(location: String, date: String, story: String) {
        _notes.add(Notes(location = location, date = date, story = story))
        _data.add(
            Notes(
                location, date, story
            )
        )
    }

    // Metode untuk mendapatkan data yang telah ditambahkan
    // Fungsi ini digunakan untuk mengambil data aktual, bukan data dummy
    // Anda bisa menambahkan logika lain sesuai kebutuhan aplikasi di sini
    fun getDataAdded(): List<Notes> {
        println(_data)
        return _data
    }
}
