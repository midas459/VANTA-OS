package dev.vanta

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VantaHomeShellTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchesVeilAndExposesHomeShell() {
        composeRule.onNodeWithText("THE VEIL").assertExists()
        composeRule.onNodeWithText("SYSTEM MODULES").assertExists()
    }

    @Test
    fun exposesFabricWorkspaceModule() {
        composeRule.onNodeWithText("FABRIC").assertExists()
    }
}
