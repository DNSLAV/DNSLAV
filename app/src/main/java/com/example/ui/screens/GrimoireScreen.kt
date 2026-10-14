package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.model.JinnClan
import com.example.data.model.SummonedJinnEntity
import com.example.ui.MainViewModel
import com.example.ui.components.PactScrollCard
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun GrimoireScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val grimoireTab by viewModel.grimoireTab.collectAsState()
    val allPacts by viewModel.allPacts.collectAsState()
    val allSummonedJinns by viewModel.allSummonedJinns.collectAsState()
    val selectedClanDetail by viewModel.selectedClanDetail.collectAsState()

    val tabTitles = listOf("Clans of Fire", "Sealed Pacts (${allPacts.size})", "Summoned Log")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianVoid)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GRIMOIRE OF THE UNSEEN",
                style = MaterialTheme.typography.displaySmall,
                color = SolomonGoldBright,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "Ancient codex of the elemental spirits, pact ledgers, and ritual annals.",
                style = MaterialTheme.typography.bodySmall,
                color = ParchmentMuted,
                textAlign = TextAlign.Center
            )
        }

        // Custom TabRow
        TabRow(
            selectedTabIndex = grimoireTab,
            containerColor = ObsidianSurface,
            contentColor = SolomonGold,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[grimoireTab]),
                    color = SolomonGold,
                    height = 3.dp
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = grimoireTab == index,
                    onClick = { viewModel.setGrimoireTab(index) },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (grimoireTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (grimoireTab == index) SolomonGoldBright else ParchmentMuted
                        )
                    },
                    modifier = Modifier.testTag("grimoire_tab_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Content
        when (grimoireTab) {
            0 -> ClansCodexContent(
                selectedClan = selectedClanDetail,
                onSelectClan = { clan ->
                    if (selectedClanDetail == clan) viewModel.setSelectedClanDetail(null)
                    else viewModel.setSelectedClanDetail(clan)
                }
            )
            1 -> PactsLedgerContent(
                pacts = allPacts,
                onToggleStatus = { pact, newStatus -> viewModel.togglePactStatus(pact, newStatus) }
            )
            2 -> SummonedLogContent(jinns = allSummonedJinns)
        }
    }
}

@Composable
private fun ClansCodexContent(
    selectedClan: JinnClan?,
    onSelectClan: (JinnClan) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(JinnClan.values()) { clan ->
            val isExpanded = selectedClan == clan
            val clanColor = Color(clan.primaryColorHex)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectClan(clan) }
                    .testTag("clan_card_${clan.name}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isExpanded) clanColor else ObsidianCardBorder
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(clanColor.copy(alpha = 0.2f))
                                .border(1.5.dp, clanColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🜂",
                                color = clanColor,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = clan.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = SolomonGoldBright,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = clan.element,
                                style = MaterialTheme.typography.bodySmall,
                                color = clanColor
                            )
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            tint = ParchmentMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = clan.nature,
                        style = MaterialTheme.typography.bodySmall,
                        color = ParchmentMuted
                    )

                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(ObsidianCardBorder)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "ANCIENT MYTHOS & ORIGIN",
                                style = MaterialTheme.typography.labelSmall,
                                color = SolomonGold,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = clan.lore,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ParchmentLight
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row {
                                Text(
                                    text = "Planetary Hour: ",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SmokelessFlameBlue,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = clan.planetaryHour,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentLight
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row {
                                Text(
                                    text = "Consecrated Incense: ",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SmokelessFlameBlue,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = clan.preferredIncense,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ParchmentLight
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Warning card
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF261217), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Warning",
                                    tint = IfritCrimson,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = clan.warning,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFFFCCBC),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PactsLedgerContent(
    pacts: List<com.example.data.model.PactEntity>,
    onToggleStatus: (com.example.data.model.PactEntity, String) -> Unit
) {
    if (pacts.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "📜", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "NO PACTS CURRENTLY SEALED",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolomonGoldBright,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Summon a spirit in the ritual chamber and submit your wish to bind a cosmic pact with Solomon's wax seal.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParchmentMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pacts, key = { it.id }) { pact ->
            PactScrollCard(
                pact = pact,
                onToggleStatus = onToggleStatus
            )
        }
    }
}

@Composable
private fun SummonedLogContent(jinns: List<SummonedJinnEntity>) {
    if (jinns.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "🕯️", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "THE CHRONICLES ARE EMPTY",
                    style = MaterialTheme.typography.titleMedium,
                    color = SolomonGoldBright,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "No entity has yet been bound through the crucible. Ignite your first incense in the Summoning Chamber.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParchmentMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

    val dateFormat = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(jinns, key = { it.id }) { jinn ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("summoned_history_${jinn.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SolomonGold.copy(alpha = 0.15f))
                            .border(1.dp, SolomonGold, CircleShape),
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

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = jinn.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = SolomonGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${jinn.clan} • ${jinn.title}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SmokelessFlameBlue
                        )
                        Text(
                            text = "Offering: ${jinn.incenseOffered}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentMuted
                        )
                        Text(
                            text = dateFormat.format(Date(jinn.summonedAt)),
                            style = MaterialTheme.typography.labelSmall,
                            color = ParchmentMuted
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${jinn.affinityLevel}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = SolomonGold,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Affinity",
                            style = MaterialTheme.typography.labelSmall,
                            color = ParchmentMuted
                        )
                    }
                }
            }
        }
    }
}
