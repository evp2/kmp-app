package com.github.evp2.model

object InMemoryData {
    val contacts = mutableListOf(
        Contact(
            id = 1,
            fullName = "Alice Smith",
            role = ContactRole.RECRUITER,
            email = "alice.smith@google.com",
            linkedInUrl = "https://www.linkedin.com/in/alicesmith",
            notes = "Very helpful during the initial screen."
        ),
        Contact(
            id = 2,
            fullName = "Bob Johnson",
            role = ContactRole.HIRING_MANAGER,
            email = "bob.johnson@meta.com",
            linkedInUrl = "https://www.linkedin.com/in/bobjohnson",
            notes = "Interested in my previous experience with Compose."
        ),
        Contact(
            id = 3,
            fullName = "Charlie Davis",
            role = ContactRole.INTERVIEWER,
            email = "charlie.davis@abc_staffing.com",
            notes = "Technical interviewer for the coding round."
        ),
    )

    val jobApplications = mutableListOf(
        JobApplication(
            id = 1,
            companyName = "Google",
            roleTitle = "Senior Android Engineer",
            roleDescription = "Developing core search features for Android.",
            location = "Mountain View, CA",
            applicationDate = "1714521600000", // 2024-05-01
            currentStage = ApplicationStage.TECHNICAL_INTERVIEW,
            status = ApplicationStatus.ACTIVE,
            source = "Recruiter",
            notes = "Focus on Jetpack Compose and Performance."
        ),
        JobApplication(
            id = 2,
            companyName = "Meta",
            roleTitle = "Software Engineer",
            roleDescription = "Working on Instagram Reels infrastructure.",
            location = "Menlo Park, CA (Remote)",
            applicationDate = "1714867200000", // 2024-05-05
            currentStage = ApplicationStage.OFFER,
            status = ApplicationStatus.OFFER,
            source = "LinkedIn",
            notes = "Negotiating compensation package."
        ),
        JobApplication(
            id = 3,
            companyName = "Apple",
            roleTitle = "iOS Engineer",
            roleDescription = "Core iOS Systems Architecture team.",
            location = "Santa Clara, CA",
            applicationDate = "1715126400000", // 2024-05-08
            currentStage = ApplicationStage.APPLIED,
            status = ApplicationStatus.ACTIVE,
            source = "Recruiter",
            notes = "Referred from a friend."
        ),
        JobApplication(
            id = 4,
            companyName = "Amazon",
            roleTitle = "SDE II",
            roleDescription = "AWS S3 storage backend.",
            location = "Seattle, WA",
            applicationDate = "1715126400000", // 2024-05-08
            currentStage = ApplicationStage.APPLIED,
            status = ApplicationStatus.REJECTED,
            source = "Recruiter",
            notes = "Position filled internally."
        ),
    )

    val events = mutableListOf(
        Event(
            id = 1,
            jobApplicationId = 1,
            contactId = 1,
            type = EventType.PHONE_CALL,
            occurredAt = "1714608000000", // 2024-05-02
            notes = "Initial recruiter screen. Went well."
        ),
        Event(
            id = 2,
            jobApplicationId = 1,
            contactId = null,
            type = EventType.VIDEO_CALL,
            occurredAt = "1714953600000", // 2024-05-06
            notes = "Technical round 1: Algorithms and Data Structures."
        ),
        Event(
            id = 3,
            jobApplicationId = 2,
            contactId = 2,
            type = EventType.INTERVIEW,
            occurredAt = "1715212800000", // 2024-05-09
            notes = "Final onsite (virtual). Met the whole team."
        )
    )

    val jobApplicationContacts = mutableListOf(
        JobApplicationContact(1, 1),
        JobApplicationContact(2, 2),
        JobApplicationContact(3, 3),
        JobApplicationContact(4, 3),
    )
}
