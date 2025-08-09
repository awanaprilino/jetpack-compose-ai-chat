package awanpc.test.ai.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    showBottomSheet: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = Color(0xFF2B2B2B)
        ) {
            BottomSheetContent(
                onCameraClick = onCameraClick,
                onGalleryClick = onGalleryClick
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2B2B2B))
            .padding(16.dp)
    ) {
        Text(
            text = "Add Image",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFFCCCCCC),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = onCameraClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007AFF),
                contentColor = Color.White
            )
        ) {
            Text("Take Photo")
        }
        Button(
            onClick = onGalleryClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF007AFF),
                contentColor = Color.White
            )
        ) {
            Text("Pick from Gallery")
        }
    }
}

@Composable
@Preview
private fun BottomSheetContentPreview() {
    BottomSheetContent(
        onCameraClick = {},
        onGalleryClick = {}
    )
}