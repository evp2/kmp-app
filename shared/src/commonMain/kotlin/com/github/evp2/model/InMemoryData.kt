package com.github.evp2.model

object InMemoryData {
    val contacts = mutableListOf(
        Contact(
            id = "c1",
            fullName = "Alice Smith",
            role = ContactRole.RECRUITER,
            email = "alice.smith@google.com",
            linkedInUrl = "https://www.linkedin.com/in/alicesmith",
            notes = "Very helpful during the initial screen."
        ),
        Contact(
            id = "c2",
            fullName = "Bob Johnson",
            role = ContactRole.HIRING_MANAGER,
            email = "bob.johnson@meta.com",
            linkedInUrl = "https://www.linkedin.com/in/bobjohnson",
            notes = "Interested in my previous experience with Compose."
        ),
        Contact(
            id = "c3",
            fullName = "Charlie Davis",
            role = ContactRole.INTERVIEWER,
            email = "charlie.davis@abc_staffing.com",
            notes = "Technical interviewer for the coding round."
        ),
    )

    val jobApplications = mutableListOf(
        JobApplication(
            id = "j1",
            companyName = "Google",
            roleTitle = "Senior Android Engineer",
            roleDescription = "Developing core search features for Android.",
            location = "Mountain View, CA",
            applicationDateEpochMillis = 1714521600000L, // 2024-05-01
            currentStage = ApplicationStage.TECHNICAL_INTERVIEW,
            status = ApplicationStatus.ACTIVE,
            source = "Recruiter",
            notes = "Focus on Jetpack Compose and Performance."
        ),
        JobApplication(
            id = "j2",
            companyName = "Meta",
            roleTitle = "Software Engineer",
            roleDescription = "Working on Instagram Reels infrastructure.",
            location = "Menlo Park, CA (Remote)",
            applicationDateEpochMillis = 1714867200000L, // 2024-05-05
            currentStage = ApplicationStage.OFFER,
            status = ApplicationStatus.OFFER,
            source = "LinkedIn",
            notes = "Negotiating compensation package."
        ),
        JobApplication(
            id = "j3",
            companyName = "Apple",
            roleTitle = "iOS Engineer",
            roleDescription = "Core iOS Systems Architecture team.",
            location = "Santa Clara, CA",
            applicationDateEpochMillis = 1715126400000L, // 2024-05-08
            currentStage = ApplicationStage.APPLIED,
            status = ApplicationStatus.ACTIVE,
            source = "Recruiter",
            notes = "Referred from a friend."
        ),
        JobApplication(
            id = "j4",
            companyName = "Amazon",
            roleTitle = "SDE II",
            roleDescription = "AWS S3 storage backend.",
            location = "Seattle, WA",
            applicationDateEpochMillis = 1715126400000L, // 2024-05-08
            currentStage = ApplicationStage.APPLIED,
            status = ApplicationStatus.REJECTED,
            source = "Recruiter",
            notes = "Position filled internally."
        ),
    )

    val interactions = mutableListOf(
        Interaction(
            id = "i1",
            jobApplicationId = "j1",
            contactId = "c1",
            type = InteractionType.PHONE_CALL,
            occurredAtEpochMillis = 1714608000000L, // 2024-05-02
            notes = "Initial recruiter screen. Went well."
        ),
        Interaction(
            id = "i2",
            jobApplicationId = "j1",
            contactId = null,
            type = InteractionType.VIDEO_CALL,
            occurredAtEpochMillis = 1714953600000L, // 2024-05-06
            notes = "Technical round 1: Algorithms and Data Structures."
        ),
        Interaction(
            id = "i3",
            jobApplicationId = "j2",
            contactId = "c2",
            type = InteractionType.INTERVIEW,
            occurredAtEpochMillis = 1715212800000L, // 2024-05-09
            notes = "Final onsite (virtual). Met the whole team."
        )
    )

    val jobApplicationContacts = mutableListOf(
        JobApplicationContact("j1", "c1"),
        JobApplicationContact("j2", "c2"),
        JobApplicationContact("j3", "c3"),
        JobApplicationContact("j4", "c3"),
    )
}
