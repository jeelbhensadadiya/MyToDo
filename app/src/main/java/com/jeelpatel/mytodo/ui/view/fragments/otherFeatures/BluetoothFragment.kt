package com.jeelpatel.mytodo.ui.view.fragments.otherFeatures

import android.R
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.jeelpatel.mytodo.databinding.FragmentBluetoothBinding


class BluetoothFragment : Fragment() {


    private var _binding: FragmentBluetoothBinding? = null
    private val binding get() = _binding!!


    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var pairedAdapter: ArrayAdapter<String>
    private lateinit var discoveredAdapter: ArrayAdapter<String>
    private val discoveredDevices = mutableListOf<BluetoothDevice>()


    // ----------- RUNTIME PERMISSION HANDLER -----------
    private val bluetoothPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result.values.all { it }) startDiscovery()
            else Toast.makeText(requireContext(), "Bluetooth permission denied", Toast.LENGTH_SHORT)
                .show()
        }


    // ----------- BROADCAST RECEIVER -----------
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    discoveredAdapter.clear()
                    discoveredDevices.clear()
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                }

                BluetoothDevice.ACTION_FOUND -> {
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    device?.let { addDeviceToList(it) }
                }

                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    device?.let { onBondStateChanged(it) }
                }
            }
        }
    }


    // ----------- ADD DEVICE TO LIST -----------
    fun addDeviceToList(device: BluetoothDevice) {

        val type = getDeviceType(device.bluetoothClass)
        val display = "${device.name ?: "Unknown"}\n${device.address}\n$type"

        if (discoveredAdapter.getPosition(display) == -1) {
            discoveredDevices.add(device)
            discoveredAdapter.add(display)
        }
    }


    // ----------- DEVICE TYPE DETECTOR -----------
    private fun getDeviceType(cls: BluetoothClass?): String {
        return when (cls?.majorDeviceClass) {
            BluetoothClass.Device.Major.AUDIO_VIDEO -> "Audio Device 🎧"
            BluetoothClass.Device.Major.PHONE -> "Phone 📱"
            BluetoothClass.Device.Major.COMPUTER -> "Computer 💻"
            BluetoothClass.Device.Major.WEARABLE -> "Wearable ⌚"
            BluetoothClass.Device.Major.IMAGING -> "Camera 📷"
            BluetoothClass.Device.Major.PERIPHERAL -> "Peripheral ⌨️"
            BluetoothClass.Device.Major.TOY -> "Toy 🎮"
            else -> "Unknown Device ❓"
        }
    }


    // ----------- BONDING STATE HANDLER -----------
    fun onBondStateChanged(device: BluetoothDevice) {
        val msg = when (device.bondState) {
            BluetoothDevice.BOND_BONDING -> "Pairing with ${device.name}..."
            BluetoothDevice.BOND_BONDED -> "Paired with ${device.name}"
            BluetoothDevice.BOND_NONE -> "Pairing failed with ${device.name}"
            else -> ""
        }
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


    // ----------- PAIR WITH CLICKED DEVICE -----------
    private fun pairDevice(device: BluetoothDevice) {
        try {
            if (bluetoothAdapter.isDiscovering) bluetoothAdapter.cancelDiscovery()
            device.createBond()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Pairing failed", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBluetoothBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // ----------- Init adapter -----------
        pairedAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1)
        discoveredAdapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1)

        binding.pairedBluetoothListView.adapter = pairedAdapter
        binding.otherBluetoothListView.adapter = discoveredAdapter


        // ----------- Init Bluetooth -----------
        val manager: BluetoothManager =
            requireContext().getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = manager.adapter


        // ----------- Register for broadcasts -----------
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        }
        requireContext().registerReceiver(receiver, filter)


        // ----------- Discover Paired Device -----------
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            pairedAdapter.add(deviceName)
        }


        // ----------- Check bluetooth is enabled or not -----------
        binding.bluetoothBtn.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == false) {
                startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            } else {
                Toast.makeText(
                    requireContext(),
                    "Scanning Devices...",
                    Toast.LENGTH_SHORT
                ).show()
                startDiscovery()
            }
        }


        // ----------- On clicking discovered device → pair -----------
        binding.otherBluetoothListView.setOnItemClickListener { _, _, pos, _ ->
            pairDevice(discoveredDevices[pos])
        }
    }


    private fun startDiscovery() {
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        } else {
            bluetoothAdapter.startDiscovery()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(receiver)
        _binding = null
    }
}