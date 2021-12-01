package com.valeriyu.notifications

import android.content.*
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.valeriyu.notifications.data.DownloadState
import com.valeriyu.notifications.databinding.FragmentSynchronizationBinding
import com.valeriyu.notifications.notifications.*
import com.valeriyu.notifications.ui.NotificationsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class SynchronizationFragment() :
    ViewBindingFragment<FragmentSynchronizationBinding>(FragmentSynchronizationBinding::inflate) {

    private val viewModel: NotificationsViewModel by viewModels()
    private var networkMonitor: NetworkStatusMonitor? = null

    override fun onResume() {
        super.onResume()
        networkMonitor?.register()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.urlTextField.editText?.setText("https://images.unsplash.com/photo-1473968512647-3e447244af8f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2250&q=80")
        binding.urlTextField.editText?.setText("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_10MB.mp4")
        binding.nameTextField.editText?.setText("test.mp4")

        networkMonitor = NetworkStatusMonitor(requireContext()) { isAvailable, type ->
                when (isAvailable) {
                    true -> {
                        Log.i("NETWORK_MONITOR_STATUS", type.orEmpty())
                        binding.tvNetworkStatus.setTextColor(Color.GREEN)
                        binding.tvNetworkStatus.setText(networkMonitor?.getConnectionTypeName())
                    }
                    false -> {
                        Log.i("NETWORK_MONITOR_STATUS", "No Connection")
                        binding.tvNetworkStatus.setTextColor(Color.RED)
                        binding.tvNetworkStatus.setText(R.string.no_connection)
                    }
                }
        }

        bindViewModel()
        initListners()
    }


    private fun initListners() {
        binding.saveButton.setOnClickListener {
            val url = binding.urlTextField.editText?.text?.toString().orEmpty()
            val name = binding.nameTextField.editText?.text?.toString().orEmpty()

            if (networkMonitor?.isNetworkConnected() == true) {
                viewModel.downloadFile(name, url)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Необходимо подключиться к интернету",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnSycronize.setOnClickListener {
            if (networkMonitor?.isNetworkConnected() == true) {
                showProgressNotification()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Необходимо подключиться к интернету",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun bindViewModel() {
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        var notificationBuilder = NotificationCompat.Builder(
            requireContext(),
            NotificationChannels.DOWNLOAD_CHANNEL_ID
        )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(android.R.drawable.stat_sys_download)
        //.setSound(null)

        DownloadState.downloadState.onEach { progress ->
            Timber.d("done => ${progress}")
            when (progress) {
                0L -> {
                    notificationBuilder
                        .setContentTitle("Выполняется загрузка файла")
                        .setContentText("${progress} %")
                    //.setSound(null)
                }
                100L -> {
                    delay(1000)
                    val finalNotification = notificationBuilder
                        .setContentTitle("Загрузка файла завершена")
                        .setContentText("")
                        .setProgress(0, 0, false)
                        .build()
                    NotificationManagerCompat.from(requireContext())
                        .notify(DOWNLOAD_NOTIFICATION_ID, finalNotification)
                    delay(1000)
                    NotificationManagerCompat.from(requireContext())
                        .cancel(DOWNLOAD_NOTIFICATION_ID)
                }
                else -> {
                    var notification = notificationBuilder
                        .setContentTitle("Выполняется загрузка файла")
                        .setContentText("${progress} %")
                        .setProgress(100, progress.toInt(), false).build()
                    NotificationManagerCompat.from(requireContext())
                        .notify(DOWNLOAD_NOTIFICATION_ID, notification)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showProgressNotification() {
        val ringURI =
            Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${requireContext().packageName}/raw/sample")
        val notificationBuilder = NotificationCompat.Builder(
            requireContext(),
            NotificationChannels.SYNCHRO_CHANNEL_ID
        )
            .setContentTitle("Выполняется синхронизация")
            //.setContentText("Состояние")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_notifications)
            .setSound(null)

        val maxProgress = 10
        lifecycleScope.launch {
            (0 until maxProgress).forEach { progress ->
                val notification = notificationBuilder
                    .setProgress(maxProgress, progress, false)
                    .setSound(null)
                    .build()

                NotificationManagerCompat.from(requireContext())
                    .notify(PROGRESS_NOTIFICATION_ID, notification)

                delay(500)
            }

            val finalNotification = notificationBuilder
                .setContentTitle("Синхронизация завершена")
                //.setContentText("Синхронизация завершена")
                .setProgress(0, 0, false)
                .build()

            NotificationManagerCompat.from(requireContext())
                .notify(PROGRESS_NOTIFICATION_ID, finalNotification)
            delay(1000)

            NotificationManagerCompat.from(requireContext())
                .cancel(PROGRESS_NOTIFICATION_ID)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkMonitor?.unregister()
        networkMonitor = null
    }

    companion object {
        private const val PROGRESS_NOTIFICATION_ID = 5555
        private const val DOWNLOAD_NOTIFICATION_ID = 6666
    }
}
