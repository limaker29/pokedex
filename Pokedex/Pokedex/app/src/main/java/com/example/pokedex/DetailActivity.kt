package com.example.pokedex

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedex.ui.theme.PokedexTheme

class DetailActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val pokemon = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra("pokemon", Pokemon::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("pokemon") as? Pokemon
        }
        
        setContent {
            PokedexTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Detalle Pokémon", color = Color.White) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Regresar",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF303943)
                            )
                        )
                    }
                ) { innerPadding ->
                    if (pokemon != null) {
                        PokemonDetailContent(pokemon, Modifier.padding(innerPadding))
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontró información del Pokémon")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonDetailContent(pokemon: Pokemon, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "#${pokemon.id.toString().padStart(3, '0')}",
            fontSize = 20.sp,
            color = Color.Gray
        )
        Text(
            text = pokemon.name,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row {
            TypeLabel(pokemon.type1)
            if (pokemon.type2.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                TypeLabel(pokemon.type2)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Estadísticas Base", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                StatRow("HP", pokemon.hp)
                StatRow("Ataque", pokemon.attack)
                StatRow("Defensa", pokemon.defense)
                StatRow("Sp. Atk", pokemon.spAtk)
                StatRow("Sp. Def", pokemon.spDef)
                StatRow("Velocidad", pokemon.speed)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                StatRow("Total", pokemon.total, isBold = true)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            InfoBox("Generación", pokemon.generation.toString())
            InfoBox("Tipo", if (pokemon.isLegendary) "Legendario" else "Común")
        }
    }
}

@Composable
fun StatRow(label: String, value: Int, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(value.toString(), fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun InfoBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TypeLabel(type: String) {
    Surface(
        color = getPokemonTypeColor(type),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = type,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
