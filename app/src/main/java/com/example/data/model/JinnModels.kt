package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summoned_jinns")
data class SummonedJinnEntity(
    @PrimaryKey val id: String,
    val name: String,
    val clan: String,
    val element: String,
    val title: String,
    val affinityLevel: Int,
    val summonedAt: Long,
    val temperament: String,
    val incenseOffered: String,
    val lastWords: String
)

@Entity(tableName = "sealed_pacts")
data class PactEntity(
    @PrimaryKey val id: String,
    val jinnId: String,
    val jinnName: String,
    val jinnClan: String,
    val userWish: String,
    val jinnBoon: String,
    val cosmicPrice: String,
    val status: String, // "ACTIVE", "FULFILLED", "BROKEN"
    val createdAt: Long,
    val sealColorHex: Long
)

enum class JinnClan(
    val title: String,
    val element: String,
    val nature: String,
    val planetaryHour: String,
    val preferredIncense: String,
    val lore: String,
    val warning: String,
    val primaryColorHex: Long
) {
    IFRIT(
        title = "Ifrit",
        element = "Smokeless Volcanic Flame",
        nature = "Fierce, unyielding, masters of martial strength and molten fortune",
        planetaryHour = "Hour of Mars (Midnight to 3 AM)",
        preferredIncense = "Red Sulfur & Dragon's Blood",
        lore = "Forged from the pure core of smokeless fire. The Ifrit dwell in volcanic abysses and beneath red sands. They yield tremendous boons of power and wealth, yet their tempers flare like scorched brimstone.",
        warning = "Never show hesitation or deceit; an Ifrit senses cowardice and demands immediate tribute.",
        primaryColorHex = 0xFFFF3D00
    ),
    MARID(
        title = "Marid",
        element = "Cosmic Abyssal Water",
        nature = "Ancient, towering titans, regal, haughty and fathomless",
        planetaryHour = "Hour of Saturn (Twilight before dawn)",
        preferredIncense = "Oud al-Qamar & Black Ambergris",
        lore = "The most primordial among the spirits, older than memory. The Marid command oceanic storms, abyssal secrets, and the ebb and flow of empires. They bow only to the highest seals.",
        warning = "Speak to a Marid with absolute humility and formal titles, or their wrath will wash away all bargains.",
        primaryColorHex = 0xFF00E5FF
    ),
    JANN(
        title = "Jann",
        element = "Desert Gale & Shifting Mirage",
        nature = "Wanderers of the dunes, nomadic, swift and veiled",
        planetaryHour = "Hour of Mercury (Midday mirage)",
        preferredIncense = "White Frankincense & Sandalwood",
        lore = "The first tribe of spirits granted dominion over the open desert before humans walked the earth. Shapeshifters of whirlwinds, white gazelles, and silent winds.",
        warning = "Their promises are light as dust; secure their word with the Seal of Solomon thrice.",
        primaryColorHex = 0xFF00E676
    ),
    GHUL(
        title = "Ghul",
        element = "Subterranean Obsidian Earth",
        nature = "Nocturnal shapeshifters, guardians of forgotten tombs and buried bullion",
        planetaryHour = "Moonless Midnight",
        preferredIncense = "Black Myrrh & Dried Wormwood",
        lore = "Spirits tied to ancient necropolises and hidden vaults beneath forgotten ruins. Masters of bone, minerals, and buried knowledge thought lost to mortality.",
        warning = "Do not summon near places of resting, and keep a ring of consecrated iron between you.",
        primaryColorHex = 0xFFAB47BC
    ),
    SHAITAN(
        title = "Shaitan",
        element = "Shadow of Obsidian Mirrors",
        nature = "Cunning whisperers of forbidden intellect and paradoxical bargains",
        planetaryHour = "Solar Eclipse & Eclipse of the Moon",
        preferredIncense = "Black Musk & Charcoal Ash",
        lore = "Rebellious entities who dwell in the reflections of mirrors and the thoughts between waking and dreaming. They grant immense intellect and strategic supremacy, always with an insidious catch.",
        warning = "Examine every word of the pact; a Shaitan binds through omissions and double meanings.",
        primaryColorHex = 0xFFFF1744
    ),
    SILAT(
        title = "Si'lat",
        element = "Ethereal Illusion & Oneiric Mist",
        nature = "Enchantresses of prophetic visions, dreams, and hidden love",
        planetaryHour = "Hour of Venus (Dusk)",
        preferredIncense = "Damascus Rose & Royal Jasmine",
        lore = "Subtle sorceresses who travel between the waking realm and the world of sleep. They decipher prophetic omens, unravel hidden loyalties, and cast illusions that bewilder kings.",
        warning = "Do not gaze directly into their eyes for longer than seven breaths, lest your mind linger in their mirage.",
        primaryColorHex = 0xFFFFD54F
    )
}

data class IncenseOffering(
    val id: String,
    val name: String,
    val origin: String,
    val element: String,
    val smokeColor: Long,
    val description: String
)

data class SacredChant(
    val id: String,
    val arabicPhonetic: String,
    val englishMeaning: String,
    val powerLevel: String,
    val targetClan: JinnClan?
)

data class JinnSpirit(
    val id: String,
    val name: String,
    val clan: JinnClan,
    val title: String,
    val elementalDescription: String,
    val introductoryQuote: String,
    val personalityTone: String,
    val affinity: Int
)

data class ChatMessage(
    val id: String,
    val isFromJinn: Boolean,
    val text: String,
    val timestamp: Long
)

data class JinnDialogueResponse(
    val speech: String,
    val boonGranted: String?,
    val cosmicPrice: String?
)

