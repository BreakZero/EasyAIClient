package org.easy.ai.client

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.database
import timber.log.Timber

class CrashlyticsTree : Timber.Tree() {
    private val database = Firebase.database
    private val ref = database.getReference("logs")

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return priority == Log.WARN || priority == Log.ERROR
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        FirebaseCrashlytics.getInstance().log("$tag: $message")
        if (t != null) {
            println("===== logging")
            ref.push().setValue(t.message)
                .addOnSuccessListener { println("===== on success") }
                .addOnFailureListener {
                    println("===== Failed to log event, $it")
                    it.printStackTrace()
                }

            FirebaseCrashlytics.getInstance().recordException(t)
        }
    }
}