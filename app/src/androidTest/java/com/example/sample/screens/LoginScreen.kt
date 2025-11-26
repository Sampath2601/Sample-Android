package com.example.sample.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.closeSoftKeyboard
import com.example.sample.TestTags
import com.example.sample.Creds


class LoginScreen(compose: ComposeTestRule) : BaseScreen(compose) {

    private val usernameField = compose.onNodeWithTag(TestTags.USERNAME)
    private val passwordField = compose.onNodeWithTag(TestTags.PASSWORD)
    private val loginButton = compose.onNodeWithTag(TestTags.LOGIN_BUTTON)
    private val rememberMe = compose.onNodeWithTag(TestTags.REMEMBER)

    fun enterValidCredentials(): LoginScreen = apply {
        enterUsername(Creds.VALID.username)
        enterPassword(Creds.VALID.password)
    }

    fun enterUsername(text: String): LoginScreen = apply {
        usernameField.performTextInput(text)
        closeSoftKeyboard()
    }

    fun enterPassword(text: String): LoginScreen = apply {
        passwordField.performTextInput(text)
        closeSoftKeyboard()
    }

    fun clearFields(): LoginScreen = apply {
        usernameField.performTextClearance()
        passwordField.performTextClearance()
    }

    fun clickLogin(): LoginScreen = apply {
        loginButton.performClick()
    }
    fun toggleRememberMe(): LoginScreen = apply {
        rememberMe.performClick()
    }

    fun assertButtonEnabled(): LoginScreen = apply {
        loginButton.assertIsEnabled()
    }
    fun assertButtonDisabled(): LoginScreen = apply {
        loginButton.assertIsNotEnabled()
    }

    fun assertError(text: String): LoginScreen = apply {
        compose.onNodeWithText(text).assertIsDisplayed()
    }

    fun assertErrorContains(substring: String): LoginScreen = apply {
        compose.onNode(hasText(substring, substring = true)).assertIsDisplayed()
    }

    fun assertNavigatedToHome() : LoginScreen = apply {
        compose.onNodeWithTag("home_screen").assertIsDisplayed()
    }
}