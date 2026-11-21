package dev.vanta

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

private val CloudAmber = Color(0xFFFFB84D)
private val CloudWhite = Color(0xFFEAF0F6)
private val CloudMuted = Color(0xFF8793A1)
private val CloudPanel = Color(0xFF1A222C)

data class SupabaseProjectConfig(val name: String, val url: String, val publishableKey: String)

object SupabaseProjectStore {
    private const val PREFS = "vanta-supabase"
    private const val PROJECTS = "projects"
    private const val ACTIVE = "active"

    fun load(context: Context): List<SupabaseProjectConfig> {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(PROJECTS, "[]") ?: "[]"
        return runCatching {
            val array = JSONArray(raw)
            (0 until array.length()).mapNotNull { i ->
                val item = array.optJSONObject(i) ?: return@mapNotNull null
                val name = item.optString("name").trim()
                val url = item.optString("url").trim()
                val key = item.optString("key").trim()
                if (name.isBlank() || !isValidUrl(url) || key.isBlank()) null
                else SupabaseProjectConfig(name, url, key)
            }
        }.getOrDefault(emptyList())
    }

    fun activeIndex(context: Context, count: Int): Int {
        if (count == 0) return -1
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(ACTIVE, 0).coerceIn(0, count - 1)
    }

    fun save(context: Context, projects: List<SupabaseProjectConfig>, activeIndex: Int) {
        val array = JSONArray()
        projects.forEach { p ->
            array.put(JSONObject().put("name", p.name).put("url", p.url).put("key", p.publishableKey))
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(PROJECTS, array.toString()).putInt(ACTIVE, activeIndex).apply()
    }

    fun isValidUrl(value: String): Boolean = runCatching {
        val uri = Uri.parse(value)
        uri.scheme == "https" && !uri.host.isNullOrBlank() && uri.host!!.endsWith(".supabase.co")
    }.getOrDefault(false)
}

object VantaSupabaseRuntime {
    fun create(context: Context, config: SupabaseProjectConfig): SupabaseClient =
        createSupabaseClient(config.url, config.publishableKey) {
            install(Auth) {
                host = "auth"
                scheme = "vanta"
                flowType = FlowType.PKCE
                sessionManager = io.github.jan.supabase.auth.SettingsSessionManager(
                    key = "vanta-session-${config.url.hashCode()}"
                )
            }
        }

