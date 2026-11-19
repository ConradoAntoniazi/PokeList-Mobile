package com.example.atividade4_pokelist.view.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atividade4_pokelist.data.repository.FirestoreRepository
import com.example.atividade4_pokelist.databinding.FragmentFavoriteBinding
import com.example.atividade4_pokelist.view.adapter.FavoriteAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoriteAdapter
    private val repository = FirestoreRepository()

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
        repository.getFavorites { favorites ->
            if (_binding == null) return@getFavorites
            adapter = FavoriteAdapter(favorites.toMutableList()) { pokemonToDelete ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Desfavoritar Pokemon")
                    .setMessage("Tem certeza que deseja desfavoritar ${pokemonToDelete.name}?")
                    .setPositiveButton("Sim") { _, _ ->
                        repository.removeFavorite(pokemonToDelete.id) { success ->
                            if (success && _binding != null) {
                                Toast.makeText(
                                    context,
                                    "${pokemonToDelete.name} removido dos favoritos",
                                    Toast.LENGTH_SHORT
                                ).show()
                                loadFavorites()
                            }
                        }
                    }
                    .setNegativeButton("Não", null)
                    .show()
            }
            binding.recyclerFavPokemons.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}