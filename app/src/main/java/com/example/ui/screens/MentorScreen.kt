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
import com.example.db.Mentor
import com.example.ui.SynapseViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorScreen(
    viewModel: SynapseViewModel
) {
    val mentors by viewModel.mentors.collectAsState()
    val isGeneratingMentorRecommendations by viewModel.isGeneratingMentorRecommendations.collectAsState()
    val mentorRecommendationsResult by viewModel.mentorRecommendationsResult.collectAsState()

    var activeTab by remember { mutableStateOf("Mentors list") } // "Mentors list" or "Become Mentor"

    // Become Mentor Fields
    var mName by remember { mutableStateOf("") }
    var mTitle by remember { mutableStateOf("") }
    var mCompany by remember { mutableStateOf("") }
    var mExpertise by remember { mutableStateOf("") }
    var mBio by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FuturisticHeader(
            title = "NEURAL MENTOR MATCH",
            subtitle = "Book 1:1 sessions with global AI scientists, venture capitalists, and cloud architects."
        )

        // Switch Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val tabsList = listOf("Mentors list", "Become Mentor")
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

        when (activeTab) {
            "Mentors list" -> {
                Column(modifier = Modifier.weight(1f)) {
                    // AI Mentor Alignment Module
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        isGlowing = true
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "AI COFOUNDER & ADVISOR ALIGNER",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Locate critical research gaps and matching VC pipelines tailored to your profile.",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                            Icon(Icons.Default.Hive, null, tint = NeonTeal, modifier = Modifier.size(18.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isGeneratingMentorRecommendations) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = NeonTeal, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Analyzing user dossiers and matching project dependencies...",
                                    color = Color.LightGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        } else {
                            val result = mentorRecommendationsResult
                            if (result != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0x06FFFFFF), RoundedCornerShape(12.dp))
                                        .border(0.5.dp, Color(0x13FFFFFF), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "SYNAPSEX INTELLIGENCE REPORT",
                                            color = NeonTeal,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Text(
                                            text = result,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp,
                                            modifier = Modifier
                                                .verticalScroll(rememberScrollState())
                                                .heightIn(max = 140.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }

                            FuturisticButton(
                                text = if (result != null) "Re-Run Neural Matching" else "Request Brain Trust Analysis",
                                onClick = { viewModel.generateMentorRecommendations() },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(mentors) { mentor ->
                            MentorShowcaseCard(mentor = mentor)
                        }
                    }
                }
            }

            "Become Mentor" -> {
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
                            text = "REGISTER AS ADVISOR",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Share advice on GTM vectors, neural architectures, or scaling strategies.",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                            value = mName,
                            onValueChange = { mName = it },
                            label = { Text("Your Professional Name") },
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
                            value = mTitle,
                            onValueChange = { mTitle = it },
                            label = { Text("Designation / Title") },
                            placeholder = { Text("e.g. GenAI Engineer, VC partner") },
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

                        OutlinedTextField(
                            value = mCompany,
                            onValueChange = { mCompany = it },
                            label = { Text("Organization / Company") },
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

                        OutlinedTextField(
                            value = mExpertise,
                            onValueChange = { mExpertise = it },
                            label = { Text("Expertise tags (comma-separated)") },
                            placeholder = { Text("e.g. Kotlin, LLMs, Fundraising") },
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
                            value = mBio,
                            onValueChange = { mBio = it },
                            label = { Text("Biographical / Experience synopsis") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = NeonPurple,
                                unfocusedBorderColor = Color(0x33FFFFFF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        FuturisticButton(
                            text = "Deploy Mentor Access",
                            onClick = {
                                if (mName.isNotBlank() && mExpertise.isNotBlank()) {
                                    viewModel.registerSelectedMentor(
                                        name = mName,
                                        title = mTitle,
                                        company = mCompany,
                                        expertise = mExpertise,
                                        bio = mBio
                                    )
                                    // Reset Fields
                                    mName = ""
                                    mTitle = ""
                                    mCompany = ""
                                    mExpertise = ""
                                    mBio = ""
                                    activeTab = "Mentors list"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isEnabled = mName.isNotBlank() && mExpertise.isNotBlank()
                        )
                    }
                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
        }
    }
}

@Composable
fun MentorShowcaseCard(
    mentor: Mentor
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = mentor.name.uppercase(),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "${mentor.title} @ ${mentor.company}",
                        color = NeonTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .background(Color(0x0AFFFFFF), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, null, tint = NeonTeal, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${mentor.rating}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = mentor.bio,
                color = Color.LightGray,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Areas of expertise
            Text(
                text = "EXPERTISE LANDSCAPE:",
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
                mentor.expertise.split(",").forEach { skill ->
                    Box(
                        modifier = Modifier
                            .background(Color(0x0CFFFFFF), RoundedCornerShape(4.dp))
                            .border(0.5.dp, Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = skill.trim(), color = Color.LightGray, fontSize = 9.sp)
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
                    text = "STATUS: ONLINE",
                    color = NeonTeal,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                Text(
                    text = "Request AI Consult Calendar",
                    color = NeonTeal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }
        }
    }
}
