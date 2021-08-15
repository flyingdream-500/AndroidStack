package com.example.androidstack.util

import android.content.SharedPreferences
import com.example.androidstack.R

const val PREFFERENCES_FILE = "uniquePreference"
private val PREFFERENCES_SORT_ID = "sortId"
private val PREFFERENCES_SORT_NAME = "sortName"
private val PREFFERENCES_ORDER = "order"
private val PREFFERENCES_ORDER_NAME = "orderName"
private val PREFFERENCES_SEARCH_NAME = "query"

fun SharedPreferences.saveSortPreference(ID: Int) {
    this.edit().putInt(PREFFERENCES_SORT_ID, ID).apply()
}
fun SharedPreferences.saveSortNamePreference(name: String) {
    this.edit().putString(PREFFERENCES_SORT_NAME, name).apply()
}
fun SharedPreferences.saveOrderPreference(check: Boolean) {
    this.edit().putBoolean(PREFFERENCES_ORDER, check).apply()
}
fun SharedPreferences.saveOrderNamePreference(name: String) {
    this.edit().putString(PREFFERENCES_ORDER_NAME, name).apply()
}
fun SharedPreferences.saveQueryPreference(query: String) {
    this.edit().putString(PREFFERENCES_SEARCH_NAME, query).apply()
}


fun SharedPreferences.getQuery(): String {
    return getString(PREFFERENCES_SEARCH_NAME, "android") ?: "android"
}
fun SharedPreferences.getSortName(): String {
    return getString(PREFFERENCES_SORT_NAME, "creation") ?: "creation"
}
fun SharedPreferences.getOrderName(): String {
    return getString(PREFFERENCES_ORDER_NAME, "desc") ?: "desc"
}


fun SharedPreferences.getSortID(): Int {
    return getInt(PREFFERENCES_SORT_ID, R.id.sort_activity)
}
fun SharedPreferences.getOrder(): Boolean {
    return getBoolean(PREFFERENCES_ORDER, false)
}