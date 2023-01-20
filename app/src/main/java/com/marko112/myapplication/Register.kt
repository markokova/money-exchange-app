package com.marko112.myapplication

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
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marko112.myapplication.ui.home.HomeFragment

class Register : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)


        auth = FirebaseAuth.getInstance()

        val fullName = findViewById<EditText>(R.id.activity_register_fullName)
        val email = findViewById<EditText>(R.id.emailRegister)
        val password = findViewById<EditText>(R.id.passwordRegister)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val goToLoginButton = findViewById<TextView>(R.id.loginNowBtn)

        goToLoginButton.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        registerButton.setOnClickListener {
            auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Log.d(TAG,"Create user with email: Successful")
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener{    task ->
                                if(task.isSuccessful){
                                    Log.d(TAG,"Email sent.")
                                }
                            }
                        var transactions = ArrayList<Transaction>()
                        val newUser = User(id = "",name = fullName.text.toString(),email = email.text.toString(),balance =100.0,transactions = transactions)
                        UsersSingleton.users.add(newUser)
                        if(newUser!=null){
                            var fbId : String = ""
                            db.collection("users").add(newUser).addOnSuccessListener {
                                newUser.id = it.id
                                fbId = it.id
                                Log.d(TAG,"new user successfully added to database with id:" + newUser.id)
                            }
                            //without this id attribute in firebase would be empty
                            db.collection("users").document(UsersSingleton.users.elementAt(UsersSingleton.users.size - 1).id)
                                .set(
                                    hashMapOf(
                                        "id" to fbId,
                                        "name" to newUser.name,
                                        "email" to newUser.email,
                                        "balance" to newUser.balance
                                    )
                                ).addOnSuccessListener {
                                    Log.d(TAG,"successfully set id to firebase")
                                }
                        }
                        else{
                            Log.d(TAG,"newUser is null")
                        }

                        Log.d(TAG,"registration: Successful")
                        startActivity(Intent(this, Login::class.java))
                    }
                    else{
                        if(email.text.toString().isEmpty() || password.text.isEmpty() || fullName.text.isEmpty()){
                            Toast.makeText(
                                baseContext, "You have to fill in all boxes in order to register.",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d(TAG,"email or password is empty")
                        }
                        //Log.d(TAG,"registration: Failed")
                        val exception = task.exception
                        if (exception is FirebaseAuthException) {
                            when (exception.errorCode) {
                                "ERROR_WEAK_PASSWORD" -> {
                                    Toast.makeText(baseContext, "The password is too weak", Toast.LENGTH_LONG).show()
                                }
                                "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                    Toast.makeText(baseContext, "This email address is already in use", Toast.LENGTH_LONG).show()
                                }
                                "ERROR_INVALID_EMAIL" -> {
                                    Toast.makeText(baseContext, "This email address is invalid", Toast.LENGTH_LONG).show()
                                }
                                else -> {
                                    Toast.makeText(baseContext, "Registration failed", Toast.LENGTH_LONG).show()
                                    exception.message?.let { it1 -> Log.e(TAG, it1) }
                                }
                            }
                        }
                    }
                }
        }


    }

    class UsersSingleton{
        companion object{
            var users = ArrayList<User>()
        }
    }
}

/*
Sto ovaj error znaci? kako popraviti?
* I/Choreographer: Skipped 96 frames!  The application may be doing too much work on its main thread.
* */