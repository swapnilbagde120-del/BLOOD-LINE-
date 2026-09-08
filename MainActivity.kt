package com.bloodbank.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val BloodRed = Color(0xFFC62828)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BloodBankApp() }
    }
}

@Composable
fun BloodBankApp() {
    var tab by remember { mutableStateOf(0) }
    var aiOpen by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = lightColorScheme(primary = BloodRed)) {
        if (aiOpen) {
            AiChatScreen(onBack = { aiOpen = false })
            return@MaterialTheme
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("BLOOD BANK", fontWeight = FontWeight.ExtraBold)
                            Text(
                                "Save Lives • Donate Blood",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val items = listOf(
                        Icons.Default.Home to "Home",
                        Icons.Default.Bloodtype to "Stock",
                        Icons.Default.Warning to "Emergency",
                        Icons.Default.LocationOn to "Nearby",
                        Icons.Default.Person to "Profile"
                    )
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = tab == index,
                            onClick = { tab = index },
                            icon = { Icon(item.first, contentDescription = item.second) },
                            label = { Text(item.second) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                when (tab) {
                    0 -> Home(
                        onEmergency = { tab = 2 },
                        onStock = { tab = 1 },
                        onAI = { aiOpen = true }
                    )
                    1 -> Stock()
                    2 -> Emergency()
                    3 -> Nearby()
                    else -> Profile(onAI = { aiOpen = true })
                }
            }
        }
    }
}

@Composable
fun Home(onEmergency: () -> Unit, onStock: () -> Unit, onAI: () -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = BloodRed)) {
                Column(Modifier.padding(22.dp)) {
                    Text(
                        "Give Blood. Save a Life.",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Find blood availability, create emergency requests and connect with verified services.",
                        color = Color.White
                    )
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = onEmergency, modifier = Modifier.weight(1f)) {
                    Text("Emergency")
                }
                OutlinedButton(onClick = onStock, modifier = Modifier.weight(1f)) {
                    Text("Blood Stock")
                }
            }
        }

        item {
            Card {
                Column(Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("AI MODE", fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("ChatGPT-style assistant for app help, FAQs and navigation.")
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = onAI) {
                        Text("Open AI Assistant")
                    }
                }
            }
        }

        item { InfoCard("Smart Matching", "Match blood group and component requirements with verified centers and donors.") }
        item { InfoCard("Live Tracking", "Real GPS, route and ETA can be connected when authorized location and routing services are configured.") }
        item {
            Text("DEMO DATA", color = BloodRed, fontWeight = FontWeight.Bold)
            Text(
                "Live stock, hospital verification, GPS, ETA and OTP are never fabricated.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun InfoCard(title: String, body: String) {
    Card {
        Column(Modifier.padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(5.dp))
            Text(body)
        }
    }
}

@Composable
fun Stock() {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Blood Stock",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text("Availability must be supplied by an authorized source with last-verified time.")
        }

        listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-").forEach { group ->
            item {
                ListItem(
                    headlineContent = { Text(group, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text("Demo — verify with authorized blood center") },
                    leadingContent = { Icon(Icons.Default.Bloodtype, contentDescription = null) }
                )
            }
        }
    }
}

@Composable
fun Emergency() {
    var group by remember { mutableStateOf("Select blood group") }
    var sent by remember { mutableStateOf(false) }

    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Emergency Request",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text("Request blood through a verified hospital/blood-center workflow.")
        }

        item {
            var open by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = open,
                onExpandedChange = { open = !open }
            ) {
                OutlinedTextField(
                    value = group,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Blood group") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = open,
                    onDismissRequest = { open = false }
                ) {
                    listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                group = it
                                open = false
                            }
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { sent = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Emergency Request")
            }
        }

        if (sent) {
            item {
                Card {
                    Text(
                        "Saved in demo mode. Real dispatch requires backend, identity and organization verification.",
                        Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Nearby() {
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Nearby",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card {
            Column(Modifier.padding(18.dp)) {
                Text("Map & Route", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text(
                    "Connect Google Maps/Mapbox, location consent, routing and realtime backend for exact locations and ETA."
                )
            }
        }

        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Enable Location")
        }
    }
}

@Composable
fun Profile(onAI: () -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                "Profile & Security",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            ListItem(
                headlineContent = { Text("Identity Verification") },
                supportingContent = { Text("OTP/backend integration required") },
                leadingContent = { Icon(Icons.Default.Security, contentDescription = null) }
            )
        }
        item {
            ListItem(
                headlineContent = { Text("Location Privacy") },
                supportingContent = { Text("Use consent and minimum necessary retention") },
                leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
        }
        item {
            ListItem(
                headlineContent = { Text("Audit Trail") },
                supportingContent = { Text("Log critical actions and status changes") },
                leadingContent = { Icon(Icons.Default.Security, contentDescription = null) }
            )
        }
        item {
            Button(onClick = onAI, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Open AI Assistant")
            }
        }
    }
}

@Composable
fun AiChatScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                "AI" to "Namaste! Main Blood Bank AI Assistant hoon. Main app ke features, blood-donation FAQs aur navigation mein help kar sakta hoon."
            )
        )
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column {
                Text("AI Assistant", fontWeight = FontWeight.Bold)
                Text("Chat mode", style = MaterialTheme.typography.labelSmall)
            }
        }

        HorizontalDivider()

        LazyColumn(
            Modifier.weight(1f).fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages.size) { index ->
                val (who, text) = messages[index]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (who == "AI") Color(0xFFF2F2F2)
                            else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(who, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(text)
                    }
                }
            }
        }

        Row(
            Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask anything about the app…") },
                maxLines = 3
            )

            IconButton(
                onClick = {
                    val question = input.trim()
                    if (question.isNotEmpty()) {
                        messages = messages +
                            ("You" to question) +
                            ("AI" to "Demo response: Main Blood Bank app ke features, navigation aur general donation information mein help kar sakta hoon. Medical emergency mein qualified healthcare professional ya emergency service se contact karein.")
                        input = ""
                    }
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}
