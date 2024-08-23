package com.miguel.calaveriado.utils

import android.R.attr.x
import android.content.Context
import android.media.MediaPlayer


class Sound(private val audioID: Int, private val context: Context) {
    private var player: MediaPlayer? = null

    /**
     * Instancia de MediaPlayer
     * **/
    fun player(): MediaPlayer? {
        return player
    }

    /**
     * Elimina instancias de mediaplayer
     * **/
    fun release(){
        player!!.release()
    }

    /**
     * Inicia el sonido
     * **/
    fun start(){
        player = MediaPlayer.create(context, audioID)
        player!!.seekTo(500)
        player!!.start()
    }

    /**
     * Detiene el sonido
     **/
    fun stop(){
        player = MediaPlayer.create(context, audioID)
        player!!.stop()
    }
}