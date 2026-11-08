package com.nawa.app.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.nawa.app.R
import com.nawa.app.data.AmbientEffect
import com.nawa.app.data.NawaData
import com.nawa.app.playback.EffectEngine
import com.nawa.app.ui.theme.Amiri
import com.nawa.app.ui.theme.Gold
import com.nawa.app.ui.theme.GoldBright
import com.nawa.app.ui.theme.GoldDeep
import com.nawa.app.ui.theme.Ivory
import com.nawa.app.ui.theme.NightGreen
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private fun Modifier.glass(): Modifier = this
    .clip(RoundedCornerShape(24.dp))
    .background(Color.White.copy(alpha = 0.07f))
    .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(24.dp))

private fun fmt(ms: Long): String {
    val totalSec = ms / 1000
    return "%02d:%02d".format(totalSec / 60, totalSec % 60)
}

@Composable
fun PlayerScreen(qariId: String, surahNumber: Int, onBack: () -> Unit) {
    val qari = NawaData.qariById(qariId)
    val surah = NawaData.surahByNumber(surahNumber)
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // ------------------------------------------------------------ recitation
    val recitation = remember { ExoPlayer.Builder(context).build() }
    var isPlaying by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(true) }
    var ended by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var positionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(0L) }

    DisposableEffect(recitation) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }

            override fun onPlaybackStateChanged(state: Int) {
                isBuffering = state == Player.STATE_BUFFERING
                ended = state == Player.STATE_ENDED
            }

            override fun onPlayerError(error: PlaybackException) {
                errorMsg = "Recitation stream unavailable. Please check your internet connection."
            }
        }
        recitation.addListener(listener)
        onDispose { recitation.removeListener(listener) }
    }

    LaunchedEffect(qari, surah) {
        recitation.setMediaItem(MediaItem.fromUri(NawaData.recitationUrl(qari, surah)))
        recitation.prepare()
        recitation.playWhenReady = true
    }

    LaunchedEffect(recitation) {
        while (true) {
            positionMs = recitation.currentPosition.coerceAtLeast(0L)
            val d = recitation.duration
            durationMs = if (d > 0) d else 0L
            delay(500)
        }
    }

    // ------------------------------------------------------------- ambience
    val engine = remember { EffectEngine(context) }
    var selectedEffectId by remember { mutableStateOf<String?>(null) }
    var recitationVolume by remember { mutableFloatStateOf(1f) }
    var effectsVolume by remember { mutableFloatStateOf(0.6f) }

    LaunchedEffect(Unit) {
        NawaData.effects.forEach { engine.load(it.soundRes) }
    }

    DisposableEffect(Unit) {
        onDispose {
            engine.release()
            recitation.release()
        }
    }

    // The ambience loops exactly while the Surah is actively reciting.
    LaunchedEffect(selectedEffectId, isPlaying) {
        val effect = selectedEffectId?.let { NawaData.effectById(it) }
        if (effect != null && isPlaying) {
            engine.play(effect.soundRes)
        } else {
            engine.stop()
        }
    }

    // Pause everything when the app goes to the background.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> recitation.pause()
                Lifecycle.Event.ON_RESUME -> if (!ended) recitation.play()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // ------------------------------------------------------------ UI layout
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        // -------- living background: changes with the chosen ambience
        Crossfade(
            targetState = selectedEffectId,
            animationSpec = tween(900),
            label = "backgroundCrossfade"
        ) { effectId ->
            val effect = effectId?.let { NawaData.effectById(it) }
            if (effect == null) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF07130C), Color(0xFF123424), Color(0xFF07130C))
                            )
                        )
                )
            } else {
                val videoPlayer = remember(effect.id) {
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(
                            MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(effect.videoRes))
                        )
                        repeatMode = Player.REPEAT_MODE_ONE
                        volume = 0f
                        playWhenReady = true
                        prepare()
                    }
                }
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                            setShutterBackgroundColor(android.graphics.Color.TRANSPARENT)
                            player = videoPlayer
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
                DisposableEffect(effect.id) {
                    onDispose { videoPlayer.release() }
                }
            }
        }

        // -------- readability scrim
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.18f),
                            Color.Black.copy(alpha = 0.68f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            // -------- top bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Ivory
                    )
                }
                Spacer(Modifier.width(4.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = surah.nameArabic,
                        fontFamily = Amiri,
                        fontSize = 26.sp,
                        color = GoldBright
                    )
                    Text(
                        text = "Recited by ${qari.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Ivory.copy(alpha = 0.75f)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.emblem),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(Modifier.weight(1f))

            // -------- recitation card
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .glass()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = fmt(positionMs),
                        style = MaterialTheme.typography.labelMedium,
                        color = Ivory.copy(alpha = 0.85f)
                    )
                    Spacer(Modifier.weight(1f))
                    if (isBuffering && !ended) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Gold
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Buffering",
                            style = MaterialTheme.typography.labelSmall,
                            color = Ivory.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = if (durationMs > 0) fmt(durationMs) else "--:--",
                        style = MaterialTheme.typography.labelMedium,
                        color = Ivory.copy(alpha = 0.85f)
                    )
                }

                var isUserSeeking by remember { mutableStateOf(false) }
                var seekValue by remember { mutableFloatStateOf(0f) }
                val seekFraction = if (isUserSeeking) seekValue
                else if (durationMs > 0) positionMs.toFloat() / durationMs else 0f

                Slider(
                    value = seekFraction.coerceIn(0f, 1f),
                    onValueChange = {
                        isUserSeeking = true
                        seekValue = it
                    },
                    onValueChangeFinished = {
                        recitation.seekTo((seekValue * durationMs).toLong())
                        isUserSeeking = false
                    },
                    colors = sliderColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(Brush.radialGradient(listOf(GoldBright, Gold, GoldDeep)))
                        .clickable {
                            errorMsg = null
                            when {
                                ended -> {
                                    recitation.seekTo(0)
                                    recitation.play()
                                }
                                recitation.isPlaying -> recitation.pause()
                                else -> recitation.play()
                            }
                        }
                ) {
                    Icon(
                        imageVector = when {
                            ended -> Icons.Filled.Replay
                            isPlaying -> Icons.Filled.Pause
                            else -> Icons.Filled.PlayArrow
                        },
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = NightGreen,
                        modifier = Modifier.size(42.dp)
                    )
                }

                errorMsg?.let { message ->
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFFB4A1),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // -------- ambience chips
            Text(
                text = "AMBIENCE — CHOOSE A MOOD",
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 2.sp,
                color = Gold.copy(alpha = 0.85f)
            )
            Spacer(Modifier.height(10.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(NawaData.effects, key = { it.id }) { effect ->
                    EffectChip(
                        effect = effect,
                        selected = selectedEffectId == effect.id,
                        onClick = {
                            selectedEffectId =
                                if (selectedEffectId == effect.id) null else effect.id
                        }
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // -------- volume card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .glass()
                    .padding(horizontal = 18.dp, vertical = 12.dp)
            ) {
                VolumeRow(
                    icon = { Icons.Filled.VolumeUp },
                    label = "Qari Voice",
                    value = recitationVolume,
                    onValue = {
                        recitationVolume = it
                        recitation.volume = it
                    }
                )
                VolumeRow(
                    icon = { Icons.Filled.GraphicEq },
                    label = "Effects",
                    value = effectsVolume,
                    onValue = {
                        effectsVolume = it
                        engine.setVolume(it)
                    }
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "The ambience loops softly for as long as the Surah is reciting.",
                    style = MaterialTheme.typography.labelSmall,
                    color = Ivory.copy(alpha = 0.5f)
                )
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = "NAWA • Created by ABDUL AHAD ARYAN",
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 1.sp,
                color = Gold.copy(alpha = 0.5f)
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun EffectChip(effect: AmbientEffect, selected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Gold.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.07f))
            .border(
                width = 1.dp,
                color = if (selected) Gold else Color.White.copy(alpha = 0.16f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Icon(
            imageVector = effect.icon,
            contentDescription = effect.label,
            tint = if (selected) Gold else Ivory.copy(alpha = 0.8f),
            modifier = Modifier.size(26.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = effect.label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) GoldBright else Ivory.copy(alpha = 0.75f)
        )
    }
}

@Composable
private fun VolumeRow(
    icon: () -> ImageVector,
    label: String,
    value: Float,
    onValue: (Float) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(
            imageVector = icon(),
            contentDescription = label,
            tint = Gold,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Ivory.copy(alpha = 0.85f),
            modifier = Modifier.width(84.dp)
        )
        Slider(
            value = value,
            onValueChange = onValue,
            colors = sliderColors(),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${(value * 100).roundToInt()}%",
            style = MaterialTheme.typography.labelMedium,
            color = Ivory.copy(alpha = 0.75f),
            textAlign = TextAlign.End,
            modifier = Modifier.width(42.dp)
        )
    }
}

@Composable
private fun sliderColors() = SliderDefaults.colors(
    thumbColor = GoldBright,
    activeTrackColor = Gold,
    inactiveTrackColor = Color.White.copy(alpha = 0.18f)
)
