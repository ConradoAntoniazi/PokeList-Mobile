package com.example.atividade4_pokelist.view.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.replace
import androidx.fragment.app.commit
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Definindo primeiro frag a ser visto
        setFragmentSearch()

        //Definindo botões da nav bar como clicáveis
        binding.searchIcoNavBar.setOnClickListener(this)
        binding.favoriteIcoNavBar.setOnClickListener(this)
        binding.listIcoNavBar.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {
            binding.searchIcoNavBar.id -> {
                setFragmentSearch()
            }
            binding.favoriteIcoNavBar.id -> {
                setFragmentFavorite()
            }
            binding.listIcoNavBar.id -> {
                setFragmentList()
            }
        }
    }

    fun setFragmentSearch() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<SearchFragment>(R.id.fragment_container_view)
        }
    }

    fun setFragmentFavorite() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<FavoriteFragment>(R.id.fragment_container_view)
        }
    }

    fun setFragmentList() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ListFragment>(R.id.fragment_container_view)
        }
    }

    fun setFragmentResult(bundle: Bundle) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<ResultFragment>(R.id.fragment_container_view, args = bundle)
        }
    }
}