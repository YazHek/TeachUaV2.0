package com.softserve.teachua.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.softserve.teachua.app.baseImageUrl
import com.softserve.teachua.R

@Composable
fun ImageAndTitleOnIt(picture: String, title: String = "") {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colors.primaryVariant,
        modifier = Modifier.height(190.dp),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = baseImageUrl + picture,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.tint(
                    color = Color(R.color.tint),
                    blendMode = BlendMode.Darken
                )
            )
            HtmlText(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center
            )
        }

    }
}