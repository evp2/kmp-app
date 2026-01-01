package com.github.evp2

import com.github.evp2.model.ApplicationStage
import com.github.evp2.model.ApplicationStatus
import com.github.evp2.model.Contact
import com.github.evp2.model.ContactRole
import com.github.evp2.model.InMemoryData
import com.github.evp2.model.JobApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SharedCommonTest {

    @Test
    fun testPlatformName() {
        val platformName = getPlatform().name
        assertTrue(platformName.isNotEmpty(), "Platform name should not be empty")
    }

    @Test
    fun testInMemoryDataInitialization() {
        assertTrue(InMemoryData.contacts.isNotEmpty(), "Contacts should not be empty")
        assertTrue(InMemoryData.jobApplications.isNotEmpty(), "Job applications should not be empty")
    }

    @Test
    fun testAddContact() {
        val initialSize = InMemoryData.contacts.size
        val newContact = Contact(
            id = "test-id",
            fullName = "Test User",
            role = ContactRole.OTHER,
            email = "test@example.com"
        )
        InMemoryData.contacts.add(newContact)
        assertEquals(initialSize + 1, InMemoryData.contacts.size)
        assertEquals("Test User", InMemoryData.contacts.last().fullName)
    }

    @Test
    fun testAddJobApplication() {
        val initialSize = InMemoryData.jobApplications.size
        val newApp = JobApplication(
            id = "test-j-id",
            companyName = "Test Co",
            roleTitle = "Test Developer",
            location = "Remote",
            applicationDateEpochMillis = 123456789L,
            currentStage = ApplicationStage.APPLIED,
            status = ApplicationStatus.ACTIVE
        )
        InMemoryData.jobApplications.add(newApp)
        assertEquals(initialSize + 1, InMemoryData.jobApplications.size)
        assertEquals("Test Co", InMemoryData.jobApplications.last().companyName)
    }
}
