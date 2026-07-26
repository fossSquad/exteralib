# exteralib

This is a reverse-engineered, open-sourced library extracted from the Exteragram Android client. 
Exteragram is a powerful Telegram client with many features, but unfortunately it is closed-source, violating the GPL-2.0 license of the official Telegram client.

This repository provides the extracted `exteragram.jar` library so that developers can build upon it, study its features, or use it in other projects (like Xposed modules).

## How it was made
1. Took the prebuilt APK of the Exteragram client.
2. Ran it through `dex2jar` to get the classes.
3. Patched the classes because they were initially compiled in Java 6, which caused issues with newer gradle builds.

## Disclaimer
This project is for educational and interoperability purposes. 
