curl -X POST -H "Authorization: key=AAAA_BqkSvo:APA91bHIEoFRLBPbg4yV7zcJ33RL_t4H2APOpLZvPEqzhCkQEf29GFW2YVlEck3jQsDambKnl2uV00_xJrtP-TRZLuxM7mIEd9QjILRyi50fKDJ-b32KwO9l-Tf1pPwbJxXLPXB98sdl" -H "Content-Type: application/json" -d '{
    "to":"fLmj_D0sRDaqRqRQSHM4RE:APA91bFANQBE0EYIAUB6UnVRYzuFH8GLEpT9-OWXWf8buRIrlXQqHP6lNHnYhUZNVk7o2O_WB6BTFdWrlDsWcE43x3cstKFyzd3npooUmAq1TdjPP5OcEzH6DXg7R6K31Eh65LsowvUz",
    "data": {
    "type": "chat",
     "data": {"created_at":0,"id":0,"text":"Грузите апельсины бочках","user":{"id":7,"userName":"Братья Карамазовы"},"userId":7}
    }
}' https://fcm.googleapis.com/fcm/send -v -i