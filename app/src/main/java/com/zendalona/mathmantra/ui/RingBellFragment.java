package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.MainActivity;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentRingBellBinding;
import com.zendalona.mathmantra.utils.AccelerometerUtility;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.ResponseFeedbackDialog;
import com.zendalona.mathmantra.utils.SoundEffectUtility;
import com.zendalona.mathmantra.utils.TTSUtility;

public class RingBellFragment extends Fragment {

    private FragmentRingBellBinding binding;
    private AccelerometerUtility accelerometerUtility;
    private SoundEffectUtility soundEffectUtility;
    private RandomValueGenerator randomValueGenerator;
    private TTSUtility tts;
    int count, target;

    public RingBellFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffectUtility = SoundEffectUtility.getInstance(requireContext());
        accelerometerUtility = new AccelerometerUtility(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRingBellBinding.inflate(inflater, container, false);
        randomValueGenerator = new RandomValueGenerator();
        tts = new TTSUtility(requireContext());
        startGame();
        return binding.getRoot();
    }

    private void ringBell() {
        binding.bellAnimationView.playAnimation();
        soundEffectUtility.playSound(R.raw.bell_ring);
        binding.ringCount.setText(String.valueOf(++count));
        if (count == target) appreciateUser();
    }

    private void appreciateUser() {
        String message = "Well done";
        int gifResource = R.drawable.right;

        LayoutInflater inflater = getLayoutInflater();
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        // Load the GIF using Glide
        Glide.with(this)
                .asGif()
                .load(gifResource)
                .into(dialogBinding.gifImageView);

        dialogBinding.messageTextView.setText(message);

        tts.speak("Well done!, Click on continue!");

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Continue", (dialog, which) -> {
                    dialog.dismiss();
                    startGame();
                })
                .create()
                .show();
    }

    private void startGame() {
        count = 0;
        binding.ringCount.setText(String.valueOf(count));
        target = randomValueGenerator.generateNumberForCountGame();
        String targetText = "Ring the bell " + target + " times";
        tts.speak(targetText);
        binding.ringMeTv.setText(targetText);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Start a thread to check for shakes
        new Thread(() -> {
            while (isVisible()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Log.d("Accelerometer Thread sleep Error", e.getLocalizedMessage());
                    e.printStackTrace();
                }
                if (accelerometerUtility.isDeviceShaken())
                    requireActivity().runOnUiThread(this::ringBell);
            }
        }).start();

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Navigate back to MainFragment
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, new DashboardFragment()); // Change fragment_container to your actual container ID
                        transaction.addToBackStack(null); // Adds to back stack for proper back navigation
                        transaction.commit();
                    }
                });

}

@Override
public void onPause() {
    super.onPause();
    accelerometerUtility.unregisterListener();
}

@Override
public void onDestroyView() {
    super.onDestroyView();
    binding = null;
    tts.shutdown();
}
}