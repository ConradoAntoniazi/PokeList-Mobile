package com.example.atividade4_pokelist.data.entity

data class PokemonListItem(
    val id: Int,
    var name: String = "Carregando...",
    val imageUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
)