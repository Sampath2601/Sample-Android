package com.example.sample

import android.R.attr.data

object Creds {

    val VALID = User("validuser", "validpass")
    val INVALID = User("invalidusername", "invalidpadd")
    val LONG = User("Longgggggooonnn","passssswwwword")
    data class User(val username: String, val password: String)
}