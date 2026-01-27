package com.igmata.lookout.util.connection

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.igmata.lookout.database.LookOutDB

class Connection {
     companion object {
         @SuppressLint("StaticFieldLeak")
         var context: Context? = null
         class ContextNotSetException(message: String) : Exception(message)

         // Firebase Realtime Database konexioa
         private val fbdb = lazy {
             Firebase
                 .database("https://lookout-5e114-default-rtdb.europe-west1.firebasedatabase.app/")
                 .reference
         }
         var connectionId: String? = null
         private val db by lazy {
             if (context == null) throw ContextNotSetException("Ez da kontexturik ezarri")
             LookOutDB.getInstance(context!!)
         }
         private val locationsDao by lazy { db.locations() }

         fun newConnection(izena: String) {
             // Firebase konexioen erreferentzia lortu
             val consRef = fbdb.value.child("connections")

             // Datu baseko datuak eguneratzerakoan
             consRef.addValueEventListener(dbDataChangeEvent)

             // Id errepikaezin berria sortu Firebase-en eta jaso
             connectionId = consRef.push().key

             // Deskonektatzerakoan erregistro berria ezabatu
             consRef.child(connectionId!!).onDisconnect().removeValue()

             // Konexio berria muntatu
             val newCon = hashMapOf(
                 "izena" to izena,
                 "sortze_data" to ServerValue.TIMESTAMP
             )

             // Konexioa berria Firebase-era igo
             connectionId?.let { id ->
                 consRef.child(id).setValue(newCon)
                     .addOnSuccessListener {
                         Log.d("Firebase", "Konexioa berria -> Izena: '$izena'  ID: $id")
                     }
                     .addOnFailureListener { error ->
                         Log.e("Firebase", "Error: ${error.message}")
                     }
             }
         }

         private val dbDataChangeEvent = object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 locationsDao.getAll().forEach {
                     if (!snapshot.children.contains())
                         locationsDao.delete(it)
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 Log.e("Firebase", "Error: ${error.message}")
             }
         }
     }
}