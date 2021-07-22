package com.example.androidstack.util

import android.content.SharedPreferences
import com.example.androidstack.R


val PREFFERENCES_SORT_ID = "sortId"
val PREFFERENCES_SORT_NAME = "sortName"
val PREFFERENCES_ORDER = "order"
val PREFFERENCES_ORDER_NAME = "orderName"
val PREFFERENCES_SEARCH_NAME = "query"

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
    val query = this.getString(PREFFERENCES_SEARCH_NAME, "android") ?: "android"
    return query
}
fun SharedPreferences.getSortName(): String {
    val sort = this.getString(PREFFERENCES_SORT_NAME, "creation") ?: "creation"
    return sort
}
fun SharedPreferences.getOrderName(): String {
    val order = this.getString(PREFFERENCES_ORDER_NAME, "desc") ?: "desc"
    return order
}


fun SharedPreferences.getSortID(): Int {
    val sortID = this.getInt(PREFFERENCES_SORT_ID, R.id.sort_activity)
    return sortID
}
fun SharedPreferences.getOrder(): Boolean {
    return this.getBoolean(PREFFERENCES_ORDER, false)
}