package com.example.androidstack.model

import com.google.gson.annotations.SerializedName

data class Owner(@SerializedName("profile_image") val profileImage: String? = "sss",
                @SerializedName("display_name") val displayName: String? = "sss"
)