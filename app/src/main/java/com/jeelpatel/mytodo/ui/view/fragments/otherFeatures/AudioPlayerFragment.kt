package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.fragment.navArgs
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentAudioPlayerBinding
import com.jeelpatel.mytodo.service.MusicService
import com.jeelpatel.mytodo.utils.Config
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AudioPlayerFragment : Fragment() {

    @Inject
    lateinit var player: ExoPlayer
    private var isShuffleOn = false


    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!
    private val args: AudioPlayerFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoPlayer()
        setupPlayerState()

    }


    private fun videoPlayer() {

        // Attach the player to a view
        binding.playerView.player = player


        // Load audio only once
        val media = MediaItem.fromUri(args.audioUrl)
        player.setMediaItem(media)
        player.prepare()


    }


    private fun setupPlayerState() {
        player.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

                when (playbackState) {
                    Player.STATE_IDLE -> {
                    }

                    Player.STATE_READY -> {
                        performUiActions()
                    }

                    Player.STATE_ENDED -> {

                        if (player.repeatMode == Player.REPEAT_MODE_OFF) {
                            binding.playPauseMediaBtn.icon =
                                AppCompatResources.getDrawable(requireContext(), R.drawable.play_24)
                        } else {

                            // reset ui
                            binding.mediaSeekSlider.value = 0f
                            val total = player.duration
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


        // play pause action
        binding.playPauseMediaBtn.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java)

            if (player.isPlaying) {
                intent.action = Config.ACTION_PAUSE
                requireContext().startService(intent)

                binding.playPauseMediaBtn.icon =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.play_24)

            } else {
                intent.action = Config.ACTION_PLAY
                requireContext().startForegroundService(intent)

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

    }


    override fun onDestroyView() {
        super.onDestroyView()
        player.removeListener(object : Player.Listener {})

        val intent = Intent(requireContext(), MusicService::class.java)
        intent.action = Config.ACTION_STOP
        requireContext().startService(intent)

//        _binding = null // clear binding

    }
}