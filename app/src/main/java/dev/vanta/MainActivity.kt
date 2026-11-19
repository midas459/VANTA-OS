package dev.vanta

import android.os.Bundle
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Obsidian = Color(0xFF090B0E)
private val Panel = Color(0xFF14181D)
private val Amber = Color(0xFFFFB547)
private val Muted = Color(0xFF89919B)
private val White = Color(0xFFE9EDF2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VantaApp() }
    }
}

@Composable
private fun VantaApp() {
    var command by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("SYSTEM READY · LOCAL MODE") }
    var activeModule by remember { mutableStateOf("OVERVIEW") }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Obsidian) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("V A N T A", color = Amber, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("THE VEIL", color = White, fontSize = 27.sp, fontWeight = FontWeight.Light)
                    }
                    Text("●  LOCAL", color = Amber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Column(
                    modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(18.dp)).padding(18.dp)
                ) {
                    Text("ENVIRONMENT STATUS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Text(status, color = Amber, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(6.dp))
                    Text("Android host · Offline-capable foundation", color = Muted, fontSize = 12.sp)
                }

                Text("COMMAND DECK", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp)).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = command,
                        onValueChange = { command = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(color = White, fontSize = 14.sp),
                        decorationBox = { inner ->
                            if (command.isEmpty()) Text("Enter a directive…", color = Muted, fontSize = 14.sp)
                            inner()
                        }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("EXECUTE", color = Amber, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            status = if (command.isBlank()) "DIRECTIVE EMPTY · NOTHING EXECUTED"
                            else "DIRECTIVE CAPTURED · PLANNER NOT CONNECTED"
                        })
                }

                Text("SYSTEMS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                val modules = listOf("DIRECTIVE", "MNEMOS", "FABRIC", "MIRROR", "GHOSTLINE", "ORACLE")
                modules.chunked(2).forEach { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        pair.forEach { module ->
                            Column(
                                modifier = Modifier.weight(1f).background(Panel, RoundedCornerShape(14.dp))
                                    .clickable {
                                        activeModule = module
                                        status = "$module · FOUNDATION SCREEN SELECTED"
                                    }.padding(15.dp)
                            ) {
                                Text(module, color = if (activeModule == module) Amber else White,
                                    fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(5.dp))
                                Text(if (module == "DIRECTIVE") "Mission engine" else "Module foundation",
                                    color = Muted, fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))
                Text("VANTA 0.1.0  /  FOUNDATION BUILD", color = Muted, fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
