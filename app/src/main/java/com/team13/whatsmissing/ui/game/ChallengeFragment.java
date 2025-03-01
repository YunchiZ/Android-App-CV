package com.team13.whatsmissing.ui.game;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team13.whatsmissing.R;
import com.team13.whatsmissing.databinding.FragmentGameChallengeBinding;

public class ChallengeFragment extends Fragment implements View.OnClickListener{
    private FragmentGameChallengeBinding binding;

    private String correct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGameChallengeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);
        //This is the result of the object detection and gaussian blur
        assert getArguments() != null;
        Bitmap bmp = getArguments().getParcelable("image");
        correct = getArguments().getString("label");

        binding.image6.setImageBitmap(bmp);
        binding.submit.setOnClickListener(l -> {
            EditText text = view.findViewById(R.id.answertext);
            String answer= text.getText().toString().toLowerCase();
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popup_feedback = inflater.inflate(R.layout.popup_feedback, null);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = false; //Forbids taps outside of popup to close it
            final PopupWindow popupWindow = new PopupWindow(popup_feedback, width, height, focusable);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            TextView textView = popup_feedback.findViewById(R.id.explanationTitle);
            Button btn1 = popup_feedback.findViewById(R.id.continueButton);
            btn1.setOnClickListener(v1 -> {
                LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View restart = inflater2.inflate(R.layout.popup_restart, null);
                int restartWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
                int restartHeight = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow window = new PopupWindow(restart, restartWidth, restartHeight, false);
                window.showAtLocation(view, Gravity.CENTER, 0, 0);
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
            if(answer.equals(correct.toLowerCase())){
                textView.setText(R.string.wellDone);
            }else{
                textView.setText(R.string.tryAgain);
                TextView content = popup_feedback.findViewById(R.id.feedback_text);
                content.setText(R.string.tryAgain);
            }
        });
    }


    @Override
    public void onClick(View v){
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
