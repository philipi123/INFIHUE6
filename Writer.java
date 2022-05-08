package json_reader_writer;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import org.json.simple.JSONObject;

public class Writer {
	public static Connection createConnection() {

		try {
			return DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
	}
	
	public static void main(String[] args) {
		try {

			Connection c = createConnection();

			Autos.dropAutos(c);
			Autos.createAutos(c);
			Autos.insertAutos(c, "Mercedes Benz", 2008, 3500.00);
			
			Autos.write(c, "/Users/philip/Desktop/HTL/SWP-Greinöcker/ECLIPSE/Infi/src/json_reader_writer/json_writer.txt");
			
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	static class Autos {
		
		public static void dropAutos(Connection c) {
			try {
				Statement stmt = c.createStatement();
				String sql = "drop table if exists Autos;";
				stmt.executeUpdate(sql);
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static void createAutos(Connection c) {
			try {
				Statement stmt = c.createStatement();
				String sql = "create table if not exists Autos (autoname varchar(30), baujahr int, preis double);"; 
				stmt.executeUpdate(sql);
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				 
			}
		}
		
		public static void insertAutos(Connection c, String autoname, int baujahr, double preis) {
			try {
				 String s = "insert into Autos (autoname, baujahr, preis) values (?, ?, ?);";
				 PreparedStatement stmt = c.prepareStatement(s);
				 
				 stmt.setString(1, autoname);
				 stmt.setInt(2, baujahr);
				 stmt.setDouble(3, preis);
				 stmt.executeUpdate();
				 stmt.close();
				 
			} catch (SQLException e) {
				e.printStackTrace();
			}
				
		}
		@SuppressWarnings("unchecked") //unchecked: man hat nicht nachgeschaut wegen schlüssel ?? -->json dokumentation nachsehen
		public static void write(Connection c, String file) {
			try {
				FileWriter f = new FileWriter(file);
				JSONObject json = new JSONObject();
				String s = "";
				
				Statement s1 = c.createStatement();
				String sql = "select autoname, baujahr, preis from Autos;";
				ResultSet rs = s1.executeQuery(sql);
				
				while (rs.next()) {
					String autoname = rs.getString("autoname");
					int baujahr = rs.getInt("baujahr");
					double preis = rs.getDouble("preis");
					
					json.put("autoname", autoname);
					json.put("baujahr", baujahr);
					json.put("preis", preis);
					s = s+json;
				}
				f.write(s);
				f.flush();
				f.close();
				rs.close();
				System.out.println("file created");
				
			} catch (SQLException e){
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
}
