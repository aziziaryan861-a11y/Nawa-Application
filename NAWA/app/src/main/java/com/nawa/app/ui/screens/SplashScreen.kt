package com.nawa.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nawa.app.R
import com.nawa.app.ui.theme.Amiri
import com.nawa.app.ui.theme.Gold
import com.nawa.app.ui.theme.Ivory
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    var shown by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (shown) 1f else 0f,
        animationSpec = tween(900),
        label = "splashAlpha"
    )

    LaunchedEffect(Unit) {
        shown = true
        delay(2400)
        onDone()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF04120B), Color(0xFF0B2A19), Color(0xFF04120B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.alpha(alpha)) {
            Image(
                painter = painterResource(R.drawable.emblem),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(22.dp))
            Text(
                text = "NAWA",
                style = MaterialTheme.typography.displayLarge,
                color = Gold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "نَوَى",
                fontFamily = Amiri,
                fontSize = 26.sp,
                color = Gold.copy(alpha = 0.85f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = "Heavenly recitation • Living ambience",
                style = MaterialTheme.typography.bodyMedium,
                color = Ivory.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(42.dp))
            Text(
                text = "Created by ABDUL AHAD ARYAN",
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 2.sp,
                color = Gold.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
