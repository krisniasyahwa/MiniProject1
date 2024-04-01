package org.d3if3087.miniproject1.ui.theme.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.d3if3087.miniproject1.R
import org.d3if3087.miniproject1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalcuLocationScreen(navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val selectedNavItem = remember { mutableStateOf(NavItem.LOCATION) }

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
                        text = stringResource(id = R.string.calculation),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                val modifier = Modifier.weight(1f)
                IconButton(
                    onClick = {
                        navController.navigate(Screen.Home.route)
                        selectedNavItem.value = NavItem.HOME
                    },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Home",
                        tint = if (selectedNavItem.value == NavItem.HOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
                }

                IconButton(
                    onClick = { },
                    modifier = modifier
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Location",
                        tint = if (selectedNavItem.value == NavItem.LOCATION) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
                }
            }
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}

@Composable
fun ScreenContent(modifier: Modifier) {
    var jarak by remember { mutableStateOf(0F) }
    var kecepatan by remember { mutableStateOf(0F) }
    var durasi by remember { mutableStateOf(0F) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = stringResource(id = R.string.calculator_intro),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = jarak.toString(),
            onValueChange = { jarak = it.toFloatOrNull() ?: 0F },
            label = {
                Text(
                    text = stringResource(R.string.jarak),
                    color = if (jarak == 0F) Color.Red else MaterialTheme.colorScheme.onBackground
                )
            },
            trailingIcon = { Text(text = "km") },
            singleLine = true,
            isError = jarak == 0F,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = kecepatan.toString(),
            onValueChange = { kecepatan = it.toFloatOrNull() ?: 0F },
            label = {
                Text(
                    text = stringResource(R.string.kecepatan),
                    color = if (kecepatan == 0F) Color.Red else MaterialTheme.colorScheme.onBackground
                )
            },
            trailingIcon = { Text(text = "km/jam") },
            singleLine = true,
            isError = kecepatan == 0F,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (jarak != 0F && kecepatan != 0F) {
                    durasi = hitungDurasi(jarak, kecepatan)
                } else {
                    Toast.makeText(context, "Tolong isi kedua field tersebut", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Text(text = stringResource(R.string.hitung))
        }
        // Tampilkan hasil di bawah tombol hitung
        Text(
            text = "Estimasi durasi perjalanan: ${durasi.toInt()} jam ${(durasi % 1 * 60).toInt()} menit",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun hitungDurasi(jarak: Float, kecepatan: Float): Float {
    return if (kecepatan != 0F) jarak / kecepatan else 0F
}

enum class NavItem { HOME, LOCATION }