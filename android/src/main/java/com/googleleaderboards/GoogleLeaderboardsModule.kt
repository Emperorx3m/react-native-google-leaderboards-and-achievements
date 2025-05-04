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
import org.json.JSONObject
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.games.LeaderboardsClient;


@ReactModule(name = GoogleLeaderboardsModule.NAME)
class GoogleLeaderboardsModule(private val reactContext: ReactApplicationContext) :
  NativeGoogleLeaderboardsSpec(reactContext) {

  companion object {
    const val NAME = "GoogleLeaderboards"
  }

  private lateinit var gamesSignInClient: GamesSignInClient
  //\\\ private var mLeaderboardsClient: LeaderboardsClient

  init {
    try {
    PlayGamesSdk.initialize(reactContext)
    gamesSignInClient = PlayGames.getGamesSignInClient(reactContext.currentActivity!!)
    } catch (e: Exception) {
        // promise.reject("PARSE_ERROR", "PlayGames Failed to initialize")
        // return
    }
  }

  


  override fun getName(): String {
    return NAME
  }

fun dynamicToJsonSimple(obj: Any, promise: Promise) {
    try {
        val json = JSONObject()
        obj.javaClass.methods
            .filter { it.name.startsWith("get") && it.parameterTypes.isEmpty() }
            .forEach { method ->
                val name = method.name.removePrefix("get").replaceFirstChar { it.lowercase() }
                val value = method.invoke(obj)
                when (value) {
                    null -> json.put(name, JSONObject.NULL)
                    is Number, is Boolean, is String -> json.put(name, value)
                    else -> json.put(name, value.toString())
                }
            }

        // Add raw_object
        json.put("raw_object", obj.toString())

        promise.resolve(json.toString())
    } catch (e: Exception) {
        promise.reject("PARSE_ERROR", e)
    }
}


private val RC_LEADERBOARD_UI = 9001
private val RC_UNUSED = 5001
private val RC_ACHIEVEMENT_UI = 9003;


@ReactMethod
override fun unlockAchievement(my_achievement_id: String, promise: Promise) {
val activity = reactContext.currentActivity ?: return

    try {
       PlayGames.getAchievementsClient(activity).unlock(my_achievement_id)

        promise.resolve("Unlocked successfully.")
    } catch (e: Exception) {
        promise.reject("SUBMIT_SCORE_ERROR", "Failed to submit score: ${e.message}", e)
    }
}

@ReactMethod
override fun incrementAchievement(my_achievement_id: String, steps: Double, promise: Promise) {
    val activity = reactContext.currentActivity ?: return
    if (steps < 1) {
        promise.reject("INVALID_SCORE", "Score must be greater than 0.")
        return
    }

    try {
       PlayGames.getAchievementsClient(activity).increment(my_achievement_id, steps.toInt())

        promise.resolve("Incremented successfully.")
    } catch (e: Exception) {
        promise.reject("SUBMIT_SCORE_ERROR", "Failed to submit score: ${e.message}", e)
    }
}

@ReactMethod
override fun showAchievements(promise: Promise) {
    val activity = reactContext.currentActivity

    if (activity == null) {
        promise.reject("NO_ACTIVITY", "Activity doesn't exist")
        return
    }

    val RC_ACHIEVEMENT_UI = 9003

    try {
        PlayGames.getAchievementsClient(activity)
            .achievementsIntent
            .addOnSuccessListener { intent ->
                activity.startActivityForResult(intent, RC_ACHIEVEMENT_UI)
                promise.resolve("Achievements UI shown.")
            }
            .addOnFailureListener { e ->
                promise.reject("SHOW_ACHIEVEMENTS_FAILED", e.message, e)
            }
    } catch (e: Exception) {
        promise.reject("SHOW_ACHIEVEMENTS_EXCEPTION", e.message, e)
    }
}


@ReactMethod
override fun onShowLeaderboardsRequested(promise: Promise) {
    val activity = reactContext.currentActivity ?: return

    PlayGames.getLeaderboardsClient(activity).getAllLeaderboardsIntent()
        .addOnSuccessListener { intent ->
            activity.startActivityForResult(intent, RC_UNUSED)
                        promise.resolve("Success")
        }
        .addOnFailureListener { e ->
           
                       promise.reject("Leaderboard_noShow", "Failed to show leaderboard")
        }
}


@ReactMethod
override fun showLeaderboard(leaderboardId: String, promise: Promise) {
    val activity = reactContext.currentActivity ?: return

    PlayGames.getLeaderboardsClient(activity)
        .getLeaderboardIntent(leaderboardId)
        .addOnSuccessListener { intent ->
            activity.startActivityForResult(intent, RC_LEADERBOARD_UI)
            promise.resolve("Success")
        }
        .addOnFailureListener { e ->
            promise.reject("Leaderboard_noShow", "Failed to show leaderboard")
        }
}


@ReactMethod
override fun submitScore(leaderboardId: String, score: Double, promise: Promise) {
    if (leaderboardId.isBlank()) {
        promise.reject("INVALID_LEADERBOARD_ID", "Leaderboard ID is required.")
        return
    }

    if (score < 0) {
        promise.reject("INVALID_SCORE", "Score must be a non-negative number.")
        return
    }

    try {
        val scoreLong = score.toLong()
        PlayGames.getLeaderboardsClient(reactContext.currentActivity!!).submitScore(leaderboardId, scoreLong)
        promise.resolve("Score $scoreLong submitted to leaderboard '$leaderboardId' successfully.")
    } catch (e: Exception) {
        promise.reject("SUBMIT_SCORE_ERROR", "Failed to submit score: ${e.message}", e)
    }
}


  @ReactMethod
  override fun login(promise: Promise) {

    val activity = reactContext.currentActivity
    if (activity == null) {
        promise.reject("NO_ACTIVITY", "Current activity is null.")
        return
    }
      gamesSignInClient.signIn().addOnCompleteListener { task ->
      val result = task.result
            val isAuthenticated = result != null && result.isAuthenticated
        if (task.isSuccessful && isAuthenticated) {
            // Sign-in successful, get player info
                Toast.makeText(activity, "Logged in to google play games", Toast.LENGTH_LONG).show()

                PlayGames.getPlayersClient(reactContext.currentActivity!!).currentPlayer.addOnCompleteListener { playerTask ->
                    if (playerTask.isSuccessful) {
                        val player = playerTask.result
                        try {
                            dynamicToJsonSimple(player, promise)
                            // promise.resolve("${playerTask.result}")
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
            // Handle sign-in failure // Handle login failure
                Toast.makeText(activity, "Unable to login to google play games", Toast.LENGTH_LONG).show()
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
                dynamicToJsonSimple(player.result, promise)

                            // promise.resolve("${playerTask.result}")
          } catch (e: Exception) {
            promise.reject("GET_PLAYER_FAILED", "Failed to get player info", e)
            // promise.resolve("${player}")
          }
        }
      } else {
        login(promise)
        Toast.makeText(reactContext, "User is not authenticated.", Toast.LENGTH_LONG).show()

        promise.reject("NOT_AUTHENTICATED", "User is not authenticated.")
      }
    }
  }
}
