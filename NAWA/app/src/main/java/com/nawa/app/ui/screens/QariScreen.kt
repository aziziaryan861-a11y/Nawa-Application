package com.nawa.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nawa.app.data.NawaData
import com.nawa.app.data.Qari
import com.nawa.app.ui.theme.Amiri
import com.nawa.app.ui.theme.Gold
import com.nawa.app.ui.theme.GoldBright
import com.nawa.app.ui.theme.Ivory
import com.nawa.app.ui.theme.NightGreen

@Composable
fun QariScreen(onQariSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(NightGreen, Color(0xFF0B2417), NightGreen)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 22.dp)
    ) {
        Spacer(Modifier.height(26.dp))
        Text(
            text = "N A W A",
            style = MaterialTheme.typography.labelMedium,
            letterSpacing = 6.sp,
            color = Gold.copy(alpha = 0.85f)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Choose Your Qari",
            style = MaterialTheme.typography.headlineMedium,
            color = Ivory
        )
        Text(
            text = "Three legendary voices of Egypt",
            style = MaterialTheme.typography.bodyMedium,
            color = Ivory.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(24.dp))

        NawaData.qaris.forEach { qari ->
            QariCard(qari = qari, onClick = { onQariSelected(qari.id) })
            Spacer(Modifier.height(16.dp))
        }

        Spacer(Modifier.weight(1f))
        Text(
            text = "Created by ABDUL AHAD ARYAN",
            style = MaterialTheme.typography.labelSmall,
            letterSpacing = 1.5.sp,
            color = Gold.copy(alpha = 0.55f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun QariCard(qari: Qari, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Box {
            Image(
                painter = painterResource(qari.avatarRes),
                contentDescription = qari.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
            Box(
                Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Gold.copy(alpha = 0.55f), RoundedCornerShape(20.dp))
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = qari.name,
                style = MaterialTheme.typography.titleMedium,
                color = Ivory
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = qari.arabicName,
                fontFamily = Amiri,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = GoldBright
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = null,
                    tint = Gold.copy(alpha = 0.8f),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = qari.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Ivory.copy(alpha = 0.6f)
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Gold.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
    }
}
