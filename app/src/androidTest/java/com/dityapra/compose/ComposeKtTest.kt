package com.dityapra.compose

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.dityapra.compose.model.HeroData
import com.dityapra.compose.navigation.Screen
import com.dityapra.compose.ui.theme.composeTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class composeKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            composeTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                compose(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHost_clickItem_navigatesToDetailWithData() {
        composeTestRule.onNodeWithTag("lazy_list").performScrollToIndex(5)
        composeTestRule.onNodeWithText(HeroData.dummyHero[5].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailHero.route)
        composeTestRule.onNodeWithText(HeroData.dummyHero[5].name).assertIsDisplayed()
    }

    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navigateTo_AboutPage() {
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.name_author).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.email_author).assertIsDisplayed()
    }

    @Test
    fun searchShowEmptyListPlayer() {
        val incorrectSearch = "aa31z"
        composeTestRule.onNodeWithStringId(R.string.search_text).performTextInput(incorrectSearch)
        composeTestRule.onNodeWithTag("emptyList").assertIsDisplayed()
    }

    // melakukan pencarian dengan keyword yang benar, lalu cek apakah data yang dicari ada di list
    @Test
    fun searchShowListPlayer() {
        val rightSearch = "kevin"
        composeTestRule.onNodeWithStringId(R.string.search_text).performTextInput(rightSearch)
        composeTestRule.onNodeWithText("kevin").assertIsDisplayed()
    }

    @Test
    fun favoriteClickInDetailScreen_ShowInFavoriteScreen() {
        composeTestRule.onNodeWithText(HeroData.dummyHero[0].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailHero.route)
        composeTestRule.onNodeWithTag("favorite_detail_button").performClick()
        composeTestRule.onNodeWithTag("back_home").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText(HeroData.dummyHero[0].name).assertIsDisplayed()
    }

    @Test
    fun favoriteClickAndDeleteFavoriteInDetailScreen_NotShowInFavoriteScreen() {
        composeTestRule.onNodeWithText(HeroData.dummyHero[0].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailHero.route)
        composeTestRule.onNodeWithTag("favorite_detail_button").performClick()
        composeTestRule.onNodeWithTag("favorite_detail_button").performClick()
        composeTestRule.onNodeWithTag("back_home").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithStringId(R.string.empty_favorite).assertIsDisplayed()
    }

    @Test
    fun favoriteClickInHome_ShowInFavoriteScreen() {
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithTag("lazy_list").performScrollToIndex(0)
        composeTestRule.onNodeWithText(HeroData.dummyHero[0].name).assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("item_favorite_button").onFirst().performClick()
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText(HeroData.dummyHero[0].name).assertIsDisplayed()
    }

    @Test
    fun favoriteClickAndDeleteFavoriteInHome_NotShowInFavoriteScreen() {
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithTag("lazy_list").performScrollToIndex(0)
        composeTestRule.onNodeWithText(HeroData.dummyHero[0].name).assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("item_favorite_button").onFirst().performClick()
        composeTestRule.onAllNodesWithTag("item_favorite_button").onFirst().performClick()
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithStringId(R.string.empty_favorite).assertIsDisplayed()
    }

}