package com.example.atividade4_pokelist.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atividade4_pokelist.data.room.AppDatabase
import com.example.atividade4_pokelist.databinding.FragmentFavoriteBinding
import com.example.atividade4_pokelist.view.adapter.FavoriteAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerFavPokemons.layoutManager = LinearLayoutManager(context)

        loadFavorites()
    }

    private fun loadFavorites() {
        val db = AppDatabase.getDatabase(requireContext())
        val pokemonDao = db.pokemonDAO()
        val favorites = pokemonDao.getAll().toMutableList()

        adapter = FavoriteAdapter(favorites) { pokemonToDelete ->
            pokemonDao.delete(pokemonToDelete)
            Toast.makeText(
                context,
                "${pokemonToDelete.name} removido dos favoritos",
                Toast.LENGTH_SHORT
            ).show()
            loadFavorites()
        }

        binding.recyclerFavPokemons.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}