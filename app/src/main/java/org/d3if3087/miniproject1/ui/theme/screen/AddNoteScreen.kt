package org.d3if3087.miniproject1.ui.theme.screen



import android.app.DatePickerDialog
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3087.miniproject1.R
import org.d3if3087.miniproject1.database.NotesDb
import org.d3if3087.miniproject1.model.MainViewModel
import org.d3if3087.miniproject1.model.Notes
import org.d3if3087.miniproject1.ui.theme.MiniProject1Theme
import org.d3if3087.miniproject1.util.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(navController: NavHostController, viewModel: MainViewModel, idNotes: Long? = null/*, idNotes: Notes? = null*/) {
//    var selectedDate = remember { mutableStateOf(System.currentTimeMillis()) }
//    var locationTextState = remember { mutableStateOf("") }
//    var storyTextState = remember { mutableStateOf("") }
    var selectedDate by remember {  mutableStateOf("")}
    var locationTextState by remember { mutableStateOf("") }
    var storyTextState by remember { mutableStateOf("") }
    LaunchedEffect(true) {
        if (idNotes != null) {
            val data = viewModel.getNotesById(idNotes)!!
            locationTextState = data.location
            selectedDate = data.date
            storyTextState = data.story
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (idNotes == null)
                        Text(text = stringResource(id = R.string.app_name))
                    else
                        Text(text = stringResource(id = R.string.edit_riwayat))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (locationTextState.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.invalid,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }

                            if (selectedDate.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.invalid,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }

                            if (storyTextState.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.invalid,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }

                            if (idNotes == null) {
                                viewModel.insert(locationTextState, selectedDate, storyTextState)
                            } else {
                                viewModel.update(locationTextState, selectedDate, storyTextState, idNotes)
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

//                    if (idNotes != null) {
//                        DeleteAction {
//                            showDialog = true
//                        }
//                        DisplayAlertDialog(
//                            openDilog = showDialog,
//                            onDismissRequest = { showDialog = false },
//                        ) {
//                            showDialog = false
//                            viewModel.deleteOutfitsDaoById(id)
//                            navController.popBackStack()
//                        }
//                    }
                }
            )
        }
    ) { padding ->
        // Apply padding to the content
//        AddNotes(
//            contentPadding = padding,
//            navController = navController,
//            viewModel = viewModel,
//            selectedDate = selectedDate,
//            locationTextState = locationTextState,
//            storyTextState = storyTextState
//        )
        AddNotes(
            location = locationTextState,
            onLocationChange = { locationTextState = it },
            date = selectedDate,
            onDateChange = { selectedDate = it },
            story = storyTextState,
            onStoryChange = { storyTextState = it },
            modifier = Modifier.padding(padding),
            viewModel = viewModel
        )
    }
}

@Composable
fun AddNotes(
    location: String,
    onLocationChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    story: String,
    onStoryChange: (String) -> Unit,
    modifier: Modifier,
    viewModel: MainViewModel
) {
    var dateSelected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = location,
            onValueChange = { onLocationChange(it) },
            label = { Text(text = "Location") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = date,
            onValueChange = { },
            label = { Text("Date") },
            trailingIcon = {
                IconButton(onClick = {
                    dateSelected = true
                }) {
                    Icon(Icons.Filled.DateRange, contentDescription = null)
                }
            },
            readOnly = true,
            isError = !dateSelected
        )

        OutlinedTextField(
            value = story,
            onValueChange = { onStoryChange(it) },
            label = { Text(text = "Story") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val locationIsBlank = location.isBlank()
                val storyIsBlank = story.isBlank()

                if (!locationIsBlank && !storyIsBlank && dateSelected) {
                    viewModel.insert(location, date, story)
                    // Kembali ke MainScreen
                    // navController.popBackStack()
                } else {
                    // Set isError menjadi true untuk menampilkan pesan kesalahan jika tidak semua input terpenuhi
                    // locationTextStateError.value = locationIsBlank
                    // storyTextStateError.value = storyIsBlank
                }
            }
        ) {
            Text("Add")
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "More options",
            tint = MaterialTheme.colors.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    delete()
                }
            ) {
                Text(text = "Delete")
            }
        }
    }
}


