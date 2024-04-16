package com.dev_bayan_ibrahim.flashcards.ui.screen.app_design

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.dev_bayan_ibrahim.flashcards.R

@Composable
fun DynamicAsyncImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    loadingIndicatorSize: Dp = 56.dp,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter = painterResource(id = R.drawable.ic_broken_image),
) {
    var isLoading by remember { mutableStateOf(!imageUrl.isResUrl()) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = getImagePainter(
        imageUrl = imageUrl,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        }
    )
    val isLocalInspection = LocalInspectionMode.current
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (isLoading && isLocalInspection.not()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(loadingIndicatorSize),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        val fetchingFailed = isError.not() && isLocalInspection.not()
        val imageModifier = if (!fetchingFailed) Modifier else modifier
        Image(
            modifier = imageModifier,
            contentScale = contentScale,
            painter = if (isError.not() && isLocalInspection.not()) imageLoader else placeholder,
            contentDescription = null
        )
    }
}

@Composable
private fun getImagePainter(
    imageUrl: String,
    onState: (AsyncImagePainter.State) -> Unit
): Painter = imageUrl.parseStringResUrl()?.let {
    painterResource(id = it)
} ?: rememberAsyncImagePainter(
    model = ImageRequest.Builder(LocalContext.current)
        .diskCachePolicy(CachePolicy.DISABLED)
        .memoryCachePolicy(CachePolicy.DISABLED)
        .data(imageUrl)
        .build(),
    onState = onState
)

fun Int.getLinkOfResDrawable(): String = "$drawableResScheme://$this"
private fun String.parseStringResUrl(): Int? {
    return removePrefix("$drawableResScheme://").toIntOrNull()
}

private fun String.isResUrl(): Boolean {
    return startsWith("$drawableResScheme://")
}

private const val drawableResScheme = "drawableRes"