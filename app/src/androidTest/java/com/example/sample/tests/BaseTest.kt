package com.example.sample.tests

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sample.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    open fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @After
    open fun tearDown() {

    }
}