//@Composable
//fun AddNotes(
//    location: String, onLocationChange: (String) -> Unit,
//    date: String, onDateChange: (String) -> Unit,
//    story: String, onStoryChange: (String) -> Unit,
//    modifier: Modifier,
//    viewModel: MainViewModel
//) {
////    val context = LocalContext.current
////    val currentDate = Date()
////    val locationTextState = remember { mutableStateOf("") }
////    val locationTextStateError = remember { mutableStateOf(false) }
////    var selectedDate by remember { mutableStateOf(currentDate) }
////    var dateString by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy").format(currentDate)) }
////    val storyTextState = remember { mutableStateOf("") }
////    val storyTextStateError = remember { mutableStateOf(false) }
////    val dateSelected = remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//
//    ) {
//        OutlinedTextField(
//            value = location,
//            onValueChange = { onLocationChange(it) },
//            label = { Text(text = stringResource(id = R.string.location)) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                capitalization = KeyboardCapitalization.Words,
////                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        TextField(
//            value = dateString,
//            onValueChange = { /* Tidak melakukan perubahan karena ini hanya pembacaan */ },
//            label = { Text("Date") },
//            trailingIcon = {
//                IconButton(onClick = {
//                    val calendar = Calendar.getInstance()
//                    DatePickerDialog(context, { _, year, month, dayOfMonth ->
//                        val date = GregorianCalendar(year, month, dayOfMonth).time
//                        selectedDate = date
//                        dateString = SimpleDateFormat("dd/MM/yyyy").format(date)
//                        dateSelected.value = true // Set dateSelected menjadi true saat tanggal dipilih
//                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
//                }) {
//                    Icon(Icons.Filled.DateRange, contentDescription = null)
//                }
//            },
//            readOnly = true,
//            isError = !dateSelected.value // Set isError menjadi true jika tanggal belum dipilih
//        )
//
//        OutlinedTextField(
//            value = story,
//            onValueChange = { onStoryChange(it) },
//            label = { Text(text = stringResource(id = R.string.story)) },
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(
//                capitalization = KeyboardCapitalization.Words,
////                imeAction = ImeAction.Next
//            ),
//            modifier = Modifier.fillMaxWidth()
//        )
//        Button(
//            onClick = {
//                val locationIsBlank = location.isBlank()
//                val storyIsBlank = story.isBlank()
//
//                if (!locationIsBlank && !storyIsBlank && dateSelected.value) {
//                    viewModel.insert(
//                        location: String,
//                        date: String,
//                        story: String
////                        locationTextState.value,
////                        SimpleDateFormat("dd/MM/yyyy").format(Date()),
////                        storyTextState.value
//                    )
//                    // Kembali ke MainScreen
//                    navController.popBackStack()
//                } else {
//                    // Set isError menjadi true untuk menampilkan pesan kesalahan jika tidak semua input terpenuhi
//                    locationTextStateError.value = locationIsBlank
//                    storyTextStateError.value = storyIsBlank
//                }
//            }
//        ) {
//            Text("Add")
//        }
//    }
//}
//
//@Composable
//fun DeleteAction(delete: () -> Unit) {
//    var expanded by remember {
//        mutableStateOf(false)
//    }
//
//    IconButton(onClick = { /*TODO*/ }) {
//        Icon(
//            imageVector = Icons.Filled.MoreVert,
//            contentDescription = stringResource(R.string.lainnya),
//            tint = MaterialTheme.colorScheme.primary
//        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//            ) {
//            DropdownMenuItem(
//                text = {
//                       Text(text = stringResource(id = R.string.hapus))
//                       },
//                onClick = {
//                    expanded = false
//                    delete()
//                })
//        }
//    }
//}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddNoteScreenPreview() {
//    val viewModel = MainViewModel()
//    viewModel.addData("Location", "Date", "Story")
    MiniProject1Theme {
        AddNoteScreen(rememberNavController(), viewModel())
    }
}