package com.example.atividade4_pokelist.data.repository

import android.util.Log
import com.example.atividade4_pokelist.data.entity.PokemonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val userId: String?
        get() = auth.currentUser?.uid

    fun isFavorite(pokemonId: Int, onResult: (Boolean) -> Unit) {
        val uid = userId
        if (uid == null) {
            onResult(false)
            return
        }

        db.collection("usuarios")
            .document(uid)
            .collection("favorites")
            .document(pokemonId.toString())
            .get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepository", "Erro ao verificar favorito: ", e)
                onResult(false)
            }
    }

    fun addFavorite(pokemon: PokemonModel, onResult: (Boolean) -> Unit) {
        val uid = userId
        if (uid == null) {
            onResult(false)
            return
        }

        val favMap = hashMapOf(
            "id" to pokemon.id,
            "name" to pokemon.name,
            "imageUrl" to pokemon.imageUrl,
            "isFavorite" to true,
        )

        db.collection("usuarios")
            .document(uid)
            .collection("favorites")
            .document(pokemon.id.toString())
            .set(favMap)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepository", "Erro ao adicionar favorito: ", e)
                onResult(false)
            }
    }

    fun removeFavorite(pokemonId: Int, onResult: (Boolean) -> Unit) {
        val uid = userId
        if (uid == null) {
            onResult(false)
            return
        }

        db.collection("usuarios")
            .document(uid)
            .collection("favorites")
            .document(pokemonId.toString())
            .delete()
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepository", "Erro ao remover favorito: ", e)
                onResult(false)
            }
    }

    fun getFavorites(onResult: (List<PokemonModel>) -> Unit) {
        val uid = userId
        if (uid == null) {
            onResult(emptyList())
            return
        }

        db.collection("usuarios")
            .document(uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val list = mutableListOf<PokemonModel>()
                for (document in result) {
                    val id = document.getLong("id")?.toInt() ?: continue
                    val name = document.getString("name") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val isFav = document.getBoolean("isFavorite") ?: true

                    val model = PokemonModel().apply {
                        this.id = id
                        this.name = name
                        this.imageUrl = imageUrl
                        this.isFavorite = isFav
                    }
                    list.add(model)
                }
                onResult(list)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepository", "Erro ao buscar favoritos: ", e)
                onResult(emptyList())
            }
    }
}