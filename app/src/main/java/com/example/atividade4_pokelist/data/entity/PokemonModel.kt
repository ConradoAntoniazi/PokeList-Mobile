package com.example.atividade4_pokelist.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoritePokemons")
class PokemonModel {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id")
    var id: Int = 0

    @ColumnInfo(name="name")
    var name: String = ""

    @ColumnInfo(name="imageUrl")
    var imageUrl: String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"

    @ColumnInfo(name="isFavorite")
    var isFavorite: Boolean = false
}