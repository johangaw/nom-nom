package com.example.eatyeaty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.eatyeaty.ui.App
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.example.eatyeaty.data.AppViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model: AppViewModel by viewModels()


        setContent {
            App(
                openUrl = this::openUrl,
                appModel = model
            )
        }
    }

    fun openUrl(url: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }
}