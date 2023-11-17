package fr.formiko.conwaygameoflife.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.formiko.conwaygameoflife.ConwayGameOfLife;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].replace("-", "").equalsIgnoreCase("version")) {
            try {
                InputStream is = Lwjgl3Launcher.class.getClassLoader().getResourceAsStream("version.md");
                String version = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines()
                    .collect(Collectors.joining("\n"));
                System.out.println(version);
                System.exit(0);
            } catch (Exception e) {
                System.out.println("Fail to get version in DesktopLauncher.");
            }
        }

        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        ConwayGameOfLife cof = new ConwayGameOfLife();
        return new Lwjgl3Application(cof, getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("ConwayGameOfLife");
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor.
        configuration.setForegroundFPS(0);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.setWindowedMode(30*20, 30*20);
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
        configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        //TODO : add an action to close
        return configuration;
    }
}
