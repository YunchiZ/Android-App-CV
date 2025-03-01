package com.team13.whatsmissing.ui.capture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraProvider;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.common.util.concurrent.ListenableFuture;
import com.team13.whatsmissing.R;
import com.team13.whatsmissing.common.ImageUtilities;
import com.team13.whatsmissing.data.model.DetectedImage;
import com.team13.whatsmissing.databinding.FragmentCaptureBinding;
import com.team13.whatsmissing.ui.home.HomeFragment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Screen for taking pictures
 */
@AndroidEntryPoint
public class CaptureFragment extends Fragment {

    private FragmentCaptureBinding binding;
    private ImageCapture imageCapture;
    private CaptureViewModel viewModel;
    private ProcessCameraProvider cameraProvider;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CaptureViewModel.class);

        // Bind preview to lifecycle
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                //ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
                Log.v("CaptureFragment", "Camera preview binding success!");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this.requireContext()));

        // Create image capture and bind to lifecycle
        imageCapture = new ImageCapture.Builder().build();
        try {
            cameraProviderFuture.get().bindToLifecycle(
                    this, CameraSelector.DEFAULT_BACK_CAMERA, imageCapture);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();


        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();


        binding.viewFinder.setScaleType(PreviewView.ScaleType.FIT_CENTER);
        preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCaptureBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.takePhoto.setOnClickListener(l -> onShutterPressed(view));

    }

    private void onShutterPressed(View view) {
        File file =
                new File(getContext().getExternalCacheDir().toString() + "out.jpg");
        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(file).build();

        binding.loading.setVisibility(View.VISIBLE);
        binding.takePhoto.setEnabled(false);

        imageCapture.takePicture(outputOptions,
                ContextCompat.getMainExecutor(this.getContext()), new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                        cameraProvider.unbindAll(); // stop the preview and pause on captured image

                        Bitmap bmp = BitmapFactory.decodeFile(outputFileResults.getSavedUri().getPath());
                        try {
                            bmp = ImageUtilities.rotateImageIfRequired(bmp, outputFileResults.getSavedUri());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        DetectedImage blurredImage = viewModel.blurObject(bmp);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("image", blurredImage.getImage());
                        bundle.putString("label", blurredImage.getLabel());
                        if(HomeFragment.mode == 0){
                            Navigation.findNavController(view).navigate(R.id.gameFragment, bundle);
                        }
                        else{
                            Navigation.findNavController(view).navigate(R.id.challengeFragment, bundle);
                        }


                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        exception.printStackTrace();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}