package domain;
import java.util.Stack;

public class Archive {
    private String path;
    private String type;
    private String metaData;
    private Stack<String> undoMetaData;
    private Stack<String> redoMetaData;

    public Archive(String path) {
        this.path = path;
        undoMetaData = new Stack<>();
        redoMetaData = new Stack<>();

    }

    public void setUndoMetaData(String undoMetaData) {
        this.undoMetaData.push(undoMetaData);
    }

    public String getUndoMetaData() {
        return undoMetaData.pop();
    }

    public void setRedoMetaData(String redoMetaData) {
        this.redoMetaData.push(redoMetaData);
    }

    public String getRedoMetaData() {
        return redoMetaData.pop();
    }

    public String getPath() {
        return path;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public String getMetaData() {
        return metaData;
    }

    public void clearRedo(){
        redoMetaData.clear();
    }
}
