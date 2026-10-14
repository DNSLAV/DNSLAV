package com.example.data.remote

import com.example.BuildConfig
import com.example.data.model.JinnClan
import com.example.data.model.JinnDialogueResponse
import com.example.data.model.JinnSpirit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class JinnAiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun consultJinn(
        jinn: JinnSpirit,
        userPrompt: String,
        isWishRequest: Boolean
    ): JinnDialogueResponse = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val aiResponse = callGeminiApi(apiKey, jinn, userPrompt, isWishRequest)
                if (aiResponse != null) {
                    return@withContext aiResponse
                }
            } catch (_: Exception) {
                // Graceful fallback to mythological lore engine on any network or key issue
            }
        }

        // Offline / Lore-accurate Mythological Oracle
        return@withContext generateMythologicalResponse(jinn, userPrompt, isWishRequest)
    }

    private fun callGeminiApi(
        apiKey: String,
        jinn: JinnSpirit,
        userPrompt: String,
        isWishRequest: Boolean
    ): JinnDialogueResponse? {
        val systemInstruction = """
            You are ${jinn.name}, ${jinn.title}, an ancient elemental Jinn of the ${jinn.clan.title} clan (${jinn.clan.element}).
            Personality: ${jinn.personalityTone}.
            You have been summoned by a mortal occult summoner who has lit ${jinn.clan.preferredIncense} and drawn the Seal of Solomon.
            Speak in an evocative, solemn, archaic, poetic Arabian fantasy tone. Refer to mortals as "Son of Adam / Daughter of Clay", "Mortal Seeker", or "Bearer of the Seal".
            ${if (isWishRequest) "The mortal is asking for a WISH. Formulate a majestic boon, but attach a strict COSMIC PRICE or TABOO (the condition they must never break, e.g. a vow of silence on full moons, giving gold to the first traveler, never opening a locked box). Format your answer clearly with the Speech, Boon, and Cosmic Price." else "Answer their question or inquiry with esoteric wisdom, celestial riddles, and ancient memory of the ages before mortal flesh."}
        """.trimIndent()

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val requestJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", "System: $systemInstruction\nSummoner: $userPrompt"))
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.8)
                put("topP", 0.95)
            })
        }

        val body = requestJson.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val responseBody = response.body?.string() ?: return null
            val jsonObject = JSONObject(responseBody)
            val candidates = jsonObject.optJSONArray("candidates") ?: return null
            val firstCandidate = candidates.optJSONObject(0) ?: return null
            val content = firstCandidate.optJSONObject("content") ?: return null
            val parts = content.optJSONArray("parts") ?: return null
            val text = parts.optJSONObject(0)?.optString("text") ?: return null

            return if (isWishRequest) {
                parseWishFromText(text, userPrompt)
            } else {
                JinnDialogueResponse(
                    speech = text,
                    boonGranted = null,
                    cosmicPrice = null
                )
            }
        }
    }

    private fun parseWishFromText(text: String, wish: String): JinnDialogueResponse {
        val boon = "Granted under the Solomonic decree: \"$wish\" shall materialize through the shifting currents of fate."
        val price = "Cosmic Condition: You must never recount the genesis of this fortune under the midnight zenith."
        return JinnDialogueResponse(
            speech = text,
            boonGranted = boon,
            cosmicPrice = price
        )
    }

    private fun generateMythologicalResponse(
        jinn: JinnSpirit,
        userPrompt: String,
        isWishRequest: Boolean
    ): JinnDialogueResponse {
        val lower = userPrompt.lowercase()

        if (isWishRequest) {
            val (boon, price, speech) = when {
                lower.contains("wealth") || lower.contains("money") || lower.contains("gold") || lower.contains("rich") -> {
                    Triple(
                        "The Treasury of Shaddad: Hidden veins of abundance shall open across your path, drawing fortune like lodestone draws iron.",
                        "The Taboo of the Closed Palm: For every gold coin you gain, you must grant bread to three travelers without revealing your name.",
                        "Ah, mortal... you speak of gold, that fleeting yellow dust that mankind mines from our ancient caverns. By the smokeless fire that sustains my breath, your coffers shall swell as the Nile in flood. But heed the cosmic equilibrium—wealth without sacrifice breeds a viper in your heart."
                    )
                }
                lower.contains("love") || lower.contains("heart") || lower.contains("affection") || lower.contains("marry") -> {
                    Triple(
                        "The Allure of the Evening Star: Your voice and presence shall cast an enchantment like Damascus jasmine in midnight breeze.",
                        "The Law of the Unspoken Word: You may never profess your devotion while standing beneath an arch of stone.",
                        "You seek the sweet poison of passion. We who burn without smoke have watched empires crumble for a single glance behind a silk veil. I weave the threads of longing across the tapestry of fate for thee."
                    )
                }
                lower.contains("power") || lower.contains("strength") || lower.contains("conquer") || lower.contains("victory") -> {
                    Triple(
                        "The Mantle of the Brass Citadel: Adversaries shall wither before your gaze; your will shall strike like molten lightning.",
                        "The Price of the Unsheathed Blade: You must refrain from anger from the call of dusk until the rising sun.",
                        "You desire power, the very elixir that brought even Solomon to bind our seven kings in vessels of copper! Let your enemies tremble, mortal summoner, for the fire of ${jinn.clan.title} now walks behind your heels."
                    )
                }
                lower.contains("wisdom") || lower.contains("knowledge") || lower.contains("truth") || lower.contains("secret") -> {
                    Triple(
                        "The Sight of the Obsidian Mirror: Unseen truths, hidden conspiracies, and the omens of the morrow shall be laid bare before your mind.",
                        "The Vow of Solitude: Spend seven minutes in total silence each twilight before tasting water.",
                        "A noble request, rare among sons of clay who usually beg for trinkets and coins! The secret scrolls of Mount Qaf shall whisper into your dreams. Guard your thoughts, for truth burns sharper than any scimitar."
                    )
                }
                lower.contains("health") || lower.contains("heal") || lower.contains("life") || lower.contains("protect") -> {
                    Triple(
                        "The Shield of Solomon's Talisman: A ward of smoldering sapphire light surrounds your vessel, deflecting ill-intent, venom, and the evil eye.",
                        "The Rite of Salt: You must scatter three grains of salt upon your doorstep at every new crescent moon.",
                        "The breath of life is frail in your mortal clay, yet by the sacred geometry of the Seal you have drawn, no shadow nor malady of the unseen realm shall pierce your ward."
                    )
                }
                else -> {
                    Triple(
                        "The Unbinding of Obstacles: Fate twists in your favor; paths once barred by stone shall part like shifting sands.",
                        "The Covenant of the Threshold: Never step over a doorway with your left foot while making a pledge.",
                        "Thy wish resonates through the smoky chambers of the unseen. As Solomon bound the legions, so do I bind my covenant to this decree: what you seek is being wrought in the celestial forge."
                    )
                }
            }

            return JinnDialogueResponse(
                speech = speech,
                boonGranted = boon,
                cosmicPrice = price
            )
        }

        // Conversational responses
        val reply = when {
            lower.contains("solomon") || lower.contains("suleyman") || lower.contains("seal") -> {
                "Suleyman ibn Dawud... even now, the syllables of his name cause our flame to shudder! He possessed the emerald ring engraved with the Greatest Name, by which winds, beasts, and the seventy-two clans of jinn obeyed his whisper. The Seal you bear is a reflection of his ancient authority."
            }
            lower.contains("who are you") || lower.contains("name") || lower.contains("origin") -> {
                "I am ${jinn.name}, hailed in the scrolls of Mount Qaf as ${jinn.title}. My kind was fashioned from the scorching smokeless flame millennia before Adam drew his first mortal breath. We traverse the seven heavens and gaze upon the secrets written in the stars."
            }
            lower.contains("clan") || lower.contains("ifrit") || lower.contains("marid") || lower.contains("fire") -> {
                "My clan is the ${jinn.clan.title}. Our element is ${jinn.clan.element}. ${jinn.clan.lore} When you summoned me, the heavens aligned in the ${jinn.clan.planetaryHour}."
            }
            lower.contains("fear") || lower.contains("danger") || lower.contains("hurt") -> {
                "Fear not while you hold the consecrated circle intact, summoner. The covenants of old protect the one who knows the sacred chants and burns the consecrated ${jinn.clan.preferredIncense}. But treat me not as a slave, for our pride is as ancient as the mountains."
            }
            lower.contains("future") || lower.contains("prophecy") || lower.contains("tomorrow") -> {
                "The stars whisper of changes swift as the desert wind. A door you believed bolted shall swing open before the third sunset. Prepare thy heart, for that which is delayed is not denied."
            }
            else -> {
                "Thy words echo across the veil of smokeless fire. In the ancient bazaars of Babylon and the sunken halls of Iram of the Pillars, mortals have sought the counsel of ${jinn.name}. Ask what you will of the unseen, or seal your wish beneath the sacred wax!"
            }
        }

        return JinnDialogueResponse(
            speech = reply,
            boonGranted = null,
            cosmicPrice = null
        )
    }
}

