package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.db.StartupIdea
import com.example.ui.SynapseViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartupHubScreen(
    viewModel: SynapseViewModel
) {
    val startupIdeas by viewModel.startupIdeas.collectAsState()
    val isGenerating by viewModel.isGeneratingStartup.collectAsState()
    val generatedBp by viewModel.generatedStartupIdea.collectAsState()
    val generateError by viewModel.generateError.collectAsState()

    var activeTab by remember { mutableStateOf("Hub Board") } // "Hub Board" or "AI Idea Lab" or "Propose Idea"

    // Propose Idea Form fields
    var newTitle by remember { mutableStateOf("") }
    var newProblem by remember { mutableStateOf("") }
    var newSolution by remember { mutableStateOf("") }
    var newSkills by remember { mutableStateOf("") }
    var newTeamSize by remember { mutableStateOf("3") }
    var newCategory by remember { mutableStateOf("SaaS") }

    // AI Lab generator inputs
    var aiDomain by remember { mutableStateOf("") }
    var aiProblem by remember { mutableStateOf("") }
    var aiTarget by remember { mutableStateOf("") }

    val categories = listOf("SaaS", "FinTech", "AI / ML", "CleanTech", "HealthTech", "Web3")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FuturisticHeader(
            title = "VENTURE STARTUP HUB",
            subtitle = "Design and review state-of-the-art startup models or deploy AI generators."
        )

        // Custom Navigation Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val tabsList = listOf("Hub Board", "AI Idea Lab", "Propose Idea")
            tabsList.forEach { tab ->
                val isSelected = activeTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            if (isSelected) NeonPurple.copy(alpha = 0.25f) else Color(0x0CFFFFFF),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isSelected) NeonPurple else Color(0x1FFFFFFF),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { activeTab = tab }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.uppercase(),
                        color = if (isSelected) Color.White else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Screen state dispatcher
        when (activeTab) {
            "Hub Board" -> {
                if (startupIdeas.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active student ventures catalogued. Deploy AI sandbox!", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(startupIdeas) { idea ->
                            IdeaShowcaseCard(
                                idea = idea,
                                onLike = { viewModel.toggleLikeStartupIdea(idea) }
                            )
                        }
                    }
                }
            }

            "AI Idea Lab" -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "SYNAPSEX ADVANCED BRAINSTORMING NODE",
                        color = NeonTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Display active generated blueprint
                    val bp = generatedBp
                    if (bp != null) {
                        GlassmorphicCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            isGlowing = true
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Celebration, null, tint = NeonTeal, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "BLUEPRINT ENCODED SUCCESSFULLY",
                                        color = NeonTeal,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                IconButton(onClick = { viewModel.clearGeneratedStartup() }) {
                                    Icon(Icons.Default.Close, null, tint = Color.Gray)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = bp.title.uppercase(),
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            BlueprintDetailItem("CORE VENTURE SOLUTION", bp.solution, NeonPurple)
                            BlueprintDetailItem("ANNUAL REVENUE CHANNEL", bp.revenueModel, NeonTeal)
                            BlueprintDetailItem("RECOMMENDED FRAMEWORK STACK", bp.techStack, NeonBlue)
                            BlueprintDetailItem("DEVELOPER TARGET MILESTONES", bp.futureScope, NeonMagenta)

                            Spacer(modifier = Modifier.height(16.dp))

                            // Viability rating bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("SYSTEM VIABILITY INDEX", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("${bp.innovationScore}/100", color = NeonTeal, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .background(Color(0x1AFFFFFF), RoundedCornerShape(3.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(bp.innovationScore / 100f)
                                        .background(NeonTeal, RoundedCornerShape(3.dp))
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FuturisticButton(
                                    text = "Publish to Hub Board",
                                    onClick = {
                                        viewModel.createStartupIdea(
                                            title = bp.title,
                                            problem = "AI Sandbox Prompt Concept",
                                            solution = bp.solution,
                                            skills = bp.techStack,
                                            teamSize = 3,
                                            category = "AI / Generated"
                                        )
                                        viewModel.clearGeneratedStartup()
                                        activeTab = "Hub Board"
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedButton(
                                    onClick = { viewModel.generateStartupIdea(aiDomain, aiProblem, aiTarget) },
                                    border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, null, tint = Color.White)
                                }
                            }
                        }
                    } else {
                        // Inputs Panel
                        GlassmorphicCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Define Ecosystem Targets",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Enter domain parameters and audience mappings to seed the AI model.",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = aiDomain,
                                onValueChange = { aiDomain = it },
                                label = { Text("Scientific / Industry Domain") },
                                placeholder = { Text("e.g. CleanTech, DeFi, DevTools") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NeonTeal,
                                    unfocusedBorderColor = Color(0x33FFFFFF),
                                    focusedLabelColor = NeonTeal
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = aiProblem,
                                onValueChange = { aiProblem = it },
                                label = { Text("Unsolved Friction / Problem Statement") },
                                placeholder = { Text("e.g. tracking micro-carbon offsets accurately") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NeonPurple,
                                    unfocusedBorderColor = Color(0x33FFFFFF),
                                    focusedLabelColor = NeonPurple
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = aiTarget,
                                onValueChange = { aiTarget = it },
                                label = { Text("Target Audience Profile") },
                                placeholder = { Text("e.g. high-volume logistics dispatchers") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = NeonMagenta,
                                    unfocusedBorderColor = Color(0x33FFFFFF),
                                    focusedLabelColor = NeonMagenta
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            if (isGenerating) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = NeonTeal, modifier = Modifier.size(28.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Assembling neural matrices... generating revenue models...",
                                        color = Color.LightGray,
                                        fontSize = 11.sp
                                    )
                                }
                            } else {
                                FuturisticButton(
                                    text = "Generate Artificial Blueprint",
                                    onClick = {
                                        if (aiDomain.isNotBlank() && aiProblem.isNotBlank() && aiTarget.isNotBlank()) {
                                            viewModel.generateStartupIdea(aiDomain, aiProblem, aiTarget)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    isEnabled = aiDomain.isNotBlank() && aiProblem.isNotBlank() && aiTarget.isNotBlank()
                                )
                            }

                            generateError?.let { err ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = "Error seeding prompt: $err", color = NeonMagenta, fontSize = 11.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }

            "Propose Idea" -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "PUBLISH NEW STARTUP CONCEPT",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Announce details of your venture, skills and needed talent coordinates to recruit collaborators.",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = newTitle,
                            onValueChange = { newTitle = it },
                            label = { Text("Venture Design Title") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonTeal,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newProblem,
                            onValueChange = { newProblem = it },
                            label = { Text("Market Problem statement") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonPurple,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newSolution,
                            onValueChange = { newSolution = it },
                            label = { Text("Proposed Technical Solution") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonMagenta,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = newSkills,
                            onValueChange = { newSkills = it },
                            label = { Text("Core Skills Required (comma-separated)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonTeal,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Category Selection list
                        Text(
                            text = "SELECT DOMAIN CATEGORY:",
                            color = Color.LightGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSelected = newCategory == cat
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (isSelected) NeonTeal.copy(alpha = 0.25f) else Color(0x0CFFFFFF),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) NeonTeal else Color(0x1FFFFFFF),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { newCategory = cat }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        color = if (isSelected) Color.White else Color.Gray,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        FuturisticButton(
                            text = "Dispatch Startup Blueprint",
                            onClick = {
                                if (newTitle.isNotBlank()) {
                                    viewModel.createStartupIdea(
                                        title = newTitle,
                                        problem = newProblem,
                                        solution = newSolution,
                                        skills = newSkills,
                                        teamSize = newTeamSize.toIntOrNull() ?: 3,
                                        category = newCategory
                                    )
                                    // Reset fields
                                    newTitle = ""
                                    newProblem = ""
                                    newSolution = ""
                                    newSkills = ""
                                    activeTab = "Hub Board"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isEnabled = newTitle.isNotBlank() && newSolution.isNotBlank()
                        )
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
fun IdeaShowcaseCard(
    idea: StartupIdea,
    onLike: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GlassWhite)
            .border(1.dp, GlassWhiteBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(NeonPurple.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, NeonPurple.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = idea.category.uppercase(), color = NeonPurple, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BY ${idea.authorName.uppercase()}",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Interactive Like Action
                Row(
                    modifier = Modifier
                        .background(Color(0x0EFFFFFF), RoundedCornerShape(8.dp))
                        .clickable { onLike() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "Like", tint = NeonMagenta, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${idea.likesCount}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = idea.title.uppercase(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(4.dp)
                        .background(NeonTeal, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "PROBLEM:", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = idea.problemStatement, color = Color.LightGray, fontSize = 12.sp, lineHeight = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(4.dp)
                        .background(NeonPurple, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = "SOLUTION:", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(text = idea.proposedSolution, color = Color.White, fontSize = 12.sp, lineHeight = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tech badges required
            Text(
                text = "SKILLS TO RECRUIT:",
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                idea.requiredSkills.split(",").forEach { skill ->
                    Box(
                        modifier = Modifier
                            .background(Color(0x0EFFFFFF), RoundedCornerShape(4.dp))
                            .border(0.5.dp, Color(0x13FFFFFF), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = skill.trim(), color = Color.White, fontSize = 9.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TEAM REQUISITE: ${idea.teamSizeNeeded} PARTICIPANTS",
                    color = NeonTeal,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Request Collaboration Hub",
                    color = NeonTeal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onLike() }
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun BlueprintDetailItem(
    title: String,
    content: String,
    bulletColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(bulletColor, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
        Text(
            text = content,
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 14.dp, top = 2.dp)
        )
    }
}
