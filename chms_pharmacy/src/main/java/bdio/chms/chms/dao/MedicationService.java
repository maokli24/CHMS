package bdio.chms.chms.dao;

import bdio.chms.chms.models.Medicament;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicationService {

    private static final Logger LOGGER = Logger.getLogger(MedicationService.class.getName());

    // Method to add a new medicament to the database
    public void addMedication(Medicament medicament) {
        String sql = "INSERT INTO medicament (NomMed, Prix_Unit, DescMed,DateExpiration, Type, Qty) VALUES (?, ?,?,?, ?, ?)";

        if (!isValidDate(medicament.getExpirationTime())) {
            throw new IllegalArgumentException("Invalid date format for expiration time: " + medicament.getExpirationTime());
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            //stmt.setString(1, medicament.getId());
            stmt.setString(1, medicament.getName());
            stmt.setDouble(2, medicament.getPrice());
            stmt.setString(3, medicament.getDescription());
            stmt.setDate(4, Date.valueOf(medicament.getExpirationTime())); // Convert to SQL Date
            stmt.setString(5, medicament.getType());
            stmt.setInt(6, 0); // Quantité par défaut à 0

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding medication", e);
        }
    }
    public Medicament getMedicationById(int id) throws SQLException {
        Medicament med = null;
        String query = "SELECT NomMed FROM medicament WHERE IDMedicament = ?";
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

    public boolean isValidDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Method to get all medications from the database
    public List<Medicament> getAllMedications() {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medicament medicament = new Medicament(
                        rs.getString("IDMedicament"),
                        rs.getString("NomMed"),
                        rs.getDouble("Prix_Unit"),
                        rs.getString("DescMed"),
                        rs.getString("DateExpiration"),
                        rs.getString("Type"),
                        rs.getInt("Qty") // Récupération de la quantité
                );
                medicaments.add(medicament);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving medications", e);
        }
        return medicaments;
    }

    // Method to remove a medication by its ID from the database
    public void removeMedication(String id) {
        String sql = "DELETE FROM medicament WHERE IDMedicament = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing medication", e);
        }
    }

    // Method to update an existing medication in the database
    public void updateMedication(Medicament updatedMedicament) {
        String sql = "UPDATE medicament SET NomMed = ?, Prix_Unit = ?, DescMed = ?, DateExpiration = ?, Type = ?, Qty = ? WHERE IDMedicament = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, updatedMedicament.getName());
            stmt.setDouble(2, updatedMedicament.getPrice());
            stmt.setString(3, updatedMedicament.getDescription());
            stmt.setDate(4, Date.valueOf(updatedMedicament.getExpirationTime())); // Convert to SQL Date
            stmt.setString(5, updatedMedicament.getType());
            stmt.setInt(6, updatedMedicament.getQuantite()); // Mise à jour de la quantité
            stmt.setString(7, updatedMedicament.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating medication", e);
        }
    }
    public void updateQuantite(String id, int nouvelleQuantite) {
        String sql = "UPDATE medicament SET Qty = ? WHERE IDMedicament = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, nouvelleQuantite);
            stmt.setString(2, id);
            stmt.executeUpdate();
            System.out.println("Quantité mise à jour avec succès dans la base de données!");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating quantity", e);
        }
    }
}
