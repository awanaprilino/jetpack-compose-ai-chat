package awanpc.test.ai.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object PermissionUtils {

    /**
     * Checks if camera permission is granted
     */
    private fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks if storage/media permission is granted
     * Handles different API levels:
     * - Android 14+ (API 34+): NO NEED
     * - Android 13 (API 33): READ_MEDIA_IMAGES
     * - Android 12 and below: READ_EXTERNAL_STORAGE
     */
    private fun hasStoragePermission(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                // For modern gallery picker (PickVisualMedia), no permission needed
                // Only return true since we're using the system picker
                return true
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13 (API 33)
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            }

            else -> {
                // Android 12 and below
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    /**
     * Checks if both camera and storage permissions are granted
     */
    fun hasCameraAndStoragePermissions(context: Context): Boolean {
        return hasCameraPermission(context) && hasStoragePermission(context)
    }

    /**
     * Returns the appropriate storage permission string based on API level
     * For Android 14+, returns array of possible permissions
     */
    private fun getStoragePermission(): String {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13 (API 33)
                android.Manifest.permission.READ_MEDIA_IMAGES
            }

            else -> {
                // Android 12 and below
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }
    }

    /**
     * Returns array of required permissions for camera and storage
     */
    fun getRequiredPermissions(): Array<String> {
        return arrayOf(
            android.Manifest.permission.CAMERA,
            getStoragePermission()
        )
    }
}