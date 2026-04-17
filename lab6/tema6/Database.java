package lab6.tema6;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/movies_db");
        config.setUsername("postgres");
        config.setPassword("password");//aici trb sa schimb
        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);
        
        dataSource = new HikariDataSource(config);
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}