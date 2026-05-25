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
import com.example.db.Hackathon
import com.example.ui.SynapseViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HackathonScreen(
    viewModel: SynapseViewModel
) {
    val hackathons by viewModel.hackathons.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FuturisticHeader(
            title = "HACKATHON PORTAL",
            subtitle = "Discover upcoming global tech sprints, form competitive teams, and capture innovation pools."
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(hackathons) { hackathon ->
                HackathonEventCard(
                    hackathon = hackathon,
                    onRegisterToggle = { viewModel.registerForHackathon(hackathon) }
                )
            }
        }
    }
}

@Composable
fun HackathonEventCard(
    hackathon: Hackathon,
    onRegisterToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(GlassWhite)
            .border(
                1.dp,
                if (hackathon.isRegistered) NeonTeal else GlassWhiteBorder,
                RoundedCornerShape(20.dp)
            )
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
                            .background(NeonTeal.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(0.5.dp, NeonTeal.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = hackathon.platform.uppercase(), color = NeonTeal, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = hackathon.date,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (hackathon.isRegistered) {
                    Box(
                        modifier = Modifier
                            .background(Color(0x3300F2FE), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("REGISTERED", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = hackathon.name.uppercase(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = hackathon.description,
                color = Color.LightGray,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "PRIZE INDEX", color = Color.Gray, fontSize = 9.sp)
                    Text(text = hackathon.prizePool, color = NeonMagenta, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }

                // Call to action button
                Button(
                    onClick = onRegisterToggle,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hackathon.isRegistered) Color(0x22FFFFFF) else NeonPurple
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = if (hackathon.isRegistered) "CANCEL SPRINT" else "LAUNCH SPRINT",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
