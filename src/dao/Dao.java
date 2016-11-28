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

import kohdeluokat.Asiakas;
import kohdeluokat.Kirja;
import kohdeluokat.Lainaus;
import kohdeluokat.Nide;
import kohdeluokat.NideLainaus;
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
	
	protected static void suljeYhteys(ResultSet rs, Statement stmt, Connection conn) {
		
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
	
	protected static void suljeYhteys(Statement stmt, Connection connection) {
		suljeYhteys (null, stmt, connection);
	}
	
	private Lainaus read(ResultSet rs) {
		Date palautusPvm = null;
		NideLainaus nideLainaus = null;
		try {
			Lainaus lainaus = new Lainaus();
			int nro = rs.getInt("numero");
			Date lainausPvm = rs.getDate("lainauspvm");
			lainaus.setNumero(nro);
			lainaus.setLainausPvm(lainausPvm);
			
			int asiakasNro = rs.getInt("asiakasnro");
			String etuNimi = rs.getString("etunimi");
			String sukuNimi = rs.getString("sukunimi");
			String osoite = rs.getString("osoite");
			String postiNro = rs.getString("postinro");
			String postiTmp = rs.getString("postitmp");
			Asiakas lainaaja = new Asiakas(asiakasNro, etuNimi, sukuNimi, osoite, new PostinumeroAlue(postiNro,postiTmp));
			lainaus.setLainaaja(lainaaja);
			
			String isbn = rs.getString("isbn");
			String nimi = rs.getString("nimi");
			String kirjoittaja = rs.getString("kirjoittaja");
			String painos = rs.getString("painos");
			String kustantaja = rs.getString("kustantaja");
			Kirja kirja = new Kirja(isbn, nimi, kirjoittaja, painos, kustantaja);
			
			int nideNro = rs.getInt("nidenro");
			Nide nide = new Nide(kirja, nideNro);	
			
			while(rs.next()) {
				palautusPvm = rs.getDate("palautuspvm");
				nideLainaus = new NideLainaus(nide, palautusPvm);
				lainaus.addNiteenLainaus(nideLainaus);					
			}rs.close();
			System.out.println("dao: "+lainaus);
			return lainaus;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private int readLainausNro(ResultSet rs){
		try{
			
			int numero = rs.getInt("numero");
						
			return numero;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
		
	}
	
	public ArrayList<Integer> haeLainausNrot(){
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int nro;
		ArrayList<Integer> nrot = new ArrayList<Integer>();
		
		try{
			yhteys = yhdista();
			
			String sqlSelect = "select numero from lainaus;";
			stmt = yhteys.prepareStatement(sqlSelect);
			
			rs=stmt.executeQuery(sqlSelect);
			
			yhteys.commit();
			yhteys.close();
			
			while(rs.next()) {
				nro = readLainausNro(rs);
				nrot.add(nro);						
			}rs.close();	
			
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			suljeYhteys(rs,stmt,yhteys);
		}
		
		
		
		return nrot;
		
	}
	
	public ArrayList <Lainaus> haeKaikki(){
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Lainaus> lainat = new ArrayList<Lainaus>();
		Lainaus lainaus = null;
		
		try{
			yhteys = yhdista();
			
			yhteys.setAutoCommit(false);
	 		yhteys.setReadOnly(true);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			String sqlSelect = "select l.numero, l.lainauspvm, l.asiakasnro, a.etunimi, a.sukunimi, a.osoite, a.postinro, p.postitmp, nl.isbn, k.nimi, k.kirjoittaja, k.painos, k.kustantaja, nl.nidenro, nl.palautuspvm"
					+ " from lainaus l join asiakas a on a.numero=l.asiakasnro join postinumeroalue p on a.postinro = p.postinro"
					+ " join nidelainaus nl on nl.lainausnro = l.numero join nide n on n.nidenro = nl.nidenro"
					+ " join nide n2 on n2.isbn = nl.isbn join kirja k on k.isbn = n.isbn"
					+ " order by l.numero;";	
			
			stmt = yhteys.prepareStatement(sqlSelect);
			
			rs=stmt.executeQuery(sqlSelect);
			
			yhteys.commit();
			yhteys.close();
			
			while(rs.next()) {
				lainaus = read(rs);
				lainat.add(lainaus);						
			}rs.close();	
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			suljeYhteys(rs,stmt,yhteys);
		}		
		return lainat;		
	}
	
	public Lainaus hae(int lainaId){
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Lainaus lainaus = null;
		
		try{
			yhteys = yhdista();
			
			yhteys.setAutoCommit(false);
	 		yhteys.setReadOnly(true);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			String sqlSelect = "select l.numero, l.lainauspvm, l.asiakasnro, a.numero, a.etunimi, a.sukunimi, a.osoite, a.postinro, p.postitmp, nl.isbn, k.nimi, k.kirjoittaja, k.painos, k.kustantaja, nl.nidenro, nl.palautuspvm"
								+ " from lainaus l join asiakas a ona.numero=l.numero join postinumeroalue p on a.postinro = p.postinro"
								+ " join nidelainaus nl on nl.lainausnro = l.numero join nide n on n.nidenro = nl.nidenro"
								+ " join nide n2 on n2.isbn = nl.isbn join kirja k on k.isbn = n.isbn"
								+ " where l.numero =?"
								+ " order by l.lainauspvm;";
			stmt = yhteys.prepareStatement(sqlSelect);
		
			stmt.setInt(1, lainaId);
			
			rs=stmt.executeQuery(sqlSelect);
			
			yhteys.commit();
			yhteys.close();
			
			lainaus = read(rs);
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			suljeYhteys(rs,stmt,yhteys);
		}		
		return lainaus;		
	}
	
}

	                  
			