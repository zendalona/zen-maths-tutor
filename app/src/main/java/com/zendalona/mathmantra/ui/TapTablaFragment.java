package com.zendalona.mathmantra.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.MainActivity;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;
import com.zendalona.mathmantra.databinding.FragmentTapTablaBinding;
import com.zendalona.mathmantra.utils.RandomValueGenerator;
import com.zendalona.mathmantra.utils.SoundEffectUtility;
import com.zendalona.mathmantra.utils.TTSUtility;

public class TapTablaFragment extends Fragment {

    private FragmentTapTablaBinding binding;
    private SoundEffectUtility soundEffectUtility;
    private RandomValueGenerator randomValueGenerator;
    private TTSUtility tts;
    private int count, target;

    public TapTablaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundEffectUtility = SoundEffectUtility.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTapTablaBinding.inflate(inflater, container, false);
        randomValueGenerator = new RandomValueGenerator();
        tts = new TTSUtility(requireContext());
        startGame();
        binding.tablaAnimationView.setOnClickListener(v -> onTablaTapped());
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
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
    private void onTablaTapped() {
        binding.tapCount.setText(String.valueOf(++count));
        binding.tablaAnimationView.playAnimation();
        soundEffectUtility.playSound(R.raw.drums_sound);
        if(count == target) appreciateUser();
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
                    binding.tapCount.setText("0");
                    startGame();
                })
                .create()
                .show();
    }

    private void startGame() {
        count = 0;
        binding.tapMeTv.setText(String.valueOf(count));
        target = randomValueGenerator.generateNumberForCountGame();
        String targetText = "Tap the drum " + target + " times";
        tts.speak(targetText);
        binding.tapMeTv.setText(targetText);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release resources related to binding
        binding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release sound effect resources when fragment is destroyed
        // FIXME : soundEffectUtility.release();
    }
}
