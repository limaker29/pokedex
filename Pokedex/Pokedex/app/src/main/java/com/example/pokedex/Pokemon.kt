package com.example.pokedex

import androidx.compose.ui.graphics.Color
import java.io.Serializable

data class Pokemon(
    val id: Int,
    val name: String,
    val type1: String,
    val type2: String,
    val total: Int,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val spAtk: Int,
    val spDef: Int,
    val speed: Int,
    val generation: Int,
    val isLegendary: Boolean
) : Serializable

fun getPokemonTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "grass" -> Color(0xFF7AC74C)
        "fire" -> Color(0xFFEE8130)
        "water" -> Color(0xFF6390F0)
        "bug" -> Color(0xFFA6B91A)
        "normal" -> Color(0xFFA8A77A)
        "poison" -> Color(0xFFA33EA1)
        "electric" -> Color(0xFFF7D02C)
        "ground" -> Color(0xFFE2BF65)
        "fairy" -> Color(0xFFD685AD)
        "fighting" -> Color(0xFFC22E28)
        "psychic" -> Color(0xFFF95587)
        "rock" -> Color(0xFFB6A136)
        "ghost" -> Color(0xFF735797)
        "ice" -> Color(0xFF96D9D6)
        "dragon" -> Color(0xFF6F35FC)
        "dark" -> Color(0xFF705746)
        "steel" -> Color(0xFFB7B7CE)
        "flying" -> Color(0xFFA98FF0)
        else -> Color.Gray
    }
}
