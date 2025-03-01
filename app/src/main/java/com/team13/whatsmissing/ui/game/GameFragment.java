package com.team13.whatsmissing.ui.game;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.team13.whatsmissing.common.Constant.OBJECTS;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team13.whatsmissing.R;
import com.team13.whatsmissing.data.repository.AssociationsRepository;
import com.team13.whatsmissing.databinding.FragmentGameClassicBinding;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Screen where player enters whats missing
 */
@AndroidEntryPoint
public class GameFragment extends Fragment {

    private FragmentGameClassicBinding binding;
    private String correct;
    private NavController navController;
    private List<String> associations;

    @Inject
    AssociationsRepository associationsRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGameClassicBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        //This is the result of the object detection and gaussian blur
        assert getArguments() != null;
        Bitmap bmp = getArguments().getParcelable("image");
        String label = getArguments().getString("label");
        setupButtons(label);
        binding.image.setImageBitmap(bmp);
        binding.option0.setOnClickListener(v -> onClick(binding.option0));
        binding.option1.setOnClickListener(v -> onClick(binding.option1));
        binding.option2.setOnClickListener(v -> onClick(binding.option2));
        binding.option3.setOnClickListener(v -> onClick(binding.option3));
    }

    private void setupButtons(String correctAnswer) {
        Thread associationsThread;
        if(correctAnswer.toLowerCase().equals("mouse")){
            final String val = "computer";
            associationsThread = new Thread(() -> associations = associationsRepository.getAssociations(val));
        }else {
            associationsThread = new Thread(() -> associations = associationsRepository.getAssociations(correctAnswer));
        }
        associationsThread.start();
        try {
            associationsThread.join();
            Log.d("GameFragment", associations.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Button[] options = {binding.option0, binding.option1, binding.option2, binding.option3};
        int correctButton = new Random().nextInt(options.length);
        options[correctButton].setText(correctAnswer);
        options[correctButton].setOnClickListener(null); // correct answer
        if (associations.size() < 4) {
            for (int i = 0; i < options.length; i++) {
                if (i != correctButton) {
                    options[i].setOnClickListener(null); // not correct answer
                    options[i].setText(getRandomObjectLabel());
                }
            }
        } else {
            List<String> words = associations;
            for (int i = 0; i < options.length; i++) {
                if (i != correctButton) {
                    options[i].setOnClickListener(null); // not correct answer
                    //options[i].setText(getRandomObjectLabel());
                    options[i].setText(words.remove(0));
                }
            }
        }
        correct = correctAnswer;
    }

    private String getRandomObjectLabel() {
        int index = new Random().nextInt(OBJECTS.length);
        return OBJECTS[index];
    }

    public void onClick(Button button) { // using binding.someid to get objects
        binding.option0.setEnabled(false);
        binding.option1.setEnabled(false);
        binding.option2.setEnabled(false);
        binding.option3.setEnabled(false);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup_feedback = inflater.inflate(R.layout.popup_feedback, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = false; //Forbids taps outside of popup to close it
        final PopupWindow popupWindow = new PopupWindow(popup_feedback, width, height, focusable);
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
        TextView textView = popup_feedback.findViewById(R.id.explanationTitle);
        Button btn1 = popup_feedback.findViewById(R.id.continueButton);
        btn1.setOnClickListener(v1 -> {
            LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View restart = inflater2.inflate(R.layout.popup_restart, null);
            int restartWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
            int restartHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow window = new PopupWindow(restart, restartWidth, restartHeight, false);
            window.showAtLocation(getView(), Gravity.CENTER, 0, 0);
            popupWindow.dismiss();
            Button re = restart.findViewById(R.id.restartButton);
            re.setOnClickListener(v2 -> {
                window.dismiss();
                navController.navigate(R.id.captureFragment);
            });
            Button menu = restart.findViewById(R.id.homeButton);
            menu.setOnClickListener(v2 -> {
                window.dismiss();
                navController.navigate(R.id.homeFragment);
            });
        });
        if (button.getText().equals(correct)) {
            textView.setText(R.string.wellDone);
        } else {
            textView.setText(R.string.tryAgain);
            TextView content = popup_feedback.findViewById(R.id.feedback_text);
            content.setText(R.string.tryAgain);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}