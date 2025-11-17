package com.jeelpatel.mytodo.ui.view.fragments.otherFutures

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.TrackSelectionDialogBuilder
import androidx.navigation.fragment.navArgs
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentVideoPlayerBinding
import com.jeelpatel.mytodo.utils.UiHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class VideoPlayerFragment : Fragment() {

    var player: ExoPlayer? = null
    private var isShuffleOn = false
    private var isFullScreen = false


    private var _binding: FragmentVideoPlayerBinding? = null
    private val binding get() = _binding!!
    private val args: VideoPlayerFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        videoPlayer()
        setupPlayerState()
    }


    private fun videoPlayer() {

        // Create the player instance
        player = ExoPlayer.Builder(requireContext())
            .setSeekForwardIncrementMs(10_000)
            .setSeekBackIncrementMs(10_000)
            .build()


        // Attach the player to a view
        binding.playerView.player = player


        // Build the media item.
        val mediaItem =
            MediaItem.fromUri(args.mediaUrl)


        // Set the media item to be played.
        player?.setMediaItem(mediaItem)


        // Prepare the player.
        player?.prepare()

    }


    private fun setupPlayerState() {
        player?.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                when (playbackState) {
                    Player.STATE_IDLE -> {
                        binding.playerView.visibility = View.GONE
                    }

                    Player.STATE_READY -> {
                        binding.playerView.visibility = View.VISIBLE
                        performUiActions()
                    }

                    Player.STATE_ENDED -> {

                        if (player?.repeatMode == Player.REPEAT_MODE_OFF) {
                            binding.playPauseMediaBtn.icon =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.play_24)
                        } else {

                            // reset ui
                            binding.mediaSeekSlider.value = 0f
                            val total = player?.duration ?: 0L
                            binding.mediaTimerTv.text = "00:00 - ${UiHelper.formatTime(total)}"

                        }
                    }
                }
            }


            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                UiHelper.showToast(requireContext(), error.message.toString())
            }
        })
    }


    private fun performUiActions() {


        // set to non null
        val player = player!!


        // play pause action
        binding.playPauseMediaBtn.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                binding.playPauseMediaBtn.icon =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.play_24)
            } else if (!player.isPlaying) {
                player.play()
                binding.playPauseMediaBtn.icon =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.pause_24)
            }
        }


        // playback speed action
        var playbackSpeed = 1.0f
        binding.playbackSpeedBtn.setOnClickListener {
            playbackSpeed = when (playbackSpeed) {
                0.25f -> 0.50f
                0.50f -> 0.75f
                0.75f -> 1.0f
                1.0f -> 1.25f
                1.25f -> 1.50f
                1.50f -> 2.0f
                else -> 0.25f
            }

            player.setPlaybackSpeed(playbackSpeed)
            binding.playbackSpeedBtn.text = "${playbackSpeed}x"
        }


        // use for live update in timer and seek slider
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                while (isActive) {

                    val rawCurrent = player.currentPosition
                    val rawTotal = player.duration.takeIf { it > 0 } ?: 0L

                    val current = rawCurrent.coerceIn(0, rawTotal)

                    binding.mediaTimerTv.text =
                        "${UiHelper.formatTime(current)} - ${UiHelper.formatTime(rawTotal)}"

                    binding.mediaSeekSlider.valueTo = rawTotal.toFloat()
                    binding.mediaSeekSlider.value = current.toFloat()

                    delay(500)
                }
            }
        }


        // seek slider
        binding.mediaSeekSlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                player.seekTo(value.toLong())
            }
        }


        // media repeat mode
        binding.mediaRepeatBtn.setOnClickListener {
            player.repeatMode = if (player.repeatMode == Player.REPEAT_MODE_OFF) {
                Player.REPEAT_MODE_ONE
            } else {
                Player.REPEAT_MODE_OFF
            }
        }


        // medial shuffle mode
        binding.mediaShuffleBtn.setOnClickListener {
            isShuffleOn = !isShuffleOn
            player.shuffleModeEnabled = isShuffleOn
        }


        // forward media to 10sec
        binding.forwardMediaBtn.setOnClickListener {
            player.seekForward()
        }


        // backward media to 10sec
        binding.backwardMediaBtn.setOnClickListener {
            player.seekBack()
        }


        // full screen
//        binding.mediaFullScreenBtn.setOnClickListener {
//            if (!isFullScreen) {
//                enterFullScreen()
//            } else {
//                exitFullScreen()
//            }
//        }


        // subtitle
        binding.mediaSubtitleBtn.setOnClickListener {
            val dialog = TrackSelectionDialogBuilder(
                requireContext(),
                "Select Subtitle",
                player,
                C.TRACK_TYPE_TEXT
            ).build()

            dialog.show()
        }
    }


    private fun enterFullScreen() {
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        // Make PlayerView fully fullscreen
        binding.playerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.playerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        isFullScreen = true
    }


    private fun exitFullScreen() {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

        // Restore height if needed (e.g., 250dp or match_parent)
        binding.playerView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.playerView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        isFullScreen = false
    }


    override fun onStop() {
        super.onStop()
        player?.pause()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        player = null // clear player
        _binding = null // clear binding
    }
}