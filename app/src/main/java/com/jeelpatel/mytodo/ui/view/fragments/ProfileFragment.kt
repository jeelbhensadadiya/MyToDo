package com.jeelpatel.mytodo.ui.view.fragments

import android.Manifest
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeelpatel.mytodo.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )


    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            val deniedList = results.filterValues { !it }.keys

            when {
                // all permission granted
                deniedList.isEmpty() -> {

                    // fresh grant success
                    showMessage("All Permissions Granted! 1")
                    onAllPermissionGranted()
                }

                // don't ask again
                deniedList.any { permission ->
                    !shouldShowRequestPermissionRationale(permission)
                } -> {
                    showSettingsDialog()
                }

                // temporally denied (normal Deny)
                else -> {
                    showRationaleDialog()
                }
            }
        }


    private val startActivityOrReCheckSettings = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        recheckPermissionsAfterSettings()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAndRequestPermissions()

    }


    private fun onAllPermissionGranted() {

    }


    private fun checkAndRequestPermissions() {

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {

            // user has already allowed them earlier
            showMessage("All permission Granted! 2")
            onAllPermissionGranted()
        } else {
            permissionLauncher.launch(notGranted.toTypedArray())
        }
    }


    private fun recheckPermissionsAfterSettings() {
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {

            // Happens after returning from settings
            showMessage("All permission Granted! 3")
            onAllPermissionGranted()
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Some Permissions still missing, please enable them from settings.")
                .setCancelable(false)
                .setPositiveButton("Go To Settings") { _, _ ->
                    openAppSettings()
                }
                .show()
        }
    }


    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Some permissions are permanently denied. You can enable them in app settings.")
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
        startActivityOrReCheckSettings.launch(intent)
    }


    private fun showRationaleDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Permission Required")
            .setMessage(
                "We need Camera and Location permissions to provide full functionality. " +
                        "Please grant them to continue."
            )
            .setPositiveButton("Grant Again") { _, _ ->
                checkAndRequestPermissions()
            }
            .setNegativeButton("Cancel") { _, _ ->
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMainFragment())
            }
            .show()
    }


    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null // clear binding
    }
}