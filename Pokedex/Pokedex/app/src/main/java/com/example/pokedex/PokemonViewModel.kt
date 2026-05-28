package com.example.pokedex

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.io.BufferedReader
import java.io.InputStreamReader

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

    private val _allPokemons = MutableStateFlow<List<Pokemon>>(emptyList())
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _typeFilter = MutableStateFlow("")
    val typeFilter: StateFlow<String> = _typeFilter

    val filteredPokemons: StateFlow<List<Pokemon>> = combine(
        _allPokemons,
        _searchQuery,
        _typeFilter,
    ) { pokemons, query, type ->
        pokemons.filter { pokemon ->
            val matchesName = pokemon.name.contains(query, ignoreCase = true)
            val matchesType = type.isEmpty() || 
                             pokemon.type1.contains(type, ignoreCase = true) || 
                             pokemon.type2.contains(type, ignoreCase = true)
            matchesName && matchesType
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        val pokemons = mutableListOf<Pokemon>()
        try {
            val inputStream = getApplication<Application>().resources.openRawResource(R.raw.pokemon)
            val reader = BufferedReader(InputStreamReader(inputStream))
            reader.readLine()
            var line: String? = reader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.size >= 13) {
                    val pokemon = Pokemon(
                        id = tokens[0].toInt(),
                        name = tokens[1],
                        type1 = tokens[2],
                        type2 = tokens[3],
                        total = tokens[4].toInt(),
                        hp = tokens[5].toInt(),
                        attack = tokens[6].toInt(),
                        defense = tokens[7].toInt(),
                        spAtk = tokens[8].toInt(),
                        spDef = tokens[9].toInt(),
                        speed = tokens[10].toInt(),
                        generation = tokens[11].toInt(),
                        isLegendary = tokens[12].toBoolean(),
                    )
                    pokemons.add(pokemon)
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _allPokemons.value = pokemons
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onTypeFilterChange(newType: String) {
        _typeFilter.value = newType
    }
}
