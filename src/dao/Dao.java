package dao;

import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kohdeluokat.Asiakas;
import kohdeluokat.PostinumeroAlue;


public class Dao {

	private Connection yhdista() throws SQLException {
		Connection tietokantayhteys = null;

		String JDBCAjuri = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://localhost/projekti";

		try {
			Class.forName(JDBCAjuri).newInstance(); // ajurin m‰‰ritys

			// otetaan yhteys tietokantaan
			tietokantayhteys = DriverManager.getConnection(url, "projekti",
					"seDEU297u"); 

			// yhteyden otto onnistu
		} catch (SQLException sqlE) {
			System.err.println("Tietokantayhteyden avaaminen ei onnistunut. "
					+ url + "\n" + sqlE.getMessage() + " " + sqlE.toString()
					+ "\n");
			throw (sqlE);
		} catch (Exception e) {
			System.err.println("TIETOKANTALIITTYN VIRHETILANNE: "
					+ "JDBC:n omaa tietokanta-ajuria ei loydy.\n\n"
					+ e.getMessage() + " " + e.toString() + "\n");
			e.printStackTrace();
			System.out.print("\n");
			throw (new SQLException("Tietokanta-ajuria ei loydy!"));
		}
		return tietokantayhteys;
	}
	
}

	                  
			