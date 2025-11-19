package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentSimpleNotificationBinding

class SimpleNotificationFragment : Fragment() {


    private var _binding: FragmentSimpleNotificationBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.generateNotificationBtn.setOnClickListener {
            val message = binding.messageEt.text.toString().trim()

            showNotify(message.ifEmpty { "Message Not Entered By User" })
        }
    }


    private fun showNotify(message: String) {
        val channelID = "notification_channel"

        val manager = requireContext().getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(
            NotificationChannel(
                channelID,
                "TestNotification",
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        val notification = NotificationCompat.Builder(requireContext(), channelID)
            .setSmallIcon(R.drawable.notification_24)
            .setContentTitle("Generated Notification")
            .setContentText(message)
            .build()

        manager.notify(5, notification)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}