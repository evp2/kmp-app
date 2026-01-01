package com.github.evp2

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ComposeAppCommonTest {

    @Test
    fun testNavigation() = runComposeUiTest {
        setContent {
            App()
        }

        // Verify initial state (Home screen)
        onNodeWithText("My Opportunities").assertExists()

        // Navigate to Contacts
        onNodeWithText("Contacts").performClick()
        onNodeWithText("My Contacts").assertExists()

        // Navigate to Add
        onNodeWithText("Add").performClick()
        onNodeWithText("What would you like to add?").assertExists()

        // Navigate to Events
        onNodeWithText("Events").performClick()
        onNodeWithText("My Events").assertExists()

        // Navigate to Profile
        onNodeWithText("Profile").performClick()
        onNodeWithText("Profile Screen").assertExists()
    }

    @Test
    fun testAddContactFlow() = runComposeUiTest {
        setContent {
            App()
        }

        // Go to Add screen
        onNodeWithText("Add").performClick()
        
        // Select New Contact
        onNodeWithText("New Contact").performClick()
        
        // Verify form
        onNodeWithText("Save Contact").assertExists()
        
        // We can't easily type in commonTest without more setup, 
        // but we can verify the UI structure is correct.
    }
}
