package org.d3if3087.miniproject1.model

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.d3if3087.miniproject1.database.NotesDao
import java.util.Date

class MainViewModel(val dao: NotesDao) : ViewModel() {

    val data: StateFlow<List<Notes>> = dao.getNotes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
    suspend fun getNotesById(id: Long): Notes? {
        return dao.getNotesById(id)
    }
    fun insert(location: String, date: String, story: String) {
        val notes = Notes(
            location = location,
            date = date,
            story = story

        )
        viewModelScope.launch {
            dao.insert(notes)
        }

    }

    fun update(location: String, date: String, story: String, id : Long){
        val notes = Notes(
            id = id,
            location = location,
            date = date,
            story = story
        )
        viewModelScope.launch {
            dao.update(notes)
        }
    }

    fun deleteById(id: Long){
        viewModelScope.launch {
            dao.deleteById(id)
        }
    }

}




