package service;

import bean.User;
import dao.UserDAO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserService {

    private static final String JNDI_NAME = "java:/comp/env/jdbc/HealthManagement"; 

    // 獲取資料庫連接
    private Connection getConnection() {
        try {
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup(JNDI_NAME);
            return ds.getConnection();
        } catch (NamingException | SQLException e) {
            throw new RuntimeException("Error getting database connection", e);
        }
    }

    // 獲取所有用戶
    public List<User> getAllUsers() {
        try (Connection conn = getConnection()) {
            UserDAO dao = new UserDAO(conn);
            return dao.getAllUsers();  // 呼叫 DAO 層的查詢方法
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error retrieving users", e);
        }
    }

    // 根據 userId 查找用戶
    public User getUserById(int userId) {
        try (Connection conn = getConnection()) {
            UserDAO dao = new UserDAO(conn);
            List<User> users = dao.getAllUsers();
            for (User user : users) {
                if (user.getUserId() == userId) {
                    return user;
                }
            }
            return null; // 如果找不到用戶，返回 null
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException("Error retrieving user by ID", e);
        }
    }
}
