package vawobe.save;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MP3Exporter {
    public static void convertWavToMp3(File wavFile, File mp3File) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "C:\\Program Files\\Java\\ffmpeg-7.1.1-essentials_build\\bin\\ffmpeg.exe",
                "-y",
                "-i", wavFile.getAbsolutePath(),
                mp3File.getAbsolutePath()
        );

        Process process = pb.start();

        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[ffmpeg stdout] " + line);
                }
            } catch (IOException e) {
                System.err.println("Fehler beim MP3 Export: " + e.getMessage());
            }
        });
        outputThread.start();

        Thread errorThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[ffmpeg stderr] " + line);
                }
            } catch (IOException e) {
                System.err.println("Fehler beim MP3 Export: " + e.getMessage());
            }
        });
        errorThread.start();

        int exitCode = process.waitFor();

        outputThread.join();
        errorThread.join();

        if (exitCode != 0) {
            throw new IOException("ffmpeg wurde mit Exit-Code " + exitCode + " beendet.");
        }
    }
}
