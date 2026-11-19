package com.example.atividade4_pokelist.view.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtendo o NavController a partir do NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        // Definindo botões da nav bar como clicáveis
        binding.searchIcoNavBar.setOnClickListener(this)
        binding.favoriteIcoNavBar.setOnClickListener(this)
        binding.listIcoNavBar.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.searchIcoNavBar.id -> {
                if (navController.currentDestination?.id != R.id.searchFragment) {
                    navController.navigate(R.id.searchFragment)
                }
            }
            binding.favoriteIcoNavBar.id -> {
                if (navController.currentDestination?.id != R.id.favoriteFragment) {
                    navController.navigate(R.id.favoriteFragment)
                }
            }
            binding.listIcoNavBar.id -> {
                if (navController.currentDestination?.id != R.id.listFragment) {
                    navController.navigate(R.id.listFragment)
                }
            }
        }
    }
}