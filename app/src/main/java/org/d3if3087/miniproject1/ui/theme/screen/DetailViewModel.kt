//package org.d3if3087.miniproject1.ui.theme.screen
//
//import androidx.lifecycle.viewmodel.compose.viewModel
//import kotlinx.coroutines.Dispatchers
//import org.d3if3087.miniproject1.database.NotesDao
//import org.d3if3087.miniproject1.model.Notes
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class DetailViewModel(private val dao: NotesDao) : viewModel() {
//
//    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
//    fun insert(location: String, date: String, story: String) {
//        val notes = Notes(
//            location = location,
//            date = formatter.format(Date()),
//            story = story
//        )
//
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.insert(notes)
//        }
//    }
//
//    suspend fun getNotes(id: Long): Notes? {
//        return dao.getNotesById(id)
//    }
//
//
//    fun update(id: Long, location: String, date: String, story: String) {
//        val notes = Notes(
//            id = id,
//            location = location,
//            date = formatter.format(Date()),
//            story = story
//        )
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.update(notes)
//        }
//    }
//
//    fun delete(id: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.deleteById(id)
//        }
//    }
//    fun getNotes(id: Long): Notes? {
//        return notesData.find { it.id == id }
//    }
//}
