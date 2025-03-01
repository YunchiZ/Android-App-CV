package com.team13.whatsmissing.ui.home;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.team13.whatsmissing.R;
import com.team13.whatsmissing.databinding.FragmentHomeBinding;

/**
 * Screen shown on startup
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    static public int mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NavController navController = Navigation.findNavController(view);
        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES){
            binding.setting.setColorFilter(Color.argb(255, 255, 255, 255));
        }
        if((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)  == Configuration.UI_MODE_NIGHT_YES){
            binding.imageView.setColorFilter(Color.argb(255, 255, 255, 255));
        }
        binding.setting.setOnClickListener(la -> {
            LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View fragment_tutorial = inflater2.inflate(R.layout.popup_settings, null);
            int width2 = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height2 = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = false; //Forbids taps outside of popup to close it
            final PopupWindow popupWindow2 = new PopupWindow(fragment_tutorial,width2,height2,focusable);
            popupWindow2.showAtLocation(view, Gravity.CENTER, 0,0);
            Button btn = fragment_tutorial.findViewById(R.id.saveButton);
            btn.setOnClickListener(v -> popupWindow2.dismiss());
        });
        //Binding for the how to play popup
        binding.howToPlay.setOnClickListener(l -> {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View fragment_tutorial = inflater.inflate(R.layout.fragment_tutorial, null);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = false; //Forbids taps outside of popup to close it
            final PopupWindow popupWindow = new PopupWindow(fragment_tutorial,width,height,focusable);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0,0);
            Button btn = fragment_tutorial.findViewById(R.id.closeButton1);
            btn.setOnClickListener(v -> popupWindow.dismiss());

            Button classicbtn = fragment_tutorial.findViewById(R.id.classicButton1);
            classicbtn.setOnClickListener(l1 -> {

                LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popup_howtoplay_classic = inflater1.inflate(R.layout.popup_howtoplay_classic, null);
                int width1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable1 = false; //Forbids taps outside of popup to close it
                final PopupWindow popupWindow1 = new PopupWindow(popup_howtoplay_classic, width1, height1, focusable1);
                popupWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
                popupWindow.dismiss();
                Button btn1 = popup_howtoplay_classic.findViewById(R.id.continueButton);
                btn1.setOnClickListener(v1 -> popupWindow1.dismiss());
            });
            Button challengebtn = fragment_tutorial.findViewById(R.id.challengeButton1);
            challengebtn.setOnClickListener(l1 -> {
                LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popup_howtoplay_challenge = inflater2.inflate(R.layout.popup_howtoplay_challenge, null);
                int width2= LinearLayout.LayoutParams.WRAP_CONTENT;
                int height2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable2 = false; //Forbids taps outside of popup to close it
                final PopupWindow popupWindow2 = new PopupWindow(popup_howtoplay_challenge, width2, height2, focusable2);
                popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0);
                popupWindow.dismiss();
                Button btn1 = popup_howtoplay_challenge.findViewById(R.id.continueButton);
                btn1.setOnClickListener(v2 -> popupWindow2.dismiss());
            });
        });
        binding.classicButton.setOnClickListener(l -> {
            mode = 0;
            navController.navigate(R.id.captureFragment);
        });
        binding.challengeButton.setOnClickListener(l -> {
            mode = 1;
            navController.navigate(R.id.captureFragment);
        });

        binding.explanation.setOnClickListener(l -> {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popup_explanation = inflater.inflate(R.layout.popup_explanation, null);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = false; //Forbids taps outside of popup to close it
            final PopupWindow popupWindow = new PopupWindow(popup_explanation,width,height,focusable);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0,0);

            Button btn = popup_explanation.findViewById(R.id.continueButton2);
            btn.setOnClickListener(v -> popupWindow.dismiss());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
