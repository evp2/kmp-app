package com.github.evp2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.evp2.model.ApplicationStage
import com.github.evp2.model.ApplicationStatus
import com.github.evp2.model.Contact
import com.github.evp2.model.ContactRole
import com.github.evp2.model.Event
import com.github.evp2.model.EventType
import com.github.evp2.model.InMemoryData
import com.github.evp2.model.JobApplication

import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock


@Composable
fun BlackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.LightGray
        )
    ) {
        content()
    }
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Home") },
                    label = { Text("Search") },
                    selected = currentScreen == Screen.Home,
                    onClick = {
                        currentScreen = Screen.Home
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Contacts, contentDescription = "Contacts") },
                    label = { Text("Contacts") },
                    selected = currentScreen == Screen.Contacts,
                    onClick = {
                        currentScreen = Screen.Contacts
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                    label = { Text("Add") },
                    selected = currentScreen == Screen.Add,
                    onClick = {
                        currentScreen = Screen.Add
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Events") },
                    label = { Text("Events") },
                    selected = currentScreen == Screen.Events,
                    onClick = {
                        currentScreen = Screen.Events
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = currentScreen == Screen.Profile,
                    onClick = {
                        currentScreen = Screen.Profile
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            when (currentScreen) {
                Screen.Home -> HomeScreen()
                Screen.Contacts -> ContactScreen()
                Screen.Add -> AddScreen()
                Screen.Events -> EventsScreen()
                Screen.Profile -> ProfileScreen()
            }
        }
    }
}

enum class Screen {
    Home, Contacts, Add, Events, Profile
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun EventsScreen() {

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Events", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        LazyColumn {
            items(InMemoryData.events) { interaction ->
                val contact = interaction.contactId?.let { id -> InMemoryData.contacts.find { it.id == id } }
                val application = InMemoryData.jobApplications.find { it.id == interaction.jobApplicationId }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = interaction.type.name, style = MaterialTheme.typography.titleMedium)
                        application?.let {
                            Text(text = "Company: ${it.companyName}", style = MaterialTheme.typography.bodyMedium)
                        }
                        contact?.let {
                            Text(text = "With: ${it.fullName}", style = MaterialTheme.typography.bodySmall)
                        }
                        Text(text = "Date: ${application?.applicationDate}", style = MaterialTheme.typography.bodySmall)
                        interaction.notes?.let {
                            Text(text = it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

enum class AddTarget { MENU, CONTACT, APPLICATION, INTERACTION }

@Composable
fun AddScreen() {
    var target by remember { mutableStateOf(AddTarget.MENU) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (target != AddTarget.MENU) {
            IconButton(onClick = { target = AddTarget.MENU }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        when (target) {
            AddTarget.MENU -> {
                Text("What would you like to add?", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 24.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BlackButton(onClick = { target = AddTarget.APPLICATION }, modifier = Modifier.fillMaxWidth()) {
                        Text("New Opportunity")
                    }
                    BlackButton(onClick = { target = AddTarget.CONTACT }, modifier = Modifier.fillMaxWidth()) {
                        Text("New Contact")
                    }
                    BlackButton(onClick = { target = AddTarget.INTERACTION }, modifier = Modifier.fillMaxWidth()) {
                        Text("New Event")
                    }
                }
            }
            AddTarget.CONTACT -> AddContactForm { target = AddTarget.MENU }
            AddTarget.APPLICATION -> AddApplicationForm { target = AddTarget.MENU }
            AddTarget.INTERACTION -> AddInteractionForm { target = AddTarget.MENU }
        }
    }
}

@Composable
fun AddContactForm(onComplete: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("New Contact", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        BlackButton(
            onClick = {
                InMemoryData.contacts.add(Contact(id = InMemoryData.contacts.size + 1, fullName = name, role = ContactRole.RECRUITER, email = email))
                onComplete()
            },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Contact")
        }
    }
}

@Composable
fun AddApplicationForm(onComplete: () -> Unit) {
    var company by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("New Application", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Role Title") }, modifier = Modifier.fillMaxWidth())
        BlackButton(
            onClick = {
                InMemoryData.jobApplications.add(
                    JobApplication(
                        id = InMemoryData.jobApplications.size + 1,
                        companyName = company,
                        roleTitle = role,
                        applicationDate = Clock.System.now().toEpochMilliseconds().toString(),
                        currentStage = ApplicationStage.APPLIED,
                        status = ApplicationStatus.ACTIVE,
                        roleDescription = "",
                        location = ""
                    )
                )
                onComplete()
            },
            enabled = company.isNotBlank() && role.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Application")
        }
    }
}

@Composable
fun AddInteractionForm(onComplete: () -> Unit) {
    var notes by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("New Interaction", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        Text("Note: Defaulting to first application for demo", style = MaterialTheme.typography.bodySmall)
        BlackButton(
            onClick = {
                InMemoryData.events.add(
                    Event(
                        id = InMemoryData.events.size + 1,
                        jobApplicationId = InMemoryData.jobApplications.firstOrNull()?.id ?: 1,
                        type = EventType.PHONE_CALL,
                        occurredAt = Clock.System.now().toEpochMilliseconds().toString(),
                        notes = notes,
                        contactId = null
                    )
                )
                onComplete()
            },
            enabled = notes.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Interaction")
        }
    }
}

@Composable
fun HomeScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val filteredApplications = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            InMemoryData.jobApplications.toList()
        } else {
            InMemoryData.jobApplications.filter {
                it.companyName?.contains(searchQuery, ignoreCase = true) == true ||
                        it.roleTitle?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Opportunities", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            placeholder = { Text("Search by company or role") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            },
            singleLine = true
        )

        LazyColumn {
            items(filteredApplications) { application ->
                val associatedContacts = InMemoryData.jobApplicationContacts
                    .filter { it.jobApplicationId == application.id }
                    .mapNotNull { jac -> InMemoryData.contacts.find { it.id == jac.contactId } }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        application.companyName?.let { Text(text = it, style = MaterialTheme.typography.headlineSmall) }
                        application.roleTitle?.let { Text(text = it, style = MaterialTheme.typography.titleMedium) }
                        Text(text = "Status: ${application.status.name}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        Text(text = "Stage: ${application.currentStage.name}", style = MaterialTheme.typography.bodySmall)

                        if (associatedContacts.isNotEmpty()) {
                            Text(
                                text = "Contacts: ${associatedContacts.joinToString { it.fullName }}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Contacts", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))
        LazyColumn {
            items(InMemoryData.contacts.toList()) { contact ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text(text = contact.fullName, style = MaterialTheme.typography.titleLarge)
                    Text(text = contact.role.name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                    contact.email?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
    }
}
