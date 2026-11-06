package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import java.util.Locale

class TextToSpeechHelper(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = mutableStateOf(false)
    val isSpeaking: State<Boolean> = _isSpeaking

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            val arabicLocale = Locale("ar")
            val result = tts?.setLanguage(arabicLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.getDefault())
            }

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
        }
    }

    fun speak(text: String) {
        if (!isInitialized || text.isBlank()) return
        stop()
        // Strip out markdown tokens for natural speech
        val cleanText = text
            .replace(Regex("[#*~_`>\\[\\]|]"), " ")
            .replace(Regex("-{3,}"), " ")
            .trim()

        _isSpeaking.value = true
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "DOC_READ")
    }

    fun stop() {
        if (_isSpeaking.value) {
            tts?.stop()
            _isSpeaking.value = false
        }
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
