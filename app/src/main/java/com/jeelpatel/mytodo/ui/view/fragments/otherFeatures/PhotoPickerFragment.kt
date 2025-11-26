package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentPhotoPickerBinding

class PhotoPickerFragment : Fragment() {


    private var _binding: FragmentPhotoPickerBinding? = null
    private val binding get() = _binding!!


    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                photoSelected(uri)
            } else {
                photoSelectionError()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoPickerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectPhotoBtn.setOnClickListener {
            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }


    private fun photoSelected(photoUri: Uri) {
        Glide.with(binding.photoIv)
            .load(photoUri)
            .placeholder(R.drawable.ic_launcher_background)
            .centerCrop()
            .into(binding.photoIv)
    }


    private fun photoSelectionError() {
        TODO("Not yet implemented")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}