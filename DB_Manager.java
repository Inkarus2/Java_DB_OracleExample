import java.sql.*;

public class DB_Manager {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
    private static final String DB_USER = "USER";
    private static final String DB_PASSWORD = "PW";

    public static void AddEnergyData(int meterId, double consumed, double generated) {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (con == null) {
                return;
            }
            String sqlString = "INSERT INTO EnergyData (meter_id, consumption_kwh, generation_kwh) VALUES (?,?,?)";
            try (PreparedStatement sqlQuery = con.prepareStatement(sqlString)) {
                sqlQuery.setInt(1, meterId);
                sqlQuery.setDouble(2, consumed);
                sqlQuery.setDouble(3, generated);
                sqlQuery.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double GetAverageConsumption(int meterId) {
        return GetAverage(meterId, "consumption_kwh");
    }

    public static double GetAverageGeneration(int meterId) {
        return GetAverage(meterId, "generation_kwh");
    }

    public static double GetAverage(int meterId, String columnName) {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (con == null) {
                return 0;
            }

            long elapsedTime = GetElapsedTime(meterId, con);
            if (elapsedTime == 0) {
                return 0;
            }

            String sqlString = "SELECT SUM(" + columnName + ") AS total FROM EnergyData WHERE meter_id = ?";
            try (PreparedStatement sqlQuery = con.prepareStatement(sqlString)) {
                sqlQuery.setInt(1, meterId);
                ResultSet results = sqlQuery.executeQuery();
                results.next();
                double total = results.getDouble("total");
                return total / elapsedTime;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static long GetElapsedTime(int meterId, Connection con) {
        String sqlString = "SELECT timestamp FROM EnergyData WHERE meter_id = ?";
        try (PreparedStatement sqlQuery = con.prepareStatement(sqlString)) {
            sqlQuery.setInt(1, meterId);
            ResultSet results = sqlQuery.executeQuery();
            Timestamp startTime = new Timestamp(System.currentTimeMillis());
            Timestamp endTime = Timestamp.valueOf("1970-01-01 00:00:00");
            while (results.next()) {
                Timestamp time = results.getTimestamp("timestamp");
                if (startTime.after(time)) {
                    startTime = time;
                }

                if (endTime.before(time)) {
                    endTime = time;
                }
            }
            return (endTime.getTime() - startTime.getTime()) / 1000;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
