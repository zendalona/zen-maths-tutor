package com.zendalona.mathmantra.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechRecognitionUtility {

    private Context context;
    private SpeechRecognizer speechRecognizer;
    private SpeechRecognitionCallback callback;
    private final String TAG = "SpeechRecognitionUtility";

    public SpeechRecognitionUtility(Context context, SpeechRecognitionCallback callback) {
        this.context = context;
        this.callback = callback;
        this.speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        this.speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                Log.d(TAG, "Ready for speech");
            }

            @Override
            public void onBeginningOfSpeech() {
                Log.d(TAG, "Beginning of speech");
            }

            @Override
            public void onRmsChanged(float rmsdB) {
                // handle changes in volume
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
                // handle raw audio data
            }

            @Override
            public void onEndOfSpeech() {
                Log.d(TAG, "End of speech");
            }

            @Override
            public void onError(int error) {
                Log.e(TAG, "Error: " + error);
                if (callback != null) callback.onError(error);
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String spokenText = matches.get(0);
                    Log.d(TAG, "Heard: " + spokenText);
                    if (callback != null) callback.onResults(spokenText);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // TODO : check partial results
            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

    public void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.startListening(intent);
    }

    public void stopListening() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
        }
    }

    public void destroy() {
        if (speechRecognizer != null) speechRecognizer.destroy();
    }

    public interface SpeechRecognitionCallback {
        void onResults(String spokenText);
        void onError(int error);
    }

}
