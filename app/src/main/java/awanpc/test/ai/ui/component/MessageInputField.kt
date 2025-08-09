package awanpc.test.ai.ui.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import awanpc.test.ai.R
import awanpc.test.ai.utils.PermissionUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInputField(
    onSendMessage: (prompt: String, bitmap: Bitmap?) -> Unit = { _, _ -> },
    enabled: Boolean = true,
    currentBitmap: Bitmap? = null
) {
    var message by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var needsPermissionCheck by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedBitmap by remember { mutableStateOf(currentBitmap) }
    val context = LocalContext.current

    // Update selectedBitmap when currentBitmap changes
    LaunchedEffect(currentBitmap) {
        selectedBitmap = currentBitmap
    }

    // Launcher for taking a photo with the camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            selectedBitmap = it
        }
        scope.launch { sheetState.hide() }
        showBottomSheet = false
    }

    // Launcher for picking an image from the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                selectedBitmap = BitmapFactory.decodeStream(stream)
            }
        }
        scope.launch { sheetState.hide() }
        showBottomSheet = false
    }

    // Function to handle sending the message
    val sendMessage = {
        if (message.isNotBlank() || selectedBitmap != null) {
            onSendMessage(message.trim(), selectedBitmap)
            message = ""
            selectedBitmap = null
            keyboardController?.hide()
        }
    }

    // Function to remove selected image
    val removeImage = {
        selectedBitmap = null
    }

    // Function to check if all permissions are already granted
    val checkPermissions = {
        if (PermissionUtils.hasCameraAndStoragePermissions(context)) {
            // Permissions already granted, show bottom sheet directly
            showBottomSheet = true
        } else {
            // Need to request permissions
            needsPermissionCheck = true
        }
    }

    // Permission Handler for camera and storage permissions
    if (needsPermissionCheck) {
        PermissionHandler(
            onPermissionsResult = { permissionsGranted ->
                if (permissionsGranted) {
                    showBottomSheet = true
                } else {
                    showPermissionDeniedAlert = true
                }
                needsPermissionCheck = false
            }
        ) { _, _ ->
            // Empty content - we just need the permission handling
        }
    }

    // Permission Denied Alert Dialog
    if (showPermissionDeniedAlert) {
        AlertDialog(
            onDismissRequest = {
                showPermissionDeniedAlert = false
            },
            title = {
                Text(
                    text = "Permissions Required",
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = "Camera and storage permissions are required to take photos or select images. Please grant permissions in Settings to use this feature.",
                    color = Color(0xFFCCCCCC)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPermissionDeniedAlert = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF007AFF),
                        contentColor = Color.White
                    )
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showPermissionDeniedAlert = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFFCCCCCC)
                    )
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color(0xFF2B2B2B)
        )
    }

    // Image Picker Bottom Sheet
    ImagePickerBottomSheet(
        showBottomSheet = showBottomSheet,
        sheetState = sheetState,
        onDismiss = { showBottomSheet = false },
        onCameraClick = {
            cameraLauncher.launch()
        },
        onGalleryClick = {
            galleryLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )

    Column {
        // Show selected image if available
        selectedBitmap?.let { bitmap ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2B2B2B)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Remove button
                    IconButton(
                        onClick = removeImage,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(32.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.6f),
                                shape = CircleShape
                            ),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove image",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Text input field
        val containerColor = if (enabled) Color(0xFF2B2B2B) else Color(0xFF1A1A1A)
        TextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            textStyle = TextStyle(
                color = if (enabled) Color(0xFFCCCCCC) else Color(0xFF888888),
                fontSize = 16.sp,
            ),
            placeholder = {
                Text(
                    text = if (enabled) "Type a message..." else "AI is typing...",
                    style = TextStyle(
                        color = if (enabled) Color(0xFFCCCCCC) else Color(0xFF888888),
                        fontSize = 16.sp,
                    )
                )
            },
            leadingIcon = {
                IconButton(
                    onClick = {
                        checkPermissions()
                    },
                    enabled = enabled,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (enabled) Color(0xFFCCCCCC) else Color(0xFF888888)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                        contentDescription = "Add photo"
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = sendMessage,
                    enabled = enabled && (message.isNotBlank() || selectedBitmap != null),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = if (enabled && (message.isNotBlank() || selectedBitmap != null)) {
                            Color(0xFF007AFF) // Blue when ready to send
                        } else {
                            Color(0xFF888888) // Gray when disabled
                        }
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "Send message"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { sendMessage() }
            ),
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            singleLine = false,
            maxLines = 4
        )
    }
}

@Composable
@Preview
private fun MessageInputFieldPreview() {
    MessageInputField()
}