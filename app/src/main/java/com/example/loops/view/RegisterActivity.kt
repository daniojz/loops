package com.example.loops.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loops.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseFirestore
    private lateinit var database: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.findViewById<Button>(R.id.btn_finishRegister).setOnClickListener(this)
        this.findViewById<ImageButton>(R.id.btn_back_authActivity).setOnClickListener(this)


        auth = FirebaseAuth.getInstance()
        storage = FirebaseFirestore.getInstance()
        database = FirebaseDatabase.getInstance()
    }


    private fun registerUser(email: String, password: String, username: String, firstName: String, surname: String, lastName: String) {
        auth.createUserWithEmailAndPassword(
            email,
            password).addOnCompleteListener {
            if (it.isSuccessful){
                registerUserStorage(username, email, firstName, surname, lastName)

                val profileUpdate = UserProfileChangeRequest.Builder().setDisplayName(username).build()
                auth.currentUser.updateProfile(profileUpdate).addOnCompleteListener{
                    if(it.isSuccessful) finishSuccessful()
                }

            } else {
                showAlert("Se ha producirdo un error al registrar el usuario")
            }
        }
    }

    private fun registerUserStorage(username: String, email: String, firstName: String, surname: String, lastName: String){
        storage.collection("users").document(email).set(
                hashMapOf("user" to username,
                        "firstName" to firstName,
                        "surname" to surname,
                        "lastName" to lastName,
                        )
        ).addOnCompleteListener{
            if (it.isSuccessful){
                this.findViewById<TextView>(R.id.tv_message_register).also {
                    it.text = "Se ha registrado correctamente!"
                }
            } else {
                showAlert("Se ha producirdo un error al añadir el usuario a al base de datos")
            }
        }
    }

    private fun finishSuccessful(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAlert(message: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ops!")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onClick(v: View?) {
        this.findViewById<TextView>(R.id.tv_message_register).text = ""
        val email = this.findViewById<TextView>(R.id.et_email).text
        val password = this.findViewById<TextView>(R.id.et_password).text
        val username = this.findViewById<TextView>(R.id.et_username).text
        var name = this.findViewById<TextView>(R.id.et_name).text
        var surname = this.findViewById<TextView>(R.id.et_surname).text
        var lastname = this.findViewById<TextView>(R.id.et_lastname).text

        if (v != null) {
            if (v.id == R.id.btn_finishRegister) {
                if (username.isNotEmpty() && password.isNotEmpty()) {

                    if(name.isBlank()) name="none"
                    if(surname.isBlank()) surname="none"
                    if(lastname.isBlank()) lastname="none"

                    registerUser(email.toString(), password.toString(), username.toString(), name.toString(), surname.toString(), lastname.toString())
                } else {
                    this.findViewById<TextView>(R.id.tv_message_login).text =
                        "Debe de rellenar todos los campos"
                }
            } else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}