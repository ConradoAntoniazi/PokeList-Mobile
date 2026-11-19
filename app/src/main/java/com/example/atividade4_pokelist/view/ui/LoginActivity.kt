package com.example.atividade4_pokelist.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLogin)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val basePadding = (24 * resources.displayMetrics.density).toInt()
            v.setPadding(
                systemBars.left + basePadding,
                systemBars.top + basePadding,
                systemBars.right + basePadding,
                systemBars.bottom + basePadding
            )
            insets
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editLoginEmail.text.toString().trim()
            val passwd = binding.editLoginPassword.text.toString().trim()

            if (email.isEmpty() || passwd.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, passwd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    val msg = when (exception) {
                        is FirebaseAuthInvalidUserException -> "Este usuário não existe"
                        is FirebaseAuthInvalidCredentialsException -> "O usuário existe mas a senha está incorreta"
                        is FirebaseNetworkException -> "Falha ao conectar com a internet"
                        else -> "Houve algum problema no login, tente novamente"
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                }
        }

        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.textForgotPassword.setOnClickListener {
            val email = binding.editLoginEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu e-mail no campo de login para redefinir a senha", Toast.LENGTH_LONG).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(this, "E-mail de redefinição enviado para $email", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao enviar e-mail de redefinição", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}