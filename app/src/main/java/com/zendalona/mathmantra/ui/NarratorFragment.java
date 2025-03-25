package com.zendalona.mathmantra.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zendalona.mathmantra.MainActivity;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.FragmentNarratorBinding;
import com.zendalona.mathmantra.utils.TTSUtility;

import java.util.ArrayList;

public class NarratorFragment extends Fragment {

    private FragmentNarratorBinding binding;
    private TTSUtility tts;

    private ArrayList<String> theoryContents;

    private int currentIndex = 0;

    public NarratorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TTSUtility(requireContext());
        tts.setSpeechRate(0.9f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNarratorBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        assert args != null;
        if(!args.isEmpty()){
            theoryContents = args.getStringArrayList("contents");
        }

        updateTheoryContent();

        binding.repeatButton.setOnClickListener(v -> tts.speak(binding.theoryText.getText().toString()));

        binding.previousButton.setOnClickListener(v -> {
            if(currentIndex == 0) currentIndex++;
            currentIndex = (currentIndex - 1) % theoryContents.size();
            updateTheoryContent();
        });

        binding.nextButton.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % theoryContents.size();
            updateTheoryContent();
        });
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

    private void updateTheoryContent() {
        String content = theoryContents.get(currentIndex);
        binding.theoryText.setText(content);
        tts.speak(content);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tts.shutdown();
    }
}