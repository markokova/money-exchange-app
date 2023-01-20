package com.marko112.myapplication

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marko112.myapplication.ui.paymenet.PaymentFragment


class User(
    var id: String,
    val name: String,
    val email: String,
    var balance: Double,
    val transactions: ArrayList<Transaction>
) {

    private val db = Firebase.firestore
    constructor(): this("","", "", 0.0, ArrayList<Transaction>())


    fun isAbleToPay(amount: Double) : Int{
        if(amount == 0.0){
            return 0
        }
        if(amount > balance){
            return 1
        }
        return 2
    }

    fun pay(amount: Double){
        balance -= amount
        db.collection("users").document(id)
            .set(hashMapOf(
                "name" to name,
                "id" to id,
                "email" to email,
                "balance" to balance))
            .addOnSuccessListener {
                Log.d(TAG,"Successfully synced local and database balance while paying.")
            }
            .addOnFailureListener{
                Log.d(TAG,"Failed to sync local and database balance while paying.")
            }
    }

    fun receive(amount: Double){
        balance += amount
        db.collection("users").document(id)
            .set(hashMapOf(
                "name" to name,
                "id" to id,
                "email" to email,
                "balance" to balance))
            .addOnSuccessListener {
                Log.d(TAG,"Successfully synced local and database balance while receiving payment.")
            }
            .addOnFailureListener {
                Log.d(TAG,"Failed to sync local and database balance while receiving payment.")
            }
    }
}