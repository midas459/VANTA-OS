package dev.vanta

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

private val ExperimentalAmber = Color(0xFFFFB84D)
private val ExperimentalWhite = Color(0xFFEAF0F6)
private val ExperimentalMuted = Color(0xFF8793A1)
private val ExperimentalPanel = Color(0xFF131820)
private val ExperimentalGreen = Color(0xFF72D6A0)

private data class MirrorProfile(val name: String, val folder: String)

private object MirrorProfileStore {
    private const val PREFS = "vanta-mirror"
    private const val KEY = "profiles"

    fun root(context: Context): File =
        File(context.filesDir, "vanta-mirror-workspaces").apply { mkdirs() }

    fun load(context: Context): List<MirrorProfile> {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, "[]") ?: "[]"
        return runCatching {
            val json = JSONArray(raw)
            (0 until json.length()).mapNotNull { index ->
                val item = json.optJSONObject(index) ?: return@mapNotNull null
                val name = item.optString("name").trim()
                val folder = item.optString("folder").trim()
                if (name.isBlank() || !folder.matches(Regex("[a-z0-9-]{1,48}"))) null
                else MirrorProfile(name, folder)
            }
        }.getOrDefault(emptyList())
    }

    fun save(context: Context, profiles: List<MirrorProfile>) {
        val json = JSONArray()
        profiles.forEach { json.put(JSONObject().put("name", it.name).put("folder", it.folder)) }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY, json.toString()).apply()
    }
}

/** MIRROR currently manages private workspaces, not a guest OS or virtual machine. */
@Composable
fun MirrorPanel(context: Context) {
    val profiles = remember {
        mutableStateListOf<MirrorProfile>().apply { addAll(MirrorProfileStore.load(context)) }
    }
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Workspace manager ready. No virtual machine is installed.") }
    var pendingDelete by remember { mutableStateOf<MirrorProfile?>(null) }

    fun createProfile() {
        val cleanName = name.trim()
        if (cleanName.isBlank() || cleanName.length > 40) {
            status = "Enter a profile name between 1 and 40 characters."
            return
        }
        val slug = cleanName.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-').take(48)
        if (slug.isBlank()) {
            status = "Use at least one letter or number in the profile name."
            return
        }
        if (profiles.any { it.folder == slug }) {
            status = "A profile with that name already exists."
            return
        }
        val directory = File(MirrorProfileStore.root(context), slug)
        val created = runCatching { directory.mkdirs() || directory.isDirectory }.getOrDefault(false)
        if (!created) {
            status = "Could not create the workspace folder. Check device storage."
            return
        }
        profiles.add(MirrorProfile(cleanName, slug))
        MirrorProfileStore.save(context, profiles)
        name = ""
        status = "Created app-private workspace: $cleanName"
    }

    fun deleteProfile(profile: MirrorProfile) {
        val root = MirrorProfileStore.root(context).canonicalFile
        val directory = File(root, profile.folder).canonicalFile
        if (!directory.toPath().startsWith(root.toPath()) || directory == root) {
            status = "Blocked unsafe workspace path."
            return
        }
        val deleted = runCatching { !directory.exists() || directory.deleteRecursively() }.getOrDefault(false)
        if (deleted) {
            profiles.remove(profile)
            MirrorProfileStore.save(context, profiles)
            status = "Deleted workspace: " + profile.name
        } else status = "Could not delete workspace: " + profile.name
        pendingDelete = null
    }

    Card(colors = CardDefaults.cardColors(containerColor = ExperimentalPanel), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("MIRROR / WORKSPACE PROFILES", color = ExperimentalAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(
                "Create separate app-private folders for experiments and project files. This is a storage workspace, NOT a virtual machine: it does not boot another OS or execute arbitrary scripts.",
                color = ExperimentalWhite, fontSize = 11.sp, lineHeight = 16.sp
            )
            OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier.fillMaxWidth(),
                label = { Text("Workspace profile name") }, singleLine = true)
            Button(onClick = { createProfile() }, modifier = Modifier.fillMaxWidth()) {
                Text("CREATE WORKSPACE PROFILE")
            }
            Text("SAVED PROFILES · " + profiles.size, color = ExperimentalAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            if (profiles.isEmpty()) {
                Text("No profiles yet. Create one above to make a private workspace folder.",
                    color = ExperimentalMuted, fontSize = 10.sp)
            }
            profiles.forEach { profile ->
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1A222C)),
                    shape = RoundedCornerShape(10.dp)) {
                    Column(Modifier.fillMaxWidth().padding(11.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text(profile.name, color = ExperimentalWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("READY · APP-PRIVATE FILE WORKSPACE", color = ExperimentalGreen, fontSize = 8.sp)
                        Text("vanta-mirror-workspaces/" + profile.folder, color = ExperimentalMuted, fontSize = 9.sp)
                        TextButton(onClick = { pendingDelete = profile }) { Text("DELETE WORKSPACE") }
                    }
                }
            }
            Text(status, color = ExperimentalGreen, fontSize = 10.sp)
        }
    }

    pendingDelete?.let { profile ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete workspace?") },
            text = { Text("This permanently deletes the app-private folder for '" + profile.name + "'.") },
            confirmButton = { TextButton(onClick = { deleteProfile(profile) }) { Text("DELETE") } },
            dismissButton = { TextButton(onClick = { pendingDelete = null }) { Text("CANCEL") } }
        )
    }
}

@Composable
fun OraclePanel(onRouteToDirective: (String) -> Unit) {
    var request by remember { mutableStateOf("") }
    Card(colors = CardDefaults.cardColors(containerColor = ExperimentalPanel), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("ORACLE / LOCAL INTELLIGENCE ROUTER", color = ExperimentalAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(
                "Offline route active. ORACLE can hand supported commands to DIRECTIVE. No remote model provider is configured, and this panel does not pretend to be generative AI.",
                color = ExperimentalWhite, fontSize = 11.sp, lineHeight = 16.sp
            )
            OutlinedTextField(value = request, onValueChange = { request = it }, modifier = Modifier.fillMaxWidth(),
                label = { Text("What should VANTA process?") },
                placeholder = { Text("Example: task Test VANTA on my phone") }, minLines = 2)
            Button(onClick = { onRouteToDirective(request.trim()) }, enabled = request.isNotBlank(),
                modifier = Modifier.fillMaxWidth()) { Text("ROUTE TO DIRECTIVE") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { request = "help" }) { Text("HELP") }
                TextButton(onClick = { request = "status" }) { Text("STATUS") }
                TextButton(onClick = { request = "list" }) { Text("LIST TASKS") }
            }
            Text("Remote AI, local model inference, provider keys and cloud usage controls remain future integrations.",
                color = ExperimentalMuted, fontSize = 9.sp, lineHeight = 13.sp)
        }
    }
}
