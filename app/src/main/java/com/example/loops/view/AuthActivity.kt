package com.example.loops.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.loops.R
import com.example.loops.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var authViewModel:AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        init()
    }

    private fun init(){
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        this.findViewById<Button>(R.id.login).setOnClickListener(this)
        this.findViewById<Button>(R.id.register).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        this.findViewById<TextView>(R.id.tv_message_login).text = ""
        var email = this.findViewById<TextView>(R.id.et_email).text
        var password = this.findViewById<TextView>(R.id.et_password).text
        if (v != null) {
                if(v.id == R.id.login){
                    if(email.isNotEmpty() && password.isNotEmpty()) {
                        loginUser(email.toString(), password.toString())
                    }else {
                        this.findViewById<TextView>(R.id.tv_message_login).text = "Debe de rellenar todos los campos"
                    }
                } else {
                    registerUser()
                }
        }
    }

    private fun loginUser(username: String, password:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                username,
                password).addOnCompleteListener {
            if (it.isSuccessful) {
                this.findViewById<TextView>(R.id.tv_message_login).also {
                    it.text = "Se ha logeado correctamente!"
                }
                successFinish(username)
            } else {
                showAlert()
            }
        }
    }

    private fun registerUser(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun successFinish(username: String) {
        val intent = Intent()
        intent.putExtra("userName", username)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ops!")
        builder.setMessage("Se ha producirdo un error al autenticar el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}