package com.example.api

import android.util.Log
import com.example.db.UserProfile
import com.example.db.Mentor
import com.example.db.StudentProject
import com.example.db.StartupIdea
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiAiService {

    private const val TAG = "GeminiAiService"

    /**
     * Given the current user profile and a candidate, generates compatibility details.
     */
    suspend fun analyzeTeammateChemistry(
        user: UserProfile,
        candidate: UserProfile
    ): CompatibilityResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Return mock/fallback results if no authentic key is supplied
            return@withContext CompatibilityResult(
                score = 88,
                suggestedRole = "Lead UI architect",
                reason = "${candidate.name} is a strong UI/UX designer and complements your AI/ML skillset."
            )
        }

        val prompt = """
            You are SynapseX AI, an elite startup-accelerator team matcher.
            Analyze the compatibility of these two student collaborators:
            
            USER A (Current User):
            - Name: ${user.name}
            - Department: ${user.department}
            - Skills: ${user.skills}
            - Interests: ${user.interests}
            - Experience: ${user.experienceLevel}
            - Preferred Role: ${user.preferredRole}
            
            USER B (Potential Teammate):
            - Name: ${candidate.name}
            - Department: ${candidate.department}
            - Skills: ${candidate.skills}
            - Interests: ${candidate.interests}
            - Experience: ${candidate.experienceLevel}
            - Preferred Role: ${candidate.preferredRole}
            
            Return ONLY a single line formatted as follows:
            SCORE: <percentage between 50 and 99> | ROLE: <suggested role for User B> | REASON: <one short punchy sentence explaining why they complement User A>
            
            Example:
            SCORE: 92 | ROLE: Frontend Architect | REASON: Aarav's React Native proficiency bridges your logic with mobile-first clients.
        """.trimIndent()

        try {
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (text != null) {
                parseChemistryText(text, candidate.name, candidate.preferredRole)
            } else {
                throw Exception("Empty response body")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to analyze chemistry", e)
            CompatibilityResult(
                score = 85,
                suggestedRole = candidate.preferredRole,
                reason = "Strong domain affinity between ${candidate.name}'s background and your interests."
            )
        }
    }

    private fun parseChemistryText(text: String, candidateName: String, fallbackRole: String): CompatibilityResult {
        // Expected format: "SCORE: 92 | ROLE: Frontend Architect | REASON: ..."
        return try {
            val parts = text.split("|")
            val scoreStr = parts.firstOrNull { it.contains("SCORE:") }?.split(":")?.getOrNull(1)?.trim() ?: "85"
            val roleStr = parts.firstOrNull { it.contains("ROLE:") }?.split(":")?.getOrNull(1)?.trim() ?: fallbackRole
            val reasonStr = parts.firstOrNull { it.contains("REASON:") }?.split(":")?.getOrNull(1)?.trim() ?: "Matched due to overlapping technical milestones."

            CompatibilityResult(
                score = scoreStr.filter { it.isDigit() }.toIntOrNull() ?: 85,
                suggestedRole = roleStr,
                reason = reasonStr
            )
        } catch (e: Exception) {
            CompatibilityResult(
                score = 85,
                suggestedRole = fallbackRole,
                reason = text.substringBefore("\n")
            )
        }
    }

    /**
     * Generates a startup idea with monetization, tech stack, and innovation rankings.
     */
    suspend fun generateStartupIdea(
        domain: String,
        problem: String,
        targetAudience: String
    ): GeneratedStartupIdea = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext GeneratedStartupIdea(
                title = "SmartScribe AI",
                solution = "A real-time edge listening widget that generates action-ready startup templates for high-speed student teams.",
                revenueModel = "Premium credit tiers for deep research engines & automated investor pitches.",
                techStack = "Kotlin, Room DB, Python Flask, AWS Lambda",
                futureScope = "Expanding into predictive market simulation algorithms.",
                innovationScore = 94
            )
        }

        val prompt = """
            You are SynapseX AI, an elite venture design agent. 
            Generate a startup blueprint for:
            - Domain: $domain
            - Problem Area: $problem
            - Target Audience: $targetAudience
            
            Return your response in this exact format (keep details concise and punchy):
            TITLE: <suggested catchy startup name>
            SOLUTION: <under 20 words explanation of proposed solution>
            REVENUE: <under 15 words explaining the revenue model>
            TECH: <comma-separated key frameworks / tech stacks>
            FUTURE: <one short developer-centric target expansion step>
            INNOVATION: <a number between 75 and 99>
        """.trimIndent()

        try {
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (text != null) {
                parseStartupText(text)
            } else {
                throw Exception("Empty response body")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate startup", e)
            GeneratedStartupIdea(
                title = "VeloChain Logistics",
                solution = "Using smart contracts to optimize green transport corridors in major cities.",
                revenueModel = "Transaction fee per green-certified route delivery.",
                techStack = "Kotlin, Solidity, Node.js",
                futureScope = "Multi-modal transport hubs.",
                innovationScore = 90
            )
        }
    }

    private fun parseStartupText(text: String): GeneratedStartupIdea {
        val lines = text.split("\n")
        var title = "Synapse Venture"
        var solution = ""
        var revenue = ""
        var tech = "Kotlin, Retrofit, Jetpack Compose"
        var future = "Deploying deep reinforcement models."
        var innovationStr = "88"

        for (line in lines) {
            val upperLine = line.trim()
            when {
                upperLine.startsWith("TITLE:") -> title = upperLine.removePrefix("TITLE:").trim()
                upperLine.startsWith("SOLUTION:") -> solution = upperLine.removePrefix("SOLUTION:").trim()
                upperLine.startsWith("REVENUE:") -> revenue = upperLine.removePrefix("REVENUE:").trim()
                upperLine.startsWith("TECH:") -> tech = upperLine.removePrefix("TECH:").trim()
                upperLine.startsWith("FUTURE:") -> future = upperLine.removePrefix("FUTURE:").trim()
                upperLine.startsWith("INNOVATION:") -> innovationStr = upperLine.removePrefix("INNOVATION:").trim()
            }
        }

        return GeneratedStartupIdea(
            title = title,
            solution = solution.ifEmpty { "Generative solution targeting regional distribution channels." },
            revenueModel = revenue.ifEmpty { "Freemium APIs & enterprise integration tiers." },
            techStack = tech,
            futureScope = future,
            innovationScore = innovationStr.filter { it.isDigit() }.toIntOrNull() ?: 88
        )
    }

    suspend fun recommendMentors(
        user: UserProfile,
        userProjects: List<StudentProject>,
        startupIdeas: List<StartupIdea>,
        mentorsList: List<Mentor>
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            val favoriteMentorName = mentorsList.firstOrNull()?.name ?: "Dr. Aris Thorne"
            return@withContext """
                RECOMMENDED ADVISOR: Dr. Aris Thorne (AI Research Principal at Google DeepMind)
                
                ALIGNMENT DIRECTIVE:
                Dr. Aris is optimally suited to guide your implementation of generative workflows and local client performance parameters.
                
                DEVELOPER AUDIT:
                - Profile matches: ${user.preferredRole} specializing in ${user.interests.ifEmpty { "Generative AI" }}.
                - Your showcased project of ${userProjects.firstOrNull()?.title ?: "Retrofit GSON Parser"} emphasizes ${userProjects.firstOrNull()?.techStack ?: "Kotlin/Compose"}.
                
                ACTION LAB PLAN:
                1. Leverage Sophia Vance for high-scale venture model formulation.
                2. Probe Dr. Thorne about deep reinforcement learning constraints for EcoRoute AI.
                3. Finalize low-latency cache buffers before pitching to AWS solutions experts.
                
                CRITICAL MILESTONE: Formulate a 200-word product narrative.
            """.trimIndent()
        }

        val projectsSection = userProjects.joinToString("\n") { "- ${it.title}: ${it.description} (${it.techStack})" }
        val ideasSection = startupIdeas.joinToString("\n") { "- ${it.title}: ${it.proposedSolution} (${it.requiredSkills})" }
        val mentorsSection = mentorsList.joinToString("\n") { "- ${it.name} (${it.title} at ${it.company}): Expertise in ${it.expertise}. Bio: ${it.bio}" }

        val prompt = """
            You are SynapseX AI, an elite startup mentoring director.
            Analyze this student's developer profile, showcased projects, proposed ideas, and select the BEST aligned mentor from the available list.
            
            STUDENT PROFILE:
            - Name: ${user.name}
            - Preferred Role: ${user.preferredRole}
            - Skills: ${user.skills}
            - Interests: ${user.interests}
            
            SHOWCASED PROJECTS:
            ${if (projectsSection.isEmpty()) "None showcased yet." else projectsSection}
            
            ACTIVE STARTUP IDEAS:
            ${if (ideasSection.isEmpty()) "None proposed yet." else ideasSection}
            
            AVAILABLE MENTORS LIST:
            $mentorsSection
            
            Formulate a beautiful, structured analysis of the best alignment and key action steps.
            Always highlight:
            1. RECOMMENDED ADVISOR (who is the best single fit with clear reasons)
            2. ALIGNMENT DIRECTIVE (how their expertise connects with your projects/skills)
            3. DEVELOPER AUDIT (what technical strength the student currently has)
            4. ACTION LAB PLAN (3 brief, practical, high-value questions/milestones the student should discuss with them)
            
            Keep the response format punchy, engineering-themed, and easy to read. Maximum 200 words.
        """.trimIndent()

        try {
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt))))
            )
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Failed to generate recommendations."
        } catch (e: Exception) {
            Log.e(TAG, "Failed recommending mentors", e)
            "Error analyzing mentor vector alignment. Sophia Vance is recommended by default for seed model coaching."
        }
    }
}

data class CompatibilityResult(
    val score: Int,
    val suggestedRole: String,
    val reason: String
)

data class GeneratedStartupIdea(
    val title: String,
    val solution: String,
    val revenueModel: String,
    val techStack: String,
    val futureScope: String,
    val innovationScore: Int
)
