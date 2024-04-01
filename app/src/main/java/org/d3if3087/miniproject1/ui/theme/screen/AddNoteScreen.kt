package org.d3if3087.miniproject1.ui.theme.screen



import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3087.miniproject1.R
import org.d3if3087.miniproject1.model.MainViewModel
import org.d3if3087.miniproject1.ui.theme.MiniProject1Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(navController: NavHostController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val selectedDate = remember { mutableStateOf(System.currentTimeMillis()) }
    val locationTextState = remember { mutableStateOf("") }
    val storyTextState = remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.add_notes)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        // Apply padding to the content
        AddNotes(
            contentPadding = padding,
            navController = navController,
            viewModel = viewModel,
            selectedDate = selectedDate,
            locationTextState = locationTextState,
            storyTextState = storyTextState
        )
    }
}

@Composable
fun AddNotes(
    contentPadding: PaddingValues,
    navController: NavHostController,
    viewModel: MainViewModel,
    selectedDate: MutableState<Long>,
    locationTextState: MutableState<String>,
    storyTextState: MutableState<String>
) {
    val context = LocalContext.current
    val currentDate = Date()
    val locationTextState = remember { mutableStateOf("") }
    val locationTextStateError = remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(currentDate) }
    var dateString by remember { mutableStateOf(SimpleDateFormat("dd/MM/yyyy").format(currentDate)) }
    val storyTextState = remember { mutableStateOf("") }
    val storyTextStateError = remember { mutableStateOf(false) }
    val dateSelected = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(
            value = locationTextState.value,
            onValueChange = { locationTextState.value = it },
            label = { Text("Location") },
            isError = locationTextState.value.isBlank(),
            singleLine = true,
            trailingIcon = {
                if (locationTextState.value.isNotBlank()) {
                    null
                } else {
                    Icon(Icons.Filled.Warning, contentDescription = null)
                }
            }
        )

        TextField(
            value = dateString,
            onValueChange = { /* Tidak melakukan perubahan karena ini hanya pembacaan */ },
            label = { Text("Date") },
            trailingIcon = {
                IconButton(onClick = {
                    val calendar = Calendar.getInstance()
                    DatePickerDialog(context, { _, year, month, dayOfMonth ->
                        val date = GregorianCalendar(year, month, dayOfMonth).time
                        selectedDate = date
                        dateString = SimpleDateFormat("dd/MM/yyyy").format(date)
                        dateSelected.value = true // Set dateSelected menjadi true saat tanggal dipilih
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                }) {
                    Icon(Icons.Filled.DateRange, contentDescription = null)
                }
            },
            readOnly = true,
            isError = !dateSelected.value // Set isError menjadi true jika tanggal belum dipilih
        )

        TextField(
            value = storyTextState.value,
            onValueChange = { storyTextState.value = it },
            label = { Text("Story") },
            isError = storyTextState.value.isBlank(),
            singleLine = true,
            trailingIcon = {
                if (storyTextState.value.isNotBlank()) {
                    null
                } else {
                    Icon(Icons.Filled.Warning, contentDescription = null)
                }
            }
        )
        Button(
            onClick = {
                // Validasi input
                val locationIsBlank = locationTextState.value.isBlank()
                val storyIsBlank = storyTextState.value.isBlank()

                // Jika tidak ada kesalahan dan tanggal sudah dipilih, tambahkan data
                if (!locationIsBlank && !storyIsBlank && dateSelected.value) {
                    viewModel.addData(
                        locationTextState.value,
                        SimpleDateFormat("dd/MM/yyyy").format(Date()),
                        storyTextState.value
                    )
                    // Kembali ke MainScreen
                    navController.popBackStack()
                } else {
                    // Set isError menjadi true untuk menampilkan pesan kesalahan jika tidak semua input terpenuhi
                    locationTextStateError.value = locationIsBlank
                    storyTextStateError.value = storyIsBlank
                }
            }
        ) {
            Text("Add")
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddNoteScreenPreview() {
    val viewModel = MainViewModel()
    viewModel.addData("Location", "Date", "Story")
    MiniProject1Theme {
        AddNoteScreen(rememberNavController(), viewModel())
    }
}
