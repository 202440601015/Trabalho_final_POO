package technicalservices;
import technicalservices.Connect;
import domain.Archive;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArchiveDAO {
    public void save(Archive doc) {
        String sql = "INSERT INTO meta_dados (path, type, metaData) VALUES (?, ?, ?)";
        try (
                Connection conn = Connect.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, doc.getPath());
            stmt.setString(2, doc.getType());
            stmt.setString(3, doc.getMetaData());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public List<Archive> listAll() {
        List<Archive> list = new ArrayList<>();

        String sql = "SELECT * FROM meta_dados";

        try (
                Connection conn = Connect.getConnection(); // Abre a conexão
                Statement stmt = conn.createStatement();   // Cria um statement simples
                ResultSet rs = stmt.executeQuery(sql)      // Executa a consulta e obtém os resultados
        ) {
            // Enquanto houver registros no resultado da consulta
            while (rs.next()) {
                
                Archive doc = new Archive(rs.getString("path"));

                doc.setMetaData(rs.getString("metaData"));
                doc.setType(rs.getString("type"));
                list.add(doc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean delete(Archive doc) {
    String sql = "DELETE FROM meta_dados WHERE path = ?";
    
    try (
            Connection conn = Connect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setString(1, doc.getPath());
        
        int rowsAffected = stmt.executeUpdate();
        
        // Retorna true se exatamente uma linha foi afetada
        return rowsAffected == 1;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

public boolean update(Archive doc) {
    String sql = "UPDATE meta_dados SET metaData = ? WHERE path = ?";
    
    try (
            Connection conn = Connect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
    ) {
        stmt.setString(1, doc.getMetaData());
        stmt.setString(2, doc.getPath());
        
        int rowsAffected = stmt.executeUpdate();
        
        // Retorna true se exatamente uma linha foi afetada
        return rowsAffected == 1;
        
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
