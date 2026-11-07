package com.example.atividade4_pokelist.data.service

import com.example.atividade4_pokelist.data.entity.PokemonEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{name}")
    fun getPokemonByName(@Path("name")name: String): Call<PokemonEntity>

    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id")id: Int): Call<PokemonEntity>
}