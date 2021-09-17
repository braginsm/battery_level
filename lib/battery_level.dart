
import 'dart:async';

import 'package:flutter/services.dart';

const getBatteryLevelMethod = "get_battery_level";

class BatteryLevel {

  final Function(double level) battaryLevelListener;

  BatteryLevel(this.battaryLevelListener) {
    _initBatteryLevel();
  }

  static const MethodChannel _channel = MethodChannel('battery_level');
  
  Future<void> getLevel() async {
    _channel.invokeMethod(getBatteryLevelMethod);
  }

  void _initBatteryLevel() {
    _channel.setMethodCallHandler((call) async{
      switch (call.method) {
        case getBatteryLevelMethod:
          battaryLevelListener(call.arguments);
          break;
      }
    });
  }
}
