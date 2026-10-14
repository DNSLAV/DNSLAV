package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PactEntity
import com.example.ui.theme.EmberAmber
import com.example.ui.theme.IfritCrimson
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ParchmentLight
import com.example.ui.theme.ParchmentMuted
import com.example.ui.theme.ParchmentSubtle
import com.example.ui.theme.SmokelessFlameBlue
import com.example.ui.theme.SolomonGold
import com.example.ui.theme.SolomonGoldBright
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PactScrollCard(
    pact: PactEntity,
    onToggleStatus: (PactEntity, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sealColor = Color(pact.sealColorHex)
    val isFulfilled = pact.status == "FULFILLED"
    val isBroken = pact.status == "BROKEN"

    val statusColor by animateColorAsState(
        targetValue = when {
            isFulfilled -> Color(0xFF00E676)
            isBroken -> IfritCrimson
            else -> SolomonGold
        },
        label = "status_color"
    )

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dateString = dateFormat.format(Date(pact.createdAt))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("pact_card_${pact.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ObsidianCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        listOf(SolomonGold.copy(alpha = 0.6f), ObsidianCardBorder)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            // Header: Clan and Seal
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Wax Seal Sigil
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(sealColor, Color(0xFF3E1218))
                                )
                            )
                            .border(1.5.dp, SolomonGoldBright, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "۞",
                            color = SolomonGoldBright,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = pact.jinnName,
                            style = MaterialTheme.typography.titleMedium,
                            color = SolomonGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Clan of the ${pact.jinnClan} • $dateString",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentMuted
                        )
                    }
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = pact.status,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // The Mortal's Wish
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F111A), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "THE MORTAL'S COVETED WISH",
                    style = MaterialTheme.typography.labelMedium,
                    color = SolomonGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "“${pact.userWish}”",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ParchmentLight,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // The Jinn's Boon Granted
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF131724), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "THE GRANTED CELESTIAL BOON",
                    style = MaterialTheme.typography.labelMedium,
                    color = SmokelessFlameBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pact.jinnBoon,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParchmentLight
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // The Cosmic Price & Taboo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF261217), RoundedCornerShape(10.dp))
                    .border(1.dp, IfritCrimson.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Cosmic Price Warning",
                        tint = EmberAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "THE COSMIC TABOO & BINDING PRICE",
                        style = MaterialTheme.typography.labelMedium,
                        color = EmberAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pact.cosmicPrice,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFFFCCBC)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pact.status == "ACTIVE") {
                    TextButton(
                        onClick = { onToggleStatus(pact, "FULFILLED") },
                        modifier = Modifier.testTag("fulfill_pact_${pact.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Mark Fulfilled",
                            tint = Color(0xFF00E676),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Mark Fulfilled",
                            color = Color(0xFF00E676),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = { onToggleStatus(pact, "BROKEN") },
                        modifier = Modifier.testTag("break_pact_${pact.id}")
                    ) {
                        Text(
                            text = "Break Taboo",
                            color = IfritCrimson,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    TextButton(
                        onClick = { onToggleStatus(pact, "ACTIVE") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = "Reactivate",
                            tint = SolomonGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Reactivate",
                            color = SolomonGold,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}
