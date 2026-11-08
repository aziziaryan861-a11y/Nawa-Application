package com.nawa.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.nawa.app.R

val Poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

/** Classical Naskh-style Arabic face used for Surah and Qari names. */
val Amiri = FontFamily(
    Font(R.font.amiri_regular, FontWeight.Normal),
    Font(R.font.amiri_bold, FontWeight.Bold)
)

private fun ts(size: TextUnit, weight: FontWeight, spacing: TextUnit = 0.sp) = TextStyle(
    fontFamily = Poppins,
    fontSize = size,
    fontWeight = weight,
    letterSpacing = spacing
)

val NawaTypography = Typography(
    displayLarge = ts(44.sp, FontWeight.Bold, 2.sp),
    headlineMedium = ts(26.sp, FontWeight.SemiBold),
    titleLarge = ts(20.sp, FontWeight.SemiBold),
    titleMedium = ts(16.sp, FontWeight.SemiBold),
    bodyLarge = ts(16.sp, FontWeight.Normal),
    bodyMedium = ts(14.sp, FontWeight.Normal),
    bodySmall = ts(12.sp, FontWeight.Normal),
    labelLarge = ts(14.sp, FontWeight.Medium, 0.6.sp),
    labelMedium = ts(12.sp, FontWeight.Medium, 0.8.sp),
    labelSmall = ts(11.sp, FontWeight.Medium, 1.2.sp)
)
