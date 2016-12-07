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
	
	private Lainaus teeLainaus(ResultSet rs) throws SQLException {
		Lainaus lainaus = null;
		int numero;
		Date pvm;
		if (rs != null) {
			try {
				// System.out.println(tulosjoukko.getInt("lainausnumero") + " "
				// + tulosjoukko.getString("lainauspvm"));
				numero = rs.getInt("numero");
				pvm = rs.getDate("lainauspvm");

				lainaus = new Lainaus(numero, pvm);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return lainaus;
	}
	
	private Asiakas teeAsiakas(ResultSet rs) throws SQLException {
		Asiakas asiakas = null;
		int numero;
		String etunimi;
		String sukunimi, osoite;
		String postinro;
		String postitmp;
		PostinumeroAlue posti = null;

		if (rs != null) {
			try {
				numero = rs.getInt("numero");
				etunimi = rs.getString("etunimi");
				sukunimi = rs.getString("sukunimi");
				osoite = rs.getString("osoite");
				postinro = rs.getString("postinro");
				postitmp = rs.getString("postitmp");
				posti = new PostinumeroAlue(postinro, postitmp);
				asiakas = new Asiakas(numero, etunimi, sukunimi, osoite, posti);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return asiakas;
	}
	
	private NideLainaus teeNideLainaus(ResultSet rs) throws SQLException {
		NideLainaus nidelainaus = null;
		Kirja kirja = null;
		Nide nide = null;
		String nimi, kirjoittaja, kustantaja, isbn;
		String painos;
		int nidenro;
		Date pvm = null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

		if (rs != null) {
			try {
				isbn = rs.getString("isbn");
				nimi = rs.getString("nimi");
				kirjoittaja = rs.getString("kirjoittaja");
				painos = rs.getString("painos");
				kustantaja = rs.getString("kustantaja");
				nidenro = rs.getInt("nidenro");
				kirja = new Kirja(isbn, nimi, kirjoittaja, painos, kustantaja);
				nide = new Nide(kirja, nidenro);
				pvm = rs.getDate("palautuspvm");

				nidelainaus = new NideLainaus(nide, pvm);

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return nidelainaus;

	}	
	
	private Nide teeNide(ResultSet rs) throws SQLException {
		Nide nide = null;
		Kirja kirja = null;
		String nimi, kirjoittaja, kustantaja, isbn;
		String painos;
		int nidenro;
		

		if (rs != null) {
			try {
				isbn = rs.getString("isbn");
				nimi = rs.getString("nimi");
				kirjoittaja = rs.getString("kirjoittaja");
				painos = rs.getString("painos");
				kustantaja = rs.getString("kustantaja");
				nidenro = rs.getInt("nidenro");
				kirja = new Kirja(isbn, nimi, kirjoittaja, painos, kustantaja);
				nide = new Nide(kirja, nidenro);
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return nide;

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
		int lainausnumero;
		Asiakas asiakas;
		NideLainaus nidelainaus;
		boolean jatkuu = false;
		
		String sqlSelect = "select l.numero, l.lainauspvm, l.asiakasnro, a.etunimi, a.sukunimi, a.osoite, a.postinro, p.postitmp, k.isbn, k.nimi, k.kirjoittaja, k.painos, k.kustantaja, nl.nidenro, nl.palautuspvm"
				+ " from lainaus l join asiakas a on a.numero=l.asiakasnro join postinumeroalue p on a.postinro = p.postinro"
				+ " join nidelainaus nl on nl.lainausnro = l.numero"
				+ " JOIN nide n ON n.isbn = nl.isbn AND n.nidenro = nl.nidenro "
				+ " join kirja k on k.isbn = n.isbn"
				+ " order by l.numero;";	
		
		try{
			yhteys = yhdista();
			
			yhteys.setAutoCommit(false);
	 		yhteys.setReadOnly(true);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			stmt = yhteys.prepareStatement(sqlSelect);
			
			rs=stmt.executeQuery(sqlSelect);
			
			yhteys.commit();
			yhteys.close();
			
			jatkuu = rs.next();
			
			while(jatkuu) {
				lainaus = teeLainaus(rs);
				lainausnumero = lainaus.getNumero();
				asiakas = teeAsiakas(rs);
				lainaus.setLainaaja(asiakas);
				lainat.add(lainaus);
				while(jatkuu && rs.getInt("numero")== lainausnumero){
					nidelainaus = teeNideLainaus(rs);
					lainaus.addNiteenLainaus(nidelainaus);
					jatkuu = rs.next();
				}
			}rs.close();	
			
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			suljeYhteys(rs,stmt,yhteys);
		}		
		System.out.println("dao, hae kaikki: "+ lainat);
		System.out.println("loppu");
		return lainat;		
	}
	
	public Lainaus hae(int lainaId){
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Lainaus lainaus = null;
		int lainausnumero;
		Asiakas asiakas;
		NideLainaus nidelainaus;
		
		try{
			yhteys = yhdista();
			
			yhteys.setAutoCommit(false);
	 		yhteys.setReadOnly(true);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			
			String sqlSelect = "select l.numero, l.lainauspvm, l.asiakasnro, a.etunimi, a.sukunimi, a.osoite, a.postinro, p.postitmp, k.isbn, k.nimi, k.kirjoittaja, k.painos, k.kustantaja, nl.nidenro, nl.palautuspvm"
								+ " from lainaus l join asiakas a on a.numero=l.numero join postinumeroalue p on a.postinro = p.postinro"
								+ " join nidelainaus nl on nl.lainausnro = l.numero"
								+ " JOIN nide n ON n.isbn = nl.isbn AND n.nidenro = nl.nidenro"
								+ " join kirja k on k.isbn = n.isbn"
								+ " where l.numero =?;";
			stmt = yhteys.prepareStatement(sqlSelect);
		
			stmt.setInt(1, lainaId);
			
			rs=stmt.executeQuery();

			if (rs != null && rs.next()) {
				yhteys.commit();
				yhteys.close(); 

				lainaus = teeLainaus(rs);

				asiakas = teeAsiakas(rs);

				lainaus.setLainaaja(asiakas);

				nidelainaus = teeNideLainaus(rs);
				lainaus.addNiteenLainaus(nidelainaus);
			
				while(rs.next()){
					nidelainaus = teeNideLainaus(rs);
					lainaus.addNiteenLainaus(nidelainaus);
				}rs.close();	
			}
		}catch(SQLException e){
			throw new RuntimeException(e);
		}finally{
			suljeYhteys(rs,stmt,yhteys);
		}		
		return lainaus;		
	} 
	
	public ArrayList<Asiakas> haeAsiakkaat() throws SQLException{
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Asiakas> asiakkaat = null;
		Asiakas asiakas = null;
		boolean jatkuu = false;
		
		try{
			yhteys = yhdista();
			if (yhteys != null) {
				yhteys.setAutoCommit(false);
				yhteys.setReadOnly(true);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			String sqlSelect = "select * from asiakas natural join postinumeroalue;";
			
			stmt = yhteys.prepareStatement(sqlSelect);			
			rs=stmt.executeQuery();
			stmt.close();
			
			if(rs != null) {
				yhteys.commit();
				yhteys.close();
				jatkuu = rs.next();
				
				while(jatkuu) {
					asiakas = teeAsiakas(rs);
					
					if (asiakkaat == null) {
						asiakkaat = new ArrayList<Asiakas>();
					}
						asiakkaat.add(asiakas);
						jatkuu = rs.next();
					} rs.close();	
					
			} else { 
				asiakkaat = null;
				yhteys.commit();
				yhteys.close();
			}
				
			
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			if (yhteys != null && yhteys.isClosed() == false) {
				try {
					yhteys.rollback(); // peruuta transaktio
					yhteys.close(); // yhteys poikki
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
		}
		System.out.println(asiakkaat);
		return asiakkaat;
		
	}
	
	public Asiakas haeAsiakas(int numero) throws SQLException{
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Asiakas asiakas = null;

		try{
			yhteys = yhdista();
			if (yhteys != null) {
				yhteys.setAutoCommit(false);
				yhteys.setReadOnly(true);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			String sqlSelect = "select * from asiakas natural join postinumeroalue"
					+ " where numero = ?;";
			
			stmt = yhteys.prepareStatement(sqlSelect);	
			stmt.setInt(1, numero);
			rs=stmt.executeQuery();
			stmt.close();
			
			if(rs != null) {
				yhteys.commit();
				yhteys.close();
				
				asiakas = teeAsiakas(rs);
				rs.close();
			} 	else {
				asiakas = null;
				yhteys.commit();
				yhteys.close();
			}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			if (yhteys != null && yhteys.isClosed() == false) {
				try {
					yhteys.rollback(); // peruuta transaktio
					yhteys.close(); // yhteys poikki
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
		}
		return asiakas;
	}
	
	public Kirja haeKirja(int numero, String isbn) throws SQLException{
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Nide nide = null;
		Kirja kirja = null;

		try{
			yhteys = yhdista();
			if (yhteys != null) {
				yhteys.setAutoCommit(false);
				yhteys.setReadOnly(true);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			
			String sqlSelect = "select * from kirja"
					+ " where isbn = ?;";
			
			stmt = yhteys.prepareStatement(sqlSelect);	
			stmt.setString(1, isbn);
			rs=stmt.executeQuery();
			stmt.close();
			
			if(rs != null) {
				yhteys.commit();
				yhteys.close();
				
				nide = teeNide(rs);
				rs.close();
			} 	else {
				asiakas = null;
				yhteys.commit();
				yhteys.close();
			}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			if (yhteys != null && yhteys.isClosed() == false) {
				try {
					yhteys.rollback(); // peruuta transaktio
					yhteys.close(); // yhteys poikki
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
		}
		return asiakas;
	}
	
	public ArrayList<Nide> haeNiteet() throws SQLException{
		Connection yhteys = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Nide> niteet = null;
		Nide nide = null;
		boolean jatkuu = false;
		
		try{
			yhteys = yhdista();

			if (yhteys!=null){
				yhteys.setAutoCommit(false);
				yhteys.setReadOnly(true);
		 		yhteys.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		 		String sqlSelect = "SELECT * FROM kirja NATURAL JOIN nide"
						+ " WHERE NOT EXISTS (SELECT null FROM nidelainaus WHERE EXISTS"
						+ " (SELECT * FROM nidelainaus WHERE nidelainaus.palautuspvm IS NULL"
						+ " AND nidelainaus.nidenro = nide.nidenro AND nidelainaus.isbn = nide.isbn)); ";
	
		 		stmt = yhteys.prepareStatement(sqlSelect);
		 		rs=stmt.executeQuery();
		 		stmt.close();
		 		
		 		if(rs !=null){
		 			System.out.println("rs != null");
		 			yhteys.commit();
					yhteys.close();
					
					jatkuu = rs.next();
					while(jatkuu) {
						nide = teeNide(rs);
						
						if (niteet == null) {
							niteet = new ArrayList<Nide>();
						}
						niteet.add(nide);
							jatkuu = rs.next();
						} rs.close();	
		 		} else {
		 			niteet = null;
		 			System.out.println("Ei niteit‰ tietokannassa!");
		 			yhteys.commit();
		 			yhteys.close();
		 		}
			} 
		}
			catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
				throw new SQLException();
			} finally {
				if (yhteys != null && yhteys.isClosed() == false) {
					try {
						yhteys.rollback(); // peruuta transaktio
						yhteys.close(); // yhteys poikki
					} catch (Exception e) {
						e.printStackTrace();
						throw new SQLException();
					}
				}
			}
		System.out.println(niteet);
		return niteet;
		
	}
		
	
	public boolean lisaaLainaus(Lainaus lainaus) throws SQLException{
		Connection yhteys = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		boolean ok = false;
		int lastId;
		
		int asiakasNro = lainaus.getLainaaja().getNumero();
		Date sqlDate = new Date(lainaus.getLainausPvm().getTime());

		String sqlInsert1="INSERT INTO lainaus VALUES(?,?);";	
		String sqlSelect = "SELECT LAST_INSERT_ID();";
		
		try{
			yhteys = yhdista();
			
			yhteys.setAutoCommit(false);
	 		yhteys.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	 		
	 		stmt = yhteys.prepareStatement(sqlInsert1);
	 		stmt.setDate(1, (java.sql.Date) sqlDate);
			stmt.setInt(2, asiakasNro);
	 		int lkm = stmt.executeUpdate();
	 		stmt.close();
	 		
	 		if (lkm == 1) {
	 			stmt2 = yhteys.prepareStatement(sqlSelect);
	 			rs = stmt2.executeQuery();
	 		}
	 		
	 		if (rs != null) {
	 			ok = true;
	 			while(rs.next()){
					lastId = rs.getInt("last_insert_id()");
					lainaus.setNumero(lastId);
				}rs.close();
	 			
	 		} else {
	 			ok = false;
	 			yhteys.commit();
				yhteys.close();
	 		}
			
			
			
		}catch(SQLException e){			
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally{
			
			if (yhteys != null && yhteys.isClosed() == false) {
				try {
					yhteys.rollback(); // peruuta transaktio
					yhteys.close(); // yhteys poikki
					suljeYhteys(stmt,yhteys);
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
			
		}
		
		
		return ok;
		
		//PreparedStatement stmt3 = null;
		/*int asiakasNro = lainaus.getLainaaja().getNumero();
		Date sqlDate = new Date(lainaus.getLainausPvm().getTime());

		String sqlInsert2="INSERT INTO lainaus VALUES(?,?);";		
		
		stmt.setDate(1, (java.sql.Date) sqlDate);
		stmt.setInt(2, asiakasNro);*/
		
	}

}

	
	


	                  
			