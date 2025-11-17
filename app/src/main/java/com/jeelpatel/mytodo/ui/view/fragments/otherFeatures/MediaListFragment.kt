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

        binding.videoBtn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToVideoPlayerFragment(
                    mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                )
            )
        }

        binding.video2Btn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToVideoPlayerFragment(
                    mediaUrl = "https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.mp4"
                )
            )
        }

        binding.audioBtn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToVideoPlayerFragment(
                    mediaUrl = "https://actions.google.com/sounds/v1/alarms/digital_watch_alarm_long.ogg"
                )
            )
        }

        binding.audio2Btn.setOnClickListener {
            findNavController().navigate(
                MediaListFragmentDirections.actionMediaListFragmentToVideoPlayerFragment(
                    mediaUrl = "https://github.com/rafaelreis-hotmart/Audio-Sample-files/raw/master/sample.mp3"
                )
            )
        }
    }
}