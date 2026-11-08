package com.example.atividade4_pokelist.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.atividade4_pokelist.databinding.FragmentResultBinding

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pokemonName = arguments?.getString("name")
        val imgSrc = arguments?.getString("front_default")

        val hp = arguments?.getInt("stat_hp", 0) ?: 0
        val attack = arguments?.getInt("stat_attack", 0) ?: 0
        val defense = arguments?.getInt("stat_defense", 0) ?: 0
        val spAtk = arguments?.getInt("stat_sp_atk", 0) ?: 0
        val spDef = arguments?.getInt("stat_sp_def", 0) ?: 0
        val speed = arguments?.getInt("stat_speed", 0) ?: 0

        Log.i("FRAG_RESULT", "Pokemon Name: $pokemonName")
        Log.i("FRAG_RESULT", "Pokemon Image: $imgSrc")

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

        // Botão Retornar (Seta no canto superior esquerdo)
        binding.btnBackResult.setOnClickListener {
            (activity as? MainActivity)?.setFragmentSearch()
        }

        // Botão Coração (Favorito) -> Toast
        binding.btnFavoriteResult.setOnClickListener {
            Toast.makeText(context, "Favoritado com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}