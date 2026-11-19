package dev.vanta

import android.os.Bundle
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val Obsidian = Color(0xFF080A0D)
private val Panel = Color(0xFF131820)
private val PanelRaised = Color(0xFF1A222C)
private val Amber = Color(0xFFFFB84D)
private val White = Color(0xFFEAF0F6)
private val Muted = Color(0xFF8793A1)
private val Green = Color(0xFF72D6A0)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VantaApp() }
    }
}

@Composable
private fun VantaApp() {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences("vanta-local", android.content.Context.MODE_PRIVATE)
    }
    val tasks = remember {
        mutableStateListOf<String>().apply {
            val saved = JSONArray(preferences.getString("tasks", "[]"))
            for (index in 0 until saved.length()) add(saved.optString(index))
        }
    }
    val notes = remember {
        mutableStateListOf<String>().apply {
            val saved = JSONArray(preferences.getString("notes", "[]"))
            for (index in 0 until saved.length()) add(saved.optString(index))
        }
    }
    var command by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("ALL SYSTEMS NOMINAL · LOCAL MODE") }
    var feedback by remember { mutableStateOf("Ready. Type help to see what VANTA can do offline.") }
    var activeModule by remember { mutableStateOf("OVERVIEW") }
    var showAllTasks by remember { mutableStateOf(false) }
    var workspaceSnapshot by remember { mutableStateOf<WorkspaceSnapshot?>(null) }

    LaunchedEffect(activeModule) {
        if (activeModule == "FABRIC") {
            workspaceSnapshot = withContext(Dispatchers.IO) { WorkspaceStorage.snapshot(context) }
        }
    }

    fun persist(key: String, values: List<String>) {
        val json = JSONArray()
        values.forEach { json.put(it) }
        preferences.edit().putString(key, json.toString()).apply()
    }

    fun executeDirective() {
        val result = DirectiveEngine.parse(command)
        feedback = result.message
        when (val action = result.action) {
            is DirectiveEngine.Action.AddTask -> {
                tasks.add(0, action.title)
                persist("tasks", tasks)
                status = "TASK STORED · ON THIS DEVICE"
            }
            is DirectiveEngine.Action.AddNote -> {
                notes.add(0, action.text)
                persist("notes", notes)
                status = "NOTE STORED · ON THIS DEVICE"
            }
            DirectiveEngine.Action.ListTasks -> {
                showAllTasks = true
                activeModule = "DIRECTIVE"
                status = "LOCAL TASK INDEX · ${tasks.size} ITEMS"
            }
            DirectiveEngine.Action.Status -> status = "CORE READY · ${tasks.size} TASKS · ${notes.size} NOTES"
            DirectiveEngine.Action.Help -> status = "HELP REQUESTED · OFFLINE COMMANDS"
            is DirectiveEngine.Action.Unknown -> status = "UNRECOGNISED · NO ACTION TAKEN"
            DirectiveEngine.Action.Empty -> status = "IDLE · NO ACTION TAKEN"
        }
        if (result.action !is DirectiveEngine.Action.Empty) command = ""
    }

    val moduleInfo = mapOf(
        "DIRECTIVE" to ("LOCAL COMMANDS" to "Capture tasks and notes with a deterministic offline parser. No remote AI or hidden actions."),
        "MNEMOS" to ("DEVICE MEMORY" to "Your notes are stored in app-private local storage. Export, search and encryption are future milestones."),
        "FABRIC" to ("WORKSPACE" to "A safe workspace browser using Android's document picker is planned. No storage is reserved today."),
        "MIRROR" to ("ISOLATED WORKBENCH" to "Runtime isolation is still under research. VANTA does not claim to provide a virtual machine yet."),
        "GHOSTLINE" to ("ANDROID BRIDGE" to "Native app and settings integrations will use explicit Android intents and permission prompts."),
        "ORACLE" to ("INTELLIGENCE ROUTER" to "Offline commands work now. Model providers and network access are not connected.")
    )

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Obsidian) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    .imePadding().padding(horizontal = 20.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("V A N T A   /   A I E", color = Amber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("THE VEIL", color = White, fontSize = 31.sp, fontWeight = FontWeight.Light)
                        Text("ADAPTIVE INTELLIGENCE ENVIRONMENT", color = Muted, fontSize = 9.sp, letterSpacing = 1.2.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("● ONLINE", color = Green, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("LOCAL CORE", color = Muted, fontSize = 9.sp)
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(17.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("SYSTEM TELEMETRY", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f))
                            Text("NO CLOUD REQUIRED", color = Green, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(status, color = Amber, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Metric("TASKS", tasks.size.toString(), Modifier.weight(1f))
                            Metric("NOTES", notes.size.toString(), Modifier.weight(1f))
                            Metric("CORE", "LOCAL", Modifier.weight(1f))
                        }
                        Text("DEVICE  ${Build.MODEL}  ·  ANDROID ${Build.VERSION.RELEASE ?: "UNKNOWN"}",
                            color = Muted, fontSize = 9.sp)
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SectionLabel("VANTA CLOUD", "SUPABASE / OPTIONAL")
                        Text("Create a separate Supabase project for VANTA. Your existing Supabase project will not be changed.",
                            color = White, fontSize = 12.sp, lineHeight = 18.sp)
                        Button(
                            onClick = {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse("https://supabase.com/dashboard/new")
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = Obsidian),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("CREATE NEW SUPABASE PROJECT", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                        Text("After creation, VANTA will need the new project's Project URL and publishable key to connect. Cloud sync remains optional; local mode continues to work.",
                            color = Muted, fontSize = 10.sp, lineHeight = 15.sp)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel("DIRECTIVE DECK", "01 / INPUT")
                    Card(colors = CardDefaults.cardColors(containerColor = PanelRaised), shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            BasicTextField(
                                value = command,
                                onValueChange = { command = it },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                                singleLine = true,
                                textStyle = TextStyle(color = White, fontSize = 14.sp),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { executeDirective() }),
                                decorationBox = { inner ->
                                    if (command.isEmpty()) Text("Try: task Build the VANTA core", color = Muted, fontSize = 13.sp)
                                    inner()
                                }
                            )
                            Button(
                                onClick = { executeDirective() },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = Obsidian),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("PROCESS DIRECTIVE", fontSize = 11.sp, fontWeight = FontWeight.Black)
                            }
                            Text(feedback, color = Muted, fontSize = 11.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                                QuickCommand("HELP") { command = "help" }
                                QuickCommand("STATUS") { command = "status" }
                                QuickCommand("LIST TASKS") { command = "list" }
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionLabel("SYSTEM MODULES", "02 / NAVIGATION")
                    val modules = listOf("DIRECTIVE", "MNEMOS", "FABRIC", "MIRROR", "GHOSTLINE", "ORACLE")
                    modules.chunked(2).forEach { pair ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            pair.forEach { module ->
                                val selected = activeModule == module
                                Card(
                                    modifier = Modifier.weight(1f).clickable {
                                        activeModule = module
                                        showAllTasks = module == "DIRECTIVE"
                                        status = "$module · MODULE VIEW"
                                    },
                                    colors = CardDefaults.cardColors(containerColor = if (selected) PanelRaised else Panel),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(module, color = if (selected) Amber else White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text(moduleInfo.getValue(module).first, color = Muted, fontSize = 9.sp)
                                        Text(if (module == "DIRECTIVE" || module == "MNEMOS") "LOCAL FOUNDATION" else "IN DEVELOPMENT",
                                            color = if (module == "DIRECTIVE" || module == "MNEMOS") Green else Muted,
                                            fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                if (activeModule != "OVERVIEW") {
                    val info = moduleInfo.getValue(activeModule)
                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(14.dp)) {
                        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            SectionLabel(activeModule, info.first)
                            Text(info.second, color = White, fontSize = 12.sp, lineHeight = 18.sp)
                            if (activeModule == "DIRECTIVE") {
                                Text(if (showAllTasks) "LOCAL TASKS" else "RECENT TASKS",
                                    color = Amber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                val visibleTasks = if (showAllTasks) tasks else tasks.take(3)
                                if (visibleTasks.isEmpty()) Text("No tasks stored yet. Try: task Your first mission",
                                    color = Muted, fontSize = 11.sp)
                                visibleTasks.forEachIndexed { index, task ->
                                    Text("${index + 1}.  $task", color = White, fontSize = 11.sp)
                                }
                            }
                            if (activeModule == "MNEMOS") {
                                Text("RECENT NOTES", color = Amber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                notes.take(4).forEach { Text("•  $it", color = White, fontSize = 11.sp) }
                                if (notes.isEmpty()) Text("No notes stored yet. Try: note Your first memory",
                                    color = Muted, fontSize = 11.sp)
                            }
                            if (activeModule == "FABRIC") {
                                Text("INFINITE FABRIC · PRIVATE WORKSPACE", color = Amber,
                                    fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                val snapshot = workspaceSnapshot
                                if (snapshot == null) {
                                    Text("Measuring this device's app-private workspace…", color = Muted, fontSize = 11.sp)
                                } else {
                                    Text("TARGET QUOTA   200 GiB", color = White, fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold)
                                    Text("USED   ${snapshot.usedBytes / WorkspaceQuota.GIB} GiB  ·  ${snapshot.quotaPercent}% of target",
                                        color = White, fontSize = 11.sp)
                                    Text("INTERNAL VOLUME FREE   ${snapshot.freeBytesOnInternalVolume / WorkspaceQuota.GIB} GiB",
                                        color = White, fontSize = 11.sp)
                                    Text("DEVICE VOLUME   ${snapshot.totalBytesOnInternalVolume / WorkspaceQuota.GIB} GiB total",
                                        color = Muted, fontSize = 10.sp)
                                    Text("SAFE HEADROOM   2 GiB reserved from VANTA writes",
                                        color = Muted, fontSize = 10.sp)
                                    Text("Private path: app sandbox / vanta-fabric",
                                        color = Muted, fontSize = 10.sp)
                                }
                                Text("200 GiB is a logical ceiling, not preallocated space. Writes must pass both quota and real free-space checks. Android remains in control of storage.",
                                    color = Muted, fontSize = 10.sp, lineHeight = 15.sp)
                            }
                        }
                    }
                } else {
                    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(14.dp)) {
                        Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            SectionLabel("CURRENT CAPABILITIES", "03 / HONEST STATUS")
                            Text("ACTIVE  ·  Offline directive parser, local task capture, local notes, on-device persistence.",
                                color = White, fontSize = 11.sp, lineHeight = 17.sp)
                            Text("NOT CONNECTED  ·  AI providers, app automation, virtual machine, cloud sync and 200 GB workspace.",
                                color = Muted, fontSize = 11.sp, lineHeight = 17.sp)
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))
                Text("VANTA 0.2.0  ·  DEVICE TEST BUILD  ·  ANDROID HOST PRESERVED",
                    color = Muted, fontSize = 9.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
private fun Metric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(Obsidian, RoundedCornerShape(9.dp)).padding(9.dp)) {
        Text(value, color = White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Muted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SectionLabel(title: String, detail: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(detail, color = Muted, fontSize = 8.sp)
    }
}

@Composable
private fun QuickCommand(label: String, onClick: () -> Unit) {
    Text(label, color = Amber, fontSize = 9.sp, fontWeight = FontWeight.Bold,
        modifier = Modifier.background(Obsidian, RoundedCornerShape(7.dp))
            .clickable(onClick = onClick).padding(horizontal = 10.dp, vertical = 8.dp))
}
