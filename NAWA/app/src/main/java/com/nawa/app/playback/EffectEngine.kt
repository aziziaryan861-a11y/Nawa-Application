package com.nawa.app.playback

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.annotation.RawRes

/**
 * Plays the selected ambience effect as a seamless, infinite loop
 * (SoundPool loop = -1) and keeps it alive until the recitation stops.
 * The stream volume follows the user's "Effects" slider so the ambience
 * never falls silent on its own.
 */
class EffectEngine(context: Context) {

    private val appContext = context.applicationContext
    private var effectsVolume = 0.6f
    private var activeRes: Int? = null
    private var streamId = 0

    private val resToSound = HashMap<Int, Int>()
    private val soundToRes = HashMap<Int, Int>()
    private val loaded = HashSet<Int>()

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(3)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        .build()

    init {
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                loaded.add(sampleId)
                val active = activeRes
                if (active != null && resToSound[active] == sampleId) {
                    startStream(active)
                }
            }
        }
    }

    fun load(@RawRes res: Int) {
        if (resToSound.containsKey(res)) return
        val soundId = soundPool.load(appContext, res, 1)
        resToSound[res] = soundId
        soundToRes[soundId] = res
    }

    /** Starts looping the given ambience (stops any previous one first). */
    fun play(@RawRes res: Int) {
        if (activeRes == res && streamId != 0) return
        stop()
        activeRes = res
        val soundId = resToSound[res]
        if (soundId != null && loaded.contains(soundId)) {
            startStream(res)
        }
        // If not finished loading yet, the load-complete listener starts it.
    }

    private fun startStream(@RawRes res: Int) {
        val soundId = resToSound[res] ?: return
        streamId = soundPool.play(soundId, effectsVolume, effectsVolume, 1, -1, 1f)
    }

    /** Stops the ambience loop completely. */
    fun stop() {
        if (streamId != 0) {
            soundPool.stop(streamId)
            streamId = 0
        }
        activeRes = null
    }

    /** Live volume control, 0f..1f. */
    fun setVolume(volume: Float) {
        effectsVolume = volume.coerceIn(0f, 1f)
        if (streamId != 0) {
            soundPool.setVolume(streamId, effectsVolume, effectsVolume)
        }
    }

    fun release() {
        stop()
        soundPool.release()
    }
}
