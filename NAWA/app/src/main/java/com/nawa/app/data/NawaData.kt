package com.nawa.app.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FlutterDash
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Waves
import androidx.compose.ui.graphics.vector.ImageVector
import com.nawa.app.R

data class Qari(
    val id: String,
    val name: String,
    val arabicName: String,
    val title: String,
    val avatarRes: Int,
    val baseUrl: String
)

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val meaning: String
)

data class AmbientEffect(
    val id: String,
    val label: String,
    val soundRes: Int,
    val videoRes: Int,
    val icon: ImageVector
)

object NawaData {

    /** The three legendary Egyptian Qaris. */
    val qaris = listOf(
        Qari(
            id = "basit",
            name = "Abdul Basit Abdul Samad",
            arabicName = "عبد الباسط عبد الصمد",
            title = "The Golden Voice of Egypt",
            avatarRes = R.drawable.qari_basit,
            baseUrl = "https://server7.mp3quran.net/basit"
        ),
        Qari(
            id = "hussary",
            name = "Mahmoud Khalil Al-Hussary",
            arabicName = "محمود خليل الحصري",
            title = "The Master of Tajweed",
            avatarRes = R.drawable.qari_hussary,
            baseUrl = "https://server8.mp3quran.net/husr"
        ),
        Qari(
            id = "minshawi",
            name = "Mohamed Siddiq Al-Minshawi",
            arabicName = "محمد صديق المنشاوي",
            title = "The Angelic Melody",
            avatarRes = R.drawable.qari_minshawi,
            baseUrl = "https://server10.mp3quran.net/minsh"
        )
    )

    /** The five most-beloved Surahs, in NAWA's order. */
    val surahs = listOf(
        Surah(55, "الرحمن", "Ar-Rahman", "The Most Merciful"),
        Surah(36, "يس", "Yasin", "Ya Seen"),
        Surah(67, "الملك", "Al-Mulk", "The Sovereignty"),
        Surah(56, "الواقعة", "Al-Waqi'ah", "The Inevitable Event"),
        Surah(50, "ق", "Qaf", "The Letter Qaf")
    )

    /** Six living ambiences: each one has a sound loop + a matching background video. */
    val effects = listOf(
        AmbientEffect("rain", "Rain", R.raw.snd_rain, R.raw.vid_rain, Icons.Filled.WaterDrop),
        AmbientEffect("fire", "Fire", R.raw.snd_fire, R.raw.vid_fire, Icons.Filled.LocalFireDepartment),
        AmbientEffect("river", "River", R.raw.snd_river, R.raw.vid_river, Icons.Filled.Waves),
        AmbientEffect("birds", "Birds", R.raw.snd_birds, R.raw.vid_birds, Icons.Filled.FlutterDash),
        AmbientEffect("wind", "Wind", R.raw.snd_wind, R.raw.vid_wind, Icons.Filled.Air),
        AmbientEffect("night", "Night", R.raw.snd_night, R.raw.vid_night, Icons.Filled.DarkMode)
    )

    fun qariById(id: String): Qari = qaris.first { it.id == id }

    fun surahByNumber(number: Int): Surah = surahs.first { it.number == number }

    fun effectById(id: String): AmbientEffect? = effects.firstOrNull { it.id == id }

    /** Full-surah recitation file on the mp3quran.net servers, e.g. 036.mp3 */
    fun recitationUrl(qari: Qari, surah: Surah): String =
        "${qari.baseUrl}/${surah.number.toString().padStart(3, '0')}.mp3"
}
