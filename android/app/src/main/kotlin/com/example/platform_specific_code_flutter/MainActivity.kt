package com.example.platform_specific_code_flutter

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.CallLog
import android.util.Log

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/getMissedCalls"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
      // This method is invoked on the main thread.
      call, result ->
      if (call.method == "getMissedCalls") {
        val batteryLevel = getMissedCalls()
    
        if (call.method == "getMissedCalls") {
          val missedCalls = getMissedCalls()
          result.success(missedCalls)
      } else {
          result.notImplemented()
      }
      } else {
        result.notImplemented()
      }
    }
  }
  fun getMissedCalls(): List<Map<String, Any>> {
    val missedCalls = mutableListOf<Map<String, Any>>()
    val callLogUri = CallLog.Calls.CONTENT_URI
    val projection = arrayOf(
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.NUMBER,
        CallLog.Calls.DATE
    )
    val where = "${CallLog.Calls.TYPE} = ? AND ${CallLog.Calls.NEW} = ?"
    val whereArgs = arrayOf(CallLog.Calls.MISSED_TYPE.toString(), "1")
    val sortOrder = "${CallLog.Calls.DATE} DESC"

    val cursor: Cursor? = contentResolver.query(
        callLogUri,
        projection,
        where,
        whereArgs,
        sortOrder
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
        val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
        val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)

        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            val number = it.getString(numberIndex)
            val date = it.getLong(dateIndex)

            Log.d("MissedCall", "Name: $name, Number: $number, Date: $date")
            val callDetails = mapOf(
                "name" to name,
                "number" to number,
                "date" to date
            )
            missedCalls.add(callDetails)
        }
    }
    return missedCalls
}

// // Inside configureFlutterEngine

}

