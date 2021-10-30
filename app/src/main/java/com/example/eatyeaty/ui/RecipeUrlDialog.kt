package com.example.eatyeaty.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eatyeaty.repositories.RecipeUrlDAO
import com.example.eatyeaty.repositories.loadData
import kotlinx.coroutines.launch

@Composable
fun RecipeUrlDialog(
    onDismissRequest: () -> Unit,
    onSuccess: (dao: RecipeUrlDAO) -> Unit
) {
    val scope = rememberCoroutineScope()
    val (url, setUrl) = remember {
        mutableStateOf("")
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { },
        text = {
            OutlinedTextField(
                value = url,
                onValueChange = setUrl,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Optional template URL") }
            )
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            if (url.isNotEmpty()) {
                                onSuccess(loadData(url))
                            } else {
                                onSuccess(RecipeUrlDAO())
                            }
                        }
                    }
                ) {
                    Text("Create")
                }
            }
        },
    )
}