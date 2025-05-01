package com.googleleaderboards

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import com.google.android.gms.games.PlayGames
import com.google.android.gms.games.GamesSignInClient
import com.google.android.gms.games.PlayGamesSdk
import com.google.gson.Gson
import android.widget.Toast


@ReactModule(name = GoogleLeaderboardsModule.NAME)
class GoogleLeaderboardsModule(private val reactContext: ReactApplicationContext) :
  NativeGoogleLeaderboardsSpec(reactContext) {

  companion object {
    const val NAME = "GoogleLeaderboards"
  }

  private lateinit var gamesSignInClient: GamesSignInClient

  init {
    PlayGamesSdk.initialize(reactContext)
    gamesSignInClient = PlayGames.getGamesSignInClient(reactContext.currentActivity!!)
    Toast.makeText(reactContext, "gamesSignInClient correct ${gamesSignInClient}", Toast.LENGTH_LONG).show()
  }

  


  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  override fun login_v2(promise: Promise) {
    val activity = reactContext.currentActivity
    if (activity == null) {
        promise.reject("NO_ACTIVITY", "Current activity is null.")
        return
    }

    val gamesSignInClientV2: GamesSignInClient = PlayGames.getGamesSignInClient(activity)


// Toast.makeText(activity, "Attempting to sign in to google play games", Toast.LENGTH_LONG).show()
    gamesSignInClientV2.signIn().addOnCompleteListener { task ->
      if (task.isSuccessful) {
        try {
          val gson = Gson()
          val taskjson = gson.toJson(task) 
          promise.resolve(taskjson)
          // promise.resolve("${task.result}")
      } catch (e: Exception) {
          val errorDetails = Gson().toJson(e)
          promise.reject("SIGN_IN_EXCEPTION", errorDetails)
      }
      } else {
        try {
          val gson = Gson()
          val taskjson = gson.toJson(task) 
          promise.resolve(taskjson)
      } catch (e: Exception) {
          // Handle JSON conversion or other errors
          val errorDetails = Gson().toJson(e)
          promise.reject("SIGN_IN_UNSUCCESSFUL", errorDetails)
      }
      }
    }
    
  }
  

  override fun login(promise: Promise) {
    PlayGamesSdk.initialize(reactContext)

    val activity = reactContext.currentActivity
    if (activity == null) {
        promise.reject("NO_ACTIVITY", "Current activity is null.")
        return
    }
gamesSignInClient.signIn().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val result = task.result
            val isAuthenticated = result != null && result.isAuthenticated

            if (isAuthenticated) {
                // Sign-in successful, get player info
                Toast.makeText(activity, "Logged in to google play games", Toast.LENGTH_LONG).show()

                PlayGames.getPlayersClient(reactContext.currentActivity!!).currentPlayer.addOnCompleteListener { playerTask ->
                    if (playerTask.isSuccessful) {
                        val player = playerTask.result
                        try {
                            val gson = Gson()
                            val playerJson = gson.toJson(player) 
                            promise.resolve(playerJson)
                        } catch (e: Exception) {
                            // Handle JSON conversion or other errors
                            val errorDetails = Gson().toJson(e)
                            promise.reject("GET_PLAYER_FAILED", errorDetails)
                        }
                    } else {
                        val exception = playerTask.exception
                        val errorDetails = Gson().toJson(mapOf(
                            "error" to "Failed to get current player",
                            "exception" to exception?.toString(),
                            "stackTrace" to exception?.stackTraceToString()
                        ))
                        promise.reject("GET_PLAYER_FAILED", errorDetails)
                    }
                }
            } else {
                // Handle login failure
                Toast.makeText(activity, "Unable to login to google play games", Toast.LENGTH_LONG).show()

                val errorDetails = Gson().toJson(mapOf(
                    "error" to "Authentication failed",
                    "result" to task.result?.toString(),
                    "stackTrace" to "No additional info"
                ))
                promise.reject("LOGIN_FAILED", errorDetails)
            }
        } else {
            // Handle sign-in failure
            val exception = task.exception
            if (exception != null) {
              val errorDetails = Gson().toJson(exception)
                promise.reject("SIGN_IN_ERROR", errorDetails)
            } else {
                val errorDetails = Gson().toJson(mapOf(
                    "error" to "Unknown sign-in failure",
                    "exception" to "No exception message",
                    "stackTrace" to "No stack trace available"
                ))
                promise.reject("SIGN_IN_FAILED_UNKNOWN", errorDetails)
            }
        }
    }
}

  @ReactMethod
  override fun checkAuth(promise: Promise) {
    val activity = reactContext.currentActivity
    if (activity == null) {
      promise.reject("NO_ACTIVITY", "Current activity is null.")
      return
    }

  
    gamesSignInClient.isAuthenticated.addOnCompleteListener { task ->
      // val isAuthenticated = task.isSuccessful 
    val isAuthenticated = task.isSuccessful && task.result?.isAuthenticated == true

      if (isAuthenticated) {
        PlayGames.getPlayersClient(reactContext.currentActivity!!).currentPlayer.addOnCompleteListener { player ->
          try {
            val gson = Gson()
            val playerJson = gson.toJson(player)
            promise.resolve(playerJson)
          } catch (e: Exception) {
            promise.reject("GET_PLAYER_FAILED", "Failed to get player info", e)
            // promise.resolve("${player}")
          }
        }
      } else {
        // promise.reject("${task}");
        Toast.makeText(reactContext, "User is not authenticated.", Toast.LENGTH_LONG).show()

        promise.reject("NOT_AUTHENTICATED", "User is not authenticated.")
      }
    }
  }
}
