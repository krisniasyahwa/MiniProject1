package org.d3if3087.miniproject1.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.d3if3087.miniproject1.database.NotesDao
import org.d3if3087.miniproject1.model.MainViewModel
import org.d3if3087.miniproject1.ui.theme.screen.DetailViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(
    private val dao: NotesDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dao) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}