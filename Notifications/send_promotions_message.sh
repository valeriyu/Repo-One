curl -X POST -H "Authorization: key=AAAA_BqkSvo:APA91bHIEoFRLBPbg4yV7zcJ33RL_t4H2APOpLZvPEqzhCkQEf29GFW2YVlEck3jQsDambKnl2uV00_xJrtP-TRZLuxM7mIEd9QjILRyi50fKDJ-b32KwO9l-Tf1pPwbJxXLPXB98sdl" -H "Content-Type: application/json" -d '{
    "to":"fBlbctptRW2GVnoZ9Irwwo:APA91bGOFJOiUGmGYjc6dY4nNAAMoOvG9Pa7A9USYQvthZKkizRcPiDZbyrfccqa6KEJQpRD-qZK6jmUm6dnq_LQq0GI709ejm2lWmTIOJ3MLtPUQxMSOPzqJKeEGbyVse6S4rXbyFeh",
    "data": {
    "type": "promotions",
    "data": {
      "title": "Лучшее в мире предложение",
      "description": "Покупайте курсы в компании Skillbox по смехотворно - низким ценам !!!",
       "imageUrl": "https://img2.freepng.ru/20180812/ros/kisspng-christmas-tree-portable-network-graphics-clip-art-papa-noel-recibes-una-llamada-telefonica-de-papa-5b701abfb36de0.979662201534073535735.jpg"
       }
    }
}' https://fcm.googleapis.com/fcm/send -v -i