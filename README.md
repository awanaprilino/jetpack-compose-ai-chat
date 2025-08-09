# Simple AI Chat Using Jetpack Compose and Gemini API

A simple Android chat application built with Jetpack Compose that integrates with Google's Gemini AI API.

## Features

- 🤖 AI-powered chat using Google Gemini API
- 📱 Modern UI built with Jetpack Compose
- 💬 Real-time conversation interface
- 📸 Image sharing and analysis capabilities
- 🎨 Clean and intuitive design

## Screenshot

| ![image1](https://github.com/user-attachments/assets/72a5cf1a-79df-4dc8-9ef2-31274d97c9fc) | ![image2](https://github.com/user-attachments/assets/dc67e8ed-b69f-4e08-aafc-597c54d3edcd) | ![image3](https://github.com/user-attachments/assets/029f45df-dd0d-43e9-9621-4952d6af4e9d) |
| --- | --- | --- |


## Prerequisites

- Android Studio (latest version recommended)
- Google AI Studio API key
- Device with camera (for image capture functionality)

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/jetpack-compose-ai-chat.git
cd jetpack-compose-ai-chat
```

### 2. Get your Google AI Studio API Key

1. Go to [Google AI Studio](https://aistudio.google.com/)
2. Sign in with your Google account
3. Create a new API key or use an existing one
4. Copy the API key for the next step

### 3. Configure API Key

#### Create local.properties file

Create a `local.properties` file in the root directory of the project (same level as `build.gradle` files) and add your Google AI Studio API key:

```properties
# This file contains machine-specific properties for the build system.
# Location of the Android SDK
sdk.dir=/path/to/your/android/sdk

# Google AI Studio API Key
apiKey={{apikey}}
```

### 4. Build and Run

1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Build and run the project on an emulator or physical device

## Usage

1. Launch the app
2. Type your message in the input field
3. **Send images**: Tap the image/camera icon to:
   - Take a photo with your camera
   - Select an image from your gallery
   - The AI can analyze and describe images
4. Tap send to get AI-powered responses
5. Enjoy conversing with the Gemini AI with both text and images!


## Support

If you encounter any issues or have questions, please open an issue on GitHub.

---

⭐ Don't forget to star this repository if you found it helpful!
