package dev.vanta

import androidx.compose.ui.test.assertExists
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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

    private fun waitForHome() {
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("THE VEIL").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun launchesVeilAndExposesHomeShell() {
        waitForHome()
        composeRule.onNodeWithText("THE VEIL").assertExists()
        composeRule.onNodeWithText("SYSTEM MODULES").assertExists()
    }

    @Test
    fun exposesFabricWorkspaceModule() {
        waitForHome()
        composeRule.onNodeWithText("FABRIC").assertExists()
    }

    @Test
    fun ghostlineOpensRealAndroidBridgeControls() {
        waitForHome()
        composeRule.onNodeWithText("GHOSTLINE").performClick()
        composeRule.onNodeWithText("GHOSTLINE / ANDROID BRIDGE").assertExists()
        composeRule.onNodeWithText("CHOOSE VANTA AS HOME APP").assertExists()
    }

    @Test
    fun wardenOpensPermissionReviewPanel() {
        waitForHome()
        composeRule.onNodeWithText("WARDEN").performClick()
        composeRule.onNodeWithText("WARDEN / PERMISSION GATE").assertExists()
        composeRule.onNodeWithText("REVIEW VANTA PERMISSIONS").assertExists()
    }
    @Test
    fun mirrorCreatesARealPrivateWorkspaceProfile() {
        waitForHome()
        composeRule.onNodeWithText("MIRROR").performClick()
        composeRule.onNodeWithText("MIRROR / WORKSPACE PROFILES").assertExists()
        composeRule.onNodeWithText("Workspace profile name").performTextInput("Emulator QA")
        composeRule.onNodeWithText("CREATE WORKSPACE PROFILE").performClick()
        composeRule.onNodeWithText("Emulator QA").assertExists()
        composeRule.onNodeWithText("READY · APP-PRIVATE FILE WORKSPACE").assertExists()
    }

    @Test
    fun oracleRoutesRequestToDirectiveForReview() {
        waitForHome()
        composeRule.onNodeWithText("ORACLE").performClick()
        composeRule.onNodeWithText("ORACLE / LOCAL INTELLIGENCE ROUTER").assertExists()
        composeRule.onNodeWithText("What should VANTA process?").performTextInput("task emulator smoke test")
        composeRule.onNodeWithText("ROUTE TO DIRECTIVE").performClick()
        composeRule.onNodeWithText("ORACLE routed the request to DIRECTIVE. Review it before processing.").assertExists()
        composeRule.onNodeWithText("PROCESS DIRECTIVE").assertExists()
    }

}
