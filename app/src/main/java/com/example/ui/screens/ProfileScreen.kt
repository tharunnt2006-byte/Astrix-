package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.db.UserProfile
import com.example.ui.SynapseViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: SynapseViewModel
) {
    val currentUser by viewModel.currentUserFlow.collectAsState()
    val myProjects by viewModel.myProjects.collectAsState()

    var name by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    var github by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var experienceLevel by remember { mutableStateOf("Beginner") }
    var preferredRole by remember { mutableStateOf("AI/ML Engineer") }

    var newProjTitle by remember { mutableStateOf("") }
    var newProjStack by remember { mutableStateOf("") }
    var newProjDesc by remember { mutableStateOf("") }
    var isRecordingNewProject by remember { mutableStateOf(false) }

    val expList = listOf("Beginner", "Intermediate", "Advanced")
    val rolesList = listOf("AI/ML Engineer", "Frontend Developer", "Backend Developer", "UI/UX Designer", "Presenter")

    // Update locals if currentUser updates
    LaunchedEffect(currentUser) {
        val u = currentUser
        if (u != null) {
            name = u.name
            college = u.college
            department = u.department
            skills = u.skills
            interests = u.interests
            github = u.githubProfile
            linkedin = u.linkedinProfile
            experienceLevel = u.experienceLevel
            preferredRole = u.preferredRole
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        FuturisticHeader(
            title = "USER TALENT DOSSIER",
            subtitle = "Maintain your technical credentials, system preferences, and cofounder role parameters."
        )

        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "SYNAPSEX IDENTITY MATRIX",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "Modify your system coordinates below to update LLM matching vectors.",
                color = Color.Gray,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Innovator Full Name") },
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

            // College
            OutlinedTextField(
                value = college,
                onValueChange = { college = it },
                label = { Text("College / University Coordinates") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color(0x33FFFFFF)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Department
            OutlinedTextField(
                value = department,
                onValueChange = { department = it },
                label = { Text("Technical Department") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = NeonMagenta,
                    unfocusedBorderColor = Color(0x33FFFFFF)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Skills
            OutlinedTextField(
                value = skills,
                onValueChange = { skills = it },
                label = { Text("Core Skills (comma-separated)") },
                placeholder = { Text("e.g. Kotlin, Python, Web3, Solidity") },
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

            // Interests
            OutlinedTextField(
                value = interests,
                onValueChange = { interests = it },
                label = { Text("Domain Interests (comma-separated)") },
                placeholder = { Text("e.g. HealthTech, AI, SaaS, DeFi") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color(0x33FFFFFF)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Github & Linkedin
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = github,
                    onValueChange = { github = it },
                    label = { Text("GitHub User") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NeonTeal,
                        unfocusedBorderColor = Color(0x32FFFFFF)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = linkedin,
                    onValueChange = { linkedin = it },
                    label = { Text("LinkedIn URL") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = NeonTeal,
                        unfocusedBorderColor = Color(0x32FFFFFF)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Experience Level Selector
            Text(
                text = "SENIORITY EXPERIENCE RANK:",
                color = Color.LightGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                expList.forEach { exp ->
                    val isSelected = experienceLevel == exp
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
                            .clickable { experienceLevel = exp }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = exp,
                            color = if (isSelected) Color.White else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Preferred Role Selector
            Text(
                text = "PREFERRED TEAM ROLE MATCH:",
                color = Color.LightGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rolesList.forEach { role ->
                    val isSelected = preferredRole == role
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
                            .clickable { preferredRole = role }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = role,
                            color = if (isSelected) Color.White else Color.Gray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            FuturisticButton(
                text = "Save Talents Coordinates",
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.saveUserProfile(
                            name = name,
                            college = college,
                            department = department,
                            skills = skills,
                            interests = interests,
                            github = github,
                            linkedin = linkedin,
                            experienceLevel = experienceLevel,
                            preferredRole = preferredRole
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isEnabled = name.isNotBlank()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Technical Project Showcase Card
        GlassmorphicCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "TECHNICAL PROJECT SHOWCASE",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "Add hacks, repositories, or prototypes to present automatically on your collaborator profile.",
                color = Color.Gray,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Render existing projects
            if (myProjects.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x06FFFFFF), RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No projects uploaded to dossier yet.",
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )
                }
            } else {
                myProjects.forEach { project ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .background(Color(0x0EFFFFFF), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 40.dp)
                        ) {
                            Text(
                                text = project.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = project.description,
                                color = Color.LightGray,
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            
                            // Tech Stack badges
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                project.techStack.split(",").forEach { tech ->
                                    val cleanTech = tech.trim()
                                    if (cleanTech.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .background(NeonTeal.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                                .border(0.5.dp, NeonTeal, RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = cleanTech,
                                                color = NeonTeal,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        IconButton(
                            onClick = { viewModel.deleteStudentProject(project.id) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Project",
                                tint = Color(0xFFEF5350),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Expandable form to add a project
            if (isRecordingNewProject) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x06FFFFFF), RoundedCornerShape(14.dp))
                        .border(1.dp, Color(0x10FFFFFF), RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "RECORD NEW LAB PROTOCOL",
                            color = NeonTeal,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        OutlinedTextField(
                            value = newProjTitle,
                            onValueChange = { newProjTitle = it },
                            label = { Text("Project Title") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonTeal,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = newProjStack,
                            onValueChange = { newProjStack = it },
                            label = { Text("Tech Stack (comma-separated)") },
                            placeholder = { Text("e.g. Flutter, Firebase, Rust") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonPurple,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = newProjDesc,
                            onValueChange = { newProjDesc = it },
                            label = { Text("Project Architectural Concept") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonMagenta,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isRecordingNewProject = false },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Abort", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            FuturisticButton(
                                text = "Deploy Showcase",
                                onClick = {
                                    if (newProjTitle.isNotBlank()) {
                                        viewModel.addStudentProject(
                                            title = newProjTitle.trim(),
                                            description = newProjDesc.trim(),
                                            techStack = newProjStack.trim()
                                        )
                                        newProjTitle = ""
                                        newProjStack = ""
                                        newProjDesc = ""
                                        isRecordingNewProject = false
                                    }
                                },
                                isEnabled = newProjTitle.isNotBlank(),
                                modifier = Modifier.weight(1.2f)
                            )
                        }
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { isRecordingNewProject = true },
                    border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Add, "Add Icon", tint = NeonTeal, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PROVISION NEW SHOWCASE RECORD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}
