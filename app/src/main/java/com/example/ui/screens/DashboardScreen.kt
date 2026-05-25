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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.db.*
import com.example.ui.SynapseViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: SynapseViewModel,
    onNavigateToMatch: () -> Unit,
    onNavigateToHub: () -> Unit
) {
    val currentUser by viewModel.currentUserFlow.collectAsState()
    val collaborators by viewModel.collaborators.collectAsState()
    val startupIdeas by viewModel.startupIdeas.collectAsState()
    val hackathons by viewModel.hackathons.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Header based on Bento template style
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo box with gradient and "SX" text
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF9333EA), Color(0xFF3B82F6)))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SX",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Synapse",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "X",
                                color = NeonPurple,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Text(
                            text = "Welcome back, ${currentUser?.name ?: "Jane Doe"}",
                            color = Color(0xFF94A3B8), // slate-400
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Profile circular icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x13FFFFFF))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (currentUser?.name ?: "Jane").take(2).uppercase(),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bento Cell 1: AI Match Discovery (Spotlight card)
            val topPartner = collaborators.sortedByDescending { it.id }.firstOrNull()
            if (topPartner != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x0AFFFFFF))
                        .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(20.dp))
                        .clickable { onNavigateToMatch() }
                        .padding(20.dp)
                ) {
                    // Badge container in top right (Absolute positioned via Box)
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .background(NeonPurple.copy(alpha = 0.2f), RoundedCornerShape(100.dp))
                            .border(1.dp, NeonPurple.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "BEST MATCH",
                            color = NeonPurple,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "AI DISCOVERY",
                            color = Color(0xFF94A3B8), // slate-400
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = topPartner.name,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val dynamicBio = "Specialising in ${topPartner.interests.split(",").firstOrNull() ?: "Tech"} with expert coordinates in ${topPartner.skills.split(",").take(2).joinToString(", ")} to complement your engineering stack."
                        Text(
                            text = "\"$dynamicBio\"",
                            color = Color(0xFFCBD5E1), // slate-300
                            fontSize = 12.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Mini skill bubbles
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val skillsToShow = topPartner.skills.split(",").take(2).map { it.trim() }
                                skillsToShow.forEach { skill ->
                                    if (skill.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0x0FFFFFFF), RoundedCornerShape(100.dp))
                                                .border(1.dp, Color(0x17FFFFFF), RoundedCornerShape(100.dp))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = skill,
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }

                            // Dynamic compatibility score
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                val gradientColors = listOf(NeonPurple, NeonBlue)
                                Text(
                                    text = "94%",
                                    style = TextStyle(
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Black,
                                        brush = Brush.linearGradient(gradientColors)
                                    )
                                )
                                Text(
                                    text = "COMPATIBILITY",
                                    color = Color(0xFF64748B), // slate-500
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bento Cells 2 & 3: Quick Stats (Side-by-Side)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatPanel(
                    title = "Hackathons",
                    count = "${hackathons.size}",
                    extra = "${hackathons.count { it.isRegistered }} active",
                    icon = Icons.Default.EmojiEvents,
                    color = NeonBlue,
                    modifier = Modifier.weight(1f),
                    featuredText = hackathons.firstOrNull()?.name ?: "CodeFest '26",
                    featuredSubtext = "Live telemetry active"
                )
                StatPanel(
                    title = "Startup Hub",
                    count = "${startupIdeas.size}",
                    extra = "${startupIdeas.size} ideas live",
                    icon = Icons.Default.Lightbulb,
                    color = Color(0xFFF59E0B), // beautiful amber color from Bento grid theme
                    modifier = Modifier.weight(1f),
                    featuredText = startupIdeas.firstOrNull()?.title ?: "EcoSync AI",
                    featuredSubtext = "Needs: Dev, CFO"
                )
            }

            // Bento Cell 4: Mentor Insights (Wide layout)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0x0AFFFFFF))
                    .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Profile avatar block
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)))
                            )
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AT",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dr. Alan Turing",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color(0xFF4ADE80), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Online",
                                    color = Color(0xFF4ADE80),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Suggested for your Neural Network and System architecture designs.",
                            color = Color(0xFF94A3B8), // slate-400
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        // Connect Button styled with Bento accents
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0x13FFFFFF))
                                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(8.dp))
                                .clickable { /* Connect simulated */ }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "CONNECT NOW",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            // Bento Cell 4: Skill Analytics Section
            Text(
                text = "SKILL REVOLUTION MATRIX",
                color = Color(0xFF94A3B8), // slate-400
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Text(
                    text = "System Talent Density",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Comparative skillset strength representation across participants.",
                    color = Color(0xFF94A3B8), // slate-400
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Modern visual bars representing telemetry statistics of skills
                ProgressMetricBar("Artificial Intelligence & LLMs", 92, NeonTeal)
                ProgressMetricBar("Advanced Kotlin / Compose", 84, NeonPurple)
                ProgressMetricBar("Cloud Engineering & Infrastructure", 68, NeonBlue)
                ProgressMetricBar("Responsive UI Experience", 78, NeonMagenta)
            }

            // Bento Cell 5: Innovation Leaderboard section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "INNOVATION LEADERBOARD",
                    color = Color(0xFF94A3B8), // slate-400
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Top Roster",
                    color = NeonPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToMatch() }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                // Ranks of highly skilled collaborators
                val rankList = collaborators.take(3)
                if (rankList.isEmpty()) {
                    Text(
                        text = "Leaderboard telemetry loading...",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                } else {
                    rankList.forEachIndexed { idx, col ->
                        LeaderboardRow(
                            rank = idx + 1,
                            name = col.name,
                            college = col.college,
                            department = col.department,
                            points = 2400 - (idx * 400),
                            role = col.preferredRole
                        )
                    }
                }
            }

            // Bento Cell 6: System Alerts / Notifications channel
            Text(
                text = "SYSTEM ENCRYPTED SENSORS",
                color = Color(0xFF94A3B8), // slate-400
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (notifications.isEmpty()) {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No passive broadcast streams listening.",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            } else {
                notifications.take(4).forEach { banner ->
                    ChannelAlertCard(banner = banner)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun StatPanel(
    title: String,
    count: String,
    extra: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    featuredText: String? = null,
    featuredSubtext: String? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x0AFFFFFF))
            .border(1.dp, Color(0x1AFFFFFF), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.15f), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = extra,
                color = Color(0xFF94A3B8), // slate-400
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            if (featuredText != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x0FFFFFFF), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0x13FFFFFF), RoundedCornerShape(10.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            text = featuredText,
                            color = color,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (featuredSubtext != null) {
                            Text(
                                text = featuredSubtext,
                                color = Color.Gray,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressMetricBar(
    name: String,
    fillPercent: Int,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(text = "$fillPercent%", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        // Progress background track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color(0x1AFFFFFF), RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fillPercent / 100f)
                    .background(
                        brush = Brush.linearGradient(listOf(color, color.copy(alpha = 0.5f))),
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

@Composable
fun LeaderboardRow(
    rank: Int,
    name: String,
    college: String,
    department: String,
    points: Int,
    role: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank visual container
        val medalColor = when (rank) {
            1 -> NeonTeal
            2 -> NeonPurple
            3 -> NeonMagenta
            else -> Color.Gray
        }
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(medalColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                .border(1.dp, medalColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$rank",
                color = medalColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = "$college · $role",
                color = Color.Gray,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$points PX",
                color = NeonTeal,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "INNOVATION RATING",
                color = Color.Gray,
                fontSize = 8.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun ChannelAlertCard(
    banner: Notification
) {
    val alertColor = when (banner.type) {
        "Match" -> NeonMagenta
        "Idea" -> NeonPurple
        else -> NeonTeal
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x06FFFFFF))
            .border(1.dp, alertColor.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(alertColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (banner.type) {
                        "Match" -> Icons.Default.Hub
                        "Idea" -> Icons.Default.Psychology
                        else -> Icons.Default.Sensors
                    },
                    contentDescription = null,
                    tint = alertColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = banner.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = banner.message,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
