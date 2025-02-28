package dao.fintness;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.fitness.ExerciseRecord;

public class ExerciseRecordDAO {

    private Connection conn;

    public ExerciseRecordDAO(Connection conn) {
        this.conn = conn;
    }
    

    // 新增運動紀錄
    public boolean addExerciseRecord(ExerciseRecord record) {
        String query = "INSERT INTO exercise_records (user_id, exercise_type, exercise_duration, calories_burned, exercise_date) " +
                       "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, record.getUserId());
            ps.setString(2, record.getExerciseType());  
            ps.setInt(3, record.getExerciseDuration());
            ps.setDouble(4, record.getCaloriesBurned());
            ps.setDate(5, Date.valueOf(record.getExerciseDate()));  
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // 查詢用戶的所有運動紀錄
    public List<ExerciseRecord> getExerciseRecords(int userId) {
        String query = "SELECT * FROM exercise_records WHERE user_id = ?";
        List<ExerciseRecord> records = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExerciseRecord record = new ExerciseRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setUserId(rs.getInt("user_id"));
                    record.setExerciseType(rs.getString("exercise_type"));
                    record.setExerciseDuration(rs.getInt("exercise_duration"));
                    record.setCaloriesBurned(rs.getDouble("calories_burned"));
                    record.setExerciseDate(rs.getDate("exercise_date").toString());  
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    
    //根據record_id查詢單筆運動紀錄
    public ExerciseRecord getExerciseRecordById(int recordId) {
        String query = "SELECT * FROM exercise_records WHERE record_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ExerciseRecord record = new ExerciseRecord();
                    record.setRecordId(rs.getInt("record_id"));
                    record.setUserId(rs.getInt("user_id"));
                    record.setExerciseType(rs.getString("exercise_type"));
                    record.setExerciseDuration(rs.getInt("exercise_duration"));
                    record.setCaloriesBurned(rs.getDouble("calories_burned"));
                    record.setExerciseDate(rs.getDate("exercise_date").toString());  
                    return record;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // 如果找不到紀錄
    }



    // 更新運動紀錄
    public boolean updateExerciseRecord(ExerciseRecord record) {
        String query = "UPDATE exercise_records SET exercise_type = ?, exercise_duration = ?, calories_burned = ?, exercise_date = ? WHERE record_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, record.getExerciseType());
            ps.setInt(2, record.getExerciseDuration());
            ps.setDouble(3, record.getCaloriesBurned());
            ps.setDate(4, Date.valueOf(record.getExerciseDate()));
            ps.setInt(5, record.getRecordId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // 刪除運動紀錄
    public boolean deleteExerciseRecord(int recordId) {
        String query = "DELETE FROM exercise_records WHERE record_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, recordId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


 // 取得運動類型的 METS 值
    public double getMETSValue(String exerciseType) throws SQLException {
        String query = "SELECT MET FROM exercise_type_coefficients WHERE ExerciseName = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, exerciseType);  // 使用運動名稱
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("MET");
                }
            }
        }
        return 0;  // 如果沒有找到對應的 METS 值，返回 0
    }

}
