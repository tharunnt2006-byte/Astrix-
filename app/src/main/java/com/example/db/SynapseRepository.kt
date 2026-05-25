package com.example.db

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SynapseRepository(private val synapseDao: SynapseDao) {

    val collaborators: Flow<List<UserProfile>> = synapseDao.getCollaborators()
    val currentUserFlow: Flow<UserProfile?> = synapseDao.getCurrentUserFlow()
    val startupIdeas: Flow<List<StartupIdea>> = synapseDao.getStartupIdeas()
    val hackathons: Flow<List<Hackathon>> = synapseDao.getHackathons()
    val mentors: Flow<List<Mentor>> = synapseDao.getMentors()
    val notifications: Flow<List<Notification>> = synapseDao.getNotifications()
    val allProjects: Flow<List<StudentProject>> = synapseDao.getAllProjects()

    fun getProjectsForUser(ownerId: String): Flow<List<StudentProject>> = synapseDao.getProjectsForUser(ownerId)

    suspend fun insertProject(project: StudentProject) = synapseDao.insertProject(project)

    suspend fun deleteProject(id: Int) = synapseDao.deleteProject(id)

    init {
        // Prepopulate with rich mock data if empty
        CoroutineScope(Dispatchers.IO).launch {
            prepopulateIfEmpty()
        }
    }

    suspend fun getCurrentUser(): UserProfile? = synapseDao.getCurrentUser()

    suspend fun insertUser(user: UserProfile) = synapseDao.insertUser(user)

    suspend fun insertStartupIdea(idea: StartupIdea) = synapseDao.insertStartupIdea(idea)

    suspend fun updateStartupIdea(idea: StartupIdea) = synapseDao.updateStartupIdea(idea)

    suspend fun updateHackathon(hackathon: Hackathon) = synapseDao.updateHackathon(hackathon)

    suspend fun insertHackathon(hackathon: Hackathon) = synapseDao.insertHackathon(hackathon)

    suspend fun insertMentor(mentor: Mentor) = synapseDao.insertMentor(mentor)

    suspend fun insertNotification(notification: Notification) = synapseDao.insertNotification(notification)

    private suspend fun prepopulateIfEmpty() {
        val existingCurrentUser = synapseDao.getCurrentUser()
        if (existingCurrentUser == null) {
            // 1. Insert original user
            synapseDao.insertUser(
                UserProfile(
                    id = "curr_user",
                    name = "Jane Doe",
                    college = "Stanford University",
                    department = "Computer Science",
                    skills = "Kotlin, Jetpack Compose, Python, Retrofit",
                    interests = "Generative AI, SaaS, HealthTech, EdTech",
                    githubProfile = "github.com/janedoe_synapse",
                    linkedinProfile = "linkedin.com/in/janedoe-synapse",
                    experienceLevel = "Intermediate",
                    preferredRole = "AI/ML Engineer",
                    isCurrentUser = true
                )
            )

            // 2. Insert classmates / tech teammates
            val students = listOf(
                UserProfile(
                    id = "student_aarav",
                    name = "Aarav Sharma",
                    college = "IIT Bombay",
                    department = "Electronics Engineering",
                    skills = "React Native, React, Web3, Solidity, Node.js",
                    interests = "DeFi, Tokenomics, AI Agents, DevTools",
                    githubProfile = "github.com/aaravsh",
                    linkedinProfile = "linkedin.com/in/aaravsh-tech",
                    experienceLevel = "Advanced",
                    preferredRole = "Backend Developer"
                ),
                UserProfile(
                    id = "student_surmila",
                    name = "Surmila Sen",
                    college = "BITS Pilani",
                    department = "Design & Human Computer Interaction",
                    skills = "Figma, Adobe XD, CSS, Wireframing, UX Research",
                    interests = "Futuristic UI, SaaS design, Cyberpunk UI",
                    githubProfile = "github.com/surmiladesign",
                    linkedinProfile = "linkedin.com/in/surmila-sen",
                    experienceLevel = "Advanced",
                    preferredRole = "UI/UX Designer"
                ),
                UserProfile(
                    id = "student_kevin",
                    name = "Kevin Carter",
                    college = "IIT Delhi",
                    department = "Mathematics & Computing",
                    skills = "Python, PyTorch, TensorFlow, LLMs, NLP, RAG Systems",
                    interests = "Generative AI, Robotics, Computer Vision",
                    githubProfile = "github.com/kevinc-ai",
                    linkedinProfile = "linkedin.com/in/kevin-carter-ai",
                    experienceLevel = "Advanced",
                    preferredRole = "AI/ML Engineer"
                ),
                UserProfile(
                    id = "student_elena",
                    name = "Elena Petrova",
                    college = "Stanford University",
                    department = "Bioengineering",
                    skills = "R, Python, Spark, Bioinformatics, data science",
                    interests = "HealthTech, BioInformatics, AI Diagnostics",
                    githubProfile = "github.com/elenap_bio",
                    linkedinProfile = "linkedin.com/in/elena-petrova-bio",
                    experienceLevel = "Intermediate",
                    preferredRole = "Presenter"
                ),
                UserProfile(
                    id = "student_siddharth",
                    name = "Siddharth Verma",
                    college = "VIT Chennai",
                    department = "Information Technology",
                    skills = "Node.js, Express, Next.js, Postgres, Redis, AWS",
                    interests = "FinTech, Real-time Web, Scalability, High Performance APIs",
                    githubProfile = "github.com/sidd IT",
                    linkedinProfile = "linkedin.com/in/siddharth-v",
                    experienceLevel = "Intermediate",
                    preferredRole = "Backend Developer"
                )
            )
            students.forEach { synapseDao.insertUser(it) }

            // 3. Prepopulate Startup Ideas
            val startupIdeas = listOf(
                StartupIdea(
                    title = "EcoRoute AI",
                    problemStatement = "Commercial vehicles emit 25% of urban greenhouse gases due to inefficient paths.",
                    proposedSolution = "A multi-agent ML model recommending energy-minimized delivery corridors and EV charging grids.",
                    requiredSkills = "Python, PyTorch, Google Maps API, GIS Engine",
                    teamSizeNeeded = 4,
                    category = "AI / CleanTech",
                    likesCount = 28,
                    authorName = "Kevin Carter"
                ),
                StartupIdea(
                    title = "DeFiScholar",
                    problemStatement = "Brilliant student coders are unable to raise crowd-funded micro-grants easily.",
                    proposedSolution = "A web3 protocol utilizing smart-milestones to release community research funds directly to wallets.",
                    requiredSkills = "Solidity, React, Node.js, Web3.js",
                    teamSizeNeeded = 3,
                    category = "Web3 / DeFi",
                    likesCount = 14,
                    authorName = "Aarav Sharma"
                ),
                StartupIdea(
                    title = "SynapseSync",
                    problemStatement = "Connecting distributed university hackathon developers feels fragmented and static.",
                    proposedSolution = "This platform! A futuristic, immersive, and AI-driven portal linking coders, designers, and mentors.",
                    requiredSkills = "Kotlin, Jetpack Compose, Room, Room DB, Gemini API",
                    teamSizeNeeded = 5,
                    category = "SaaS / Synergy",
                    likesCount = 42,
                    authorName = "Jane Doe"
                )
            )
            startupIdeas.forEach { synapseDao.insertStartupIdea(it) }

            // 4. Prepopulate Hackathons
            val hackathons = listOf(
                Hackathon(
                    name = "Gemini Visionary Hackathon",
                    description = "Construct agentic bots, smart visual tools, or localized custom solvers using the model 'gemini-3.5-flash'.",
                    platform = "Google AI Studio",
                    date = "June 12–14, 2026",
                    prizePool = "$30,000"
                ),
                Hackathon(
                    name = "Global DeFi Innovation Hub",
                    description = "Re-architect token liquidity pools, decentralized trust oracles, or digital micro-lending portals.",
                    platform = "Devpost",
                    date = "June 25–28, 2026",
                    prizePool = "$50,000"
                ),
                Hackathon(
                    name = "CleanTech Pioneers Sprint",
                    description = "Propose carbon-neutral supply solutions, EV efficiency tracking, or regional bio-energy grid forecasts.",
                    platform = "EcoLabs Platform",
                    date = "July 10–12, 2026",
                    prizePool = "$12,026"
                )
            )
            hackathons.forEach { synapseDao.insertHackathon(it) }

            // 5. Prepopulate Mentors
            val mentors = listOf(
                Mentor(
                    name = "Dr. Aris Thorne",
                    title = "AI Research Principal",
                    company = "Google DeepMind",
                    expertise = "Large Language Models, Deep Reinforcement Learning, Core ML",
                    bio = "Aris directs scientific inquiries into recursive logic chains and visual-text multimodal models.",
                    rating = 4.9f
                ),
                Mentor(
                    name = "Sophia Vance",
                    title = "Managing Venture Partner",
                    company = "Aether Capital",
                    expertise = "Venture Finance, Product-Market Fit, Seed Stage GTM",
                    bio = "Sophia spent 12 years helping early startups clarify core value streams, raise capital, and scale past $10M ARR.",
                    rating = 4.8f
                ),
                Mentor(
                    name = "Marcus Brody",
                    title = "Senior Cloud Architect",
                    company = "AWS Solutions",
                    expertise = "Microservices, Distributed Databases, Edge Cache Networks",
                    bio = "Marcus is a cloud engineering systems generalist who scales massive streaming infrastructures.",
                    rating = 4.7f
                )
            )
            mentors.forEach { synapseDao.insertMentor(it) }

            // 6. Prepopulate Notifications
            val alerts = listOf(
                Notification(
                    title = "Welcome to SynapseX!",
                    message = "Discover teammates, test startup formulas, and link up with world-class mentors. Try the AI Gen tool!",
                    type = "System"
                ),
                Notification(
                    title = "Hackathon Matching Enabled",
                    message = "Gemini Visionary Hackathon is open! Our AI recommends Kevin and Surmila as optimal teammates.",
                    type = "Match"
                )
            )
            alerts.forEach { synapseDao.insertNotification(it) }

            // 7. Prepopulate Student Projects
            val initialProjects = listOf(
                StudentProject(
                    title = "Retrofit GSON Parser",
                    description = "Optimized REST client layer with offline caching pipelines designed for low-connection networks.",
                    techStack = "Kotlin, Jetpack Compose, Retrofit, Room DB",
                    ownerId = "curr_user"
                ),
                StudentProject(
                    title = "Solana Liquidity Vault",
                    description = "Web3 non-custodial wallet script utilizing multisig thresholds for startup funding syndicates.",
                    techStack = "Solidity, Web3.js, React, Node.js",
                    ownerId = "student_aarav"
                ),
                StudentProject(
                    title = "Futuristic Carbon Dashboard UI",
                    description = "Immersive glassmorphic mockup including interactive real-time emission tracking widgets.",
                    techStack = "Figma, CSS, React, Framer Motion",
                    ownerId = "student_surmila"
                ),
                StudentProject(
                    title = "LLM Context Compress Engine",
                    description = "A localized pipeline that processes large prompts into ultra-lean embeddings preserving semantic weight.",
                    techStack = "Python, PyTorch, LangChain, HuggingFace",
                    ownerId = "student_kevin"
                ),
                StudentProject(
                    title = "DNA Sequence Segmenter",
                    description = "Big data pipeline utilizing parallel clusters to locate mutations in genomic strain chains.",
                    techStack = "R, Spark, Hadoop, BioConductor",
                    ownerId = "student_elena"
                ),
                StudentProject(
                    title = "Redis Cache Booster Proxy",
                    description = "Extremely light broker service achieving sub-4ms response latency for web feed servers.",
                    techStack = "Node.js, Express, Redis, PGPool",
                    ownerId = "student_siddharth"
                )
            )
            initialProjects.forEach { synapseDao.insertProject(it) }
        }
    }
}
