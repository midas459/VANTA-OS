package dev.vanta

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BridgeAmber = Color(0xFFFFB84D)
private val BridgeWhite = Color(0xFFEAF0F6)
private val BridgeMuted = Color(0xFF8793A1)
private val BridgePanel = Color(0xFF131820)
private val BridgeGreen = Color(0xFF72D6A0)

@Composable
fun GhostlinePanel(context: Context) {
    var feedback by remember { mutableStateOf("Android bridge ready. Choose an explicit action.") }
    val launchableApps = remember {
        val query = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        runCatching {
            context.packageManager.queryIntentActivities(query, 0)
                .filter { it.activityInfo.packageName != context.packageName }
                .distinctBy { it.activityInfo.packageName }
                .sortedBy { it.loadLabel(context.packageManager).toString().lowercase() }
                .take(24)
        }.getOrDefault(emptyList())
    }

    fun openIntent(intent: Intent, label: String) {
        runCatching {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            feedback = "Opened $label."
        }.onFailure {
            feedback = "Could not open $label: " + (it.message ?: "not available on this device")
        }
    }

    fun openApp(packageName: String, label: String) {
        val launch = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launch == null) feedback = "$label does not expose a launch activity."
        else openIntent(launch, label)
    }

    Card(colors = CardDefaults.cardColors(containerColor = BridgePanel), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("GHOSTLINE / ANDROID BRIDGE", color = BridgeAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("Launch installed apps and open Android settings through normal system intents. Android remains in control of permissions and confirmations.",
                color = BridgeWhite, fontSize = 11.sp, lineHeight = 16.sp)
            Text("SYSTEM SHORTCUTS", color = BridgeAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { openIntent(Intent(Settings.ACTION_SETTINGS), "Android Settings") }) { Text("SETTINGS") }
                Button(onClick = { openIntent(Intent(Settings.ACTION_WIFI_SETTINGS), "Wi-Fi settings") }) { Text("WI-FI") }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { openIntent(Intent(Settings.ACTION_BLUETOOTH_SETTINGS), "Bluetooth settings") }) { Text("BLUETOOTH") }
                Button(onClick = { openIntent(Intent(Settings.ACTION_DISPLAY_SETTINGS), "Display settings") }) { Text("DISPLAY") }
            }
            Button(onClick = { openIntent(Intent(Settings.ACTION_HOME_SETTINGS), "Home app settings") }) {
                Text("CHOOSE VANTA AS HOME APP")
            }
            Text("INSTALLED APPS", color = BridgeAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            if (launchableApps.isEmpty()) {
                Text("No launchable apps were returned by Android.", color = BridgeMuted, fontSize = 10.sp)
            } else {
                launchableApps.forEach { item ->
                    val label = item.loadLabel(context.packageManager).toString()
                    Button(onClick = { openApp(item.activityInfo.packageName, label) }, modifier = Modifier.fillMaxWidth()) {
                        Text("OPEN  $label")
                    }
                }
            }
            Text("GALAXY S10 / BIXBY KEY", color = BridgeAmber, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text("After VANTA is installed: open Settings → Advanced features → Bixby key. Choose whether Bixby opens with one or two presses, enable the other press pattern for an app, select Open app, then choose VANTA. Samsung controls this mapping; VANTA cannot take over the physical key itself.",
                color = BridgeWhite, fontSize = 10.sp, lineHeight = 15.sp)
            Text(feedback, color = BridgeGreen, fontSize = 10.sp)
        }
    }
}

@Composable
fun WardenPanel(context: Context) {
    var feedback by remember { mutableStateOf("No permissions are granted by this panel.") }
    Card(colors = CardDefaults.cardColors(containerColor = BridgePanel), shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("WARDEN / PERMISSION GATE", color = BridgeAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("Review VANTA's Android permissions in the system settings. Grant only permissions needed for a feature you choose to use.",
                color = BridgeWhite, fontSize = 11.sp, lineHeight = 16.sp)
            Button(onClick = {
                runCatching {
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + context.packageName)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                    feedback = "Opened VANTA app information and permission controls."
                }.onFailure { feedback = "Could not open permission controls: " + (it.message ?: "unknown error") }
            }, modifier = Modifier.fillMaxWidth()) { Text("REVIEW VANTA PERMISSIONS") }
            Button(onClick = {
                runCatching {
                    context.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    feedback = "Opened Android security settings."
                }.onFailure { feedback = "Could not open security settings: " + (it.message ?: "unknown error") }
            }, modifier = Modifier.fillMaxWidth()) { Text("ANDROID SECURITY SETTINGS") }
            Text(feedback, color = BridgeGreen, fontSize = 10.sp)
        }
    }
}
