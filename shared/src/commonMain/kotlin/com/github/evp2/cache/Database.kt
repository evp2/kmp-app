package com.github.evp2.cache

import app.cash.sqldelight.EnumColumnAdapter
import com.github.evp2.model.ApplicationStage
import com.github.evp2.model.ApplicationStatus


internal class Database(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = AppDatabase(
        databaseDriverFactory.createDriver(),
        contactAdapter = Contact.Adapter(
            roleAdapter = EnumColumnAdapter()
        ),
        eventAdapter = Event.Adapter(
            typeAdapter = EnumColumnAdapter()
        ),
        jobApplicationAdapter = JobApplication.Adapter(
            currentStageAdapter = EnumColumnAdapter(),
            statusAdapter = EnumColumnAdapter()
        )
    )
    private val dbQuery = database.jobApplicationQueries



    private fun mappingJobApplicationSelecting(
        id: String,
        companyName: String?,
        roleTitle: String?,
        roleDescription: String?,
        location: String?,
        applicationDate: String?,
        currentStage: ApplicationStage,
        status: ApplicationStatus,
        source: String?,
        notes: String?
    ): JobApplication {
        return JobApplication(
            id = id,
            companyName = companyName,
            roleTitle = roleTitle,
            roleDescription = roleDescription,
            location = location,
            applicationDate = applicationDate,
            currentStage = currentStage,
            status = status,
            source = source,
            notes = notes
        )
    }


}