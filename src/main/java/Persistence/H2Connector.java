/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistence;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

/**
 *
 * @author Nico
 */
public class H2Connector {
    
    private Connection conn;
    
    public H2Connector() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("Scripts/create_tables.sql"));
            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = br.readLine()) != null) sql.append(line);
            
            Statement smt = this.conn.createStatement();
            
            smt.execute(sql.toString());
            
        } catch (Exception ex) {
            
        }
        
    }
}
