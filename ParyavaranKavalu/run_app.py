import subprocess
import time
import os

# Configuration
SDK_PATH = r"C:\Users\kishorab\AppData\Local\Android\Sdk"
ADB = os.path.join(SDK_PATH, "platform-tools", "adb.exe")
EMULATOR = os.path.join(SDK_PATH, "emulator", "emulator.exe")
AVD_NAME = "Small_Phone"
APK_PATH = r"app\build\outputs\apk\debug\app-debug.apk"
PACKAGE_NAME = "com.paryavarankavalu"
LAUNCH_ACTIVITY = "com.paryavarankavalu.ui.splash.SplashActivity"

def run_command(cmd, wait=True):
    if wait:
        return subprocess.run(cmd, shell=True, capture_output=True, text=True)
    else:
        return subprocess.Popen(cmd, shell=True)

def is_device_connected():
    result = run_command(f'"{ADB}" devices')
    lines = result.stdout.strip().split('\n')
    # First line is "List of devices attached", check if there is a second line with "device"
    for line in lines[1:]:
        if "\tdevice" in line:
            return True
    return False

def main():
    if not is_device_connected():
        print(f"Starting emulator {AVD_NAME}...")
        run_command(f'"{EMULATOR}" -avd {AVD_NAME}', wait=False)
        print("Waiting for device to connect...")
        run_command(f'"{ADB}" wait-for-device')
        # Give it a few seconds to fully boot after connecting
        time.sleep(5)

    if os.path.exists(APK_PATH):
        print("Installing APK...")
        run_command(f'"{ADB}" install -r "{APK_PATH}"')
    else:
        print(f"Error: APK not found at {APK_PATH}. Run a build first.")
        return

    print("Launching App...")
    run_command(f'"{ADB}" shell am start -n {PACKAGE_NAME}/{LAUNCH_ACTIVITY}')
    print("Done!")

if __name__ == "__main__":
    main()
