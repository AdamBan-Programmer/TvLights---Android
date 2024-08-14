# TvLights:
This is an application that manages the LED strip. With this app you can manually set the color of the LEDs.

# About:
- I created this app to make my TV backlight. The system includes a TV application and a script written in Python in the controller (Raspberry Pi). The Android application dedicated to phones creates a connection via a TCP socket with the controller. All you need to do is configure the app in "SETTINGS". Set the port and IP of your control device (for example Arduino or Raspberry pi). Then you can go to the "COLOR" tab and select a color by clicking on the color palette. If you want to turn off the backlight, press the 'OFF' button. Settings are saved by serializing the object in: internal memory/Android/TvLights
# How to use:
- Download TvLights.py
- Install NeoPixel. Tutorial: https://learn.adafruit.com/neopixels-on-raspberry-pi/python-usage
- Modify script, set: server_ip, port (of raspberry pi), strip_length
- Run TvLights.py in raspberry pi.
- Download TvLights.apk and install it on your phone.
- Configure network settings in 'SETTINGS'

# Requirements:
- Android 11.
- All files managment permission
- Programmable ledStrip (for example: LED RGB WS2812B)
- raspberry pi/ Arduino
- Power supply

# Screenshots
![image](https://github.com/user-attachments/assets/89bc8a3e-5bd5-41b9-b07b-801b7f73907a)
![image](https://github.com/user-attachments/assets/96e5dcbb-945e-4e2c-ac87-cc34e82503d7)
![image](https://github.com/user-attachments/assets/1930e788-ddbf-4993-aa4d-0240b3961ede)


