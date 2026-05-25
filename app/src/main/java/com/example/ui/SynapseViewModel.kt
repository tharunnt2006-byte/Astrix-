package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.db.*
import com.example.api.GeminiAiService
import com.example.api.CompatibilityResult
import com.example.api.GeneratedStartupIdea
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SynapseViewModel(application: Application) : AndroidViewModel(application) {

    private val database = SynapseDatabase.getDatabase(application)
    private val repository = SynapseRepository(database.synapseDao())

    // Database flows
    val currentUserFlow: StateFlow<UserProfile?> = repository.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val collaborators: StateFlow<List<UserProfile>> = repository.collaborators
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val startupIdeas: StateFlow<List<StartupIdea>> = repository.startupIdeas
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val hackathons: StateFlow<List<Hackathon>> = repository.hackathons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mentors: StateFlow<List<Mentor>> = repository.mentors
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<Notification>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myProjects: StateFlow<List<StudentProject>> = repository.getProjectsForUser("curr_user")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allProjects: StateFlow<List<StudentProject>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isGeneratingMentorRecommendations = MutableStateFlow(false)
    val isGeneratingMentorRecommendations: StateFlow<Boolean> = _isGeneratingMentorRecommendations.asStateFlow()

    private val _mentorRecommendationsResult = MutableStateFlow<String?>(null)
    val mentorRecommendationsResult: StateFlow<String?> = _mentorRecommendationsResult.asStateFlow()

    // AI Teammate Matching UI state
    private val _selectedCollaborator = MutableStateFlow<UserProfile?>(null)
    val selectedCollaborator: StateFlow<UserProfile?> = _selectedCollaborator.asStateFlow()

    private val _chemistryAnalysisLoading = MutableStateFlow(false)
    val chemistryAnalysisLoading: StateFlow<Boolean> = _chemistryAnalysisLoading.asStateFlow()

    private val _chemistryAnalysisResult = MutableStateFlow<CompatibilityResult?>(null)
    val chemistryAnalysisResult: StateFlow<CompatibilityResult?> = _chemistryAnalysisResult.asStateFlow()

    // AI Startup Generator UI state
    private val _isGeneratingStartup = MutableStateFlow(false)
    val isGeneratingStartup: StateFlow<Boolean> = _isGeneratingStartup.asStateFlow()

    private val _generatedStartupIdea = MutableStateFlow<GeneratedStartupIdea?>(null)
    val generatedStartupIdea: StateFlow<GeneratedStartupIdea?> = _generatedStartupIdea.asStateFlow()

    private val _generateError = MutableStateFlow<String?>(null)
    val generateError: StateFlow<String?> = _generateError.asStateFlow()

    fun selectCollaborator(student: UserProfile?) {
        _selectedCollaborator.value = student
        _chemistryAnalysisResult.value = null
        if (student != null) {
            triggerTeammateAnalysis(student)
        }
    }

    private fun triggerTeammateAnalysis(student: UserProfile) {
        viewModelScope.launch {
            _chemistryAnalysisLoading.value = true
            val currentUser = currentUserFlow.value ?: UserProfile(
                id = "curr_user", name = "Jane Doe", college = "Stanford", department = "CS",
                skills = "Kotlin, Compose", interests = "AI", githubProfile = "", linkedinProfile = "",
                experienceLevel = "Intermediate", preferredRole = "Developer"
            )
            val result = GeminiAiService.analyzeTeammateChemistry(currentUser, student)
            _chemistryAnalysisResult.value = result
            _chemistryAnalysisLoading.value = false
        }
    }

    // AI Startup Idea Generator
    fun generateStartupIdea(domain: String, problem: String, audience: String) {
        viewModelScope.launch {
            _isGeneratingStartup.value = true
            _generateError.value = null
            try {
                val result = GeminiAiService.generateStartupIdea(domain, problem, audience)
                _generatedStartupIdea.value = result
            } catch (e: Exception) {
                _generateError.value = e.localizedMessage ?: "Connection error"
            } finally {
                _isGeneratingStartup.value = false
            }
        }
    }

    fun clearGeneratedStartup() {
        _generatedStartupIdea.value = null
        _generateError.value = null
    }

    // User actions
    fun saveUserProfile(
        name: String,
        college: String,
        department: String,
        skills: String,
        interests: String,
        github: String,
        linkedin: String,
        experienceLevel: String,
        preferredRole: String
    ) {
        viewModelScope.launch {
            val updated = UserProfile(
                id = "curr_user",
                name = name,
                college = college,
                department = department,
                skills = skills,
                interests = interests,
                githubProfile = github,
                linkedinProfile = linkedin,
                experienceLevel = experienceLevel,
                preferredRole = preferredRole,
                isCurrentUser = true
            )
            repository.insertUser(updated)
            repository.insertNotification(
                Notification(
                    title = "Profile Updated",
                    message = "Your talent coordinates have been synchronized. Teammate suggestions will re-align.",
                    type = "System"
                )
            )
        }
    }

    fun createStartupIdea(
        title: String,
        problem: String,
        solution: String,
        skills: String,
        teamSize: Int,
        category: String
    ) {
        viewModelScope.launch {
            val user = currentUserFlow.value
            val author = user?.name ?: "Jane Doe"
            val newIdea = StartupIdea(
                title = title,
                problemStatement = problem,
                proposedSolution = solution,
                requiredSkills = skills,
                teamSizeNeeded = teamSize,
                category = category,
                authorName = author
            )
            repository.insertStartupIdea(newIdea)
            repository.insertNotification(
                Notification(
                    title = "Venture Idea Published",
                    message = "You posted '$title' successfully! Collaborators can now discover your hub submission.",
                    type = "Idea"
                )
            )
        }
    }

    fun sendTeamMatchRequest(candidate: UserProfile) {
        viewModelScope.launch {
            repository.insertNotification(
                Notification(
                    title = "Match Request Forwarded",
                    message = "You requested collaboration with ${candidate.name} as a suitable '${candidate.preferredRole}'.",
                    type = "Match"
                )
            )
        }
    }

    fun toggleLikeStartupIdea(idea: StartupIdea) {
        viewModelScope.launch {
            val updated = idea.copy(likesCount = idea.likesCount + 1)
            repository.updateStartupIdea(updated)
        }
    }

    fun registerForHackathon(hackathon: Hackathon) {
        viewModelScope.launch {
            val updated = hackathon.copy(isRegistered = !hackathon.isRegistered)
            repository.updateHackathon(updated)
            val suffix = if (updated.isRegistered) "Registered!" else "Cancelled registration."
            repository.insertNotification(
                Notification(
                    title = "Hackathon Sync",
                    message = "Successfully $suffix for ${hackathon.name}.",
                    type = "System"
                )
            )
        }
    }

    // Add mentor registrations
    fun registerSelectedMentor(name: String, title: String, company: String, expertise: String, bio: String) {
        viewModelScope.launch {
            val nMentor = Mentor(
                name = name,
                title = title,
                company = company,
                expertise = expertise,
                bio = bio
            )
            repository.insertMentor(nMentor)
            repository.insertNotification(
                Notification(
                    title = "Mentor Registered",
                    message = "Welcome aboard as a SynapseX guide! Students can now request your strategic insights.",
                    type = "System"
                )
            )
        }
    }

    // Projects Management
    fun addStudentProject(title: String, description: String, techStack: String) {
        viewModelScope.launch {
            val nProj = StudentProject(
                title = title,
                description = description,
                techStack = techStack,
                ownerId = "curr_user"
            )
            repository.insertProject(nProj)
            repository.insertNotification(
                Notification(
                    title = "New Project Showcased",
                    message = "Successfully uploaded '$title' to your developer credentials card.",
                    type = "System"
                )
            )
        }
    }

    fun deleteStudentProject(id: Int) {
        viewModelScope.launch {
            repository.deleteProject(id)
        }
    }

    // AI Mentor Recommendations
    fun generateMentorRecommendations() {
        viewModelScope.launch {
            _isGeneratingMentorRecommendations.value = true
            _mentorRecommendationsResult.value = null
            val currentUser = currentUserFlow.value
            val projects = myProjects.value
            val ideas = startupIdeas.value
            val mentorsList = mentors.value
            if (currentUser != null) {
                val report = GeminiAiService.recommendMentors(
                    user = currentUser,
                    userProjects = projects,
                    startupIdeas = ideas,
                    mentorsList = mentorsList
                )
                _mentorRecommendationsResult.value = report
            } else {
                _mentorRecommendationsResult.value = "Please complete your user talent dossier profile first to calculate alignment indices."
            }
            _isGeneratingMentorRecommendations.value = false
        }
    }
}
