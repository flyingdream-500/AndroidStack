package com.example.androidstack.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.androidstack.R
import com.example.androidstack.model.Question
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit



fun getDateWith(date: Long, prefix: String): String {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        getTimeFromCreated(date, prefix)
    } else {
        getTimeFromCreatedOld(date, prefix)
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun getTimeFromCreated(date: Long, prefix: String): String {

    val then = Instant.ofEpochSecond(date)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()

    val now = LocalDateTime.now()

    val timeList = arrayListOf(ChronoUnit.YEARS, ChronoUnit.MONTHS, ChronoUnit.DAYS,
            ChronoUnit.HOURS, ChronoUnit.MINUTES, ChronoUnit.SECONDS)

    val pairTime = timeList.map { it.between(then, now) to it.name.toLowerCase(Locale.getDefault()) }
            .first { it.first > 0 }

    val result = hasSingleDate(pairTime)

    return "$prefix ${result.first} ${result.second} ago"

}

fun getTimeFromCreatedOld(date: Long, prefix: String): String {

    val createdDate = TimeUnit.SECONDS.toMillis(date)
    val currentDate = Date().time
    val differenceTime = currentDate - createdDate

    val seconds = differenceTime / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    val months = days / 30
    val years = months / 12

    val timeMap = mutableMapOf(
            years to "years",
            months to "months",
            days to "days",
            hours to "hours",
            minutes to "minutes",
            seconds to "seconds").toList()
    val pairTime = timeMap.first{it.first > 0}

    val result = hasSingleDate(pairTime)

    return "$prefix ${result.first} ${result.second} ago"
}

fun hasSingleDate(pair: Pair<Long, String>): Pair<Long, String> {
    return if (pair.first < 2) {
        pair.copy(second = pair.second.dropLast(1))
    } else {
        pair
    }
}

fun colorOfVotes(context: Context, vote: Int): Int {
    return when {
        vote < -4 -> ContextCompat.getColor(context, R.color.red)
        vote in -4..4 -> ContextCompat.getColor(context,R.color.dark_grey)
        else -> ContextCompat.getColor(context,R.color.light_green)
    }
}

fun getIconState(context: Context, question: Question): Drawable? {
    //return if (question.closedReason == null) {
    return if (question.isAnswered) ContextCompat.getDrawable(context, R.drawable.ic_check_circle_accepted)
        else null
}

fun getLogosMap(): Map<String, String> {
    return  mutableMapOf(
            "python" to "file:///android_asset/python.png",
            "c" to "file:///android_asset/c.png",
            "c++" to "file:///android_asset/cplus.png",
            "c#" to "file:///android_asset/csharp.png",
            "html" to "file:///android_asset/html.jpg",
            "java" to "file:///android_asset/java.png",
            "javascript" to "file:///android_asset/js.png",
            "kotlin" to "file:///android_asset/kotlin.png",
            "php" to "file:///android_asset/php.png",
            "ruby" to "file:///android_asset/ruby.jpg",
            "swift" to "file:///android_asset/swift.png"
    )
}

fun getLogoUrl(tags: List<String>): String {

    val result = tags
        .filter { getLogosMap().containsKey(it) }
        .map { getLogosMap().getValue(it) }
        .firstOrNull()
    return result ?: "file:///android_asset/others.png"

}

fun getEncodedTitle(question: Question): String {
    return HtmlCompat.fromHtml(question.title, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}

fun getViews(question: Question): String {
    if (question.viewCount >= 1000) {
        return "${question.viewCount/1000}k"
    } else {
        return question.viewCount.toString()
    }
}


