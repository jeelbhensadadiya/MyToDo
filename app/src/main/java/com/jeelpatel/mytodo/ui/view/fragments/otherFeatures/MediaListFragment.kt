package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jeelpatel.mytodo.databinding.FragmentMediaListBinding

class MediaListFragment : Fragment() {


    private var _binding: FragmentMediaListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoUrl()
        audioUrl()

    }


    private fun audioUrl() {
        binding.audioBtn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToAudioPlayerFragment(
                    audioUrl = "https://webaudioapi.com/samples/audio-tag/chrono.mp3"
                )
            )
        }

        binding.audio2Btn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToAudioPlayerFragment(
                    audioUrl = "https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.mp3",

                    )
            )
        }
    }


    private fun videoUrl() {

        binding.videoBtn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToVideoPlayerFragment(
                    videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                )
            )
        }

        binding.video2Btn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToVideoPlayerFragment(
                    videoUrl = "https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.mp4"
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}