package com.example.atividade4_pokelist.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.atividade4_pokelist.data.DAO.PokemonDAO
import com.example.atividade4_pokelist.data.entity.PokemonModel

@Database(entities = [PokemonModel::class], version = 2)
abstract class AppDatabase() : RoomDatabase() {

    abstract fun pokemonDAO(): PokemonDAO

    companion object {
        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "favpokemonsdatabase.db"
                    )
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}
