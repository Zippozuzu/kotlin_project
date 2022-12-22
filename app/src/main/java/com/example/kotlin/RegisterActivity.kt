package com.example.kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase



class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        // Initialize Firebase Auth
        auth = Firebase.auth

        val loginText: TextView = findViewById(R.id.textView_login_now)

        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.button_register)

        registerButton.setOnClickListener{
            performSignU()
        }

        //email and password from the user
    }
    private fun performSignU(){

        val firstName = findViewById<EditText>(R.id.editText_first_name)
        val lastName = findViewById<EditText>(R.id.editText_last_name)
        val email = findViewById<EditText>(R.id.editText_email_register)
        val password = findViewById<EditText>(R.id.editText_password_register)


        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()
        val inputFirstName = firstName.text.toString()
        val inputLastName = lastName.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Users")
        val User = User(inputEmail, inputFirstName,inputLastName)

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    // Sign in  success, lets move to the next actvity i.e. MainActvity

                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("user_id", firebaseUser.uid)
                    database.child(firebaseUser.uid).setValue(User)
                    startActivity(intent)
                    finish()

                    Toast.makeText(
                        baseContext, "Success",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

            .addOnFailureListener {
                Toast.makeText( this, "Error occurred ${it.localizedMessage}",Toast.LENGTH_SHORT)
                    .show()
            }


    }
}