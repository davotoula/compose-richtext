package com.halilibo.richtext.markdown


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size

private val DEFAULT_IMAGE_SIZE = 64.dp

/**
 * Implementation of MarkdownImage by using Coil library for Android.
 */
@Composable
public actual fun MarkdownImage(
  url: String,
  contentDescription: String?,
  modifier: Modifier,
  contentScale: ContentScale
) {
  // DO NOT USE THIS. This overrides the request builder of the
  // application
  val painter = rememberAsyncImagePainter(
    ImageRequest.Builder(LocalContext.current)
      .data(data = url)
      .size(Size.ORIGINAL)
      .httpHeaders(
        NetworkHeaders.Builder()
          .set("User-Agent", "MarkdownDemo")
          .build()
      )
      .crossfade(true)
      .build()
  )

  @SuppressLint("UnusedBoxWithConstraintsScope")
  BoxWithConstraints(modifier, contentAlignment = Alignment.Center) {
    val painterState by painter.state.collectAsState()
    val painterIntrinsicSize = painterState.painter?.intrinsicSize

    val sizeModifier = renderInSize(painterIntrinsicSize)

    Image(
      painter = painter,
      contentDescription = contentDescription,
      modifier = sizeModifier,
      contentScale = contentScale
    )
  }
}

@Composable
public fun BoxWithConstraintsScope.renderInSize(
  painterIntrinsicSize: androidx.compose.ui.geometry.Size?,
): Modifier {
  val density = LocalDensity.current

  val sizeModifier = if (painterIntrinsicSize != null &&
    painterIntrinsicSize.isSpecified &&
    painterIntrinsicSize.width != Float.POSITIVE_INFINITY &&
    painterIntrinsicSize.height != Float.POSITIVE_INFINITY
  ) {
    val width = painterIntrinsicSize.width
    val height = painterIntrinsicSize.height
    val scale = if (width > constraints.maxWidth) {
      constraints.maxWidth.toFloat() / width
    } else {
      1f
    }

    with(density) {
      Modifier.size(
        (width * scale).toDp(),
        (height * scale).toDp()
      )
    }
  } else {
    // if size is not defined at all, Coil fails to render the image
    // here, we give a default size for images until they are loaded.
    Modifier.size(DEFAULT_IMAGE_SIZE)
  }

  return sizeModifier
}