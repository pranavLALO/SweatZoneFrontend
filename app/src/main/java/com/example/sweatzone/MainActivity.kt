package com.example.sweatzone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sweatzone.ui.theme.SweatzoneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.sweatzone.data.local.TokenManager.init(this)
        enableEdgeToEdge()
        setContent {
            SweatzoneTheme {
                NavGraph()
            }
        }
    }
}