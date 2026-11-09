package com.example.atividade4_pokelist.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.atividade4_pokelist.data.entity.PokemonListItem
import com.example.atividade4_pokelist.databinding.ItemPokemonBinding

class PokemonAdapter(
    private var items: List<PokemonListItem>,
    private val onItemClick: (PokemonListItem) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = items[position]
        holder.binding.txtPokemonNameItem.text = item.name.replaceFirstChar { it.uppercase() }

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.binding.imgPokemonItem)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<PokemonListItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}