package com.jeelpatel.mytodo.utils

import android.Manifest
import android.os.Build

object AppPermissions {

    val cameraPermissions = arrayOf(
        Manifest.permission.CAMERA
    )

    val bluetoothPermissions = buildList {
        add(Manifest.permission.BLUETOOTH)
        add(Manifest.permission.BLUETOOTH_ADMIN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_SCAN)
            add(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }.toTypedArray()

    val wifiPermissions = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }
    }.toTypedArray()

    val audioPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )

    val notificationPermissions =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else emptyArray()

    val storagePermissions =
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else emptyArray()

    val usbPermissions = emptyArray<String>()

    val permissions = cameraPermissions +
            bluetoothPermissions +
            wifiPermissions +
            audioPermissions +
            notificationPermissions +
            storagePermissions
}