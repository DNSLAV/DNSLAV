package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.JinnDialogueResponse
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.theme.DarkOverlay
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JinnAudienceScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val audienceState by viewModel.audienceState.collectAsState()
    val jinn = audienceState.jinn

    var messageInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(audienceState.chatMessages.size) {
        if (audienceState.chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(audienceState.chatMessages.size - 1)
        }
    }

    if (jinn == null) {
        // Empty state if no jinn is summoned yet
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(ObsidianVoid)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "۞",
                    fontSize = 64.sp,
                    color = SolomonGold.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "NO JINN MANIFESTED",
                    style = MaterialTheme.typography.titleLarge,
                    color = SolomonGoldBright,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The crucible is cold. Return to the Solomonic Circle to light the sacred incense and summon a spirit of smokeless fire.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParchmentMuted,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.navigateTo(AppScreen.SUMMONING) },
                    colors = ButtonDefaults.buttonColors(containerColor = SolomonGold, contentColor = ObsidianVoid)
                ) {
                    Text("Enter Summoning Chamber")
                }
            }
        }
        return
    }

    val clanColor = Color(jinn.clan.primaryColorHex)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianVoid)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Hero Manifestation Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, clanColor.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Generated Hero Art Thumbnail
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(1.5.dp, SolomonGold, RoundedCornerShape(14.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_jinn_manifestation),
                            contentDescription = jinn.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = jinn.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = SolomonGoldBright,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = jinn.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = clanColor,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Clan of the ${jinn.clan.title} • ${jinn.clan.element}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentMuted
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Affinity indicator
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Affinity",
                                tint = SolomonGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Solomonic Bond: ${jinn.affinity}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = SolomonGold
                            )
                        }
                    }

                    // Wish button
                    Button(
                        onClick = { viewModel.openWishModal() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SolomonGold,
                            contentColor = ObsidianVoid
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("open_wish_modal_button")
                    ) {
                        Text("Make Wish", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Quick Prompt Chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val suggestions = listOf(
                    "Tell me of King Solomon",
                    "What price does fire demand?",
                    "Reveal the secret of your clan",
                    "Give me an omen of my fortune"
                )
                suggestions.forEach { suggestion ->
                    SuggestionChip(
                        onClick = { viewModel.sendChatMessage(suggestion) },
                        label = { Text(suggestion, fontSize = 11.sp, color = ParchmentLight) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = ObsidianSurface
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = ObsidianCardBorder
                        )
                    )
                }
            }

            // Chat Messages Stream
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(audienceState.chatMessages, key = { it.id }) { message ->
                    ChatMessageBubble(
                        message = message,
                        jinnName = jinn.name,
                        clanColor = clanColor
                    )
                }

                if (audienceState.isThinking) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = SmokelessFlameBlue,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${jinn.name} is consulting the smokeless flame...",
                                style = MaterialTheme.typography.bodySmall,
                                color = SmokelessFlameBlue,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }

            // Chat Input Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ObsidianSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianCardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { messageInput = it },
                        placeholder = { Text("Speak across the veil...", color = ParchmentMuted, fontSize = 14.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("jinn_chat_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SolomonGold,
                            unfocusedBorderColor = ObsidianCardBorder,
                            focusedTextColor = ParchmentLight,
                            unfocusedTextColor = ParchmentLight,
                            focusedContainerColor = ObsidianCard,
                            unfocusedContainerColor = ObsidianCard
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (messageInput.isNotBlank()) {
                                viewModel.sendChatMessage(messageInput)
                                messageInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(SolomonGold)
                            .testTag("send_chat_message_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send Message",
                            tint = ObsidianVoid
                        )
                    }
                }
            }
        }

        // Make Wish Modal Dialog
        if (audienceState.isWishModalOpen) {
            AlertDialog(
                onDismissRequest = { viewModel.closeWishModal() },
                modifier = Modifier.fillMaxWidth(0.95f),
                containerColor = ObsidianCard,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SEAL A SACRED WISH",
                            style = MaterialTheme.typography.titleMedium,
                            color = SolomonGoldBright,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { viewModel.closeWishModal() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = ParchmentMuted
                            )
                        }
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "State your desire to ${jinn.name}. Every boon carries an inviolable cosmic taboo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = ParchmentMuted
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = audienceState.wishInput,
                            onValueChange = { viewModel.updateWishInput(it) },
                            placeholder = { Text("e.g. Unshakable wisdom to conquer my adversaries...", color = ParchmentMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("wish_text_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SolomonGold,
                                unfocusedBorderColor = ObsidianCardBorder,
                                focusedTextColor = ParchmentLight,
                                unfocusedTextColor = ParchmentLight,
                                focusedContainerColor = ObsidianSurface,
                                unfocusedContainerColor = ObsidianSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (audienceState.evaluatingWish) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = SolomonGold
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Consulting the celestial scales...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SolomonGold
                                )
                            }
                        } else {
                            val eval = audienceState.evaluatedWish
                            if (eval != null) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF0F111A), RoundedCornerShape(12.dp))
                                        .border(1.dp, SolomonGold.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "THE JINN'S DECREE",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SmokelessFlameBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = eval.speech,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = ParchmentLight,
                                        fontStyle = FontStyle.Italic
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = "Price",
                                            tint = IfritCrimson,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "COSMIC PRICE / TABOO:",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = IfritCrimson,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = eval.cosmicPrice ?: "Never break your oath.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFFFCCBC)
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    if (audienceState.evaluatedWish == null) {
                        Button(
                            onClick = { viewModel.evaluateWish() },
                            enabled = audienceState.wishInput.isNotBlank() && !audienceState.evaluatingWish,
                            colors = ButtonDefaults.buttonColors(containerColor = SmokelessFlameBlue, contentColor = ObsidianVoid),
                            modifier = Modifier.testTag("evaluate_wish_button")
                        ) {
                            Text("Consult Price", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.confirmSealPact() },
                            colors = ButtonDefaults.buttonColors(containerColor = SolomonGold, contentColor = ObsidianVoid),
                            modifier = Modifier.testTag("confirm_seal_pact_button")
                        ) {
                            Text("Stamp Solomon's Wax Seal", fontWeight = FontWeight.Bold)
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.closeWishModal() }) {
                        Text("Dismiss", color = ParchmentMuted)
                    }
                }
            )
        }
    }
}

@Composable
private fun ChatMessageBubble(
    message: ChatMessage,
    jinnName: String,
    clanColor: Color
) {
    val isJinn = message.isFromJinn

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isJinn) Arrangement.Start else Arrangement.End
    ) {
        if (isJinn) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(clanColor.copy(alpha = 0.2f))
                    .border(1.dp, clanColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "۞",
                    color = clanColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = RoundedCornerShape(
                topStart = if (isJinn) 4.dp else 16.dp,
                topEnd = if (isJinn) 16.dp else 4.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = if (isJinn) ObsidianCard else SolomonGold.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = if (isJinn) clanColor.copy(alpha = 0.4f) else SolomonGold
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (isJinn) {
                    Text(
                        text = jinnName,
                        style = MaterialTheme.typography.labelSmall,
                        color = SolomonGold,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ParchmentLight
                )
            }
        }
    }
}
