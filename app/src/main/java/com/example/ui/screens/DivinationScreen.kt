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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
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

data class QuizQuestion(
    val id: Int,
    val prompt: String,
    val options: List<String>
)

@Composable
fun DivinationScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val dailyOmen = viewModel.dailyOmen
    val quizAffinityResult by viewModel.quizAffinityResult.collectAsState()
    val scrollState = rememberScrollState()

    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }

    val questions = remember {
        listOf(
            QuizQuestion(
                id = 0,
                prompt = "When midnight falls upon the desert, where does your gaze wander?",
                options = listOf(
                    "Into the raging crimson heart of the firepit",
                    "Toward the silent, deep stars and cosmic dark",
                    "Along the shifting, restless crests of sand dunes",
                    "Downward into forgotten subterranean vaults"
                )
            ),
            QuizQuestion(
                id = 1,
                prompt = "If Solomon's seal ring were placed upon your hand, what would you command?",
                options = listOf(
                    "Martial victory and supreme authority over adversaries",
                    "The fathomless mysteries of eternity and ancient oceans",
                    "The swiftness of gales to travel untethered between realms",
                    "The buried gold and hidden knowledge of fallen dynasties"
                )
            ),
            QuizQuestion(
                id = 2,
                prompt = "Which virtue burns highest in your mortal vessel?",
                options = listOf(
                    "Unyielding courage in the face of peril",
                    "Profound patience that outlasts empires",
                    "Unshakable devotion to freedom and independence",
                    "Incisive intellect and strategic cunning"
                )
            ),
            QuizQuestion(
                id = 3,
                prompt = "Which sacred incense fragrance summons your focus?",
                options = listOf(
                    "Dragon's Blood resin and red sulfur",
                    "Aged dark agarwood and sea ambergris",
                    "Royal white frankincense and sandalwood",
                    "Damascus rose and nocturnal night jasmine"
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianVoid)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "CELESTIAL DIVINATION",
            style = MaterialTheme.typography.displaySmall,
            color = SolomonGoldBright,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "Omens of the seven planetary spheres and the spirit affinity rite.",
            style = MaterialTheme.typography.bodySmall,
            color = ParchmentMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Daily Omen Card
        val clanColor = Color(dailyOmen.clanAffinity.primaryColorHex)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("daily_omen_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .border(
                        width = 1.5.dp,
                        brush = Brush.verticalGradient(
                            listOf(clanColor.copy(alpha = 0.7f), ObsidianCardBorder)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = "Omen",
                            tint = clanColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DAILY OMENS OF FIRE",
                            style = MaterialTheme.typography.labelLarge,
                            color = SolomonGold,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = clanColor.copy(alpha = 0.18f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, clanColor)
                    ) {
                        Text(
                            text = dailyOmen.talismanElement,
                            style = MaterialTheme.typography.labelSmall,
                            color = clanColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = dailyOmen.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = SolomonGoldBright,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Clan in Ascendance: ${dailyOmen.clanAffinity.title} (${dailyOmen.clanAffinity.element})",
                    style = MaterialTheme.typography.bodySmall,
                    color = clanColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Astrological parameters
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F111A), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Hour",
                                tint = SmokelessFlameBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Auspicious Hour",
                                style = MaterialTheme.typography.labelSmall,
                                color = ParchmentMuted
                            )
                        }
                        Text(
                            text = dailyOmen.auspiciousHour,
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NightsStay,
                                contentDescription = "Planets",
                                tint = SolomonGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sphere Influence",
                                style = MaterialTheme.typography.labelSmall,
                                color = ParchmentMuted
                            )
                        }
                        Text(
                            text = dailyOmen.planetaryInfluence,
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "“${dailyOmen.counsel}”",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParchmentLight,
                    fontStyle = FontStyle.Italic
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Clan Affinity Questionnaire
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = ObsidianCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Spirit Affinity",
                        tint = SolomonGoldBright,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "SPIRIT AFFINITY RITE",
                            style = MaterialTheme.typography.titleMedium,
                            color = SolomonGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Discover which Clan of Jinn walks in your shadow.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (quizAffinityResult != null) {
                    // Result Card
                    val resultClan = quizAffinityResult!!
                    val resultColor = Color(resultClan.primaryColorHex)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0F111A), RoundedCornerShape(16.dp))
                            .border(1.5.dp, resultColor, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(resultColor.copy(alpha = 0.2f))
                                .border(2.dp, resultColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "۞",
                                color = resultColor,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "YOUR SPIRIT CLAN: ${resultClan.title.uppercase()}",
                            style = MaterialTheme.typography.titleLarge,
                            color = SolomonGoldBright,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = resultClan.element,
                            style = MaterialTheme.typography.bodyMedium,
                            color = resultColor,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = resultClan.nature,
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentLight,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    viewModel.navigateTo(AppScreen.SUMMONING)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SolomonGold,
                                    contentColor = ObsidianVoid
                                )
                            ) {
                                Text("Summon in Chamber", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.resetAffinityQuiz()
                                    selectedAnswers.clear()
                                }
                            ) {
                                Text("Retake Rite", color = ParchmentMuted)
                            }
                        }
                    }
                } else {
                    // Questions List
                    questions.forEach { question ->
                        Column(modifier = Modifier.padding(bottom = 16.dp)) {
                            Text(
                                text = "${question.id + 1}. ${question.prompt}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ParchmentLight,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            question.options.forEachIndexed { optIndex, optText ->
                                val isSelected = selectedAnswers[question.id] == optIndex

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) SolomonGold.copy(alpha = 0.2f)
                                            else Color(0xFF0E1019)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) SolomonGold else ObsidianCardBorder,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            selectedAnswers[question.id] = optIndex
                                        }
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) SolomonGold else Color.Transparent)
                                            .border(1.dp, if (isSelected) SolomonGold else ParchmentMuted, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Selected",
                                                tint = ObsidianVoid,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = optText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isSelected) SolomonGoldBright else ParchmentMuted
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val answersList = (0 until questions.size).map { selectedAnswers[it] ?: 0 }
                            viewModel.calculateAffinity(answersList)
                        },
                        enabled = selectedAnswers.size == questions.size,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("reveal_affinity_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SolomonGold,
                            contentColor = ObsidianVoid
                        )
                    ) {
                        Text(
                            text = "REVEAL MY SPIRIT CLAN",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}
