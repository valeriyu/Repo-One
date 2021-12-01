package com.valeriyu.scopedstorage

import android.Manifest
import android.app.Activity
import android.app.RemoteAction
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.valeriyu.scopedstorage.databinding.FragmentMoviesListBinding
import com.valeriyu.scopedstorage.ui.adapter.MoviesAdapter

@RequiresApi(Build.VERSION_CODES.O)
class MoviesListFragment :
    ViewBindingFragment<FragmentMoviesListBinding>(FragmentMoviesListBinding::inflate) {
    private val viewModel: MoviesViewModel by viewModels()
    private var moviesAdapter: MoviesAdapter? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var recoverableActionLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var favoritersTrachedActionLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionResultListener()
        initRecoverableActionListener()
        initFavTrashActionListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        initCallbacks()
        bindViewModel()
        if (hasPermission().not()) {
            requestPermissions()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePermissionState(hasPermission())
    }

    private fun initList() {
        moviesAdapter = MoviesAdapter(
            { id, btnId ->
                when (btnId) {
                    R.id.deleteButton -> {
                        viewModel.deleteImage(id)
                    }
                    R.id.starredButton -> {
                        var item = moviesAdapter!!.items.filter { it.id == id }.last()
                        viewModel.addToFavorites(listOf(item), item.favorite.not())
                    }
                    R.id.trashedButton -> {
                        var item = moviesAdapter!!.items.filter { it.id == id }.last()
                        viewModel.addToTrashed(listOf(item), item.trashed.not())
                    }
                }
            }
        )
        with(binding.moviesList) {
            setHasFixedSize(true)
            adapter = moviesAdapter
        }
    }


    private fun initCallbacks() {
        binding.addMovieButton.setOnClickListener {
            findNavController().navigate(MoviesListFragmentDirections.actionMoviesListFragmentToAddMovieFragment())
        }
        binding.grantPermissionButton.setOnClickListener {
            requestPermissions()
        }
    }

    private fun bindViewModel() {
        viewModel.toastLiveData.observe(viewLifecycleOwner) { toast(it) }
        viewModel.moviesLiveData.observe(viewLifecycleOwner) {
            moviesAdapter!!.items = it
        }
        viewModel.permissionsGrantedLiveData.observe(viewLifecycleOwner, ::updatePermissionUi)
        viewModel.recoverableActionLiveData.observe(viewLifecycleOwner, ::handleRecoverableAction)

        viewModel.actions.observe(viewLifecycleOwner) { intentSender ->
            val request = IntentSenderRequest.Builder(intentSender).build()
            favoritersTrachedActionLauncher.launch(request)
        }
    }

    private fun updatePermissionUi(isGranted: Boolean) {
        binding.grantPermissionButton.isVisible = isGranted.not()
        binding.addMovieButton.isVisible = isGranted
        binding.moviesList.isVisible = isGranted
    }

    private fun hasPermission(): Boolean {
        return PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

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

    private fun initRecoverableActionListener() {
        recoverableActionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            val isConfirmed = activityResult.resultCode == Activity.RESULT_OK
            if (isConfirmed) {
                viewModel.confirmDelete()
            } else {
                viewModel.declineDelete()
            }
        }
    }

    private fun initFavTrashActionListener() {
        favoritersTrachedActionLauncher = registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult(), {})
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(*PERMISSIONS.toTypedArray())
    }


    private fun handleRecoverableAction(action: RemoteAction) {
        val request = IntentSenderRequest.Builder(action.actionIntent.intentSender)
            .build()
        recoverableActionLauncher.launch(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        moviesAdapter = null
    }

    /*private fun requesMediaStorePermission(intentSender: IntentSender, requestCode: Int) {
        startIntentSenderForResult(
            intentSender, requestCode, null, 0, 0,
            0, null
        )
    }*/


    companion object {
        private val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
                .takeIf { haveQ().not() }
        )
  }
}