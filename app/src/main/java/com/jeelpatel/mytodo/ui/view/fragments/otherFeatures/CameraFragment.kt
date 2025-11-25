package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeelpatel.mytodo.databinding.FragmentCameraBinding
import com.jeelpatel.mytodo.utils.UiHelper
import kotlinx.coroutines.launch
import java.io.File

class CameraFragment : Fragment() {


    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private lateinit var previewView: PreviewView
    private var imageCapture: ImageCapture? = null
    private var imageCaptureFlashMode: Int = ImageCapture.FLASH_MODE_OFF
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var camera: Camera? = null // 👈 store camera reference
    private var cameraControl: CameraControl? = null
    private var cameraInfo: CameraInfo? = null
    private var isTorchOn = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCameraUi()

    }


    private fun setupCameraUi() {
        previewView = binding.imagePreview

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    startCamera()
                }

                // Switch front/back camera
                binding.rotateCameraBtn.setOnClickListener {
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    else
                        CameraSelector.DEFAULT_BACK_CAMERA
                    launch {
                        startCamera()
                    }
                }


                // Toggle flash (auto-on when capturing)
                binding.flashBtn.setOnClickListener {
                    if (imageCaptureFlashMode == ImageCapture.FLASH_MODE_ON) {
                        imageCaptureFlashMode = ImageCapture.FLASH_MODE_OFF
                        UiHelper.showToast(requireContext(), "Flash Off")
                    } else {
                        imageCaptureFlashMode = ImageCapture.FLASH_MODE_ON
                        UiHelper.showToast(requireContext(), "Flash On")
                    }
                    launch {
                        startCamera()
                    }
                }
            }
        }


        // Capture image
        binding.captureImageBtn.setOnClickListener { takePhoto() }


        // Torch (continuous flashlight)
        binding.flashBtn.setOnClickListener {
            isTorchOn = !isTorchOn
            cameraControl?.enableTorch(isTorchOn)
            UiHelper.showToast(requireContext(), if (isTorchOn) "Torch On" else "Torch Off")
        }

        // Zoom controls
        binding.zoomSlider.addOnChangeListener { _, value, _ ->
            try {
                cameraControl?.setZoomRatio(value)
            } catch (e: Exception) {
                Log.e("CameraX", "Zoom failed: ${e.message}")
            }
        }

        // Tap to focus
        setupTapToFocus()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupTapToFocus() {


        // Focus on the point user tapped
        previewView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val factory = previewView.meteringPointFactory
                val point = factory.createPoint(event.x, event.y)
                val action = FocusMeteringAction.Builder(point).build()
                cameraControl?.startFocusAndMetering(action)
            }
            true
        }
    }


    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val storageLocation = requireContext().filesDir // internal storage

        val photoFile = File(
            storageLocation,
            System.currentTimeMillis().toString() + ".jpg"
        )

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    UiHelper.showToast(requireContext(), "Image Saved")
                }

                override fun onError(exception: ImageCaptureException) {
                    UiHelper.showToast(requireContext(), "Capture Failed")
                }
            }
        )
    }


    private fun startCamera() {

        // Step 1. Get camera provider
        val cameraProviderFuture = ProcessCameraProvider.Companion.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()


            // Step 2. Build the Preview
            val preview = Preview.Builder().build()
            preview.surfaceProvider = previewView.surfaceProvider


            // Step 3. Prepare ImageCapture use case
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setFlashMode(imageCaptureFlashMode)
                .build()

            try {
                cameraProvider.unbindAll()


                // Step 4. Bind everything to lifecycle
                camera = cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )


                // Step 5. Get CameraControl & CameraInfo
                cameraControl = camera?.cameraControl
                cameraInfo = camera?.cameraInfo


                // Step 6. Synchronize Zoom with Slider
                cameraInfo?.zoomState?.observe(viewLifecycleOwner) { zoomState ->
                    val minZoom = zoomState.minZoomRatio
                    val maxZoom = zoomState.maxZoomRatio
                    val current = zoomState.zoomRatio

                    if (minZoom < maxZoom) {
                        binding.zoomSlider.valueFrom = minZoom
                        binding.zoomSlider.valueTo = maxZoom
                    } else {
                        binding.zoomSlider.valueFrom = 0f
                        binding.zoomSlider.valueTo = 1f
                    }

                    binding.zoomSlider.stepSize = 0f

                    // Sync slider → camera
                    if (binding.zoomSlider.value != current) {
                        binding.zoomSlider.value = current
                    }
                }


            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}