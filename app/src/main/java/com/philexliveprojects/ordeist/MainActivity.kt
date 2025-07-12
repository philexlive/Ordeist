package com.philexliveprojects.ordeist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.philexliveprojects.ordeist.ui.OrdeistApp
import com.philexliveprojects.ordeist.ui.theme.ProfPrintTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfPrintTheme {
                OrdeistApp()
            }
        }
    }
}
