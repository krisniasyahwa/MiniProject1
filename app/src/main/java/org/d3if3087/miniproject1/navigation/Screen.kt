package org.d3if3087.miniproject1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object AddNote: Screen("addNoteScreen")
    data object Notification: Screen("notification")
    data object CalcuLocation: Screen("calcuLocation")
}