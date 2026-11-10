package com.example.atividade4_pokelist.data.entity

import com.google.gson.annotations.SerializedName

class PokemonEntity {
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String = ""

    @SerializedName("sprites")
    var sprites: SpritesEntity = SpritesEntity()

    @SerializedName("stats")
    var stats: List<StatItemEntity> = listOf()
}

class SpritesEntity {
    @SerializedName("front_default")
    var frontDefault: String = ""
}

class StatItemEntity {
    @SerializedName("base_stat")
    var baseStat: Int = 0

    @SerializedName("stat")
    var stat: StatDetailEntity = StatDetailEntity()
}

class StatDetailEntity {
    @SerializedName("name")
    var name: String = ""
}