package awanpc.test.ai.ui.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import awanpc.test.ai.utils.PermissionUtils

@Composable
fun PermissionHandler(
    onPermissionsResult: (Boolean) -> Unit,
    content: @Composable (permissionsGranted: Boolean, showPermissionDeniedMessage: Boolean) -> Unit
) {
    // State to track permission status
    var permissionsGranted by remember { mutableStateOf(false) }
    var showPermissionDeniedMessage by remember { mutableStateOf(false) }

    // Get required permissions from utility
    val permissions = PermissionUtils.getRequiredPermissions()

    // Launcher for requesting multiple permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        // Check if all permissions are granted
        val allGranted = permissionsResult.values.all { it }
        permissionsGranted = allGranted
        showPermissionDeniedMessage = !allGranted
        onPermissionsResult(allGranted)
    }

    // Request permissions when the composable is first launched
    LaunchedEffect(Unit) {
        permissionLauncher.launch(permissions)
    }

    // Provide the content with permission states
    content(permissionsGranted, showPermissionDeniedMessage)
}