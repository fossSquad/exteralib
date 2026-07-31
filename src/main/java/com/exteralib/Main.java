package com.exteralib;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.Arrays;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3 || !args[1].equals("-o")) {
            System.out.println("Usage: java -jar exteralib.jar <input.apk> -o <output.jar>");
            System.exit(1);
        }

        String inputApk = args[0];
        String outputJar = args[2];
        
        File out = new File(outputJar);
        if (out.isDirectory()) {
            String apkName = new File(inputApk).getName();
            String jarName = apkName.endsWith(".apk") ? apkName.substring(0, apkName.length() - 4) + ".jar" : apkName + ".jar";
            outputJar = new File(out, jarName).getPath();
        }
        
        try {
            Path tempJar = Files.createTempFile("dex2jar-temp", ".jar");
            System.out.println("Running dex2jar on " + inputApk + "...");
            
            // We use reflection to call Dex2jarCmd to avoid compilation issues if the class isn't found
            Class<?> dex2jarCmdClass = Class.forName("com.googlecode.dex2jar.tools.Dex2jarCmd");
            Object dex2jarCmd = dex2jarCmdClass.getDeclaredConstructor().newInstance();
            Method doMainMethod = dex2jarCmdClass.getMethod("doMain", String[].class);
            
            doMainMethod.invoke(dex2jarCmd, (Object) new String[]{"-f", inputApk, "-o", tempJar.toString()});
            
            System.out.println("Dex2jar finished. Patching Java version to 17...");
            
            patchJar(tempJar.toFile(), new File(outputJar));
            
            Files.deleteIfExists(tempJar);
            System.out.println("Success! Patched jar saved to: " + outputJar);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void patchJar(File inputJar, File outputJar) throws Exception {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(inputJar.toPath()));
             ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(outputJar.toPath()))) {
             
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                zos.putNextEntry(new ZipEntry(entry.getName()));
                
                if (entry.getName().endsWith(".class")) {
                    byte[] classData = readAllBytes(zis);
                    // Java class file format: 
                    // 0-3: CA FE BA BE (magic)
                    // 4-5: minor version
                    // 6-7: major version
                    if (classData.length >= 8 && 
                        classData[0] == (byte) 0xCA && classData[1] == (byte) 0xFE && 
                        classData[2] == (byte) 0xBA && classData[3] == (byte) 0xBE) {
                        
                        // Set major version to 61 (Java 17)
                        classData[7] = 61;
                    }
                    zos.write(classData);
                } else {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                zos.closeEntry();
                zis.closeEntry();
            }
        }
    }

    private static byte[] readAllBytes(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = is.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }
}
