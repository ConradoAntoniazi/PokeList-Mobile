package com.example.atividade4_pokelist.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.databinding.ActivityRegisterBinding
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRegister)) { v, insets ->
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

        binding.btnBackRegister.setOnClickListener {
            finish()
        }

        binding.btnRegisterSubmit.setOnClickListener {
            val name = binding.editRegisterName.text.toString().trim()
            val nick = binding.editRegisterNick.text.toString().trim()
            val email = binding.editRegisterEmail.text.toString().trim()
            val pass1 = binding.editRegisterPassword1.text.toString().trim()
            val pass2 = binding.editRegisterPassword2.text.toString().trim()

            if (name.isEmpty() || nick.isEmpty() || email.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass1 != pass2) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass1.length < 6) {
                Toast.makeText(this, "A senha deve conter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, pass1)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserData(name, nick, email)
                    }
                }
                .addOnFailureListener { exception ->
                    val msg = when (exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Digite um e-mail válido"
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres"
                        is FirebaseAuthUserCollisionException -> "Este e-mail já foi cadastrado"
                        is FirebaseNetworkException -> "Falha ao conectar com a internet"
                        else -> "Houve algum problema no cadastro: ${exception.localizedMessage}"
                    }
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun saveUserData(name: String, nick: String, email: String) {
        val currUserId = auth.currentUser?.uid
        if (currUserId == null) {
            Toast.makeText(this, "Erro ao identificar usuário cadastrado", Toast.LENGTH_SHORT).show()
            return
        }

        val userMap = hashMapOf(
            "name" to name,
            "nick" to nick,
            "email" to email,
            "createdAt" to System.currentTimeMillis(),
        )

        db.collection("usuarios").document(currUserId)
            .set(userMap)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Dados do usuário salvos com sucesso no Firestore")
                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Erro ao salvar dados no Firestore: ", e)
                Toast.makeText(this, "Cadastro criado, mas erro ao salvar dados do perfil", Toast.LENGTH_LONG).show()
                finish()
            }
    }
}