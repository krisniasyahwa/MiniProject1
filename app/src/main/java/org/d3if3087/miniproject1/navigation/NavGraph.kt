package org.d3if3087.miniproject1.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.d3if3087.miniproject1.model.MainViewModel
import org.d3if3087.miniproject1.ui.theme.screen.AboutScreen
import org.d3if3087.miniproject1.ui.theme.screen.AddNoteScreen
import org.d3if3087.miniproject1.ui.theme.screen.AddNotes
import org.d3if3087.miniproject1.ui.theme.screen.CalcuLocationScreen
import org.d3if3087.miniproject1.ui.theme.screen.MainScreen
import org.d3if3087.miniproject1.ui.theme.screen.NotificationScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController(), viewModel: MainViewModel = viewModel()){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route) {
            MainScreen(navController, viewModel)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
        composable(route = Screen.AddNote.route) {
            AddNoteScreen(navController, viewModel)
        }
        composable(route = Screen.Notification.route) {
            NotificationScreen(navController)
        }
        composable(route = Screen.CalcuLocation.route) {
            CalcuLocationScreen(navController)
        }
    }
}