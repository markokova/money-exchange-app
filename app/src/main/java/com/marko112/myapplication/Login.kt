package com.marko112.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val users = Register.UsersSingleton.users
    private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId", "WrongViewCast")
//    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
//        super.onCreate(savedInstanceState, persistentState)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_login)

        syncLocalUsersWithDB()

        val goToRegisterButton = findViewById<TextView>(R.id.registerNowBtn)
        goToRegisterButton.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val userName = findViewById<EditText>(R.id.emailLogin)
            val password = findViewById<EditText>(R.id.passwordLogin)
            if(userName.text.isNotEmpty() && password.text.isNotEmpty()){
                auth.signInWithEmailAndPassword(userName.text.toString(), password.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            startActivity(Intent(this, MainActivity::class.java))
                            // Navigate to home fragment or show a success message
                        }
                        else{
                            // If sign in fails, display a message to the user.

                                Log.w(TAG, "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
            }
            else{
                Toast.makeText(
                    baseContext,
                    "You have to fill in all the fields in order to login.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun syncLocalUsersWithDB(){
        db.collection("users")
            .get()
            .addOnSuccessListener {
                for(data in it.documents){
                    val user = data.toObject(User::class.java)
                    if(user != null){
                        users.add(user)
                        val sizeOfUsers = users.size
                        users.elementAt(sizeOfUsers - 1).id = data.id
                    }
                    else{
                        Log.d(TAG,"In sync method in Login Class, firebase user is null so your local users will be empty too.")
                    }
                }
            }
    }
}

//                        for(localUser in users){
//                            if(localUser.email == user.email){
//                                Log.d(TAG,"User already added in register, no need to sync it.")
//                            }
//                            else{
//                                counter++
//                                users.add(user)
//                                val sizeOfUsers = users.size
//                                users.elementAt(sizeOfUsers - 1).id = data.id
//                                Log.d(TAG,"user added in sync method.")
//                            }
//                        }