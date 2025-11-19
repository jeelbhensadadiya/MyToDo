package com.jeelpatel.mytodo.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay

class WorkerExample(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            for (i: Int in 0..40) {
                Log.d("UPLOADING", "Progress $i")
                delay(250)
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

}