package domain;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import exceptions.*;

public abstract class Worker {
    protected List<String> tools;

    public Worker() {
        tools = new ArrayList<>();
        tools.add("/usr/bin/site_perl/exiftool");
    }

    public String pickMetaData(Archive archive) throws IllegalPathException{
    	String metaData = "";
        try {
            ProcessBuilder command = new ProcessBuilder(
            		tools.get(0),
                    archive.getPath());
            //command.environment().put("PATH", "/usr/local/bin /usr/bin /bin /usr/local/sbin /var/lib/flatpak/exports/bin /usr/lib/jvm/default/bin /usr/bin/site_perl /usr/bin/vendor_perl /usr/bin/core_perl");

            command.redirectErrorStream(true);
            Process exif = command.start();
            Process sla = exif.onExit().get();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exif.getInputStream()));

            String text;
            while ((text = reader.readLine()) != null) {
                metaData += text + "\n";
            }

            if (sla.exitValue() != 0) {
                throw new IllegalPathException("Path error: "+ metaData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }

        archive.setUndoMetaData(pickJsonMetaData(archive));
        return metaData;
    }

    public void changeMetaData(Archive archive, String tag, String value) throws IllegalTagException{

        String paramter = "-" + tag + "=" + value;
        String commandText = "";

        try {

            ProcessBuilder command = new ProcessBuilder(
                    tools.get(0),
                    paramter,
                    archive.getPath());

            command.redirectErrorStream(true);
            Process exif = command.start();
            Process sla = exif.onExit().get();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exif.getInputStream()));

            String text;

            while ((text = reader.readLine()) != null) {
                commandText += text + "\n";
            }

            if (sla.exitValue() != 0) {
                throw new IllegalTagException("Tag error: "+ commandText);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        }

        archive.setMetaData(pickMetaData(archive));
        archive.clearRedo();

    }

    public String pickType(Archive archive) {
        String type = "";

        try {
            ProcessBuilder command = new ProcessBuilder(
                    tools.get(0),
                    "-Filetype",
                    "-s4",
                    archive.getPath());

            command.redirectErrorStream(true);
            Process exif = command.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exif.getInputStream()));

            String text;

            while ((text = reader.readLine()) != null) {
                type += text;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return type;
    }

    public void undoChanges(Archive archive) {
        archive.setRedoMetaData(archive.getUndoMetaData());
        changeMetaDataJson(archive);
        archive.setMetaData(pickMetaData(archive));

    }

    public void redoChanges(Archive archive) {
        archive.setUndoMetaData(archive.getRedoMetaData());
        changeMetaDataJson(archive);
        archive.setMetaData(pickMetaData(archive));

    }

    private void changeMetaDataJson(Archive archive) {
        String jsonString;
        String output = "";

        jsonString = archive.getUndoMetaData();

        try {
            Files.write(Paths.get("/tmp/MetaData.json"), jsonString.getBytes());
        } catch (IOException e) {
            System.err.println("Erro ao criar arquivo: " + e.getMessage());
        }

        try {
            ProcessBuilder command = new ProcessBuilder(
                    tools.get(0),
                    "-json=/tmp/MetaData.json",
                    archive.getPath());

            command.redirectErrorStream(true);
            Process exif = command.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exif.getInputStream()))) {

                String text;

                while ((text = reader.readLine()) != null) {
                    output += text;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(output);

    }

    private String pickJsonMetaData(Archive archive) {
        String json = "";

        try {
            ProcessBuilder command = new ProcessBuilder(
                    tools.get(0),
                    "-G1",
                    "-s",
                    "-struct",
                    "-j",
                    archive.getPath());

            command.redirectErrorStream(true);
            Process exif = command.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(exif.getInputStream()))) {

                String text;

                while ((text = reader.readLine()) != null) {
                    json += text;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

}
