package com.example.atividade4_pokelist.view.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.atividade4_pokelist.R
import com.example.atividade4_pokelist.data.entity.PokemonModel
import com.example.atividade4_pokelist.data.repository.FirestoreRepository
import com.example.atividade4_pokelist.databinding.FragmentResultBinding
import com.google.android.material.snackbar.Snackbar

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val repository = FirestoreRepository()
    private var isCurrentlyFav = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemonId = arguments?.getInt("id", 0) ?: 0
        val pokemonName = arguments?.getString("name")
        val imgSrc = arguments?.getString("front_default")

        val hp = arguments?.getInt("stat_hp", 0) ?: 0
        val attack = arguments?.getInt("stat_attack", 0) ?: 0
        val defense = arguments?.getInt("stat_defense", 0) ?: 0
        val spAtk = arguments?.getInt("stat_sp_atk", 0) ?: 0
        val spDef = arguments?.getInt("stat_sp_def", 0) ?: 0
        val speed = arguments?.getInt("stat_speed", 0) ?: 0

        Log.i("FRAG_RESULT", "Pokemon ID: $pokemonId | Name: $pokemonName")

        binding.pokemonNameResultFrag.text = pokemonName ?: "Pokémon"

        if (!imgSrc.isNullOrEmpty()) {
            Glide.with(this)
                .load(imgSrc)
                .into(binding.pokemonImageResultFrag)
        }

        // Stats values and progress bars
        binding.statHpVal.text = hp.toString()
        binding.statHpBar.progress = hp

        binding.statAttackVal.text = attack.toString()
        binding.statAttackBar.progress = attack

        binding.statDefenseVal.text = defense.toString()
        binding.statDefenseBar.progress = defense

        binding.statSpAtkVal.text = spAtk.toString()
        binding.statSpAtkBar.progress = spAtk

        binding.statSpDefVal.text = spDef.toString()
        binding.statSpDefBar.progress = spDef

        binding.statSpeedVal.text = speed.toString()
        binding.statSpeedBar.progress = speed

        // Verifica estado inicial de favorito no Firestore
        repository.isFavorite(pokemonId) { isFav ->
            if (_binding != null) {
                isCurrentlyFav = isFav
                updateHeartIcon(isFav)
            }
        }

        // Botão Retornar (Seta no canto superior esquerdo)
        binding.btnBackResult.setOnClickListener {
            findNavController().popBackStack()
        }

        // Definindo snackbar para favoritar
        val snackFav = Snackbar.make(binding.root, "$pokemonName adicionado aos favoritos!", Snackbar.LENGTH_SHORT)
        snackFav.setBackgroundTint(Color.GRAY)
        snackFav.setTextColor(Color.WHITE)
        snackFav.setTextMaxLines(1)

        snackFav.setAction("Desfavoritar", View.OnClickListener {
            repository.removeFavorite(pokemonId) { success ->
                if (success && _binding != null) {
                    isCurrentlyFav = false
                    updateHeartIcon(false)
                    Snackbar.make(binding.root, "$pokemonName removido dos favoritos.", Snackbar.LENGTH_SHORT).show()
                }
            }
        })

        // Definindo snackbar para desfavoritar
        val snackDesfav = Snackbar.make(binding.root, "$pokemonName removido dos favoritos.", Snackbar.LENGTH_SHORT)
        snackDesfav.setBackgroundTint(Color.GRAY)
        snackDesfav.setTextColor(Color.WHITE)
        snackDesfav.setTextMaxLines(1)

        // Botão Coração (Favorito) -> Alterna (Adiciona/Remove)
        binding.btnFavoriteResult.setOnClickListener {
            if (!pokemonName.isNullOrEmpty()) {
                if (isCurrentlyFav) {
                    repository.removeFavorite(pokemonId) { success ->
                        if (success && _binding != null) {
                            isCurrentlyFav = false
                            updateHeartIcon(false)
                            snackDesfav.show()
                        }
                    }
                } else {
                    val model = PokemonModel().apply {
                        this.id = pokemonId
                        this.name = pokemonName
                        this.imageUrl = imgSrc ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"
                        this.isFavorite = true
                    }
                    repository.addFavorite(model) { success ->
                        if (success && _binding != null) {
                            isCurrentlyFav = true
                            updateHeartIcon(true)
                            snackFav.show()
                        }
                    }
                }
            }
        }
    }

    fun updateHeartIcon(isFav: Boolean) {
        val color = if (isFav) R.color.red else R.color.black
        binding.btnFavoriteResult.setColorFilter(
            ContextCompat.getColor(requireContext(), color)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}