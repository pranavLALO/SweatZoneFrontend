package com.example.sweatzone

import android.net.Uri
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(videoResId: Int) {
    val context = LocalContext.current

    // Safety check: If ID is 0, show error and don't try to play
    if (videoResId == 0) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Error: Video not found", Toast.LENGTH_SHORT).show()
        }
        return
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Construct URI strictly for raw resources
            val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")

            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true // Show play/pause buttons
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
