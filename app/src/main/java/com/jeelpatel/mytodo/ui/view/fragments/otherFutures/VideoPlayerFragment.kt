package com.jeelpatel.mytodo.ui.view.fragments.otherFutures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.jeelpatel.mytodo.databinding.FragmentVideoPlayerBinding


class VideoPlayerFragment : Fragment() {

    var player: ExoPlayer? = null


    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        videoPlayer()
    }

    private fun videoPlayer() {

        // Create the player instance
        player = ExoPlayer.Builder(requireContext()).build()


        // Attach the player to a view
        binding.playerView.player = player


        // Build the media item.
        val mediaItem =
            MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")


        // Set the media item to be played.
        player?.setMediaItem(mediaItem)


        // Prepare the player.
        player?.prepare()


    }


    override fun onStop() {
        super.onStop()
        player?.pause()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null
        _binding = null
    }
}