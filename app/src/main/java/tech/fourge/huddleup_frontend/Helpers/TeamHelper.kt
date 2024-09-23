package tech.fourge.huddleup_frontend.Helpers

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.functions
import kotlinx.coroutines.tasks.await
import tech.fourge.huddleup_frontend.Models.Team
import tech.fourge.huddleup_frontend.Utils.CurrentUserUtil

class TeamHelper {
    private val auth: FirebaseAuth = Firebase.auth
    private val functions = Firebase.functions

    // On Class Creation
    init {
        // Use the emulator for local development (comment out for production)
        functions.useEmulator("10.0.2.2", 5001)
        auth.useEmulator("10.0.2.2", 9099)
    }

    // Register a new team
    suspend fun registerTeam(
        teamName: String,
        location: String,
        league: String
    ): String {
        val currentUserId = CurrentUserUtil.currentUserUID
        val teamId = generateTeamId() // Function to generate a team ID
        val teamCode = generateTeamCode() // Function to generate a unique team code

        val team = Team(
            teamId = teamId,
            teamName = teamName,
            teamCode = teamCode,
            location = location,
            league = league,
            createdBy = currentUserId,
            members = mapOf(currentUserId to "Manager"),
            managers = listOf(currentUserId),
            players = emptyList(),
            events = emptyList()
        )

        return try {
            val data = team.toMap()
            val result = functions.getHttpsCallable("registerTeam").call(data).await()
            val resultData = result.data as? Map<*, *>
            val status = resultData?.get("status") as? String
            val message = resultData?.get("message") as? String

            if (status == "success") {
                "success"
            } else {
                message ?: "unknown_error"
            }
        } catch (e: FirebaseFunctionsException) {
            Log.w(TAG, "Team registration failed", e)
            "functions_error"
        } catch (e: Exception) {
            Log.w(TAG, "Team registration failed", e)
            "unknown_error"
        }
    }

    // Update team information
    suspend fun updateTeam(teamId: String, updatedTeam: Team): Boolean {
        return try {
            val teamData = updatedTeam.toMap()

            // Call the Firebase Cloud Function to update the team
            val result = functions.getHttpsCallable("updateTeam").call(mapOf("teamId" to teamId, "teamData" to teamData)).await()

            val success = result.data as? Boolean
            if (success == true) {
                Log.d(TAG, "Team successfully updated")
                true
            } else {
                Log.d(TAG, "Team update failed")
                false
            }
        } catch (e: Exception) {
            Log.w(TAG, "Team update failed", e)
            false
        }
    }

    // Fetch team details
    suspend fun getTeam(teamId: String): Team? {
        return try {
            val result = functions.getHttpsCallable("getTeam").call(mapOf("teamId" to teamId)).await()
            val data = result.data as? Map<String, Any>
            data?.let {
                Team.fromMap(it)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to get team", e)
            null
        }
    }

    // Helper function to generate team ID
    private fun generateTeamId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid + "_" + System.currentTimeMillis().toString()
    }

    // Helper function to generate team code
    private fun generateTeamCode(): String {
        val charset = ('A'..'Z') + ('0'..'9')
        return List(6) { charset.random() }.joinToString("")
    }

    companion object {
        private const val TAG = "TeamHelper"
    }




}