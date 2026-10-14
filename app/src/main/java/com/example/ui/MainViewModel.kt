package com.example.ui

import android.app.Application
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.IncenseOffering
import com.example.data.model.JinnClan
import com.example.data.model.JinnDialogueResponse
import com.example.data.model.JinnSpirit
import com.example.data.model.PactEntity
import com.example.data.model.SacredChant
import com.example.data.model.SummonedJinnEntity
import com.example.data.repository.DailyOmen
import com.example.data.repository.JinnRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen(val title: String) {
    SUMMONING("Ritual Circle"),
    AUDIENCE("Jinn Audience"),
    GRIMOIRE("Grimoire"),
    DIVINATION("Celestial Omen")
}

data class RitualState(
    val selectedIncense: IncenseOffering,
    val selectedChant: SacredChant,
    val activeNodes: Set<Int> = emptySet(),
    val isSummoningInProgress: Boolean = false,
    val summoningProgress: Float = 0f,
    val showSummonFlash: Boolean = false
)

data class AudienceState(
    val jinn: JinnSpirit? = null,
    val chatMessages: List<ChatMessage> = emptyList(),
    val isThinking: Boolean = false,
    val isWishModalOpen: Boolean = false,
    val wishInput: String = "",
    val evaluatingWish: Boolean = false,
    val evaluatedWish: JinnDialogueResponse? = null,
    val pactSealedRecently: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = JinnRepository(application)
    private val vibrator = application.getSystemService(Vibrator::class.java)

    private val _currentScreen = MutableStateFlow(AppScreen.SUMMONING)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    val incenseOfferings: List<IncenseOffering> = repository.incenseOfferings
    val sacredChants: List<SacredChant> = repository.sacredChants

    private val _ritualState = MutableStateFlow(
        RitualState(
            selectedIncense = incenseOfferings.first(),
            selectedChant = sacredChants.first()
        )
    )
    val ritualState: StateFlow<RitualState> = _ritualState.asStateFlow()

    private val _audienceState = MutableStateFlow(AudienceState())
    val audienceState: StateFlow<AudienceState> = _audienceState.asStateFlow()

    val allSummonedJinns: StateFlow<List<SummonedJinnEntity>> = repository.allSummonedJinns
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allPacts: StateFlow<List<PactEntity>> = repository.allPacts
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val dailyOmen: DailyOmen = repository.getDailyOmen()

    private val _grimoireTab = MutableStateFlow(0)
    val grimoireTab: StateFlow<Int> = _grimoireTab.asStateFlow()

    private val _selectedClanDetail = MutableStateFlow<JinnClan?>(null)
    val selectedClanDetail: StateFlow<JinnClan?> = _selectedClanDetail.asStateFlow()

    private val _quizAffinityResult = MutableStateFlow<JinnClan?>(null)
    val quizAffinityResult: StateFlow<JinnClan?> = _quizAffinityResult.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectIncense(incense: IncenseOffering) {
        _ritualState.value = _ritualState.value.copy(selectedIncense = incense)
    }

    fun selectChant(chant: SacredChant) {
        _ritualState.value = _ritualState.value.copy(selectedChant = chant)
    }

    fun toggleRitualNode(nodeId: Int) {
        val current = _ritualState.value.activeNodes.toMutableSet()
        if (current.contains(nodeId)) {
            current.remove(nodeId)
        } else {
            current.add(nodeId)
            triggerHapticFeedback(50)
        }
        _ritualState.value = _ritualState.value.copy(activeNodes = current)
    }

    fun activateAllNodes() {
        _ritualState.value = _ritualState.value.copy(activeNodes = setOf(1, 2, 3, 4, 5, 6))
        triggerHapticFeedback(80)
    }

    fun resetRitualCircle() {
        _ritualState.value = _ritualState.value.copy(
            activeNodes = emptySet(),
            summoningProgress = 0f,
            isSummoningInProgress = false
        )
    }

    fun performSummoning() {
        val ritual = _ritualState.value
        viewModelScope.launch {
            _ritualState.value = ritual.copy(isSummoningInProgress = true)

            // Simulate ritual invocation surge with vibration pulses
            for (step in 1..10) {
                _ritualState.value = _ritualState.value.copy(summoningProgress = step / 10f)
                triggerHapticFeedback((step * 15).toLong())
                delay(120)
            }

            // Burst of celestial light
            _ritualState.value = _ritualState.value.copy(showSummonFlash = true)
            triggerHapticFeedback(250)

            val summonedJinn = repository.summonJinn(
                incenseId = ritual.selectedIncense.id,
                chantId = ritual.selectedChant.id,
                nodesCount = ritual.activeNodes.size
            )

            // Save to Room DB
            repository.recordSummoning(summonedJinn, ritual.selectedIncense.name)

            // Setup audience
            _audienceState.value = AudienceState(
                jinn = summonedJinn,
                chatMessages = listOf(
                    ChatMessage(
                        id = UUID.randomUUID().toString(),
                        isFromJinn = true,
                        text = summonedJinn.introductoryQuote,
                        timestamp = System.currentTimeMillis()
                    )
                )
            )

            delay(300)
            _ritualState.value = _ritualState.value.copy(
                isSummoningInProgress = false,
                showSummonFlash = false,
                summoningProgress = 0f
            )
            _currentScreen.value = AppScreen.AUDIENCE
        }
    }

    fun sendChatMessage(userText: String) {
        val jinn = _audienceState.value.jinn ?: return
        if (userText.isBlank()) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            isFromJinn = false,
            text = userText,
            timestamp = System.currentTimeMillis()
        )

        val updatedMessages = _audienceState.value.chatMessages + userMessage
        _audienceState.value = _audienceState.value.copy(
            chatMessages = updatedMessages,
            isThinking = true
        )

        viewModelScope.launch {
            val response = repository.consultJinn(
                jinn = jinn,
                userPrompt = userText,
                isWish = false
            )

            val jinnReply = ChatMessage(
                id = UUID.randomUUID().toString(),
                isFromJinn = true,
                text = response.speech,
                timestamp = System.currentTimeMillis()
            )

            _audienceState.value = _audienceState.value.copy(
                chatMessages = _audienceState.value.chatMessages + jinnReply,
                isThinking = false
            )
            triggerHapticFeedback(70)
        }
    }

    fun openWishModal() {
        _audienceState.value = _audienceState.value.copy(
            isWishModalOpen = true,
            evaluatedWish = null,
            wishInput = ""
        )
    }

    fun closeWishModal() {
        _audienceState.value = _audienceState.value.copy(
            isWishModalOpen = false,
            evaluatedWish = null,
            evaluatingWish = false
        )
    }

    fun updateWishInput(text: String) {
        _audienceState.value = _audienceState.value.copy(wishInput = text)
    }

    fun evaluateWish() {
        val jinn = _audienceState.value.jinn ?: return
        val wish = _audienceState.value.wishInput
        if (wish.isBlank()) return

        viewModelScope.launch {
            _audienceState.value = _audienceState.value.copy(evaluatingWish = true)
            val response = repository.consultJinn(
                jinn = jinn,
                userPrompt = wish,
                isWish = true
            )
            _audienceState.value = _audienceState.value.copy(
                evaluatingWish = false,
                evaluatedWish = response
            )
            triggerHapticFeedback(120)
        }
    }

    fun confirmSealPact() {
        val jinn = _audienceState.value.jinn ?: return
        val wish = _audienceState.value.wishInput
        val eval = _audienceState.value.evaluatedWish ?: return

        viewModelScope.launch {
            repository.sealPact(
                jinn = jinn,
                wish = wish,
                boon = eval.boonGranted ?: "Granted by Solomonic Decree.",
                cosmicPrice = eval.cosmicPrice ?: "Never recount this covenant before midnight."
            )

            triggerHapticFeedback(300)
            _audienceState.value = _audienceState.value.copy(
                isWishModalOpen = false,
                pactSealedRecently = true,
                chatMessages = _audienceState.value.chatMessages + ChatMessage(
                    id = UUID.randomUUID().toString(),
                    isFromJinn = true,
                    text = "The wax is sealed. The cosmic decree is written upon the parchment of the unseen. Heed your taboo, mortal!",
                    timestamp = System.currentTimeMillis()
                )
            )

            delay(2500)
            _audienceState.value = _audienceState.value.copy(pactSealedRecently = false)
        }
    }

    fun togglePactStatus(pact: PactEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updatePactStatus(pact, newStatus)
            triggerHapticFeedback(50)
        }
    }

    fun setGrimoireTab(index: Int) {
        _grimoireTab.value = index
    }

    fun setSelectedClanDetail(clan: JinnClan?) {
        _selectedClanDetail.value = clan
    }

    fun calculateAffinity(answers: List<Int>) {
        val clans = JinnClan.values()
        val sum = answers.sum()
        val index = (sum + 1) % clans.size
        _quizAffinityResult.value = clans[index]
        triggerHapticFeedback(100)
    }

    fun resetAffinityQuiz() {
        _quizAffinityResult.value = null
    }

    private fun triggerHapticFeedback(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        durationMs,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {
            // Ignore if vibration is disabled or unavailable
        }
    }
}
