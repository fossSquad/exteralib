## exteralib
*[Licensed under the GNU General Public License v3.0](LICENSE)*

Standalone Java tool to automatically extract, convert, and patch exteraGram core libraries from prebuilt APKs for plugin development.

[![Channel](https://img.shields.io/badge/Channel-Telegram-blue.svg)](https://t.me/fossSquad)

### Features
- **Automated extraction** — instantly unpacks core classes from any exteraGram APK
- **Dex to Jar** — natively embeds dex2jar to convert `.dex` back into compiled `.jar` format
- **Bytecode patching** — dynamically changes major versions in `.class` files (e.g. to Java 17 bytecode) to allow compiling modern plugins
- **Lightweight** — single executable `.jar` file that takes seconds to run

### Building
```bash
git clone https://github.com/fossSquad/exteralib.git
cd exteralib
./gradlew shadowJar
cp build/libs/exteralib-1.0-SNAPSHOT-all.jar exteralib.jar
```

### Usage
Run the tool against any exteraGram APK (like beta or stable releases) to get a clean, Java-17 compatible library for your plugin:

```bash
java -jar exteralib.jar <input_exteragram.apk> -o <output_exteragram.jar>
```

You can then place `<output_exteragram.jar>` directly into your `re-extera/libs/` folder to compile your plugin against it.

### Credits
- [@aartzz](https://github.com/aartzz) — development and automation
- [dex2jar](https://github.com/pxb1988/dex2jar) — `.dex` to `.class` conversion tool
