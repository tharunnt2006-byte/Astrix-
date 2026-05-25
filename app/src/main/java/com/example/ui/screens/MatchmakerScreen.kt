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
import androidx.compose.ui.graphics.Brush
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
fun MatchmakerScreen(
    viewModel: SynapseViewModel
) {
    val collaborators by viewModel.collaborators.collectAsState()
    val selectedCollaborator by viewModel.selectedCollaborator.collectAsState()
    val analysisLoading by viewModel.chemistryAnalysisLoading.collectAsState()
    val analysisResult by viewModel.chemistryAnalysisResult.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf("All Roles") }

    val rolesList = listOf("All Roles", "AI/ML Engineer", "Backend Developer", "UI/UX Designer", "Presenter")

    // Sort/Filter list logic
    val filteredList = collaborators.filter { student ->
        val matchesSearch = student.name.contains(searchQuery, ignoreCase = true) ||
                student.skills.contains(searchQuery, ignoreCase = true) ||
                student.interests.contains(searchQuery, ignoreCase = true)

        val matchesRole = selectedRoleFilter == "All Roles" || student.preferredRole == selectedRoleFilter
        matchesSearch && matchesRole
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FuturisticHeader(
            title = "SI-CO MATCHMAKER",
            subtitle = "Compute technical chemistry, role division, and compatibility scores dynamically."
        )

        // Selected active chemistry dashboard
        AnimatedVisibility(
            visible = selectedCollaborator != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            val candidate = selectedCollaborator
            if (candidate != null) {
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "COFOUNDER CHEMSPEC ANALYSIS",
                        color = NeonTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    GlassmorphicCard(
                        modifier = Modifier.fillMaxWidth(),
                        isGlowing = true
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = candidate.name.uppercase(),
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "${candidate.college} · ${candidate.department}",
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            }
                            IconButton(onClick = { viewModel.selectCollaborator(null) }) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.LightGray)
                            }
                        }

                        Divider(color = Color(0x13FFFFFF), modifier = Modifier.padding(vertical = 12.dp))

                        if (analysisLoading) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = NeonTeal, modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Invoking Google Gemini 3.5-Flash agentic neural metrics...",
                                    color = Color.LightGray,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        } else {
                            val result = analysisResult
                            if (result != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // High tech score dial
                                    Box(
                                        modifier = Modifier
                                            .size(72.dp)
                                            .background(NeonPurple.copy(alpha = 0.15f), CircleShape)
                                            .border(2.dp, NeonTeal, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = "${result.score}%",
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                            Text(
                                                text = "MATCH",
                                                color = NeonTeal,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.5.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.FlashOn, null, tint = NeonPurple, modifier = Modifier.size(13.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "ROLE MATCH: ${result.suggestedRole.uppercase()}",
                                                color = NeonPurple,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = 0.5.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = result.reason,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FuturisticButton(
                                        text = "Send Team Match Proposal",
                                        onClick = {
                                            viewModel.sendTeamMatchRequest(candidate)
                                            viewModel.selectCollaborator(null)
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedButton(
                                        onClick = { viewModel.selectCollaborator(candidate) }, // regenerate
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                        border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                                        modifier = Modifier.height(48.dp)
                                    ) {
                                        Icon(Icons.Default.Refresh, "Re-compute", tint = Color.White)
                                    }
                                }
                            } else {
                                Text(
                                    text = "Empty analysis coordinates. Tap refresh to spin.",
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Filter / Search Tools
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search skills, names, interests...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = NeonTeal,
                    unfocusedBorderColor = Color(0x35FFFFFF),
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }

        // Filtering horizontal role buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            rolesList.forEach { role ->
                val isSelected = selectedRoleFilter == role
                Box(
                    modifier = Modifier
                        .background(
                            if (isSelected) NeonPurple.copy(alpha = 0.25f) else Color(0x0CFFFFFF),
                            RoundedCornerShape(8.dp)
                        )
                        .border(
                            1.dp,
                            if (isSelected) NeonPurple else Color(0x1FFFFFFF),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedRoleFilter = role }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = role,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Talent Directory List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SearchOff, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("No cofounders aligned with these filter configurations.", color = Color.Gray, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(filteredList) { student ->
                    val studentProjects = allProjects.filter { it.ownerId == student.id }
                    CollaboratorListingCard(
                        student = student,
                        isSelected = selectedCollaborator?.id == student.id,
                        projects = studentProjects,
                        onClick = { viewModel.selectCollaborator(student) }
                    )
                }
            }
        }
    }
}

@Composable
fun CollaboratorListingCard(
    student: UserProfile,
    isSelected: Boolean,
    projects: List<com.example.db.StudentProject>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0x1EFFFFFF) else GlassWhite)
            .border(
                1.dp,
                if (isSelected) NeonTeal else GlassWhiteBorder,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = student.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        NeonBadge(text = student.experienceLevel, color = NeonPurple)
                    }
                    Text(
                        text = "${student.college} · ${student.department}",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(NeonTeal.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, NeonTeal.copy(alpha = 0.38f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SensorOccupied, null, tint = NeonTeal, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = student.preferredRole.uppercase(),
                            color = NeonTeal,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Display skills tags
            Text(
                text = "SKILL VECTOR:",
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                student.skills.split(",").forEach { skill ->
                    Box(
                        modifier = Modifier
                            .background(Color(0x0AFFFFFF), RoundedCornerShape(4.dp))
                            .border(0.5.dp, Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = skill.trim(), color = Color.LightGray, fontSize = 9.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display interest tags
            Text(
                text = "SYSTEM FOCUS:",
                color = Color.Gray,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                student.interests.split(",").forEach { interest ->
                    Box(
                        modifier = Modifier
                            .background(Color(0x06FFFFFF), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = interest.trim(), color = NeonPurple, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Show student showcased projects if any exist
            if (projects.isNotEmpty()) {
                Text(
                    text = "ACTIVE SHOWCASED INITIATIVE:",
                    color = Color.Gray,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                projects.forEach { project ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .background(Color(0x06FFFFFF), RoundedCornerShape(8.dp))
                            .border(0.5.dp, Color(0x12FFFFFF), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(NeonTeal, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = project.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = project.description,
                                color = Color.Gray,
                                fontSize = 10.sp,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Quick view trigger prompt info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "GitHub: ${student.githubProfile}",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light
                    )
                }
                Text(
                    text = "Tap to compute chemistry",
                    color = NeonTeal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
