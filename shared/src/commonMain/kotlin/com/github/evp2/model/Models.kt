package com.github.evp2.model

import kotlinx.serialization.Serializable

@Serializable
enum class ApplicationStatus {
    ACTIVE,
    REJECTED,
    OFFER,
    WITHDRAWN
}

@Serializable
enum class ApplicationStage {
    APPLIED,
    RECRUITER_SCREEN,
    TECHNICAL_INTERVIEW,
    ONSITE,
    OFFER
}

@Serializable
enum class ContactRole {
    RECRUITER,
    HIRING_MANAGER,
    INTERVIEWER,
    REFERRAL,
    OTHER
}

@Serializable
enum class InteractionType {
    EMAIL,
    PHONE_CALL,
    VIDEO_CALL,
    INTERVIEW,
    FOLLOW_UP
}

@Serializable
data class Contact(
    val id: String,
    val fullName: String,
    val role: ContactRole,
    val email: String? = null,
    val linkedInUrl: String? = null,
    val notes: String? = null
)

@Serializable
data class Interaction(
    val id: String,
    val jobApplicationId: String,
    val contactId: String?,
    val type: InteractionType,
    val occurredAtEpochMillis: Long,
    val notes: String? = null
)

@Serializable
data class JobApplication(
    val id: String,
    val companyName: String,
    val roleTitle: String,
    val roleDescription: String? = null,
    val location: String,
    val applicationDateEpochMillis: Long,
    val currentStage: ApplicationStage,
    val status: ApplicationStatus,
    val source: String? = null, // e.g. LinkedIn, referral, recruiter
    val notes: String? = null
)

@Serializable
data class JobApplicationContact(
    val jobApplicationId: String,
    val contactId: String
)