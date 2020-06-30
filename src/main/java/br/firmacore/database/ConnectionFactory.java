package br.firmacore.database;

import br.firmacore.utils.MessageUtils;
import com.dieselpoint.norm.Database;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory {
    private final String USER = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "firmadata";
    private final String HOST = "localhost";
    private final String PORT = "3306";
    private Database database;

    public ConnectionFactory(){
        this.database = new Database();
        this.database.setMaxPoolSize(10);
        this.database.setJdbcUrl("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE);
        this.database.setUser(USER);
        this.database.setPassword(PASSWORD);
        MessageUtils.messageToConsole("Banco de dados configurado. &a" + (char)0x221A);
    }

    public Database getDatabase() {
        return this.database;
    }

    public boolean containsTable(String tableName){
        try {
            ResultSet set = this.database.getConnection().getMetaData().getTables(null, null, tableName, null);
            while(set.next()){
                String table = set.getString("TABLE_NAME");
                if(table != null && table.equals(tableName)){
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
