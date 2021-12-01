package com.valeriyu.notifications.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.messaging.FirebaseMessaging
import com.valeriyu.notifications.Message
import com.valeriyu.notifications.models.Promotions
import com.valeriyu.notifications.R
import com.valeriyu.notifications.User
import com.valeriyu.notifications.models.RootModel
import com.valeriyu.notifications.databinding.FragmentFirebaseBinding
import com.valeriyu.notifications.notifications.ApiClient
import com.valeriyu.notifications.models.DataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseFragment : Fragment(R.layout.fragment_firebase) {

    private val binding: FragmentFirebaseBinding by viewBinding(FragmentFirebaseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.getTokenButton.setOnClickListener {
            getToken()
        }

        binding.chatButton.setOnClickListener {
            chatMessage()
        }

        binding.promotionsButton.setOnClickListener {
            promotionsMessage()
        }
    }

    private fun promotionsMessage() {
        lifecycleScope.launch(Dispatchers.IO) {
            var body = RootModel(
                to = ApiClient.Token.value,
                data = DataModel(
                    type= "promotions",
                    data = Promotions(
                        title= "Лучшее в мире предложение !!!",
                        description= "Покупайте курсы в компании Skillbox по смехотворно - низким ценам !!!",
                        imageUrl =  "https://img2.freepng.ru/20180812/ros/kisspng-christmas-tree-portable-network-graphics-clip-art-papa-noel-recibes-una-llamada-telefonica-de-papa-5b701abfb36de0.979662201534073535735.jpg"
                    )
                )
            )
            var serverKey = ApiClient.ServerKey.value
            var res = ApiClient.api?.sendNotification(serverKey, body)?.execute()
            Timber.d(res.toString())
        }
    }

    private fun chatMessage() {
        lifecycleScope.launch(Dispatchers.IO){
            var body = RootModel(
                to = ApiClient.Token.value,
                data = DataModel(
                    type= "chat",
                    data = Message(
                        userId = 7,
                        user = User(
                        id = 7,
                        userName = "Братья Карамазовы",
                        ),
                        text = "Грузите апельсины бочках"
                    )
                )
            )
            var serverKey = ApiClient.ServerKey.value
            var res = ApiClient.api?.sendNotification(serverKey, body)?.execute()
            Timber.d(res.toString())
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