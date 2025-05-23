package org.easy.ai.client

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.database
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    private val database = Firebase.database.apply { setPersistenceEnabled(true) }
    private val ref = database.getReference("logs")

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority == Log.WARN || priority == Log.ERROR
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        FirebaseCrashlytics.getInstance().log("$tag: $message")
        if (tag != null) {
            FirebaseCrashlytics.getInstance().setCustomKey("log_tag", tag)
        }
        FirebaseCrashlytics.getInstance().setCustomKey("log_priority", priority)
        FirebaseCrashlytics.getInstance().setCustomKey("log_message", message)
        t?.let { logThrowable(it) }
    }

    private fun logThrowable(throwable: Throwable) {
        ref.child("Nothing").setValue(throwable.message ?: "No error message")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("===== success")
                } else {
                    println("===== failed")
                }
            }
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}