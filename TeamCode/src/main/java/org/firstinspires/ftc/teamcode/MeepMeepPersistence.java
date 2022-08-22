package org.firstinspires.ftc.teamcode;

import com.noahbres.meepmeep.MeepMeep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class to persist the state of the visualizer window.
 * <p>Basically saves the position of the {@link MeepMeep} GUI window so that it opens in the some position
 * that it was closed in.
 *
 * <pre>{@code
 * // Basic usage:
 *
 * MeepMeep meepMeep = new MeepMeep(windowSize);
 *
 * // Create a persistence object linked to the MeepMeep instance
 * MeepMeepPersistence persistence = new MeepMeepPersistence(meepMeep);
 *
 * // Restore the settings from the persistence object to the MeepMeep instance
 * persistence.restore();
 * }</pre>
 *
 * <b>NOTE:</b> 'DEFAULT_FILE_PATH' must be changed to the desired file path of the persistence
 * file.
 * <p>
 * <b>NOTE:</b> The settings autosave every 2 seconds, and when MeepMeep is closed from the 'X'.
 *
 * @author KG
 * @see MeepMeep
 * @see Properties
 */
public class MeepMeepPersistence {
    /**
     * The default file path for the persistence file. Should be changed to suit the user's primary
     * desired file path.
     */
    static final String DEFAULT_FILE_PATH = "TeamCode/src/main/res/raw/meepmeep.properties";

    /**
     * The {@link Properties} object used to save and interpret the settings.
     */
    private final Properties properties;

    /**
     * The {@link MeepMeep} instance to persist the settings of, or to restore settings to.
     */
    private final MeepMeep meepMeep;

    /**
     * Constructor for the {@code MeepMeepPersistence} class.
     *
     * <p>Starts an auto-save thread to save the settings every 2 seconds,
     * as well as a shutdown hook to save the settings when the program is closed.
     *
     * @param meepMeep The linked {@link MeepMeep} instance
     * @param path     The file path to initially load state from
     */
    public MeepMeepPersistence(MeepMeep meepMeep, String path) {
        this.properties = new Properties();
        this.meepMeep = meepMeep;

        reload();

        startPersistenceThread();

        Runtime.getRuntime().addShutdownHook(
            new Thread(this::save)
        );
    }

    /**
     * Constructor for the {@link MeepMeepPersistence} class that uses the default file path.
     *
     * @param meepMeep The linked {@link MeepMeep} instance
     */
    public MeepMeepPersistence(MeepMeep meepMeep) {
        this(meepMeep, DEFAULT_FILE_PATH);
    }

    /**
     * Runs the auto-save thread. The assigned runnable is called every 2 seconds, saving
     * the {@code MeepMeep} state every time it is called.
     */
    private void startPersistenceThread() {
        Executors.newSingleThreadScheduledExecutor().schedule(
            (Runnable) this::save, 2L, TimeUnit.SECONDS
        );
    }

    /**
     * Default method for {@link MeepMeepPersistence#save(String) save(String path)}, using the default file path.
     */
    public void save() {
        save(DEFAULT_FILE_PATH);
    }

    /**
     * Saves the {@link MeepMeep} state to the given file path.
     *
     * <p>At the moment, it only saves the window position of the {@link MeepMeep} GUI window.
     * However, the code is fully extensible, and other desired settings may be saved
     * to be restored.
     *
     * @param path The file path to save the state to
     */
    public void save(String path) {
        ensureFileExistence(path);

        properties.setProperty("windows_x", String.valueOf(meepMeep.getWindowFrame().getX()));
        properties.setProperty("windows_y", String.valueOf(meepMeep.getWindowFrame().getY()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            properties.store(writer, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Default method for {@link MeepMeepPersistence#reload(String) reload(String path)}, using the default file path.
     */
    public void reload() {
        reload(DEFAULT_FILE_PATH);
    }

    /**
     * Reloads the {@code Properties} object from the given file path.
     *
     * @param path The file path to save the state to
     */
    public void reload(String path) {
        ensureFileExistence(path);

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            properties.load(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Restores the {@code MeepMeep} state from the {@code Properties} object.
     *
     * <p>At the moment, it only restores the window position of the {@link MeepMeep} GUI window.
     * However, the code is fully extensible, and other desired settings may be restored.
     */
    public void restore() {
        meepMeep.getWindowFrame().setLocation(
            Integer.parseInt(properties.getProperty("windows_x")),
            Integer.parseInt(properties.getProperty("windows_y"))
        );
    }

    /**
     * Ensures that the given file path exists. Creates the file if not.
     *
     * @param path The file path to ensure exists
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void ensureFileExistence(String path) {
        try {
            new File(path).createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
