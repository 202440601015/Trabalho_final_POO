package domain;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import exceptions.*;

public abstract class Midia extends Worker {
    public Midia() {
        super.tools.add("ffmpeg");
    }

    public void changeType(Archive archive, String type) throws IllegalPathException {

        String newName = nameCheck(archive, type);
        try {
            ProcessBuilder command = new ProcessBuilder(
                    tools.get(1),
                    "-i",
                    archive.getPath(),
                    newName);

            command.redirectErrorStream(true);
            Process thread = command.start();
            Process sla = thread.onExit().get();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(thread.getInputStream()));

            StringBuilder output = new StringBuilder();
            String text;

            while ((text = reader.readLine()) != null) {
                output.append(text).append("\n");
            }

            if (sla.exitValue() != 0) {
                throw new IllegalPathException("Path error: " + output);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
        }

    }

    public void concatenate(Archive archive1, Archive archive2) {

    }

    public void resize(Archive archive, String size) {

    }

    private String nameCheck(Archive archive, String type) {

        String output = "";
        String newName = "";

        try {
            ProcessBuilder command = new ProcessBuilder(
                    tools.get(0),
                    "-FileTypeExtension",
                    "-s4",
                    archive.getPath());

            command.redirectErrorStream(true);
            Process thread = command.start();
            Process sla = thread.onExit().get();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(thread.getInputStream()));

            String text;
            while ((text = reader.readLine()) != null) {
                output += text;
            }

            if (sla.exitValue() != 0) {
                throw new IllegalPathException("Path error: " + output);
            }

            if (archive.getPath().endsWith(output)) {
                newName = archive.getPath().replace(output, type);
            } else {
                newName = archive.getPath().concat("." + type);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }

        return newName;

    }

}
