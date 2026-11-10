package com.example.atividade4_pokelist.view.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.atividade4_pokelist.data.entity.PokemonModel
import com.example.atividade4_pokelist.databinding.PokemonFavLineBinding

class ListPokemonsFavViwHolder(private val binding: PokemonFavLineBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindVH(pokemon: PokemonModel, onDeleteClick: ((PokemonModel) -> Unit)? = null) {
        binding.txtPokemonNameFavItem.text = pokemon.name.replaceFirstChar { it.uppercase() }

        Glide.with(itemView.context)
            .load(pokemon.imageUrl)
            .into(binding.imgPokemonFavItem)

        binding.imgHeartFavItem.setOnClickListener {
            onDeleteClick?.invoke(pokemon)
        }
    }
}