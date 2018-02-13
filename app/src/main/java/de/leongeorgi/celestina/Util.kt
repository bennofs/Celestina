package de.leongeorgi.celestina

import android.util.Log

/**
 * @author leon
 */
fun Any.debug(string: String) = Log.d(this::class.java.simpleName, string)
fun Any.error(string: String) = Log.e(this::class.java.simpleName, string)