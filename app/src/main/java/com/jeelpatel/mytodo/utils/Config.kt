package com.jeelpatel.mytodo.utils

object Config {


    // Room Database
    const val APP_DATABASE = "app_database"
    const val USER_TABLE = "user_table"
    const val TASK_TABLE = "task_table"


    // Shared Pref
    const val SHARED_PREF_NAME = "user_session"
    const val KEY_USER_ID = "uId"
    const val KEY_IS_LOGGED_IN = "isLoggedIn"


    // Remote Todos
    const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    const val GET_TODOS = "todos"


    // Date time formats
    const val DATE_TIME_FORMAT = "dd-MMM-yyyy, hh:mm a"


    // Notifications Services
    const val CHANNEL_ID = "music_service_channel"
    const val NOTIFICATION_ID = 1

    const val ACTION_PLAY = "ACTION_PLAY"
    const val ACTION_PAUSE = "ACTION_PAUSE"
    const val ACTION_STOP = "ACTION_STOP"


    // Weather Services

    const val WEATHER_BASE_URL = "https://api.weatherapi.com/v1/"
    const val WEATHER_API_KEY = "4085e8a1284a4385bf842054252811"


}