    fun handleDeeplink(context: Context, intent: Intent?) {
        if (intent == null) return
        val projects = SupabaseProjectStore.load(context)
        val index = SupabaseProjectStore.activeIndex(context, projects.size)
        val config = projects.getOrNull(index) ?: return
        runCatching { create(context, config).handleDeeplinks(intent) }
    }
}

@Composable
fun SupabasePanel(context: Context) {
    val projects = remember {
        mutableStateListOf<SupabaseProjectConfig>().apply { addAll(SupabaseProjectStore.load(context)) }
    }
    var activeIndex by remember { mutableStateOf(SupabaseProjectStore.activeIndex(context, projects.size)) }
    var projectName by remember { mutableStateOf(projects.getOrNull(activeIndex)?.name.orEmpty()) }
    var projectUrl by remember { mutableStateOf(projects.getOrNull(activeIndex)?.url.orEmpty()) }
    var publishableKey by remember { mutableStateOf(projects.getOrNull(activeIndex)?.publishableKey.orEmpty()) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var signUpMode by remember { mutableStateOf(false) }
    var signedInEmail by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf("Cloud is optional. VANTA's local tools work without it.") }
    var busy by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val activeProject = projects.getOrNull(activeIndex)
    val client = remember(activeProject?.url, activeProject?.publishableKey) {
        activeProject?.let { VantaSupabaseRuntime.create(context, it) }
    }

    LaunchedEffect(client) {
        signedInEmail = runCatching { client?.auth?.currentSessionOrNull()?.user?.email }.getOrNull()
    }

    Card(colors = CardDefaults.cardColors(containerColor = CloudPanel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text("VANTA CLOUD / SUPABASE", color = CloudAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(
                "Connect and switch between separate Supabase projects. Use only a project URL and publishable key. Never enter a service-role or secret key.",
                color = CloudWhite, fontSize = 11.sp, lineHeight = 16.sp
            )
            if (projects.isNotEmpty()) {
                Text("SAVED PROJECTS", color = CloudMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                projects.forEachIndexed { index, p ->
                    Row(
                        Modifier.fillMaxWidth().clickable {
                            activeIndex = index
                            projectName = p.name
                            projectUrl = p.url
                            publishableKey = p.publishableKey
                            signedInEmail = null
                            SupabaseProjectStore.save(context, projects, index)
                            status = "Selected project: ${p.name}"
                        }.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text((if (index == activeIndex) "●  " else "○  ") + p.name,
                            color = if (index == activeIndex) CloudAmber else CloudWhite, fontSize = 11.sp)
                        Text("SELECT", color = CloudMuted, fontSize = 9.sp)
                    }
                }
            }
            OutlinedTextField(projectName, { projectName = it }, Modifier.fillMaxWidth(),
                label = { Text("Project name") }, singleLine = true)
            OutlinedTextField(projectUrl, { projectUrl = it.trim() }, Modifier.fillMaxWidth(),
                label = { Text("Project URL") }, placeholder = { Text("https://your-project.supabase.co") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri), singleLine = true)
            OutlinedTextField(publishableKey, { publishableKey = it.trim() }, Modifier.fillMaxWidth(),
                label = { Text("Publishable key") }, singleLine = true)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val n = projectName.trim()
                    val u = projectUrl.trim().trimEnd('/')
                    val k = publishableKey.trim()
                    if (n.isBlank() || !SupabaseProjectStore.isValidUrl(u) || k.isBlank()) {
                        status = "Enter a name, valid HTTPS Supabase URL and publishable key."
                    } else {
                        val found = projects.indexOfFirst { it.url == u }
                        val config = SupabaseProjectConfig(n, u, k)
                        if (found >= 0) { projects[found] = config; activeIndex = found }
                        else { projects.add(config); activeIndex = projects.lastIndex }
                        SupabaseProjectStore.save(context, projects, activeIndex)
                        signedInEmail = null
                        status = "Project saved. Connect and authenticate below."
                    }
                }) { Text("SAVE / CONNECT") }
                TextButton(onClick = {
                    projectName = ""; projectUrl = ""; publishableKey = ""
                    activeIndex = -1; signedInEmail = null
                    status = "Enter another project's details, then save."
                }) { Text("ADD ANOTHER") }
            }
            if (activeProject != null && client != null) {
                Text("ACTIVE PROJECT: ${activeProject.name}", color = CloudAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                if (signedInEmail != null) {
                    Text("SIGNED IN AS ${signedInEmail}", color = CloudWhite, fontSize = 11.sp)
                    Button(enabled = !busy, onClick = {
                        scope.launch {
                            busy = true
                            status = runCatching {
                                client.auth.signOut()
                                signedInEmail = null
                                "Signed out."
                            }.getOrElse { "Sign-out failed: ${it.message ?: "unknown error"}" }
                            busy = false
                        }
                    }) { Text("SIGN OUT") }
                } else {
                    Text(if (signUpMode) "CREATE ACCOUNT" else "ACCOUNT LOGIN",
                        color = CloudAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    OutlinedTextField(email, { email = it.trim() }, Modifier.fillMaxWidth(),
                        label = { Text("Email address") }, singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                    OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(),
                        label = { Text("Password") }, singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                    Button(enabled = !busy, onClick = {
                        if (email.isBlank() || password.length < 8) {
                            status = "Enter an email and password of at least 8 characters."
                        } else {
                            scope.launch {
                                busy = true
                                status = runCatching {
                                    if (signUpMode) {
                                        client.auth.signUpWith(Email, redirectUrl = "vanta://auth") {
                                            this.email = email
                                            this.password = password
                                        }
                                        signedInEmail = client.auth.currentSessionOrNull()?.user?.email
                                        if (signedInEmail == null)
                                            "Account created. Check your email to confirm, then return to VANTA."
                                        else "Account created and signed in."
                                    } else {
                                        client.auth.signInWith(Email) {
                                            this.email = email
                                            this.password = password
                                        }
                                        signedInEmail = client.auth.currentSessionOrNull()?.user?.email
                                        "Signed in successfully."
                                    }
                                }.getOrElse { "Authentication failed: ${it.message ?: "check network and project settings"}" }
                                busy = false
                            }
                        }
                    }) { Text(if (signUpMode) "CREATE ACCOUNT" else "SIGN IN") }
                    TextButton(onClick = {
                        signUpMode = !signUpMode
                        status = if (signUpMode) "Create a new account." else "Sign in to an existing account."
                    }) { Text(if (signUpMode) "I ALREADY HAVE AN ACCOUNT" else "CREATE A NEW ACCOUNT") }
                }
                Text("Email confirmation redirect: vanta://auth", color = CloudMuted, fontSize = 9.sp)
                Text("Add vanta://auth under Supabase Authentication → URL Configuration → Redirect URLs.",
                    color = CloudMuted, fontSize = 9.sp, lineHeight = 13.sp)
            }
            Text(status, color = CloudWhite, fontSize = 10.sp, lineHeight = 15.sp)
            Text("Offline fallback: MNEMOS, DIRECTIVE and FABRIC remain available locally.",
                color = CloudMuted, fontSize = 9.sp)
        }
    }
}
