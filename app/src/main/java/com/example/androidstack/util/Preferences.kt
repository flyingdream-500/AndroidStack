package com.example.androidstack.util

import android.content.SharedPreferences
import android.util.Log
import com.example.androidstack.R

const val PREFERENCES_FILE = "uniquePreference"

const val DEFAULT_SEARCH = "android"

const val SORT_ACTIVITY = "activity"
const val SORT_CREATION = "creation"
const val SORT_VOTES = "votes"
const val ORDER_ASC = "asc"
const val ORDER_DESC = "desc"

private const val PREFERENCES_SORT_ID = "sortId"
private const val PREFERENCES_SORT_NAME = "sortName"
private const val PREFERENCES_ORDER = "order"
private const val PREFERENCES_ORDER_NAME = "orderName"
private const val PREFERENCES_SEARCH_NAME = "query"

fun SharedPreferences.saveSortPreference(ID: Int) {
    this.edit().putInt(PREFERENCES_SORT_ID, ID).apply()
}
fun SharedPreferences.saveSortNamePreference(name: String) {
    this.edit().putString(PREFERENCES_SORT_NAME, name).apply()
}
fun SharedPreferences.saveOrderPreference(check: Boolean) {
    this.edit().putBoolean(PREFERENCES_ORDER, check).apply()
}
fun SharedPreferences.saveOrderNamePreference(name: String) {
    this.edit().putString(PREFERENCES_ORDER_NAME, name).apply()
}
fun SharedPreferences.saveQueryPreference(query: String) {
    this.edit().putString(PREFERENCES_SEARCH_NAME, query).apply()
}


fun SharedPreferences.getQuery(): String {
    return getString(PREFERENCES_SEARCH_NAME, DEFAULT_SEARCH) ?: DEFAULT_SEARCH
}
fun SharedPreferences.getSortName(): String {
    return getString(PREFERENCES_SORT_NAME, SORT_CREATION) ?: SORT_CREATION
}
fun SharedPreferences.getOrderName(): String {
    return getString(PREFERENCES_ORDER_NAME, ORDER_DESC) ?: ORDER_DESC
}


fun SharedPreferences.getSortID(): Int {
    Log.d("POPUP", "sort ${R.id.sort_activity}")
    return getInt(PREFERENCES_SORT_ID, R.id.sort_activity)
}
fun SharedPreferences.getOrder(): Boolean {
    return getBoolean(PREFERENCES_ORDER, false)
}