package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IncenseOffering
import com.example.data.model.SacredChant
import com.example.ui.MainViewModel
import com.example.ui.components.SolomonCircleCanvas
import com.example.ui.theme.EmberAmber
import com.example.ui.theme.IfritCrimson
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianVoid
import com.example.ui.theme.ParchmentLight
import com.example.ui.theme.ParchmentMuted
import com.example.ui.theme.SmokelessFlameBlue
import com.example.ui.theme.SolomonGold
import com.example.ui.theme.SolomonGoldBright

@Composable
fun SummoningChamberScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val ritualState by viewModel.ritualState.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianVoid)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "SOLOMONIC SUMMONING CIRCLE",
                style = MaterialTheme.typography.displaySmall,
                color = SolomonGoldBright,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Trace the sacred alchemical nodes, offer consecrated incense, and chant the ancient invocation.",
                style = MaterialTheme.typography.bodyMedium,
                color = ParchmentMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // The Interactive Canvas Circle
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SolomonCircleCanvas(
                        activeNodes = ritualState.activeNodes,
                        onNodeTapped = { nodeId -> viewModel.toggleRitualNode(nodeId) },
                        circleThemeColor = Color(ritualState.selectedIncense.smokeColor)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Node status bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = "Active Nodes",
                                tint = if (ritualState.activeNodes.size == 6) SolomonGoldBright else SmokelessFlameBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Alignment: ${ritualState.activeNodes.size} / 6 Nodes",
                                style = MaterialTheme.typography.labelLarge,
                                color = ParchmentLight,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row {
                            OutlinedButton(
                                onClick = { viewModel.activateAllNodes() },
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .testTag("auto_align_nodes_button"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = SolomonGold
                                )
                            ) {
                                Text("Complete Seal", fontSize = 11.sp)
                            }

                            IconButton(
                                onClick = { viewModel.resetRitualCircle() },
                                modifier = Modifier.testTag("reset_circle_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset Nodes",
                                    tint = ParchmentMuted
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step 1: Incense Offering Selection
            RitualSectionHeader(
                stepNumber = "I",
                title = "CONSECRATED INCENSE OFFERING",
                description = "The element of the offering calls upon a specific clan of smokeless fire."
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.incenseOfferings.forEach { incense ->
                    val isSelected = ritualState.selectedIncense.id == incense.id
                    val smokeColor = Color(incense.smokeColor)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectIncense(incense) }
                            .testTag("incense_item_${incense.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ObsidianCard else Color(0xFF0D0F18)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) smokeColor else ObsidianCardBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(smokeColor.copy(alpha = 0.25f))
                                    .border(1.dp, smokeColor, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🜍",
                                    color = smokeColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = incense.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (isSelected) SolomonGoldBright else ParchmentLight,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${incense.element} • ${incense.origin}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = smokeColor
                                )
                                Text(
                                    text = incense.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentMuted,
                                    maxLines = 2
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = SolomonGoldBright,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step 2: Sacred Invocations
            RitualSectionHeader(
                stepNumber = "II",
                title = "SACRED ARABIAN INVOCATION",
                description = "Speak the words of power engraved upon the ring of Solomon."
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.sacredChants.forEach { chant ->
                    val isSelected = ritualState.selectedChant.id == chant.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectChant(chant) }
                            .testTag("chant_item_${chant.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ObsidianCard else Color(0xFF0D0F18)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) SolomonGold else ObsidianCardBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = chant.powerLevel,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SolomonGold,
                                    fontWeight = FontWeight.Bold
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected Chant",
                                        tint = SolomonGoldBright,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "“${chant.arabicPhonetic}”",
                                style = MaterialTheme.typography.titleMedium,
                                color = ParchmentLight,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = chant.englishMeaning,
                                style = MaterialTheme.typography.bodySmall,
                                color = ParchmentMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Step 3: Summoning Crucible Action Button
            if (ritualState.isSummoningInProgress) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SUMMONING IN PROGRESS... BREAKING THE VEIL",
                        style = MaterialTheme.typography.labelLarge,
                        color = SolomonGoldBright,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { ritualState.summoningProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = SmokelessFlameBlue,
                        trackColor = ObsidianCardBorder
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.performSummoning() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("summon_jinn_action_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SolomonGold,
                        contentColor = ObsidianVoid
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Flare,
                        contentDescription = "Summon Jinn",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "IGNITE CRUCIBLE & SUMMON JINN",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // Fullscreen flash effect when veil breaks
        AnimatedVisibility(
            visible = ritualState.showSummonFlash,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.85f))
            )
        }
    }
}

@Composable
private fun RitualSectionHeader(
    stepNumber: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = SolomonGold.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, SolomonGold)
        ) {
            Text(
                text = stepNumber,
                color = SolomonGoldBright,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = SolomonGoldBright,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentMuted
            )
        }
    }
}
