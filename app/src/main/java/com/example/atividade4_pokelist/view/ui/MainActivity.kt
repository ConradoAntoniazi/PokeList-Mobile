package com.example.atividade4_pokelist.view.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
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

        // 1. Obtendo o NavController primeiro
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        // 2. Configurando a Toolbar e o NavigationUI
        setupToolbar()
    }

    private fun setupToolbar() {
        // Define quais são os destinos principais (para não exibir a seta de voltar neles)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.searchFragment,
                R.id.favoriteFragment,
                R.id.listFragment,
            )
        )

        // Conecta a Toolbar ao NavController
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Cor branca para o título e o ícone dos 3 pontinhos (overflow)
        binding.toolbar.setTitleTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.white))
        binding.toolbar.overflowIcon?.setTint(androidx.core.content.ContextCompat.getColor(this, R.color.white))

        // Infla o menu na Toolbar
        binding.toolbar.inflateMenu(R.menu.menu_toolbar)
        binding.toolbar.overflowIcon?.setTint(androidx.core.content.ContextCompat.getColor(this, R.color.white))

        // Integra os cliques do menu automaticamente com o NavigationUI
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.action_logout) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            } else {
                NavigationUI.onNavDestinationSelected(menuItem, navController)
            }
        }

        // Oculta a Toolbar no ResultFragment e exibe nas outras telas
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.resultFragment) {
                binding.toolbar.visibility = View.GONE
            } else {
                binding.toolbar.visibility = View.VISIBLE
            }
        }
    }
}