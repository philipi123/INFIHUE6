package json_reader_writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import json_reader_writer.Writer.Autos;

public class Reader {
	
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

			Schueler.dropSchueler(c);
			Schueler.createSchueler(c);
			Schueler.read(c, "/Users/philip/Desktop/HTL/SWP-Greinöcker/ECLIPSE/Infi/src/json_reader_writer/json_reader.txt");
			
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static class Schueler {
		
		public static void dropSchueler(Connection c) {
			try {
				Statement stmt = c.createStatement();
				String sql = "drop table if exists sschueler;";
				stmt.executeUpdate(sql);
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		public static void createSchueler(Connection c) {
			try {
				Statement stmt = c.createStatement();
				String sql = "create table if not exists sschueler (vorname varchar(30), nachname varchar(30), age int, klasse varchar(30));"; 
				stmt.executeUpdate(sql);
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				 
			}
		}
		
		
		static void read(Connection c, String file) {
			try {
				File f = new File(file);
				Scanner scanner = new Scanner(f);
				String t = "";
				
				while (scanner.hasNextLine()) {
					t = scanner.nextLine();
					Object obj = new JSONParser().parse(t);
					JSONObject js = (JSONObject) obj; 
					
					String vorname = (String)js.get("vorname");
					String nachname = (String)js.get("nachname");
					long age = (long)js.get("age");
					String klasse = (String)js.get("klasse");
					
					String sql = "insert into sschueler (vorname, nachname, age, klasse) values (?, ?, ?, ?);";
					
					PreparedStatement stmt = c.prepareStatement(sql);
					stmt.setString(1, vorname);
					stmt.setString(2, nachname); 
					stmt.setLong(3, age); 
					stmt.setString(4, klasse);
					stmt.executeUpdate();
					stmt.close();
					System.out.println("file read");
					System.out.printf("Vorname: %s\nNachname: %s\nAlter: %d\nKlasse: %s\n", vorname, nachname, age, klasse);
					System.out.println();
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Datei nicht gefunden");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
