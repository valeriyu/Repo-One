curl -X POST -H "Authorization: key=AAAA_BqkSvo:APA91bHIEoFRLBPbg4yV7zcJ33RL_t4H2APOpLZvPEqzhCkQEf29GFW2YVlEck3jQsDambKnl2uV00_xJrtP-TRZLuxM7mIEd9QjILRyi50fKDJ-b32KwO9l-Tf1pPwbJxXLPXB98sdl" -H "Content-Type: application/json" -d '{
    "to":"dfF8THnBQqC4gR48OPOdhO:APA91bEScI12XEHFgx7P4pRTlettEjSsBNg9cDe3s61RFShMvoZux2VAeIRKPGxgMtHuAH3BirNcroT1ABrEuLUNqz-mCywNwPz7Cwq1g9dPEjgXLw-lTVJ36GV_NkgqYyO_3H1H46Ab",
    "data": {
    "type": "chat",
    "data": {
      "userId": 7
      "userName":"Братья Карамазовы"
      "text": "Грузите апельсины бочках",
       }
       "data": {"created_at":0,"id":0,"text":"Грузите апельсины бочках","user":{"id":7,"userName":"Братья Карамазовы"},"userId":7}
    }
}' https://fcm.googleapis.com/fcm/send -v -i