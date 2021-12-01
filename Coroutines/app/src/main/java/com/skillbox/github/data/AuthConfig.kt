package com.skillbox.github.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"

    // const val CLIENT_ID = "14df2600b0708f9ae6e4"
    // const val CLIENT_SECRET = "c100be3117140f2a42c006d6774033de5e5da434"
    // const val CLIENT_SECRET = "2ec84ff5364f26661303c4cdf06351258a0f423f"

    const val CALLBACK_URL = "skillbox://skillbox.ru/callback"


    const val CLIENT_ID =  "5c2d54fd7dded650f4b9"
    const val CLIENT_SECRET = "3e56245880a85d37b390538a1f4675255d122f5d"
}
