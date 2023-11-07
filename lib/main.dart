import 'dart:async';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _batteryLevel = 'Unknown battery level.';
  static const platform = MethodChannel('samples.flutter.dev/getMissedCalls');

  Future<void> _getMissedCalls() async {
    checkPermissionAndRetrieveMissedCalls();
    String missedCalls;
    try {
      final result = await platform.invokeMethod('getMissedCalls');
      print(result);
      // Handle the result of missed calls
      missedCalls = 'Missed Calls: $result';
    } catch (e) {
      missedCalls = "Failed to get missed calls: '${e.toString()}'.";
    }

    setState(() {
      _batteryLevel = missedCalls;
    });
  }

  Future<void> requestCallLogPermission() async {
    var status = await Permission.contacts.request();
    if (status.isDenied) {
      // The user declined the permission request
      // Handle it accordingly
    }
  }

  void checkPermissionAndRetrieveMissedCalls() async {
    var status = await Permission.contacts.status;
    if (status.isDenied) {
      await requestCallLogPermission();
    }
    // Now try to retrieve missed calls
    // Access the platform-specific code after ensuring the permission is granted
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            ElevatedButton(
              onPressed: _getMissedCalls,
              child: const Text('Get Battery Level'),
            ),
            Text(_batteryLevel),
          ],
        ),
      ),
    );
  }
}
