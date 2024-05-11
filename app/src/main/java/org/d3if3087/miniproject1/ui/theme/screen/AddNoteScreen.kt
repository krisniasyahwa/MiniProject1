import android.app.DatePickerDialog
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3087.miniproject1.R
import org.d3if3087.miniproject1.database.NotesDb
import org.d3if3087.miniproject1.model.MainViewModel
import org.d3if3087.miniproject1.ui.theme.MiniProject1Theme
import org.d3if3087.miniproject1.ui.theme.screen.AlertDialogScreen
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(navController: NavHostController, viewModel: MainViewModel, idNotes: Long? = null) {
    var selectedDate by remember { mutableStateOf("") }
    var locationTextState by remember { mutableStateOf("") }
    var storyTextState by remember { mutableStateOf("") }
    var dateSelected by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

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
                    if (idNotes != null) {
                        DeleteAction { showDialog = true }
                    }
                }
            )
        }
    ) { padding ->
        AddNotes(
            location = locationTextState,
            onLocationChange = { locationTextState = it },
            date = selectedDate,
            onDateChange = { selectedDate = it },
            story = storyTextState,
            onStoryChange = { storyTextState = it },
            modifier = Modifier.padding(padding),
            viewModel = viewModel,
            dateSelected = dateSelected,
            onDateSelected = { dateSelected = true },
            onAddClicked = {
                val locationIsBlank = locationTextState.isBlank()
                val storyIsBlank = storyTextState.isBlank()

                if (!locationIsBlank && !storyIsBlank && dateSelected) {
                    viewModel.insert(locationTextState, selectedDate, storyTextState)
                    navController.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        R.string.invalid,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
        AlertDialogScreen(
            openDialog = showDialog,
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                viewModel.deleteById(idNotes!!)
                navController.popBackStack()
            }
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
    viewModel: MainViewModel,
    dateSelected: Boolean,
    onDateSelected: () -> Unit,
    onAddClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = location,
                onValueChange = { onLocationChange(it) },
                label = { Text(text = "Location") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }
        DatePicker(
            onDateChange = onDateChange,
            selectedDate = try {
                SimpleDateFormat.getDateInstance().parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                Date()
            },
            onDateSelected = onDateSelected
        )

        OutlinedTextField(
            value = story,
            onValueChange = { onStoryChange(it) },
            label = { Text(text = "Story") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onAddClicked
        ) {
            Text("Add")
        }
    }
}

@Composable
fun DatePicker(
    onDateChange: (String) -> Unit,
    selectedDate: Date,
    onDateSelected: () -> Unit
) {
    val context = LocalContext.current

    OutlinedTextField(
        value = SimpleDateFormat.getDateInstance().format(selectedDate),
        onValueChange = { /* No-op since this is read-only */ },
        label = { Text("Date") },
        trailingIcon = {
            IconButton(onClick = {
                val calendar = Calendar.getInstance()
                val datePicker = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = GregorianCalendar(year, month, dayOfMonth).time
                        val formattedDate = SimpleDateFormat.getDateInstance().format(selectedDate)
                        if (formattedDate.isNotEmpty()) {
                            onDateChange(formattedDate)
                            onDateSelected() // Invoke the callback when date is selected
                        } else {
                            // Handle error action if parsing fails or date is empty
                            // For example: display error message to the user or use default date
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.show()
            }) {
                Icon(Icons.Filled.DateRange, contentDescription = "Select date")
            }
        },
        readOnly = true
    )
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = {expanded = true}) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddNoteScreenPreview() {
    val context = LocalContext.current // Mendapatkan context lokal
    val database = NotesDb.getInstance(context) // Mendapatkan instance database Anda
    val dao = database.dao // Menggunakan properti dao dari instance NotesDb
    val viewModel = MainViewModel(dao)

    MiniProject1Theme {
        AddNoteScreen(rememberNavController(), viewModel) // Menggunakan viewModel, bukan dao
    }
}
