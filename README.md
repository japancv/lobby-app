# Lobby App
A JCV Cloud Identity Manager add-on for Paypad application

## Setup
> Please make sure that the device is connected to the computer

### Logo
1. Run `adb push <PATH_TO_YOUR_LOGO> /storage/emulated/0/Download/` to add your logo image file to the device

### `config.ini`
1. Find `config.ini.example` in the `constants` folder
2. Copy and rename it to `config.ini`
3. Modify `confing.ini` according to your needs
4. Run `adb push app/src/main/java/com/example/lobbyapp/constants/config.ini /storage/emulated/0/Download/` to add the config file to the device

### Signature and `.env`
1. Run `keytool -genkey -v -keystore paypadplus.keystore -alias release -keyalg RSA -keysize 2048 -validity 10000` at the root folder to generate a signature
2. Copy `.env.example` and rename it to `.env`, and fill in the password which is used for generating the signature in the previous step
