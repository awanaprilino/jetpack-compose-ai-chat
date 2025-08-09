package awanpc.test.ai.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import awanpc.test.ai.data.Message

@Composable
fun MessageItem(message: Message) {
    val modifier = if (message.isMe) {
        Modifier
            .padding(start = 16.dp, end = 8.dp)
            .defaultMinSize(minHeight = 60.dp)
            .widthIn(max = 280.dp) // Limit max width for better readability
            .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp, bottomStart = 20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF007EF4),
                        Color(0xFF2A75BC),
                    )
                )
            )
    } else {
        Modifier
            .padding(start = 8.dp, end = 16.dp)
            .defaultMinSize(minHeight = 60.dp)
            .widthIn(max = 280.dp) // Limit max width for better readability
            .clip(RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp, bottomEnd = 20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF454545),
                        Color(0xFF2B2B2B),
                    )
                )
            )
    }

    val boxArrangement = if (message.isMe) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxWidth(),
        contentAlignment = boxArrangement
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Box(
                modifier = modifier
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    // Display image if available
                    message.image?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Message image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Add spacing between image and text if text exists
                        if (message.message.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Display text message if not empty
                    if (message.message.isNotBlank()) {
                        Text(
                            text = message.message,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    } else if (message.image != null) {
                        // If only image, add spacing before timestamp
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Always show timestamp
                    Text(
                        text = message.time,
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MessageItemPreview() {
    Column {
        MessageItem(message = Message("Hi there!", "9:00 AM", true))
        MessageItem(message = Message("What's up!", "9:01 AM", false))
        // Preview with image only (you can't show actual bitmap in preview, but structure will be there)
        MessageItem(message = Message("", "9:02 AM", true, image = null))
        // Preview with both text and image
        MessageItem(message = Message("Check this out!", "9:03 AM", false, image = null))
    }
}