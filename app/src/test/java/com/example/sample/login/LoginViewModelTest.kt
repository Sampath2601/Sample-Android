package com.example.sample.login

import android.app.Application
import com.example.sample.Creds
import com.example.sample.MockNetworkMonitor
import com.example.sample.auth.TokenStorage
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {


    private val testDispatcher = StandardTestDispatcher()

    // mocks
    private lateinit var viewModel: LoginViewModel
    private val repository = mockk<AuthRepository>(relaxed = true)
    private val networkMonitor = mockk<MockNetworkMonitor>()
    private val tokenStorage = mockk<TokenStorage>(relaxed = true)
    private val application = mockk<Application>(relaxed = true)

    @BeforeEach
    fun setUp() {
        // Replace main dispatcher
        Dispatchers.setMain(testDispatcher)

        every { networkMonitor.isOnline } returns MutableStateFlow(true)

        // Instantiate VM with mocks
        viewModel = LoginViewModel(application, repository, networkMonitor, tokenStorage)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    // MARK: - 1. Validation & Button Enable/Disable

    @Test
    fun `login button is disabled initially`() = runTest(testDispatcher) {
        // Default State
        assertFalse(viewModel.uiState.value.isButtonEnabled)
    }

    @Test
    fun `login button enabled only when both fields are valid`() = runTest(testDispatcher) {

        // Empty
        viewModel.onUsernameChanged("")
        viewModel.onPasswordChanged("")
        assertFalse(viewModel.uiState.value.isButtonEnabled)

        // Username long
        viewModel.onUsernameChanged(Creds.LONG.username)
        viewModel.onPasswordChanged(Creds.VALID.password)
        assertFalse(viewModel.uiState.value.isButtonEnabled)

        // Password long
        viewModel.onUsernameChanged(Creds.VALID.username)
        viewModel.onPasswordChanged(Creds.LONG.password)
        assertFalse(viewModel.uiState.value.isButtonEnabled)

        // Both valid
        viewModel.onUsernameChanged(Creds.VALID.username)
        viewModel.onPasswordChanged(Creds.VALID.password)
        assertTrue(viewModel.uiState.value.isButtonEnabled)
    }

    // MARK: - 2. Login Success without Remember Me

    @Test
    fun `login success without remember me does not save token`() = runTest(testDispatcher) {

        coEvery { repository.login(Creds.VALID.username, Creds.VALID.password) } returns Result.success("token")

        // Login Flow
        viewModel.onUsernameChanged(Creds.VALID.username)
        viewModel.onPasswordChanged(Creds.VALID.password)
        viewModel.onRememberChanged(false)
        viewModel.login()

        advanceUntilIdle()

        // Check Authentication Token
        assertTrue(viewModel.uiState.value.navigateHome)
        assertEquals("token", viewModel.uiState.value.authToken)
        coVerify(exactly = 0) { tokenStorage.saveToken(any()) }
    }

    // MARK: - 3. Failed login increments count + 4. Lockout after 3 failures

    @Test
    fun `three failed logins show correct messages and lock out user`() = runTest(testDispatcher) {
        coEvery {
            repository.login(any(), any())
        } returns Result.failure(Exception())

        viewModel.onUsernameChanged(Creds.INVALID.username)
        viewModel.onPasswordChanged(Creds.INVALID.password)

        // 1st failure
        viewModel.login()
        advanceUntilIdle()
        assertEquals("Invalid credentials (1/3)", viewModel.uiState.value.errorMessage)

        // 2nd failure
        viewModel.login()
        advanceUntilIdle()
        assertEquals("Invalid credentials (2/3)", viewModel.uiState.value.errorMessage)

        // 3rd failure
        viewModel.login()
        advanceUntilIdle()
        assertEquals("Locked out after 3 failed attempts", viewModel.uiState.value.errorMessage)
        assertTrue(viewModel.uiState.value.lockedOut)
    }

    // MARK: - 5. Offline → show message, no service call

    @Test
    fun `login when offline shows error and does not call repository`() = runTest(testDispatcher) {

        // Set Network
        val networkMonitor = MockNetworkMonitor().apply {
            setOnline(false)
        }

        // Reinstantiate with new Network variable
        viewModel = LoginViewModel(application, repository, networkMonitor, tokenStorage)

        advanceUntilIdle()

        viewModel.onUsernameChanged(Creds.VALID.username)
        viewModel.onPasswordChanged(Creds.VALID.password)
        viewModel.login()
        advanceUntilIdle()

        // Check Offline State
        assertTrue(viewModel.uiState.value.isOffline)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    // MARK: - 6. Remember Me saves token

    @Test
    fun `login success with remember me saves token`() = runTest(testDispatcher) {

        coEvery { repository.login(Creds.VALID.username, Creds.VALID.password) } returns Result.success("auth-123")

        // login and save token
        viewModel.onUsernameChanged(Creds.VALID.username)
        viewModel.onPasswordChanged(Creds.VALID.password)
        viewModel.onRememberChanged(true)
        viewModel.login()

        advanceUntilIdle()

        // Check Token Saved
        assertEquals("auth-123", viewModel.uiState.value.authToken)
        assertTrue(viewModel.uiState.value.navigateHome)
        coVerify { tokenStorage.saveToken("auth-123") }
    }

    // MARK: - 7. Logout clears everything

    @Test
    fun `logout clears token and resets UI state`() = runTest(testDispatcher) {
        // login and save token
        coEvery { repository.login(Creds.VALID.username, Creds.VALID.password) } returns Result.success("saved")
        viewModel.onUsernameChanged(Creds.VALID.username)
        viewModel.onPasswordChanged(Creds.VALID.password)
        viewModel.onRememberChanged(true)
        viewModel.login()
        advanceUntilIdle()

        viewModel.logout()

        val state = viewModel.uiState.value
        assertNull(state.authToken)
        assertFalse(state.navigateHome)
        assertTrue(state.username.isEmpty())
        assertTrue(state.password.isEmpty())
        assertFalse(state.rememberMe)
        coVerify { tokenStorage.clear() }
    }
}