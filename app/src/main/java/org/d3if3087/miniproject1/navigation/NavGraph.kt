package org.d3if3087.miniproject1.navigation

import AddNoteScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3087.miniproject1.database.NotesDb
import org.d3if3087.miniproject1.model.MainViewModel
import org.d3if3087.miniproject1.ui.theme.screen.AboutScreen
import org.d3if3087.miniproject1.ui.theme.screen.CalcuLocationScreen
import org.d3if3087.miniproject1.ui.theme.screen.MainScreen
import org.d3if3087.miniproject1.ui.theme.screen.NotificationScreen
import org.d3if3087.miniproject1.util.ViewModelFactory

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController(), /*viewModel: MainViewModel = viewModel()*/){
    val context = LocalContext.current
    val db = NotesDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)

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

        composable( route = Screen.EditNote.route) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("withId")?.toLongOrNull()
            id?.let {
                AddNoteScreen(navController, viewModel, it)
            } ?: run {
                AddNoteScreen(navController, viewModel)
            }
        }
        ////////////////////////////////////////
        composable(route = Screen.Notification.route) {
            NotificationScreen(navController)
        }
        composable(route = Screen.CalcuLocation.route) {
            CalcuLocationScreen(navController)
        }
    }
}