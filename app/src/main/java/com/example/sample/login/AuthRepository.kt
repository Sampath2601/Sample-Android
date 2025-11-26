package com.example.sample.login

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<String>
    suspend fun saveToken(token: String)
}
