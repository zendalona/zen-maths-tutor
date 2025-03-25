package com.zendalona.mathmantra.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import com.zendalona.mathmantra.R;
import com.zendalona.mathmantra.databinding.DialogResultBinding;

import java.util.Random;

public class ResponseFeedbackDialog {

    private static final int DIALOG_DURATION = 3 * 1000;
    private static final String[] POSITIVE_MESSAGES = {
            "Awesome!",
            "Great job!",
            "Well done!",
            "Correct!",
            "Bravo!"
    };
    private static final String[] NEGATIVE_MESSAGES = {
            "Try again!",
            "Not quite!",
            "Oops, wrong answer!",
            "Better luck next time!",
            "Incorrect!"
    };


    public static void showFeedbackDialog(Context context, boolean isCorrect, Runnable onDismissAction) {
        String message = isCorrect ? getRandomPositiveMessage() : getRandomNegativeMessage();
        int gifResource = isCorrect ? R.drawable.right : R.drawable.wrong;

        LayoutInflater inflater = LayoutInflater.from(context);
        DialogResultBinding dialogBinding = DialogResultBinding.inflate(inflater);
        View dialogView = dialogBinding.getRoot();

        Glide.with(context)
                .asGif()
                .load(gifResource)
                .into(dialogBinding.gifImageView);

        dialogBinding.messageTextView.setText(message);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        alertDialog.show();

        // Automatically dismiss the dialog after t seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
                if (onDismissAction != null) {
                    onDismissAction.run();
                }
            }
        }, DIALOG_DURATION);
    }

    private static String getRandomPositiveMessage() {
        return POSITIVE_MESSAGES[new Random().nextInt(POSITIVE_MESSAGES.length)];
    }

    private static String getRandomNegativeMessage() {
        return NEGATIVE_MESSAGES[new Random().nextInt(NEGATIVE_MESSAGES.length)];
    }

}
