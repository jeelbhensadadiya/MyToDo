package com.jeelpatel.mytodo.ui.view.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.util.Log
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeelpatel.mytodo.databinding.FragmentProfileBinding
import com.jeelpatel.mytodo.utils.AppPermissions


class ProfileFragment : Fragment() {


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedList = result.filterValues { !it }.keys

            when {
                deniedList.isEmpty() -> {
                    onAllPermissionGranted()
                    Log.d("PERMISSIONS", "ALL GRANTED : ${deniedList.joinToString()}")
                }

                deniedList.any { permission ->
                    !shouldShowRequestPermissionRationale(permission)
                } -> {
                    showSettingsDialog()
                    Log.d("PERMISSIONS", "Show Settings : ${deniedList.joinToString()}")
                }

                else -> {
                    showRationalDialog()
                    Log.d("PERMISSIONS", "Show Rational : ${deniedList.joinToString()}")
                }
            }
        }


    private val reCheckPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            recheckPermissions()
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        checkPermissions()


    }

    private fun checkPermissions() {
        val notGranted = AppPermissions.permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            onAllPermissionGranted()
        } else {
            permissionLauncher.launch(notGranted.toTypedArray())
        }

    }


    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Some permission is permanently denied. You can enable them in app settings.")
            .setPositiveButton("Go To Settings") { _, _ ->
                openAppSettings()
            }
            .setCancelable(false)
            .setNegativeButton("Cancel") { _, _ ->
                findNavController().popBackStack()
            }
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        reCheckPermissionLauncher.launch(intent)

    }


    private fun recheckPermissions() {
        val notGranted = AppPermissions.permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            onAllPermissionGranted()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Some Permissions still missing, please enable them from settings.")
                .setPositiveButton("Go To Settings") { _, _ ->
                    openAppSettings()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    findNavController().popBackStack()
                }
                .show()
        }
    }


    private fun showRationalDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Some permission is permanently denied. You can enable them in app settings.")
            .setPositiveButton("Go To Settings") { _, _ ->
                openAppSettings()
            }
            .setCancelable(false)
            .show()
    }


    private fun onAllPermissionGranted() {

        binding.cameraButtonBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToCameraFragment())
        }

        binding.mediaPlayerBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMediaListFragment())
        }

        binding.servicesBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToServicesFragment())
        }

        binding.simpleNotificationBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSimpleNotificationFragment())
        }

        binding.workManagerBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWorkManagerFragment())
        }

        binding.sensorsBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSensorsFragment())
        }

        binding.connectivityBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToConnectivityFragment())
        }

        binding.photoPickerBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPhotoPickerFragment())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}