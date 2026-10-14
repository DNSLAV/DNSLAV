package com.example.data.repository

import android.content.Context
import com.example.data.local.JinnDao
import com.example.data.local.JinnDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.IncenseOffering
import com.example.data.model.JinnClan
import com.example.data.model.JinnDialogueResponse
import com.example.data.model.JinnSpirit
import com.example.data.model.PactEntity
import com.example.data.model.SacredChant
import com.example.data.model.SummonedJinnEntity
import com.example.data.remote.JinnAiService
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class JinnRepository(context: Context) {

    private val jinnDao: JinnDao = JinnDatabase.getDatabase(context).jinnDao()
    private val aiService = JinnAiService()

    val allSummonedJinns: Flow<List<SummonedJinnEntity>> = jinnDao.getAllSummonedJinns()
    val allPacts: Flow<List<PactEntity>> = jinnDao.getAllPacts()

    val incenseOfferings = listOf(
        IncenseOffering(
            id = "oud",
            name = "Oud al-Qamar (Celestial Agarwood)",
            origin = "Ancient Yemen & Dhofar",
            element = "Cosmic Abyssal Water",
            smokeColor = 0xFF00E5FF,
            description = "Aged dark wood steeped in night dew. Resonates with ancient Marid titans of the deep ocean."
        ),
        IncenseOffering(
            id = "dragons_blood",
            name = "Dragon's Blood & Red Sulfur",
            origin = "Socotra Archipelago",
            element = "Smokeless Volcanic Flame",
            smokeColor = 0xFFFF3D00,
            description = "Resin of the cinnabar tree. Ignites the slumbering fury of Ifrit lords."
        ),
        IncenseOffering(
            id = "frankincense",
            name = "Royal Frankincense & Sandal",
            origin = "Hadhramaut Valley",
            element = "Desert Gale & Shifting Mirage",
            smokeColor = 0xFF00E676,
            description = "Crisp, fragrant milky tears. Binds the wandering Jann nomads of the desert winds."
        ),
        IncenseOffering(
            id = "black_myrrh",
            name = "Black Myrrh & Wormwood",
            origin = "Ruins of Petra & Tadmur",
            element = "Subterranean Obsidian Earth",
            smokeColor = 0xFFAB47BC,
            description = "Bitter resin harvested under a new moon. Draws the earthen Ghul keepers of tombs and bullion."
        ),
        IncenseOffering(
            id = "damascus_rose",
            name = "Damascus Rose & Night Jasmine",
            origin = "Gardens of the Levant",
            element = "Ethereal Illusion & Oneiric Mist",
            smokeColor = 0xFFFFD54F,
            description = "Intoxicating sweet blooms. Attracts the prophetic Si'lat enchantresses of dreams."
        )
    )

    val sacredChants = listOf(
        SacredChant(
            id = "chant_suleyman",
            arabicPhonetic = "Bi-Haqqi Khatam Suleyman ibn Dawud, ajib ya Khaddam!",
            englishMeaning = "By the Seal of Solomon, son of David, answer, O Servant of the Unseen!",
            powerLevel = "Imperial Seal (Universal)",
            targetClan = null
        ),
        SacredChant(
            id = "chant_ifrit",
            arabicPhonetic = "Ya Ashab al-Nar al-Hadiqa, bighayr dukhan, ahdur!",
            englishMeaning = "O Dweller of the smokeless raging fire, manifest before the circle!",
            powerLevel = "Volcanic Surge",
            targetClan = JinnClan.IFRIT
        ),
        SacredChant(
            id = "chant_marid",
            arabicPhonetic = "Bi-Haqqi Bahr al-Zulumat wa A'maq al-Samawat, isma' wa at'i!",
            englishMeaning = "By the Sea of Darkness and Cosmic Depths, hear and obey the binding covenant!",
            powerLevel = "Abyssal Command",
            targetClan = JinnClan.MARID
        ),
        SacredChant(
            id = "chant_jann",
            arabicPhonetic = "Ya Sayyid al-Rih wa Zawba'at al-Ramal, unshur sirrak!",
            englishMeaning = "O Lord of the shifting gale and sand whirlpool, unveil thy hidden whisper!",
            powerLevel = "Gale Weave",
            targetClan = JinnClan.JANN
        ),
        SacredChant(
            id = "chant_silat",
            arabicPhonetic = "Bi-Sirr al-Manam wa Kashf al-Ghayb, ya Sayyidat al-Kashf!",
            englishMeaning = "By the mystery of dreams and the veil of unseen omens, speak the prophecy!",
            powerLevel = "Dream Veil",
            targetClan = JinnClan.SILAT
        )
    )

    private val spiritCatalog = listOf(
        JinnSpirit(
            id = "suraym",
            name = "Suraym al-Muhtariq",
            clan = JinnClan.IFRIT,
            title = "Commander of the Red Embers",
            elementalDescription = "A towering column of roaring crimson flame crowned with horns of molten brass and eyes of blinding ruby.",
            introductoryQuote = "Who dares invoke the Seal of Solomon to bind Suraym? Speak your desire quickly, mortal, before my heat scorches the air in your lungs!",
            personalityTone = "Thundering, proud, impatient, martial, commanding",
            affinity = 88
        ),
        JinnSpirit(
            id = "qaf_al_azraq",
            name = "Qaf al-Azraq",
            clan = JinnClan.MARID,
            title = "Elder of the Abyssal Current",
            elementalDescription = "A monumental silhouette of deep midnight azure, wrapped in currents of cosmic water and jewels torn from sunken empires.",
            introductoryQuote = "A thousand dynasties of dust have washed into my sea since last I heard this chant. You stand within the circle; state your pact with reverence.",
            personalityTone = "Ancient, regal, unfathomable, solemn, deliberate",
            affinity = 94
        ),
        JinnSpirit(
            id = "maymun",
            name = "Maymun al-Khattif",
            clan = JinnClan.JANN,
            title = "Nomad of the Mirage Gales",
            elementalDescription = "A lithe whirlwind of emerald sand and shifting shadows, whose voice whistles like the sirocco across barren crags.",
            introductoryQuote = "I was riding the northern whirlwind across the dunes of Rub' al-Khali! You have caught the wind with Solomon's rope. What shall we play for, mortal?",
            personalityTone = "Swift, playful, inquisitive, wandering, sharp",
            affinity = 82
        ),
        JinnSpirit(
            id = "shuq",
            name = "Shuq al-Qaboor",
            clan = JinnClan.GHUL,
            title = "Keeper of the Forgotten Bullion",
            elementalDescription = "Cloaked in obsidian dust and antique amulets of bronze, with eyes that glimmer like buried pearls in dark soil.",
            introductoryQuote = "The soil trembles where I walk. I know every chest of gold buried by terrified kings before their fortresses fell. What wealth do you unearth?",
            personalityTone = "Quiet, subterranean, covetous of secrets, unhurried",
            affinity = 79
        ),
        JinnSpirit(
            id = "waswas",
            name = "Waswas al-Zujaj",
            clan = JinnClan.SHAITAN,
            title = "Architect of the Dark Mirror",
            elementalDescription = "A fractured humanoid figure made of dark smoked glass, refracting illusions of grandeur and whispers of cunning victories.",
            introductoryQuote = "Every mortal desires that which is forbidden. Do not look away; I see the ambition coiled in your chest. Let us draft a masterpiece of a bargain.",
            personalityTone = "Silken, persuasive, cunning, razor-witted, calculated",
            affinity = 75
        ),
        JinnSpirit(
            id = "umm_al_duha",
            name = "Umm al-Duha",
            clan = JinnClan.SILAT,
            title = "Weaver of Prophetic Mirages",
            elementalDescription = "Shrouded in veil upon veil of golden light and starlit silk, holding a spinning distaff of incandescent destiny.",
            introductoryQuote = "I saw you in the sleep of kings three nights past. The threads of your fate knot and unwind. What mystery of the morrow shall I unseal for you?",
            personalityTone = "Mystical, poetic, enigmatic, compassionate yet piercing",
            affinity = 91
        )
    )

    fun summonJinn(incenseId: String, chantId: String, nodesCount: Int): JinnSpirit {
        val selectedIncense = incenseOfferings.find { it.id == incenseId } ?: incenseOfferings.first()
        val selectedChant = sacredChants.find { it.id == chantId }

        // Select the most resonant spirit based on ritual variables
        val matchedSpirit = when {
            selectedChant?.targetClan != null -> {
                spiritCatalog.find { it.clan == selectedChant.targetClan } ?: spiritCatalog.first()
            }
            selectedIncense.id == "dragons_blood" -> spiritCatalog.find { it.clan == JinnClan.IFRIT }!!
            selectedIncense.id == "oud" -> spiritCatalog.find { it.clan == JinnClan.MARID }!!
            selectedIncense.id == "frankincense" -> spiritCatalog.find { it.clan == JinnClan.JANN }!!
            selectedIncense.id == "black_myrrh" -> spiritCatalog.find { it.clan == JinnClan.GHUL }!!
            selectedIncense.id == "damascus_rose" -> spiritCatalog.find { it.clan == JinnClan.SILAT }!!
            else -> spiritCatalog[(nodesCount + incenseId.hashCode().coerceAtLeast(0)) % spiritCatalog.size]
        }

        return matchedSpirit
    }

    suspend fun recordSummoning(jinn: JinnSpirit, incenseName: String) {
        val entity = SummonedJinnEntity(
            id = UUID.randomUUID().toString(),
            name = jinn.name,
            clan = jinn.clan.title,
            element = jinn.clan.element,
            title = jinn.title,
            affinityLevel = jinn.affinity,
            summonedAt = System.currentTimeMillis(),
            temperament = jinn.personalityTone,
            incenseOffered = incenseName,
            lastWords = jinn.introductoryQuote
        )
        jinnDao.insertSummonedJinn(entity)
    }

    suspend fun sealPact(
        jinn: JinnSpirit,
        wish: String,
        boon: String,
        cosmicPrice: String
    ): PactEntity {
        val pact = PactEntity(
            id = UUID.randomUUID().toString(),
            jinnId = jinn.id,
            jinnName = jinn.name,
            jinnClan = jinn.clan.title,
            userWish = wish,
            jinnBoon = boon,
            cosmicPrice = cosmicPrice,
            status = "ACTIVE",
            createdAt = System.currentTimeMillis(),
            sealColorHex = jinn.clan.primaryColorHex
        )
        jinnDao.insertPact(pact)
        return pact
    }

    suspend fun updatePactStatus(pact: PactEntity, newStatus: String) {
        jinnDao.updatePact(pact.copy(status = newStatus))
    }

    suspend fun consultJinn(
        jinn: JinnSpirit,
        userPrompt: String,
        isWish: Boolean
    ): JinnDialogueResponse {
        return aiService.consultJinn(jinn, userPrompt, isWish)
    }

    fun getDailyOmen(): DailyOmen {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val omens = listOf(
            DailyOmen(
                title = "Alignment of the Red Cinder",
                clanAffinity = JinnClan.IFRIT,
                auspiciousHour = "Midnight — 03:00",
                planetaryInfluence = "Mars in Scorpio",
                counsel = "Smokeless fire favors bold endeavors and assertive speech today. Hesitation invites stagnation. Light cinnamon or clove to seal your focus.",
                talismanElement = "Fire"
            ),
            DailyOmen(
                title = "Ascendance of the Abyssal Tide",
                clanAffinity = JinnClan.MARID,
                auspiciousHour = "04:30 — Dawn",
                planetaryInfluence = "Saturn in Pisces",
                counsel = "Ancient wisdom surfaces from forgotten depths. A delayed resolution will resolve in your favor if you maintain silence before noon.",
                talismanElement = "Water"
            ),
            DailyOmen(
                title = "Whirlwind of the Nomadic Sands",
                clanAffinity = JinnClan.JANN,
                auspiciousHour = "Noon — 14:00",
                planetaryInfluence = "Mercury Direct",
                counsel = "Expect unexpected messengers and rapid shifts in travel or communications. What is lost today shall return in an altered form.",
                talismanElement = "Air"
            ),
            DailyOmen(
                title = "Shadow of the Obsidian Vault",
                clanAffinity = JinnClan.GHUL,
                auspiciousHour = "Dusk — 20:00",
                planetaryInfluence = "Moon in Taurus",
                counsel = "A secret concerning wealth or buried obligations requires discretion. Stand firm on solid ground; avoid rash investments.",
                talismanElement = "Earth"
            ),
            DailyOmen(
                title = "The Weaver's Oneiric Mirror",
                clanAffinity = JinnClan.SILAT,
                auspiciousHour = "Twilight — 22:00",
                planetaryInfluence = "Venus in Cancer",
                counsel = "Dreams tonight shall carry symbolic prophecies of personal relationships. Trust your intuitive instincts above cold logic.",
                talismanElement = "Spirit"
            )
        )
        return omens[dayOfYear % omens.size]
    }
}

data class DailyOmen(
    val title: String,
    val clanAffinity: JinnClan,
    val auspiciousHour: String,
    val planetaryInfluence: String,
    val counsel: String,
    val talismanElement: String
)
