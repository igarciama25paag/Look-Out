package com.igmata.lookout.util

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.igmata.lookout.database.LookOutDB
import com.igmata.lookout.database.entity.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Connection(
    val lifecycleOwner: LifecycleOwner
) {
    companion object {
        var Connected: Boolean = false
        var ConnectionId: String? = null
        var Izena: String = ""
        var Longitudea: Double = 0.0
        var Latitudea: Double = 0.0

        // Firebase Realtime Database konexioa
        private val fbdb = lazy {
            Firebase
                .database("https://lookout-5e114-default-rtdb.europe-west1.firebasedatabase.app/")
                .reference
        }

        // Firebase konexioen erreferentzia lortu
        private val consRef = lazy {
            fbdb.value.child("connections")
        }
    }

    // Room kokapenak lortu
    private val db by lazy { LookOutDB.Companion.getInstance(lifecycleOwner as Context) }
    private val locationsDao by lazy { db.locations() }

    // Firebase-era konektatu
    fun connect(izena: String) {

        // Izena gorde
        Izena = izena

        // Datu baseko datuak eguneratzerakoan
        consRef.value.addValueEventListener(dbDataChangeEvent)

        // Id errepikaezin berria sortu Firebase-en eta jaso
        ConnectionId = consRef.value.push().key

        // Deskonektatzerakoan erregistro berria ezabatu
        consRef.value.child(ConnectionId!!).onDisconnect().removeValue()

        // Konexio berria
        sendNewLocation()
        Connected = true

        Log.d("Firebase", "Konektatuta")

        // Longitudea eta latitudea lortu 5 segunduro
        CoroutineScope(Dispatchers.IO).launch {
            while (Connected) {
                Gps.getLocation(lifecycleOwner as Context) { lat, lon ->
                    Longitudea = lon
                    Latitudea = lat
                }
                sendNewLocation()
                Log.d("Look Out", "Kokapena eskatu: lat=${Latitudea} lon=${Longitudea}")
                Thread.sleep(5000)
            }
        }
    }

    // Firebase-etik deskonektatu
    fun disconnect() {

        // Firebase Listener ezabatu
        consRef.value.removeEventListener(dbDataChangeEvent)

        // Firebase-eko erregistroa ezabatu
        ConnectionId?.let { id ->
            consRef.value.child(id).removeValue()
        }

        // Datuak garbitu
        ConnectionId = null
        Connected = false

        Log.d("Firebase", "Deskonektatuta")
    }

    // Firebase-ri kokapen berria bat sortbidali
    private fun sendNewLocation() {

        // Kokapen berria muntatu
        val newCon = hashMapOf(
            "sortze_data" to ServerValue.TIMESTAMP,
            "izena" to Izena,
            "longitudea" to Longitudea,
            "latitudea" to Latitudea
        )

        // Kokapen berria Firebase-era igo
        ConnectionId?.let { id ->
            fbdb.value.child("connections").child(id).setValue(newCon)
                .addOnSuccessListener {
                    Log.d("Firebase", "Konexio elementu berria -> Izena: '$Izena'  ID: $id")
                }
                .addOnFailureListener { error ->
                    Log.e("Firebase", "Error: ${error.message}")
                }
        }
    }

    // Firebase kokapenak aldatzerako gertaera
    private val dbDataChangeEvent = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

                // Room eta Firebase datuak hartu
                val localLocations = locationsDao.getAll()
                //snapshot.children.removeAll { it.key == ConnectionId }
                val firebaseKeys = snapshot.children.map { it.key.toString() }

                // Existitzen ez diren kokapenak ezabatu
                localLocations.forEach { local ->
                    if (!firebaseKeys.contains(local.id))
                        locationsDao.delete(local)
                }

                // Kokapenak eguneratu edo gehitu
                snapshot.children.forEach { fbLocation ->
                    if (fbLocation.key != ConnectionId) {
                        val id = fbLocation.key ?: return@forEach
                        locationsDao.upsert(
                            Location(
                                id,
                                fbLocation.child("izena").getValue(String::class.java) ?: "null",
                                fbLocation.child("longitudea").getValue(Double::class.java) ?: 0.0,
                                fbLocation.child("latitudea").getValue(Double::class.java) ?: 0.0
                            )
                        )
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Firebase", "Error: ${error.message}")
        }
    }
}