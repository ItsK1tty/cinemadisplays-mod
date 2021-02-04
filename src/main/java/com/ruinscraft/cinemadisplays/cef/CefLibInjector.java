package com.ruinscraft.cinemadisplays.cef;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.file.Paths;

public final class CefLibInjector {

    public static final void inject() throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        final String defaultPath;
        final String currentPath = System.getProperty("java.library.path");
        final String cefNativesPath;
        final String currentRelativePath = Paths.get("").toAbsolutePath().toString();

        if (os.contains("win")) {
            defaultPath = getCommandOutput("cmd.exe", "/c", "echo %PATH%");
            cefNativesPath = currentRelativePath + "\\chromium\\win64";
        } else if (os.contains("mac")) {
            // TODO:
            defaultPath = getCommandOutput("echo $PATH");
            cefNativesPath = currentRelativePath + "/chromium/macos";
        } else if (os.contains("linux")) {
            // TODO:
            defaultPath = getCommandOutput("echo $PATH");
            cefNativesPath = currentRelativePath + "/chromium/linux64";
        } else {
            throw new RuntimeException("Unknown operating system: " + os);
        }

        String newPath = defaultPath +
                File.pathSeparator +
                currentPath +
                File.pathSeparator +
                cefNativesPath;

        // Will break in future Java versions
        System.setProperty("java.library.path", newPath);
        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        fieldSysPath.set(null, null);

        Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);
        usrPathsField.set(null, newPath.split(File.pathSeparator));

        System.out.println("Set java.library.path to: " + newPath);
    }

    private static String getCommandOutput(String... command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);

        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        return reader.readLine();
    }

}
