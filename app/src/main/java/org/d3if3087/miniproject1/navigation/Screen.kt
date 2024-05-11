package org.d3if3087.miniproject1.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object AddNote: Screen("addNoteScreen")
    data object EditNote: Screen("addNoteScreen/{withId}") {
        fun withId(withId: Long): String {
            val routeWithId = route.replace("{withId}", withId.toString())
            println("EditTask: TaskId included in route: $routeWithId") // Buat nge cek route dengan withId
            return routeWithId
        }
    }
    data object Notification: Screen("notification")
    data object CalcuLocation: Screen("calcuLocation")
}