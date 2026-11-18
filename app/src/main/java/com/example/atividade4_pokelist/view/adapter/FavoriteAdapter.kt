package com.example.atividade4_pokelist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.atividade4_pokelist.data.entity.PokemonModel
import com.example.atividade4_pokelist.databinding.PokemonFavLineBinding
import com.example.atividade4_pokelist.view.ui.ListPokemonsFavViwHolder

class FavoriteAdapter(
    private var items: MutableList<PokemonModel>,
    private val onDeleteClick: (PokemonModel) -> Unit
) : RecyclerView.Adapter<ListPokemonsFavViwHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPokemonsFavViwHolder {
        val binding = PokemonFavLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListPokemonsFavViwHolder(binding)
    }

    override fun onBindViewHolder(holder: ListPokemonsFavViwHolder, position: Int) {
        val pokemon = items[position]
        holder.bindVH(pokemon) { itemToDelete ->
            onDeleteClick(itemToDelete)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<PokemonModel>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}