package com.jeelpatel.mytodo.di

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {


    @Provides
    @Singleton
    fun bluetoothManagerProvider(@ApplicationContext context: Context): BluetoothManager =
        context.getSystemService(BluetoothManager::class.java)


    @Provides
    @Singleton
    fun bluetoothAdapterProvider(bluetoothManager: BluetoothManager): BluetoothAdapter =
        bluetoothManager.adapter
            ?: throw IllegalStateException("Bluetooth Not supported on this Device")
}