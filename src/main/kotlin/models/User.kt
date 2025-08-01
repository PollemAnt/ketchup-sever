package com.example.models

data class User(val username: String, val password: String)

val users = mutableListOf<User>()