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
		System.out.print("filter your search by country's name: ");
		
		String search = in.nextLine();
		System.out.println("\n");
		
		try (Connection con 
			      = DriverManager.getConnection(url, user, pws)) {  
			  
			  final String SQL = "SELECT countries.name , countries.country_id, regions.name, continents.name "
					 +" FROM countries "
					 +" JOIN regions "
					 +" ON countries.region_id = regions.region_id "
					 +" JOIN continents"
					 +" ON regions.continent_id = continents.continent_id "
					 +" WHERE countries.name LIKE ? "
					 +" ORDER BY countries.name ASC "
					 +" ; ";
			  
			  final String SQL2 = "SELECT  languages.language"
					  +" FROM countries"
					  + " JOIN country_languages"
					  +" ON countries.country_id = country_languages.country_id"
					  +" JOIN languages"
					  +" ON country_languages.language_id = languages.language_id"
					  +" WHERE countries.country_id = ?"
					  +";";
			  
			  final String SQL3 = "SELECT country_stats.year, country_stats.population, country_stats.gdp"
					  +" FROM countries"
					  +" JOIN country_stats"
					  +" ON countries.country_id = country_stats.country_id"
					  +" WHERE countries.country_id = ?"
					  +" ORDER BY country_stats.year DESC"
					  +" LIMIT 1"
					  +";";
			  
			  final String SQL4 = "SELECT countries.name"
					  			+ " FROM countries"
					  			+ " WHERE countries.country_id = ?"
					  			+ ";";
			  
			  try(PreparedStatement ps = con.prepareStatement(SQL)){
				  ps.setString(1, "%"+search+"%");
			    try(ResultSet rs = ps.executeQuery()){
			    	 int idLength = 10;
			         int nameLength = 20;
			         int regionLength = 20;
			         int continentLength = 20;
			         String format = " %-"+idLength+"s  %-"+nameLength+"s  %-"+regionLength+"s  %-"+continentLength+"s \n";
			         
			         System.out.format(" ID          COUNTRY                REGION             CONTINENT       \n");
			         
			    	while(rs.next()) {
			    		
			    		String name = rs.getString(1);
			    		int id = rs.getInt(2);
			    		String regionsName = rs.getString(3);
			    		String continentName = rs.getString(4);
			    		
			    		
			    		System.out.format(format, id, name, regionsName, continentName);

			    	}
			    	
			    }
			  }
			  System.out.println("\n\n");
			  System.out.print("filter your search by nation's id ");
			  String strAnsId = in.nextLine();
			  int ansId = Integer.valueOf(strAnsId);
			  
			  try(PreparedStatement ps= con.prepareStatement(SQL4)){
				  ps.setInt(1, ansId);
				  try(ResultSet rs = ps.executeQuery()){
					  while(rs.next()) {
						  String name = rs.getString(1);
						  System.out.println("\nDetails for country: " + name);
					  }
				  }
			  }
			  
			  try(PreparedStatement ps = con.prepareStatement(SQL2)){
				  ps.setInt(1, ansId);
				  try(ResultSet rs = ps.executeQuery()){
					  
					  System.out.print("spoken languages: " );
					  while(rs.next()) {
						  
						  String language = rs.getString(1);
						  if (!rs.isLast()) {

	                          System.out.print(language + ", ");

	                      } else {

	                          System.out.print(language);
	                      }
					  }
				  }
			  }
			  
			 
			  try(PreparedStatement ps = con.prepareStatement(SQL3)){
				  ps.setInt(1, ansId);
				  try(ResultSet rs = ps.executeQuery()){
					 System.out.println("\nMost recent stats");
					  while(rs.next()) {
						  
						  int year = rs.getInt(1);
						  String population = rs.getString(2);
						  String gdp = rs.getString(3);
						  
						  
						  System.out.println("Year: " + year +"\n"
								  			+ "Population: " + population +"\n"
								  			+ "GDP: " + gdp
								  );
					  }
				  }
			  }
			  
			} catch (Exception e) {
				
				System.out.println("Error in db: " + e.getMessage());
			}
		in.close();
		
	}
}
