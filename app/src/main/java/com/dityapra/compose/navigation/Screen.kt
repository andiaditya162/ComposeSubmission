package com.dityapra.compose.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object Profile : Screen("profile")
    object DetailHero : Screen("home/{heroId}") {
        fun createRoute(heroId: Int) = "home/$heroId"
    }
}