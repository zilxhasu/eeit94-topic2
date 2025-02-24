package service.fitness;

import model.fitness.ExerciseRecord;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import dao.fintness.ExerciseRecordDAO;

import java.sql.*;
import java.util.List;

public class ExerciseRecordService {

    private static final String JNDI_NAME = "java:/comp/env/jdbc/HealthManagement"; 

    // 獲取資料庫連接
    public Connection getConnection() throws SQLException, NamingException {
        InitialContext context = new InitialContext();
        DataSource ds = (DataSource) context.lookup(JNDI_NAME);
        return ds.getConnection();
    }

    // 新增運動紀錄
    public boolean addExerciseRecord(ExerciseRecord record) throws SQLException, NamingException {
        double caloriesBurned = calculateCaloriesBurned(record);
        record.setCaloriesBurned(caloriesBurned); // 設定計算出的卡路里

        try (Connection conn = getConnection()) {
            ExerciseRecordDAO dao = new ExerciseRecordDAO(conn);
            return dao.addExerciseRecord(record); // 呼叫 DAO 層的新增方法
        }
    }

    // 更新運動紀錄
    public boolean updateExerciseRecord(ExerciseRecord record) throws SQLException, NamingException {
        double caloriesBurned = calculateCaloriesBurned(record);
        record.setCaloriesBurned(caloriesBurned); // 設定計算出的卡路里

        try (Connection conn = getConnection()) {
            ExerciseRecordDAO dao = new ExerciseRecordDAO(conn);
            return dao.updateExerciseRecord(record); // 呼叫 DAO 層的更新方法
        }
    }

    // 計算卡路里
    public double calculateCaloriesBurned(ExerciseRecord record) throws SQLException, NamingException {
        try {
            // 取得用戶體重
            double userWeight = getUserWeight(record.getUserId()); // 改為 double
            
            if (userWeight == 0) {
                throw new SQLException("User weight is zero or invalid!");
            }

            // 取得運動類型的 METS 值
            try (Connection conn = getConnection()) {
                ExerciseRecordDAO dao = new ExerciseRecordDAO(conn);
                double metValue = dao.getMETSValue(record.getExerciseType());

                if (metValue == 0) {
                    throw new SQLException("METS value not found for exercise type: " + record.getExerciseType());
                }

                // 計算卡路里
                double exerciseDurationInHours = record.getExerciseDuration() / 60.0; // 運動時間轉換為小時
                return userWeight * metValue * exerciseDurationInHours;
            }

        } catch (SQLException e) {
            System.out.println("Error calculating calories: " + e.getMessage());
            throw e;
        }
    }

    // 獲取用戶體重
    private double getUserWeight(int userId) throws SQLException, NamingException {
        String query = "SELECT Weight FROM BodyMetrics WHERE UserID = ?";
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Weight");
                } else {
                    throw new SQLException("No weight found for user ID: " + userId);
                }
            }
        }
    }

    // 刪除運動紀錄
    public boolean deleteExerciseRecord(int recordId) throws SQLException, NamingException {
        try (Connection conn = getConnection()) {
            ExerciseRecordDAO dao = new ExerciseRecordDAO(conn);
            return dao.deleteExerciseRecord(recordId);
        }
    }

    // 查詢用戶的所有運動紀錄
    public List<ExerciseRecord> getExerciseRecords(int userId) throws SQLException, NamingException {
        try (Connection conn = getConnection()) {
            ExerciseRecordDAO dao = new ExerciseRecordDAO(conn);
            return dao.getExerciseRecords(userId);
        }
    }
}
