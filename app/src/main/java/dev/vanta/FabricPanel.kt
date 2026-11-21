package dev.vanta

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val FabricBackground = Color(0xFF080A0D)
private val FabricText = Color(0xFFEAF0F6)
private val FabricMuted = Color(0xFF8793A1)
private val FabricAmber = Color(0xFFFFB84D)
private val FabricGreen = Color(0xFF72D6A0)
private val FabricPanel = Color(0xFF1A222C)

@Composable
fun FabricPanel(context: Context, onFeedback: (String) -> Unit) {
    var path by remember { mutableStateOf("") }
    var entries by remember { mutableStateOf<List<WorkspaceEntry>>(emptyList()) }
    var snapshot by remember { mutableStateOf<WorkspaceSnapshot?>(null) }
    var name by remember { mutableStateOf("") }
    var draft by remember { mutableStateOf("") }
    var openedPath by remember { mutableStateOf<String?>(null) }
    var openedContents by remember { mutableStateOf<String?>(null) }
    var pendingDelete by remember { mutableStateOf<WorkspaceEntry?>(null) }
    var refresh by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(path, refresh) {
        val result = withContext(Dispatchers.IO) {
            WorkspaceStorage.snapshot(context) to WorkspaceStorage.list(context, path)
        }
        snapshot = result.first
        entries = result.second
    }

    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text("INFINITE FABRIC · PRIVATE WORKSPACE", color = FabricAmber,
            fontSize = 10.sp, fontWeight = FontWeight.Bold)
        snapshot?.let {
            Text("TARGET 200 GiB  ·  USED ${it.usedBytes / WorkspaceQuota.GIB} GiB  ·  ${it.quotaPercent}%",
                color = FabricText, fontSize = 11.sp)
            Text("DEVICE FREE ${it.freeBytesOnInternalVolume / WorkspaceQuota.GIB} GiB  ·  2 GiB safety reserve",
                color = FabricMuted, fontSize = 10.sp)
        } ?: Text("Measuring app-private workspace…", color = FabricMuted, fontSize = 11.sp)

        Text("PATH  /fabric/${path}", color = FabricAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                path = path.substringBeforeLast('/', "")
                openedPath = null
                openedContents = null
            }, enabled = path.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = FabricPanel, contentColor = FabricText)) {
                Text("UP", fontSize = 9.sp)
            }
            Text("ROOT", color = FabricMuted, fontSize = 9.sp,
                modifier = Modifier.clickable { path = ""; openedPath = null; openedContents = null }.padding(7.dp))
        }

        BasicTextField(value = name, onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth().background(FabricBackground, RoundedCornerShape(8.dp)).padding(10.dp),
            singleLine = true, textStyle = TextStyle(color = FabricText, fontSize = 12.sp),
            decorationBox = { inner -> if (name.isEmpty()) Text("New folder or file name", color = FabricMuted, fontSize = 12.sp); inner() })
        BasicTextField(value = draft, onValueChange = { draft = it },
            modifier = Modifier.fillMaxWidth().background(FabricBackground, RoundedCornerShape(8.dp)).padding(10.dp),
            textStyle = TextStyle(color = FabricText, fontSize = 12.sp),
            decorationBox = { inner -> if (draft.isEmpty()) Text("Text file contents…", color = FabricMuted, fontSize = 12.sp); inner() })

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                val itemName = name
                scope.launch {
                    val result = withContext(Dispatchers.IO) { WorkspaceStorage.createDirectory(context, path, itemName) }
                    onFeedback(result.message)
                    if (result.success) name = ""
                    refresh++
                }
            }, enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = FabricPanel, contentColor = FabricText)) {
                Text("NEW FOLDER", fontSize = 9.sp)
            }
            Button(onClick = {
                val itemName = name
                val body = draft
                scope.launch {
                    val result = withContext(Dispatchers.IO) { WorkspaceStorage.createTextFile(context, path, itemName, body) }
                    onFeedback(result.message)
                    if (result.success) { name = ""; draft = "" }
                    refresh++
                }
            }, enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = FabricAmber, contentColor = FabricBackground)) {
                Text("SAVE FILE", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
        }

        Text("WORKSPACE ITEMS · ${entries.size}", color = FabricAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        if (entries.isEmpty()) Text("This folder is empty.", color = FabricMuted, fontSize = 11.sp)
        entries.forEach { entry ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f).clickable {
                    if (entry.isDirectory) {
                        path = entry.relativePath
                        openedPath = null
                        openedContents = null
                    } else {
                        scope.launch {
                            openedPath = entry.relativePath
                            openedContents = withContext(Dispatchers.IO) {
                                WorkspaceStorage.readTextFile(context, entry.relativePath)
                            }
                        }
                    }
                }.padding(vertical = 5.dp)) {
                    Text((if (entry.isDirectory) "FOLDER  " else "FILE  ") + entry.name,
                        color = FabricText, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    Text(if (entry.isDirectory) "Tap to open" else "${entry.sizeBytes} bytes · tap to preview",
                        color = FabricMuted, fontSize = 9.sp)
                }
                Text("DELETE", color = FabricMuted, fontSize = 8.sp, modifier = Modifier.clickable {
                    pendingDelete = entry
                }.padding(6.dp))
            }
        }
        if (openedPath != null) {
            Text("FILE PREVIEW · ${openedPath!!.substringAfterLast('/')}", color = FabricAmber,
                fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(openedContents ?: "Preview unavailable (file may exceed 2 MiB).",
                color = FabricText, fontSize = 11.sp)
        }
        Text("200 GiB is a logical ceiling, not preallocated space. Files remain in Android's app-private sandbox. Writes check real free space and the safety reserve.",
            color = FabricMuted, fontSize = 10.sp, lineHeight = 15.sp)
    }
    pendingDelete?.let { entry ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete workspace item?") },
            text = { Text("Permanently delete '" + entry.name + "' and its contents if it is a folder?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            WorkspaceStorage.delete(context, entry.relativePath)
                        }
                        onFeedback(result.message)
                        if (openedPath == entry.relativePath) { openedPath = null; openedContents = null }
                        pendingDelete = null
                        refresh++
                    }
                }) { Text("DELETE") }
            },
            dismissButton = { TextButton(onClick = { pendingDelete = null }) { Text("CANCEL") } }
        )
    }
}
