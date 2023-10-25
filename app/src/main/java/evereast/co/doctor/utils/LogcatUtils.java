package evereast.co.doctor.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogcatUtils {
    private static final String TAG = "LogcatUtils";

    public static void saveLogcatToFile(Context context) {
        // Check if the app has permission to read logs (required for Android 6.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_LOGS) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission to read logs not granted");
//            return;
        }

        // Check if the app has permission to write to external storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission to write to external storage not granted");
            return;
        }


        // Create a file to save the logs
        File logFile = new File(Environment.getExternalStorageDirectory(), "logcat.txt");

        try {
            // Clear existing content of the log file
            if (logFile.exists()) {
                logFile.delete();
            }

            // Run logcat command to capture the logs
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                log.append(line).append(System.lineSeparator());
            }
            reader.close();

            // Write the logs to the file
            FileOutputStream outputStream = new FileOutputStream(logFile);
            outputStream.write(log.toString().getBytes());
            outputStream.close();

            Log.i(TAG, "Logcat logs saved to file: " + logFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to save logcat logs to file", e);
        }
    }
}
