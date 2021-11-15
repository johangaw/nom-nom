package com.example.nomnom.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.nomnom.data.ImageModel
import com.example.nomnom.services.ImageDecoder
import com.example.nomnom.services.throwOnCancellation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberImageDecoder(): ImageDecoder {
    val resolver = LocalContext.current.contentResolver
    return remember(LocalContext.current.contentResolver) {
        ImageDecoder(resolver)
    }
}

@Composable
fun rememberImage(uri: String): ImageModel {
    var imageModel by remember(uri) {
        mutableStateOf(
            ImageModel(
                uri = uri, bitmap = null, loading = true,
            )
        )
    }

    val decoder = rememberImageDecoder()

    LaunchedEffect(uri) {
        if (uri.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                runCatching { uri }
                    .map { Uri.parse(it) }
                    .mapCatching { decoder.decode(it) }
                    .throwOnCancellation()
                    .getOrNull()
            }.let {
                imageModel = imageModel.copy(bitmap = it, loading = false)
            }
        }
    }

    return imageModel
}

@Composable
fun ImageWithPlaceholder(
    modifier: Modifier = Modifier,
    uri: String,
    placeholder: Painter,
) {
    val image = rememberImage(uri)

    when {
        image.loading -> SplashLoader(modifier)
        image.bitmap != null -> Image(
            modifier = modifier,
            bitmap = image.bitmap.asImageBitmap(),
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
        else -> Image(
            modifier = modifier,
            painter = placeholder,
            contentDescription = "",
            contentScale = ContentScale.FillWidth,
        )
    }
}