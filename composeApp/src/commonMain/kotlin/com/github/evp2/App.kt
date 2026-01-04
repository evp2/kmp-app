package com.github.evp2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableStateListOf
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
import com.github.evp2.model.EventContact
import com.github.evp2.model.EventType
import com.github.evp2.model.InMemoryData
import com.github.evp2.model.JobApplication
import com.github.evp2.model.JobApplicationContact

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> EnumDropdown(
    label: String,
    options: Array<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption.name,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
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
            items(InMemoryData.events) { event ->
                val application = InMemoryData.jobApplications.find { it.id == event.jobApplicationId }
                val associatedContacts = InMemoryData.eventContacts
                    .filter { it.eventId == event.id }
                    .mapNotNull { ec -> InMemoryData.contacts.find { it.id == ec.contactId } }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = event.type.name, style = MaterialTheme.typography.titleMedium)
                        application?.let {
                            Text(text = "Company: ${it.companyName}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Text(text = "Date: ${application?.applicationDate}", style = MaterialTheme.typography.bodySmall)
                        event.notes?.let {
                            Text(text = it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                        }
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

enum class AddTarget { MENU, CONTACT, APPLICATION, EVENT }

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
                    BlackButton(onClick = { target = AddTarget.EVENT }, modifier = Modifier.fillMaxWidth()) {
                        Text("New Event")
                    }
                }
            }
            AddTarget.CONTACT -> AddContactForm { target = AddTarget.MENU }
            AddTarget.APPLICATION -> AddApplicationForm { target = AddTarget.MENU }
            AddTarget.EVENT -> AddEventForm { target = AddTarget.MENU }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactForm(onComplete: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(ContactRole.OTHER) }
    val selectedApplications = remember { mutableStateListOf<JobApplication>() }
    var appExpanded by remember { mutableStateOf(false) }

    val hasChanged = name.isNotEmpty() || email.isNotEmpty() || role != ContactRole.OTHER || selectedApplications.isNotEmpty()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("New Contact", style = MaterialTheme.typography.headlineSmall)
            BlackButton(
                onClick = {
                    val newId = (InMemoryData.contacts.maxOfOrNull { it.id } ?: 0) + 1
                    InMemoryData.contacts.add(Contact(id = newId, fullName = name, role = role, email = email))
                    
                    selectedApplications.forEach { app ->
                        InMemoryData.jobApplicationContacts.add(JobApplicationContact(app.id, newId))
                    }
                    
                    onComplete()
                },
                enabled = hasChanged && name.isNotBlank()
            ) {
                Text("Save")
            }
        }

        Column(
            //ensure form vertical scrolling
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            
            EnumDropdown(
                label = "Role",
                options = ContactRole.values(),
                selectedOption = role,
                onOptionSelected = { role = it }
            )

            Text("Associated Applications", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
            ExposedDropdownMenuBox(
                expanded = appExpanded,
                onExpandedChange = { appExpanded = !appExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "Select Application",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = appExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = appExpanded,
                    onDismissRequest = { appExpanded = false }
                ) {
                    InMemoryData.jobApplications.forEach { app ->
                        if (app !in selectedApplications) {
                            DropdownMenuItem(
                                text = { Text("${app.companyName} (${app.id})") },
                                onClick = {
                                    selectedApplications.add(app)
                                    appExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            selectedApplications.forEach { app ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    Text("${app.companyName} (${app.id})", modifier = Modifier.weight(1f))
                    IconButton(onClick = { selectedApplications.remove(app) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddApplicationForm(onComplete: () -> Unit) {
    var company by remember { mutableStateOf("") }
    var roleTitle by remember { mutableStateOf("") }
    var roleDescription by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ApplicationStatus.ACTIVE) }
    var stage by remember { mutableStateOf(ApplicationStage.APPLIED) }
    val selectedContacts = remember { mutableStateListOf<Contact>() }
    var contactExpanded by remember { mutableStateOf(false) }

    val hasChanged = company.isNotEmpty() || roleTitle.isNotEmpty() || roleDescription.isNotEmpty() || 
                     status != ApplicationStatus.ACTIVE || stage != ApplicationStage.APPLIED || 
                     selectedContacts.isNotEmpty()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("New Application", style = MaterialTheme.typography.headlineSmall)
            BlackButton(
                onClick = {
                    val newId = (InMemoryData.jobApplications.maxOfOrNull { it.id } ?: 0) + 1
                    InMemoryData.jobApplications.add(
                        JobApplication(
                            id = newId,
                            companyName = company,
                            roleTitle = roleTitle,
                            applicationDate = Clock.System.now().toEpochMilliseconds().toString(),
                            currentStage = stage,
                            status = status,
                            roleDescription = roleDescription,
                            location = ""
                        )
                    )
                    selectedContacts.forEach { contact ->
                        InMemoryData.jobApplicationContacts.add(JobApplicationContact(newId, contact.id))
                    }
                    onComplete()
                },
                enabled = hasChanged && company.isNotBlank() && roleTitle.isNotBlank()
            ) {
                Text("Save")
            }
        }

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(value = company, onValueChange = { company = it }, label = { Text("Company Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = roleTitle, onValueChange = { roleTitle = it }, label = { Text("Role Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = roleDescription,
                onValueChange = { roleDescription = it },
                label = { Text("Role Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            EnumDropdown(
                label = "Status",
                options = ApplicationStatus.values(),
                selectedOption = status,
                onOptionSelected = { status = it }
            )
            
            EnumDropdown(
                label = "Stage",
                options = ApplicationStage.values(),
                selectedOption = stage,
                onOptionSelected = { stage = it }
            )

            Text("Associated Contacts", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(top = 8.dp))
            ExposedDropdownMenuBox(
                expanded = contactExpanded,
                onExpandedChange = { contactExpanded = !contactExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "Select Contact",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = contactExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = contactExpanded,
                    onDismissRequest = { contactExpanded = false }
                ) {
                    InMemoryData.contacts.forEach { contact ->
                        if (contact !in selectedContacts) {
                            DropdownMenuItem(
                                text = { Text(contact.fullName) },
                                onClick = {
                                    selectedContacts.add(contact)
                                    contactExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            selectedContacts.forEach { contact ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    Text(contact.fullName, modifier = Modifier.weight(1f))
                    IconButton(onClick = { selectedContacts.remove(contact) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventForm(onComplete: () -> Unit) {
    var notes by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(EventType.EMAIL) }
    var selectedApplication by remember { mutableStateOf(InMemoryData.jobApplications.firstOrNull()) }
    var selectedContact by remember { mutableStateOf(InMemoryData.contacts.firstOrNull()) }
    var appExpanded by remember { mutableStateOf(false) }
    var contactExpanded by remember { mutableStateOf(false) }

    val initialApp = remember { InMemoryData.jobApplications.firstOrNull() }
    val initialContact = remember { InMemoryData.contacts.firstOrNull() }
    val hasChanged = notes.isNotEmpty() || type != EventType.EMAIL || 
                     selectedApplication != initialApp || selectedContact != initialContact

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("New Event", style = MaterialTheme.typography.headlineSmall)
            BlackButton(
                onClick = {
                    val eventId = (InMemoryData.events.maxOfOrNull { it.id } ?: 0) + 1
                    InMemoryData.events.add(
                        Event(
                            id = eventId,
                            jobApplicationId = selectedApplication?.id ?: 1,
                            type = type,
                            occurredAt = Clock.System.now().toEpochMilliseconds().toString(),
                            notes = notes
                        )
                    )
                    selectedContact?.let {
                        InMemoryData.eventContacts.add(EventContact(eventId, it.id))
                    }
                    onComplete()
                },
                enabled = hasChanged && notes.isNotBlank() && selectedApplication != null
            ) {
                Text("Save")
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = contactExpanded,
                    onExpandedChange = { contactExpanded = !contactExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedContact?.fullName ?: "Select Contact",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Contact") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = contactExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = contactExpanded,
                        onDismissRequest = { contactExpanded = false }
                    ) {
                        InMemoryData.contacts.forEach { contact ->
                            DropdownMenuItem(
                                text = { Text(contact.fullName) },
                                onClick = {
                                    selectedContact = contact
                                    contactExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = appExpanded,
                    onExpandedChange = { appExpanded = !appExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedApplication?.let { "${it.companyName} (${it.id})" }
                            ?: "Select Application",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Job Application") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = appExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = appExpanded,
                        onDismissRequest = { appExpanded = false }
                    ) {
                        InMemoryData.jobApplications.forEach { app ->
                            DropdownMenuItem(
                                text = { Text("${app.companyName} (${app.id})") },
                                onClick = {
                                    selectedApplication = app
                                    appExpanded = false
                                }
                            )
                        }
                    }
                }

                EnumDropdown(
                    label = "Event Type",
                    options = EventType.values(),
                    selectedOption = type,
                    onOptionSelected = { type = it }
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
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
