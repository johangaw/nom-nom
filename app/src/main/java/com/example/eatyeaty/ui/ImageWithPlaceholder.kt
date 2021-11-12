package com.example.eatyeaty.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.example.eatyeaty.repositories.ImageModel

@Composable
fun ImageWithPlaceholder(
    modifier: Modifier = Modifier,
    image: ImageModel,
    placeholder: Painter,
) {
    when {
        image.loading -> SplashLoader()
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