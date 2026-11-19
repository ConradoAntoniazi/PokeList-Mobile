package com.example.atividade4_pokelist.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.data.client.RetrofitClient
import com.example.atividade4_pokelist.data.entity.PokemonEntity
import com.example.atividade4_pokelist.data.entity.PokemonListItem
import com.example.atividade4_pokelist.data.service.PokeApiService
import com.example.atividade4_pokelist.databinding.FragmentListBinding
import com.example.atividade4_pokelist.view.adapter.PokemonAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private var currentStartId = 1
    private val pageSize = 15

    private lateinit var adapter: PokemonAdapter
    private val pokemonList = mutableListOf<PokemonListItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonAdapter(pokemonList) { item ->
            onPokemonClicked(item)
        }

        binding.recyclerViewPokemons.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerViewPokemons.adapter = adapter

        binding.btnNextPage.setOnClickListener {
            currentStartId += pageSize
            loadPage()
        }

        binding.btnPrevPage.setOnClickListener {
            if (currentStartId > 1) {
                currentStartId = (currentStartId - pageSize).coerceAtLeast(1)
                loadPage()
            }
        }

        loadPage()
    }

    private fun loadPage() {
        binding.btnPrevPage.isEnabled = currentStartId > 1
        binding.txtPageIndicator.text = "$currentStartId - ${currentStartId + pageSize - 1}"

        pokemonList.clear()
        val items = mutableListOf<PokemonListItem>()
        for (i in 0 until pageSize) {
            val pokemonId = currentStartId + i
            items.add(PokemonListItem(id = pokemonId, name = "#$pokemonId"))
        }

        pokemonList.addAll(items)
        adapter.updateData(pokemonList)

        val pokeApiService = RetrofitClient.createService(PokeApiService::class.java)

        for (index in pokemonList.indices) {
            val item = pokemonList[index]
            pokeApiService.getPokemonById(item.id).enqueue(object : Callback<PokemonEntity> {
                override fun onResponse(call: Call<PokemonEntity>, response: Response<PokemonEntity>) {
                    if (response.isSuccessful && response.body() != null) {
                        val pokemon = response.body()!!
                        if (index < pokemonList.size && pokemonList[index].id == item.id) {
                            pokemonList[index].name = pokemon.name
                            adapter.notifyItemChanged(index)
                        }
                    }
                }

                override fun onFailure(call: Call<PokemonEntity>, t: Throwable) {
                    // Ignore failure
                }
            })
        }
    }

    private fun onPokemonClicked(item: PokemonListItem) {
        val pokeApiService = RetrofitClient.createService(PokeApiService::class.java)
        pokeApiService.getPokemonById(item.id).enqueue(object : Callback<PokemonEntity> {
            override fun onResponse(call: Call<PokemonEntity>, response: Response<PokemonEntity>) {
                if (response.isSuccessful && response.body() != null) {
                    val pokemon = response.body()!!
                    val bundle = Bundle().apply {
                        putString("origin", "list")
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
                    findNavController().navigate(R.id.resultFragment, bundle)
                } else {
                    Toast.makeText(context, "Erro ao carregar detalhes do Pokémon", Toast.LENGTH_SHORT).show()
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