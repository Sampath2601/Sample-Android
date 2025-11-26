package com.example.sample.login

import kotlinx.coroutines.delay

class AuthRepositoryImpl(
    private val simulateDelay: Boolean = true
) : AuthRepository {

    private var savedToken: String? = null

    override suspend fun login(username: String, password: String): Result<String> {

        if (simulateDelay) delay(700)

        return if (username == "test" && password == "password") {
            Result.success("fake_token_123")
        } else {
            Result.failure(Exception("Invalid login"))
        }
    }

    override suspend fun saveToken(token: String) {
        savedToken = token
        println("TOKEN SAVED → $token")
    }

}