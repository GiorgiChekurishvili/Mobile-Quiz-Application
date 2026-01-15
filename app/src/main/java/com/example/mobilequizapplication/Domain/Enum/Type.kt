package com.example.mobilequizapplication.Domain.Enum

import com.google.gson.annotations.SerializedName

enum class Type(val apiValue: String) {
    @SerializedName("multiple")
    MultipleChoice("multiple"),
    @SerializedName("boolean")
    TrueFalse("boolean")
}