package byfr0n.whipslogs;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Whipslogs implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("Whipslogs");
    public static File logFile;

    @Override
    public void onInitialize() {
        setupLogFile();
        LOGGER.info("Whipslogs initialized");
    }

    private void setupLogFile() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = "whipslogs-" + date + ".txt";
        logFile = new File(fileName);

        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
                writeToLog("whipslogs started: " + date);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create log file: " + e.getMessage());
        }
    }

    public static void writeToLog(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            LOGGER.error("Failed to write to log file: " + e.getMessage());
        }
    }
}