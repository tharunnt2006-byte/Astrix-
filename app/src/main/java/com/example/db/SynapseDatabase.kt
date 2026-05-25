package com.example.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "users")
data class UserProfile(
    @PrimaryKey val id: String,
    val name: String,
    val college: String,
    val department: String,
    val skills: String, // Comma-separated strings
    val interests: String, // Comma-separated strings
    val githubProfile: String,
    val linkedinProfile: String,
    val experienceLevel: String, // "Beginner", "Intermediate", "Advanced"
    val preferredRole: String, // "AI/ML Engineer", "Frontend Developer", "Backend Developer", "UI/UX Designer", "Presenter"
    val isCurrentUser: Boolean = false
)

@Entity(tableName = "startup_ideas")
data class StartupIdea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val problemStatement: String,
    val proposedSolution: String,
    val requiredSkills: String,
    val teamSizeNeeded: Int,
    val category: String,
    val likesCount: Int = 0,
    val authorName: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "hackathons")
data class Hackathon(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val platform: String,
    val date: String,
    val prizePool: String,
    val isRegistered: Boolean = false
)

@Entity(tableName = "mentors")
data class Mentor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val title: String,
    val company: String,
    val expertise: String,
    val bio: String,
    val rating: Float = 4.8f
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val type: String, // "Match", "Idea", "System"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "projects")
data class StudentProject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val techStack: String, // Comma-separated
    val ownerId: String
)

@Dao
interface SynapseDao {
    @Query("SELECT * FROM users WHERE isCurrentUser = 0")
    fun getCollaborators(): Flow<List<UserProfile>>

    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM users WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Query("SELECT * FROM startup_ideas ORDER BY timestamp DESC")
    fun getStartupIdeas(): Flow<List<StartupIdea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStartupIdea(idea: StartupIdea)

    @Update
    suspend fun updateStartupIdea(idea: StartupIdea)

    @Query("SELECT * FROM hackathons")
    fun getHackathons(): Flow<List<Hackathon>>

    @Update
    suspend fun updateHackathon(hackathon: Hackathon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHackathon(hackathon: Hackathon)

    @Query("SELECT * FROM mentors")
    fun getMentors(): Flow<List<Mentor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMentor(mentor: Mentor)

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("SELECT * FROM projects WHERE ownerId = :ownerId")
    fun getProjectsForUser(ownerId: String): Flow<List<StudentProject>>

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<StudentProject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: StudentProject)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: Int)
}

@Database(
    entities = [UserProfile::class, StartupIdea::class, Hackathon::class, Mentor::class, Notification::class, StudentProject::class],
    version = 2,
    exportSchema = false
)
abstract class SynapseDatabase : RoomDatabase() {
    abstract fun synapseDao(): SynapseDao

    companion object {
        @Volatile
        private var INSTANCE: SynapseDatabase? = null

        fun getDatabase(context: Context): SynapseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SynapseDatabase::class.java,
                    "synapse_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
