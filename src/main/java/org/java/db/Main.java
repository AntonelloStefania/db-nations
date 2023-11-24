package org.java.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main {
	private static final String url = "jdbc:mysql://localhost:3306/db_nations";
	private static final String user = "root";
	private static final String pws = "";
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("filter your search by country's name: ");
		
		String search = in.nextLine();
		
		try (Connection con 
			      = DriverManager.getConnection(url, user, pws)) {  
			  
			  final String sql = "SELECT countries.name , countries.country_id, regions.name, continents.name "
					 +" FROM countries "
					 +" JOIN regions "
					 +" ON countries.region_id = regions.region_id "
					 +" JOIN continents"
					 +" ON regions.continent_id = continents.continent_id "
					 +" WHERE countries.name LIKE ? "
					 +" ORDER BY countries.name ASC "
					 +" ; ";
			  
			  try(PreparedStatement ps = con.prepareStatement(sql)){
				  ps.setString(1, "%"+search+"%");
			    try(ResultSet rs = ps.executeQuery()){
			    	while(rs.next()) {
			    		
			    		String name = rs.getString(1);
			    		int id = rs.getInt(2);
			    		String regionsName = rs.getString(3);
			    		String continentName = rs.getString(4);
			    		
			    		
			    		System.out.println("country Name: " + name + "\n" 
			    						+ "country Id: " + id + "\n"
			    						+ "region Name: " + regionsName +"\n"
			    						+ "continent Name: " + continentName 
			    						);
			    						
			    	}
			    }
			  }
			} catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
			}
	}
}
