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
import java.sql.Statement;

import pizzicato.model.Pizza;
import pizzicato.model.dao.Exception;
import pizzicato.model.dao.RuntimeException;
import pizzicato.model.dao.Statement;
import pizzicato.model.dao.String;
import kohdeluokat.Asiakas;
import kohdeluokat.Lainaus;
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
	
	protected static void close(ResultSet rs, Statement stmt, Connection conn) {
		
		try {
			if (rs !=null) {
				rs.close();
			}
			if (stmt !=null) {
				stmt.close();
			}
			
			if (conn !=null) {
				conn.close();
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static void close(Statement stmt, Connection connection) {
		close (null, stmt, connection);
	}
	
	private Lainaus read(ResultSet rs) {
		try {
			int numero = rs.getInt("numero");
			Date lainausPvm = rs.getDate("lainauspvm");
			int asiakasNro = rs.getInt("asiakasnro");
			return new Lainaus(numero, lainausPvm, asiakasNro);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ArrayList <Lainaus> haeKaikki(){
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Lainaus> lainat = new ArrayList<Lainaus>();
		Lainaus lainaus = null;
		
		try{
			yhteys = yhdista();
			String sqlSelect = "";
			stmt = yhteys.prepareStatement(sqlSelect);
			rs=stmt.executeQuery(sqlSelect);
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			close(rs,stmt,yhteys);
		}
		
		
		
		return lainat;
		
	}
	
}

	                  
			