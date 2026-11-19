package com.example.atividade4_pokelist.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.data.client.RetrofitClient
import com.example.atividade4_pokelist.data.entity.PokemonEntity
import com.example.atividade4_pokelist.data.service.PokeApiService
import com.example.atividade4_pokelist.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSearchFrag.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        val query = binding.inputNamePokemonSearchFrag.text.toString().trim().lowercase()

        if (query.isEmpty()) {
            Toast.makeText(context, "Digite o nome de um Pokemon", Toast.LENGTH_SHORT).show()
            return
        }

        val pokeApiService = RetrofitClient.createService(PokeApiService::class.java)
        pokeApiService.getPokemonByName(query).enqueue(object : Callback<PokemonEntity> {
            override fun onResponse(call: Call<PokemonEntity>, response: Response<PokemonEntity>) {
                if (response.isSuccessful && response.body() != null) {
                    val pokemon = response.body()!!
                    val bundle = Bundle().apply {
                        putString("origin", "search")
                        putInt("id", pokemon.id)
                        putString("name", pokemon.name)
                        putString("front_default", pokemon.sprites.frontDefault)

                        for (statItem in pokemon.stats) {
                            when (statItem.stat.name) {
                                "hp" -> putInt("stat_hp", statItem.baseStat)
                                "attack" -> putInt("stat_attack", statItem.baseStat)
                                "defense" -> putInt("stat_defense", statItem.baseStat)
                                "special-attack" -> putInt("stat_sp_atk", statItem.baseStat)
                                "special-defense" -> putInt("stat_sp_def", statItem.baseStat)
                                "speed" -> putInt("stat_speed", statItem.baseStat)
                            }
                        }
                    }
                    findNavController().navigate(R.id.action_searchFragment_to_resultFragment, bundle)
                } else {
                    Toast.makeText(context, "Pokemon não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonEntity>, t: Throwable) {
                Toast.makeText(context, "Erro de conexão", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}