package com.example.mobilequizapplication.Domain.Enum

import com.google.gson.annotations.SerializedName

enum class Difficulty(val apiValue: String) {
    @SerializedName("easy")
    Easy("easy"),
    @SerializedName("medium")
    Medium("medium"),
    @SerializedName("hard")
    Hard("hard")
}