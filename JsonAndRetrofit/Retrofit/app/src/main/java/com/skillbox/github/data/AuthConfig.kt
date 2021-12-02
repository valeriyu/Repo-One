package com.skillbox.github.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"

    const val CALLBACK_URL = "skillbox://skillbox.ru/callback"


    const val CLIENT_ID =  "CLIENT_ID"
    const val CLIENT_SECRET = "CLIENT_SECRET"
}
