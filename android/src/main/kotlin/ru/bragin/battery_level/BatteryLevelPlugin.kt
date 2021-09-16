package ru.bragin.battery_level

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** BatteryLevelPlugin */
class BatteryLevelPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  private lateinit var channel : MethodChannel
  private lateinit var binding: ActivityPluginBinding

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "battery_level")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "get_battery_level") {
      channel.invokeMethod("get_battery_level", getBatteryLevel())
    } else {
      result.notImplemented()
    }
  }

  fun getBatteryLevel() : Float? {
    val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
      binding.activity.window.context.registerReceiver(null, ifilter)
    }
    val batteryPct: Float? = batteryStatus?.let { intent ->
      val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
      val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
      level * 100 / scale.toFloat()
    }
    return batteryPct
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.binding = binding
  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    TODO("Not yet implemented")
  }

  override fun onDetachedFromActivity() {
    TODO("Not yet implemented")
  }
}
