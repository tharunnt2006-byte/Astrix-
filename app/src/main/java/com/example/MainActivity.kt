package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SynapseViewModel
import com.example.ui.components.NeonGradientBackground
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.NeonTeal
import com.example.ui.theme.SpaceBackground
import com.example.ui.theme.SpaceCard

class MainActivity : ComponentActivity() {
  
  private val viewModel: SynapseViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        var enteredPlatform by remember { mutableStateOf(false) }
        var currentTab by remember { mutableStateOf("dashboard") }

        Scaffold(
          modifier = Modifier.fillMaxSize(),
          contentWindowInsets = WindowInsets.safeDrawing // Prevent notch clipping at top
        ) { innerPadding ->
          Box(
              modifier = Modifier
                  .fillMaxSize()
                  .background(SpaceBackground)
                  .padding(innerPadding)
          ) {
              if (!enteredPlatform) {
                  WelcomeScreen(
                      onEnterPlatform = { enteredPlatform = true }
                  )
              } else {
                  // Core platform interface with a beautiful bottom navbar layout
                  Column(
                      modifier = Modifier.fillMaxSize()
                  ) {
                      // Main Content Area
                      Box(
                          modifier = Modifier
                              .weight(1f)
                              .fillMaxWidth()
                      ) {
                          AnimatedContent(
                              targetState = currentTab,
                              transitionSpec = {
                                  fadeIn() togetherWith fadeOut()
                              },
                              label = "tab_fade"
                          ) { target ->
                              when (target) {
                                  "dashboard" -> DashboardScreen(
                                      viewModel = viewModel,
                                      onNavigateToMatch = { currentTab = "match" },
                                      onNavigateToHub = { currentTab = "hub" }
                                  )
                                  "match" -> MatchmakerScreen(
                                      viewModel = viewModel
                                  )
                                  "hub" -> StartupHubScreen(
                                      viewModel = viewModel
                                  )
                                  "hackathon" -> HackathonScreen(
                                      viewModel = viewModel
                                  )
                                  "mentor" -> MentorScreen(
                                      viewModel = viewModel
                                  )
                                  "profile" -> ProfileScreen(
                                      viewModel = viewModel
                                  )
                              }
                          }
                      }

                      // Floating Bottom Navigation Row - styled like a futuristic glass bar
                      Box(
                          modifier = Modifier
                              .fillMaxWidth()
                              .windowInsetsPadding(WindowInsets.navigationBars) // Prevent gesture bars overlap
                              .padding(horizontal = 8.dp, vertical = 6.dp)
                      ) {
                          Row(
                              modifier = Modifier
                                  .fillMaxWidth()
                                  .clip(RoundedCornerShape(20.dp))
                                  .background(Color(0x13FFFFFF))
                                  .border(1.dp, Color(0x1EFFFFFF), RoundedCornerShape(20.dp))
                                  .padding(horizontal = 6.dp, vertical = 8.dp),
                              horizontalArrangement = Arrangement.SpaceAround,
                              verticalAlignment = Alignment.CenterVertically
                          ) {
                              BottomNavItem(
                                  label = "Feed",
                                  icon = Icons.Default.Dashboard,
                                  isSelected = currentTab == "dashboard",
                                  onClick = { currentTab = "dashboard" }
                              )
                              BottomNavItem(
                                  label = "Match",
                                  icon = Icons.Default.People,
                                  isSelected = currentTab == "match",
                                  onClick = { currentTab = "match" }
                              )
                              BottomNavItem(
                                  label = "Hub",
                                  icon = Icons.Default.Lightbulb,
                                  isSelected = currentTab == "hub",
                                  onClick = { currentTab = "hub" }
                              )
                              BottomNavItem(
                                  label = "Hacks",
                                  icon = Icons.Default.EmojiEvents,
                                  isSelected = currentTab == "hackathon",
                                  onClick = { currentTab = "hackathon" }
                              )
                              BottomNavItem(
                                  label = "Mentors",
                                  icon = Icons.Default.Psychology,
                                  isSelected = currentTab == "mentor",
                                  onClick = { currentTab = "mentor" }
                              )
                              BottomNavItem(
                                  label = "Me",
                                  icon = Icons.Default.Person,
                                  isSelected = currentTab == "profile",
                                  onClick = { currentTab = "profile" }
                              )
                          }
                      }
                  }
              }
          }
        }
      }
    }
  }
}

@Composable
fun BottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (isSelected) NeonTeal else Color.Gray
    val backColor = if (isSelected) NeonTeal.copy(alpha = 0.1f) else Color.Transparent

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backColor)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = tint,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
