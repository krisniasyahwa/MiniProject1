package org.d3if3087.miniproject1.ui.theme.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import org.d3if3087.miniproject1.R
import org.d3if3087.miniproject1.database.NotesDb
import org.d3if3087.miniproject1.model.MainViewModel
import org.d3if3087.miniproject1.model.Notes
import org.d3if3087.miniproject1.navigation.Screen
import org.d3if3087.miniproject1.ui.theme.MiniProject1Theme
import org.d3if3087.miniproject1.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel, noteToEdit: Notes? = null) {
    val data = viewModel.data
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val selectedNavItem = remember { mutableStateOf(NavItems.HOME) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Notification.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notification"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (noteToEdit != null) {
                        navController.navigate(Screen.EditNote.route + "/${noteToEdit.id}")
                    } else {
                        navController.navigate(Screen.AddNote.route)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_riwayat),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        bottomBar = {
            BottomAppBar {
                val modifier = Modifier.weight(1f)
                IconButton(
                    onClick = {
                    },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        tint = if (selectedNavItem.value == NavItems.HOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate(Screen.CalcuLocation.route)
                        selectedNavItem.value = NavItems.LOCATION
                    },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = if (selectedNavItem.value == NavItems.LOCATION) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    ) { padding ->
        // Tampilkan konten layar utama dengan data yang telah ditambahkan
        ScreenContent(Modifier.padding(padding), navController, viewModel)
    }
}

@Composable
fun ScreenContent(modifier: Modifier, navController: NavHostController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val db = NotesDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            items(data) { note ->
               // ListActivity(navController, note) { navController.navigate(Screen.EditNote.withId(note.id)) }
                ListActivity(navController, note) { navController.navigate(Screen.EditNote.withId(note.id)) }
                Divider()
            }
        }
    }
}


@Composable
fun ListActivity(navController: NavController, note: Notes, onClick: () -> Unit) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = note.location,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = note.date,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = note.story,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = {

            },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    MiniProject1Theme {
        MainScreen(rememberNavController(), viewModel())
    }
}

enum class NavItems { HOME, LOCATION }
