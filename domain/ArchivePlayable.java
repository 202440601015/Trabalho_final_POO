package domain;
public interface ArchivePlayable {

    public void changeType(Archive archive);
    public void concatenate(Archive archive1, Archive archive2);
    public void resize(Archive archive, String size);
}
