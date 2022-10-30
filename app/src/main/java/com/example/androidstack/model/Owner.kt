package com.example.androidstack.model

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("profile_image") var profileImage: String? = "unknownProfile",
    @SerializedName("display_name") var displayName: String? = "unknownName"
)