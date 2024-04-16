package com.dev_bayan_ibrahim.flashcards.data.di

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

class FirebaseAppInstallation : AppInstallation {
    override val id: Flow<String> = callbackFlow {
        val installation = FirebaseInstallations.getInstance()
        val idRetrievalTaskCallback: OnCompleteListener<String> = OnCompleteListener<String> { task ->
            if(task.isSuccessful) {
                trySend(task.result)
                Log.d("FirebaseInstallation", "ID: ${task.result}")
            } else {
                trySend("")
                Log.e("FirebaseInstallation", "Can't Retrieve ID")
            }
        }
        installation.id.addOnCompleteListener(idRetrievalTaskCallback)
        awaitClose { }
    }.conflate()
}
interface AppInstallation {
    val id: Flow<String>
}
