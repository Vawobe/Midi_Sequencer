package vawobe.save;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.*;
import java.nio.file.Paths;

import static vawobe.Main.mainPane;

public class MP3Exporter {
    public static void convertWavToMp3(File wavFile, File mp3File) throws IOException, InterruptedException {
        String ffmpegPath = Paths.get("ffmpeg", "ffmpeg.exe").toAbsolutePath().toString();
        ProcessBuilder pb = new ProcessBuilder(
                ffmpegPath,
                "-y",
                "-i", wavFile.getAbsolutePath(),
                mp3File.getAbsolutePath()
        );

        Process process = pb.start();

        Thread outputThread = getOutputThread(process.getInputStream());

        Thread errorThread = getErrorThread(process);

        int exitCode = process.waitFor();

        outputThread.join();
        errorThread.join();

        if (exitCode != 0) {
            Notifications.create()
                    .title("Fehler beim MP3 Export")
                    .text("ffmpeg wurde mit Exit-Code " + exitCode + " beendet.")
                    .owner(mainPane.getScene().getWindow())
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.BOTTOM_RIGHT)
                    .showInformation();
            throw new IOException("ffmpeg wurde mit Exit-Code " + exitCode + " beendet.");
        }
    }

    private static Thread getErrorThread(Process process) {
        Thread errorThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[ffmpeg stderr] " + line);
                }
            } catch (IOException e) {
                Notifications.create()
                        .title("Fehler beim MP3 Export")
                        .text("Datei konnte nicht exportiert werden.\n" + e.getMessage())
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            }
        });
        errorThread.start();
        return errorThread;
    }

    private static Thread getOutputThread(InputStream process) {
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[ffmpeg stdout] " + line);
                }
            } catch (IOException e) {
                Notifications.create()
                        .title("Fehler beim MP3 Export")
                        .text("Datei konnte nicht exportiert werden.\n" + e.getMessage())
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            }
        });
        outputThread.start();
        return outputThread;
    }
}
