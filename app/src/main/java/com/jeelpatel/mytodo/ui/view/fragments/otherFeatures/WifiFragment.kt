package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeelpatel.mytodo.databinding.FragmentWifiBinding
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class WifiFragment : Fragment() {


    private var _binding: FragmentWifiBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var wifiManager: WifiManager
    private lateinit var adapter: ArrayAdapter<String>


    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)

            if (success) {
                scanSuccess()
            } else {
                scanFailure()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWifiBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, ArrayList())
        binding.wifiListView.adapter = adapter

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        requireContext().registerReceiver(wifiScanReceiver, intentFilter)

        binding.wifiOnOffSwitch.setOnClickListener {
            val isWifiOn = wifiManager.isWifiEnabled

            if (isWifiOn) {
                startWifiScan()
                Toast.makeText(requireContext(), "Wifi is Enabled...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Wifi is Not Enabled...", Toast.LENGTH_SHORT)
                    .show()
                adapter.clear()
                adapter.notifyDataSetChanged()

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Enable Wifi")
                    .setMessage("Enable Wifi for Scanning")
                    .setPositiveButton("Enable") { _, _ ->
                        startActivity(Intent(Settings.Panel.ACTION_WIFI))
                    }
                    .setCancelable(false)
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }


    private fun startWifiScan() {
        val success = wifiManager.startScan()
        if (!success) {
            scanFailure()
        }
    }


    private fun scanFailure() {
        adapter.clear()
        adapter.notifyDataSetChanged()
    }


    private fun scanSuccess() {

        val results = wifiManager.scanResults

        val wifiNames =
            results.map { scan ->
                "${scan.wifiSsid} \n${scan.level} \n${scan.capabilities}"
            }

        adapter.clear()
        adapter.addAll(wifiNames)
        adapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        try {
            requireContext().unregisterReceiver(wifiScanReceiver)
        } catch (e: Exception) {
        }
        _binding = null
    }
}