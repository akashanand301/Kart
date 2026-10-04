package com.example.kart.ui.main

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import com.example.kart.MainActivity
import org.junit.Rule
import org.junit.Test

/** UI tests for Kart App. */
class KartAppUiTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<MainActivity>()

  @Test
  fun appStarts_and_displaysTopBarTitle() {
    // The top app bar should have the "Kart" title.
    composeTestRule.onNodeWithText("Kart").assertIsDisplayed()
  }

  @Test
  fun appStarts_and_displaysSearchField() {
    // The search bar should be visible on the home screen.
    composeTestRule.onNodeWithText("Search products...").assertIsDisplayed()
  }
}
