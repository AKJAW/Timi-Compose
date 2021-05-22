package com.akjaw.settings.data

import com.akjaw.settings.domain.BooleanSettingsOption
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue

internal class InMemorySettingsRepositoryTest : SettingsRepositoryContractTest() {

    private lateinit var systemUnderTest: InMemorySettingsRepository

    @BeforeEach
    fun setUp() {
        systemUnderTest = InMemorySettingsRepository()
    }

    @Test
    override fun `The default value is false`() {
        val result = systemUnderTest.getBoolean(BooleanSettingsOption.DARK_MODE)

        expectThat(result).isFalse()
    }

    @Test
    override fun `The saved setting is persisted`() {
        systemUnderTest.setBoolean(BooleanSettingsOption.DARK_MODE, true)

        val result = systemUnderTest.getBoolean(BooleanSettingsOption.DARK_MODE)

        expectThat(result).isTrue()
    }
}
