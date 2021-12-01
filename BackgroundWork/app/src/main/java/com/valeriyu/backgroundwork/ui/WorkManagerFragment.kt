package com.valeriyu.backgroundwork.ui

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.valeriyu.backgroundwork.worker.DownloadWorker
import com.valeriyu.backgroundwork.R
import com.valeriyu.backgroundwork.databinding.FragmentWorkManagerBinding
import com.valeriyu.backgroundwork.service.MediaBrowserPlayerService
import com.valeriyu.backgroundwork.utils.checkedChangesFlow
import com.valeriyu.notifications.haveQ
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WorkManagerFragment : Fragment(R.layout.fragment_work_manager) {
    private val binding by viewBinding(FragmentWorkManagerBinding::bind)
    private val viewModel: WmViewModel by viewModels()
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var mediaController: MediaControllerCompat? = null
    private var mediaControllerCallbacks: MediaControllerCompat.Callback? = null
    private var mediaBrowser: MediaBrowserCompat? = null
    private lateinit var connectionCallbacks: MediaBrowserCompat.ConnectionCallback

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionResultListener()
    }

    @ExperimentalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRadio()
        bindViewModel()

        binding.startDownload.setOnClickListener {
            startDownload( binding.downloadUrl.text.toString())
        }

        binding.retryDownload.setOnClickListener {
            startDownload(binding.downloadUrl.text.toString())
        }

        binding.cancelDownload.setOnClickListener {
            stopDownload()
        }

        binding.downloadUrl.setText("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/720/Big_Buck_Bunny_720_10s_10MB.mp4")

        if (activity?.intent?.action == requireContext().packageName + ".RADIO_ACTIVE") {
            binding.startStopRadio.isChecked = true
        }
    }

    @ExperimentalCoroutinesApi
    private fun initRadio() {
        mediaControllerCallbacks = object : MediaControllerCompat.Callback() {
            override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            }

            override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            }
        }

        connectionCallbacks = object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {
                // Get the token for the MediaSession
                mediaBrowser?.sessionToken.also { token ->
                    // Create a MediaControllerCompat
                    mediaController = token?.let {
                        MediaControllerCompat(
                            requireContext(),
                            it
                        )
                    }
                    // Save the controller
                    MediaControllerCompat.setMediaController(
                        requireContext() as Activity,
                        mediaController
                    )
                }
                // Finish building the UI
                //buildTransportControls()
                mediaController?.registerCallback(mediaControllerCallbacks as MediaControllerCompat.Callback)
            }

            override fun onConnectionSuspended() {
                // The Service has crashed. Disable transport controls until it automatically reconnects
                Timber.d("onConnectionSuspended")
            }

            override fun onConnectionFailed() {
                // The Service has refused our connection
                Timber.d("onConnectionFailed")
            }
        }

        mediaBrowser = MediaBrowserCompat(
            requireContext(),
            ComponentName(requireContext(), MediaBrowserPlayerService::class.java),
            connectionCallbacks,
            null // optional Bundle
        )

        mediaBrowser?.connect()
    }

    @ExperimentalCoroutinesApi
    private fun bindViewModel() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DownloadWorker.DOWNLOAD_WORK_ID)
            .observe(viewLifecycleOwner, {
                if (it.isNotEmpty()) {
                    handleWorkInfo(it.first())
                }
            })

        binding.startStopRadio.setOnCheckedChangeListener { buttonView, isChecked ->
            if (mediaController == null) return@setOnCheckedChangeListener
            if (isChecked) {
                if (mediaController?.playbackState?.state != PlaybackStateCompat.STATE_PLAYING) {
                    mediaController!!.transportControls.play()
                }
            } else {
                mediaController!!.transportControls.stop()
            }
        }
    }

    fun startDownload(url: String) {
        val workData = workDataOf(
            DownloadWorker.DOWNLOAD_URL_KEY to url
        )

        val workConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setInputData(workData)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 20, TimeUnit.SECONDS)
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniqueWork(
                DownloadWorker.DOWNLOAD_WORK_ID,
                ExistingWorkPolicy.KEEP,
                workRequest
            )
    }

    fun stopDownload() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork(DownloadWorker.DOWNLOAD_WORK_ID)
    }


    private fun handleWorkInfo(workInfo: WorkInfo) {
        Timber.d("handleWorkInfo new state = ${workInfo.state}")
        val isFinished = workInfo.state.isFinished
        if (isFinished) {
            binding.downloadProgress.isVisible = false
        } else {
            binding.downloadProgress.isVisible = true
        }

        when (workInfo.state) {
            WorkInfo.State.ENQUEUED -> {
                binding.startDownload.isVisible = false
                binding.cancelDownload.isVisible = true
                binding.retryDownload.isVisible = false
                binding.infoTextView.setText("Ожидание начала загрузки")
            }
            WorkInfo.State.CANCELLED -> {
                binding.startDownload.isVisible = true
                binding.cancelDownload.isVisible = false
                binding.retryDownload.isVisible = false
                binding.infoTextView.setText("Загрузка прервана пользователем")
            }
            WorkInfo.State.RUNNING -> {
                binding.startDownload.isVisible = false
                binding.cancelDownload.isVisible = true
                binding.retryDownload.isVisible = false
                binding.infoTextView.setText("Выполняется загрузка ")
            }
            WorkInfo.State.FAILED -> {
                binding.startDownload.isVisible = false
                binding.cancelDownload.isVisible = false
                binding.retryDownload.isVisible = true
                binding.infoTextView.setText("Ошибка загрузки")
            }
            WorkInfo.State.SUCCEEDED -> {
                binding.startDownload.isVisible = true
                binding.cancelDownload.isVisible = false
                binding.retryDownload.isVisible = false
                binding.infoTextView.setText("")
                Toast.makeText(
                    requireContext(),
                    "Загрузка выполнена успешно",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        super.onDestroy()
        if (mediaController != null) {
            mediaController!!.unregisterCallback(mediaControllerCallbacks!!)
            mediaController = null
        }
    }

//================================================================================

    private fun requestPermissions() {
        requestPermissionLauncher.launch(*PERMISSIONS.toTypedArray())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initPermissionResultListener() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionToGrantedMap: Map<String, Boolean> ->
            if (permissionToGrantedMap.values.all { it }) {
                viewModel.permissionsGranted()
            } else {
                viewModel.permissionsDenied()
            }
        }
    }

    private fun hasPermission(): Boolean {
        return PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

//================================================================================

    companion object {
        private val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
                .takeIf { haveQ().not() }
        )
    }
}