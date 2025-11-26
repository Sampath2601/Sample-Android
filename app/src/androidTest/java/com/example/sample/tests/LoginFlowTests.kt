package com.example.sample.tests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sample.screens.LoginScreen
import com.example.sample.Creds
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFlowTest : BaseTest() {

    private lateinit var login: LoginScreen

    override fun setUp() {
        super.setUp()
        login = LoginScreen(composeRule)
    }

    @Test
    fun validation_enablesButtonOnlyWhenBothFieldsFilled() {
        login.assertButtonDisabled()
            .enterUsername(Creds.VALID.username)
            .assertButtonDisabled()
            .enterPassword(Creds.VALID.password)
            .assertButtonEnabled()
    }

    @Test
    fun success_navigatesToHome() {
        login.enterValidCredentials()
            .clickLogin()
            .assertNavigatedToHome()
    }

    @Test
    fun lockout_after3Failures() {
        login.enterUsername(Creds.INVALID.username)
            .enterPassword(Creds.INVALID.password)

        for (attempt in 1..3) {
            login.clickLogin()
            Thread.sleep(2000)
            if (attempt < 2) {
                login.assertErrorContains("Invalid credentials ($attempt/3)")
            }
        }
        login.assertError("Locked out after 3 failed attempts")
    }

}