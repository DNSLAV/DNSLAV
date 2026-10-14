package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.JinnClan
import com.example.data.repository.JinnRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Jinn Summoner", appName)
  }

  @Test
  fun `summoning jinn matches elemental incense offering`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = JinnRepository(context)

    // Dragon's blood ignites Ifrit
    val jinnIfrit = repository.summonJinn(
      incenseId = "dragons_blood",
      chantId = "chant_suleyman",
      nodesCount = 6
    )
    assertEquals(JinnClan.IFRIT, jinnIfrit.clan)

    // Oud al-Qamar awakens Marid
    val jinnMarid = repository.summonJinn(
      incenseId = "oud",
      chantId = "chant_suleyman",
      nodesCount = 6
    )
    assertEquals(JinnClan.MARID, jinnMarid.clan)
  }

  @Test
  fun `seal pact generates active covenant with price`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = JinnRepository(context)

    val jinn = repository.summonJinn("oud", "chant_suleyman", 6)
    val pact = repository.sealPact(
      jinn = jinn,
      wish = "Wisdom of the ancient seas",
      boon = "The sight of Mount Qaf",
      cosmicPrice = "Never taste salt after twilight"
    )

    assertNotNull(pact.id)
    assertEquals("ACTIVE", pact.status)
    assertEquals(jinn.name, pact.jinnName)
  }

  @Test
  fun `daily omen provides celestial counsel`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = JinnRepository(context)
    val omen = repository.getDailyOmen()

    assertNotNull(omen)
    assertTrue(omen.title.isNotEmpty())
    assertTrue(omen.counsel.isNotEmpty())
  }
}
