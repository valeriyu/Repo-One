package com.valeriyu.notifications.ui

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.valeriyu.notifications.R
import com.valeriyu.notifications.SlaveActivity
import com.valeriyu.notifications.databinding.FragmentMenuBinding
import com.valeriyu.notifications.notifications.ApiClient
import com.valeriyu.notifications.notifications.BatteryBroadcastReceiver
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private val binding: FragmentMenuBinding by viewBinding(FragmentMenuBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.firebaseButton.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToFirebaseFragment())
        }
        binding.synchcButton.setOnClickListener {
            findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSynchronizationFragment())
        }

        getToken()

        val arguments = activity?.intent?.getExtras()
        if (arguments != null) {
            val type = arguments.getString("message_type") ?: ""
            if (type.isBlank().not()) {
                activity?.intent?.removeExtra("message_type")
                val intent = Intent(requireContext(), SlaveActivity::class.java)
                startActivity(intent)
            }
        } else {

        }
    }

    private fun getToken() {
        lifecycleScope.launch {
            val token = getTokenSuspend()
            Timber.d("token = $token")
            if (token != null) {
                ApiClient.Token.value = token
            }
        }
    }

    private suspend fun getTokenSuspend(): String? = suspendCoroutine { continuation ->
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                continuation.resume(token)
            }
            .addOnFailureListener { exception ->
                continuation.resume(null)
            }
            .addOnCanceledListener {
                continuation.resume(null)
            }
    }
}