package com.github.evp2.model

import kotlinx.serialization.SerialName
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
enum class EventType {
    EMAIL,
    PHONE_CALL,
    VIDEO_CALL,
    INTERVIEW,
    FOLLOW_UP
}

@Serializable
data class Contact(
    @SerialName("_id")
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("role")
    val role: ContactRole,
    @SerialName("email")
    val email: String? = null,
    @SerialName("linkedin_url")
    val linkedInUrl: String? = null,
    @SerialName("notes")
    val notes: String? = null
)

@Serializable
data class Event(
    @SerialName("id")
    val id: String,
    @SerialName("job_application_id")
    val jobApplicationId: String,
    @SerialName("contact_id")
    val contactId: String?,
    @SerialName("type")
    val type: EventType,
    @SerialName("occurred_at")
    val occurredAt: String? = null,
    @SerialName("notes")
    val notes: String? = null
)

@Serializable
data class JobApplication(
    @SerialName("id")
    val id: String,
    @SerialName("company_name")
    val companyName: String? = null,
    @SerialName("role_title")
    val roleTitle: String? = null,
    @SerialName("role_description")
    val roleDescription: String? = null,
    @SerialName("location")
    val location: String? = null,
    @SerialName("application_date")
    val applicationDate: String? = null,
    @SerialName("current_stage")
    val currentStage: ApplicationStage,
    @SerialName("status")
    val status: ApplicationStatus,
    @SerialName("source")
    val source: String? = null, // e.g. LinkedIn, referral, recruiter
    @SerialName("notes")
    val notes: String? = null
)

@Serializable
data class JobApplicationContact(
    @SerialName("job_application_id")
    val jobApplicationId: String,
    @SerialName("contact_id")
    val contactId: String
)