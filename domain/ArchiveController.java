package domain;
import technicalservices.*;

public class ArchiveController{
    private ArchiveDAO aDAO = new ArchiveDAO();

    public void save (Archive doc){
        aDAO.save(doc);
    }

    public boolean delete(Archive doc){
        return aDAO.delete(doc);
    }

    public void update(Archive doc){
        aDAO.update(doc);
    }

}