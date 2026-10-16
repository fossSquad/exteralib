## exteralib
*[Licensed under the GNU General Public License v3.0](LICENSE)*

Standalone Java tool to extract exteraGram compile-time API from prebuilt APKs for DEX plugin development.

[![Channel](https://img.shields.io/badge/Channel-Telegram-blue.svg)](https://t.me/fossSquad)

### Features
- **Focused API extraction** — retains only `com.exteragram.*`; use official Telegram sources for `org.telegram.*`
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
Run tool against any exteraGram APK to create a clean Java-17-compatible compile-time API JAR:

```bash
java -jar exteralib.jar <input_exteragram.apk> -o <output_exteragram.jar>
```

Output contains only `com.exteragram.*` classes. It does not include `org.telegram.*`, AndroidX, Kotlin, or client dependencies.

Use `--all` only when legacy full-library output is required:

```bash
java -jar exteralib.jar <input_exteragram.apk> -o <output.jar> --all
```

### Credits
- [dex2jar](https://github.com/pxb1988/dex2jar) — `.dex` to `.class` conversion tool
