package com.example.atividade4_pokelist.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.atividade4_pokelist.data.entity.PokemonModel

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(p: PokemonModel): Long

    @Delete
    fun delete(p: PokemonModel)

    @Query("DELETE FROM FavoritePokemons WHERE id = :id")
    fun deleteById(id: Int)

    @Query("SELECT * FROM FavoritePokemons")
    fun getAll(): List<PokemonModel>

    @Query("SELECT EXISTS(SELECT 1 FROM FavoritePokemons WHERE id = :id)")
    fun isFavorite(id: Int): Boolean
}