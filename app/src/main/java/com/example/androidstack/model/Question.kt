package com.example.androidstack.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.androidstack.util.DEFAULT_SEARCH
import com.example.androidstack.util.ORDER_ASC
import com.example.androidstack.util.SORT_CREATION
import com.google.gson.annotations.SerializedName
import java.util.stream.Collectors

@Entity(tableName = "dbcache")
data class Question(val title: String,
               val link: String,
               @PrimaryKey
               @SerializedName("question_id")
               val id: Int,
               val tags: List<String>,
               @SerializedName("is_answered")
               val isAnswered: Boolean,
               @SerializedName("view_count")
               val viewCount: Int,
               @SerializedName("answer_count")
               val answerCount: Int,
               val score: Int,
               @SerializedName("last_activity_date")
               val activityDate: Long,
               @SerializedName("creation_date")
               val creationDate: Long,
               @Embedded
               val owner: Owner) {
    var query: String = DEFAULT_SEARCH
    var sort: String = SORT_CREATION
    var order: String = ORDER_ASC
}

class OwnerConverter {
    @TypeConverter
    fun fromOwner(owners: List<String?>): String {
        owners.forEach {  }
        return owners.joinToString(",")
        //return owners.stream().collect(Collectors.joining(","))
    }

    @TypeConverter
    fun toOwner(data: String): List<String> {
        return data.split(",")
    }
}
