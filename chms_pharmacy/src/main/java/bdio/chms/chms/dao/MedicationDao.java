package bdio.chms.chms.dao;

import bdio.chms.chms.models.Medicament;
import bdio.chms.chms.dao.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;




public class MedicationDao {

    public Medicament getMedicationById(int id) throws SQLException {
        Medicament med = null;
        String query = "SELECT nomMed FROM medicament WHERE IDMedicament = ?";
        try (PreparedStatement preparedStatement = DatabaseConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    med = new Medicament(
                            resultSet.getString("NomMed")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return med;
    }

    public List<Medicament> getAllMedications() {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicament medicament = new Medicament(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getString("expiration_time"),
                        rs.getString("type"),
                        rs.getInt("quantite") // Récupération de la quantité
                );
                medicaments.add(medicament);
            }
        } catch (SQLException e) {

        }
        return medicaments;
    }
}
