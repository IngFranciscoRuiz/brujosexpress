package com.fjapps.brujosexpress.admin.data.di

import android.content.Context

object StoreManager {
    private const val PREFS = "admin_prefs"
    private const val KEY_STORE_ID = "current_store_id"

    @Volatile
    var currentStoreId: String? = null
        private set

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        currentStoreId = prefs.getString(KEY_STORE_ID, null)
    }

    fun save(context: Context, storeId: String) {
        currentStoreId = storeId
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_STORE_ID, storeId).apply()
    }

    fun clear(context: Context) {
        currentStoreId = null
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_STORE_ID).apply()
    }
}


