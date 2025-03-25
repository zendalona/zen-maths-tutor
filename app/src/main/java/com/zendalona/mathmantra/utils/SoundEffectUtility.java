package com.zendalona.mathmantra.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class SoundEffectUtility {
    private static SoundEffectUtility instance;
    private SoundPool soundPool;
    private Context context;
    private Map<Integer, Integer> soundMap; // Map to hold sound resource IDs and their corresponding SoundPool IDs

    // Singleton pattern to ensure a single instance of SoundEffectUtility
    public static synchronized SoundEffectUtility getInstance(Context context) {
        if (instance == null) {
            instance = new SoundEffectUtility(context.getApplicationContext());
        }
        return instance;
    }

    private SoundEffectUtility(Context context) {
        this.context = context;
        this.soundMap = new HashMap<>();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(1)
                .build();
    }

    public void loadSound(int soundResId) {
        int soundId = soundPool.load(context, soundResId, 1);
        soundMap.put(soundResId, soundId); // Map the resource ID to the SoundPool ID
    }

    public void playSound(int soundResId) {
        Integer soundId = soundMap.get(soundResId);
        if (soundId != null) {
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            Log.d("Sound played",soundId.toString());
        } else {
            this.loadSound(soundResId);
            this.playSound(soundResId);
        }
    }

    public void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
