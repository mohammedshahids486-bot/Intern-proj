@echo off
SET ADB="C:\Users\kishorab\AppData\Local\Android\Sdk\platform-tools\adb.exe"
SET EMULATOR="C:\Users\kishorab\AppData\Local\Android\Sdk\emulator\emulator.exe"
SET APK="app\build\outputs\apk\debug\app-debug.apk"

echo Checking for device...
%ADB% devices | findstr /C:"device" > nul
if %errorlevel% neq 0 (
    echo Starting emulator...
    start "" %EMULATOR% -avd Small_Phone
    echo Waiting for device...
    %ADB% wait-for-device
    timeout /t 5
)

if exist %APK% (
    echo Installing %APK%...
    %ADB% install -r %APK%
    echo Launching App...
    %ADB% shell am start -n com.paryavarankavalu/com.paryavarankavalu.ui.splash.SplashActivity
) else (
    echo Error: APK not found. Please build the project first.
)

pause
