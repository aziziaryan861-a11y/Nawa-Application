package com.nawa.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nawa.app.data.NawaData
import com.nawa.app.data.Surah
import com.nawa.app.ui.theme.Amiri
import com.nawa.app.ui.theme.Gold
import com.nawa.app.ui.theme.GoldBright
import com.nawa.app.ui.theme.Ivory
import com.nawa.app.ui.theme.NightGreen

@Composable
fun SurahScreen(qariId: String, onSurahSelected: (Int) -> Unit, onBack: () -> Unit) {
    val qari = NawaData.qariById(qariId)

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
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Ivory
                )
            }
            Spacer(Modifier.width(4.dp))
            Column {
                Text(
                    text = "Top Five Surahs",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Ivory
                )
                Text(
                    text = "Recited by ${qari.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gold.copy(alpha = 0.85f)
                )
            }
        }
        Spacer(Modifier.height(20.dp))

        NawaData.surahs.forEach { surah ->
            SurahCard(surah = surah, onClick = { onSurahSelected(surah.number) })
            Spacer(Modifier.height(14.dp))
        }

        Spacer(Modifier.weight(1f))
        Text(
            text = qari.arabicName,
            fontFamily = Amiri,
            fontSize = 20.sp,
            color = Ivory.copy(alpha = 0.35f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun SurahCard(surah: Surah, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(listOf(GoldBright, Gold))
                )
        ) {
            Text(
                text = surah.number.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = NightGreen
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = surah.nameArabic,
                fontFamily = Amiri,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = GoldBright
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "${surah.nameEnglish} • ${surah.meaning}",
                style = MaterialTheme.typography.bodySmall,
                color = Ivory.copy(alpha = 0.65f)
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Gold.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
    }
}
