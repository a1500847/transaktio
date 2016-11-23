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
import kohdeluokat.Tili;


public class Dao {

	private Connection yhdista() throws SQLException {
		Connection tietokantayhteys = null;

		String JDBCAjuri = "org.mariadb.jdbc.Driver";
		String url = "jdbc:mariadb://localhost:3306/projekti"; //TÄHÄN PROTON OSOITE!

		try {
			Class.forName(JDBCAjuri).newInstance(); // ajurin määritys

			// otetaan yhteys tietokantaan
			tietokantayhteys = DriverManager.getConnection(url, "projekti",
					"SALASANA TÄHÄN"); 

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

	public Tili haeTili(String numero) throws SQLException {
		String sql = "SELECT tilinro, saldo, a.numero AS asiakasnumero, "
				+ "etunimi, sukunimi, osoite, p.postinro AS postinro, postitmp "
				+ " FROM tili t JOIN asiakas a ON t.omistaja=a.numero "
				+" JOIN postinumeroalue p ON a.postinro=p.postinro "
				+" WHERE tilinro=?"; 
		
		PreparedStatement preparedStatement = null; // suoritettava SQL-lause
		ResultSet tulosjoukko = null; // SQL-kyselyn tulokset
		Connection conn = null;
		Tili tili=null;
		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				/*
				 * level - one of the following Connection constants:
				 * Connection.TRANSACTION_READ_UNCOMMITTED ,
				 * Connection.TRANSACTION_READ_COMMITTED ,
				 * Connection.TRANSACTION_REPEATABLE_READ or
				 * Connection.TRANSACTION_SERIALIZABLE.
				 */
				// eristyvyystason määritys
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, numero);
				tulosjoukko = preparedStatement.executeQuery();
				preparedStatement.close();

				if (tulosjoukko != null && tulosjoukko.next()) {
					conn.commit(); // lopeta transaktio hyväksymällä
					conn.close(); // sulje yhteys kantaan heti

					tili = teeTili(tulosjoukko);
					tulosjoukko.close();
				} else // tilia ei löytynyt
				{
					tili = null;
					conn.commit();
					conn.close();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			if (conn != null && conn.isClosed() == false) {
				try {
					conn.rollback(); // peruuta transaktio
					conn.close(); // yhteys poikki
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
		}
		return tili;
	}

	private Tili teeTili(ResultSet tulosjoukko) throws SQLException {
		Tili tili = null;
		String numero;
		double saldo;
		Asiakas omistaja;
		

		if (tulosjoukko != null) {
			try {
				// System.out.println(tulosjoukko.getInt("lainausnumero") + " "
				// + tulosjoukko.getString("lainauspvm"));
				numero = tulosjoukko.getString("tilinro");
				saldo = tulosjoukko.getDouble("saldo");
				omistaja = teeAsiakas(tulosjoukko);
				tili = new Tili(numero, saldo, omistaja);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return tili;
	}

	private Asiakas teeAsiakas(ResultSet tulosjoukko) throws SQLException {
		Asiakas asiakas = null;
		int numero;
		String etunimi;
		String sukunimi, osoite;
		String postinro;
		String postitmp;
		PostinumeroAlue posti = null;

		if (tulosjoukko != null) {
			try {
				numero = tulosjoukko.getInt("asiakasnumero");
				etunimi = tulosjoukko.getString("etunimi");
				sukunimi = tulosjoukko.getString("sukunimi");
				osoite = tulosjoukko.getString("osoite");
				postinro = tulosjoukko.getString("postinro");
				postitmp = tulosjoukko.getString("postitmp");
				posti = new PostinumeroAlue(postinro, postitmp);
				asiakas = new Asiakas(numero, etunimi, sukunimi, osoite, posti);
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}

		return asiakas;
	}

	public boolean tilinsiirto(Tili tili1, Tili tili2, double maara)
			throws SQLException {
		boolean ok = false;
		
		System.out.println("DAO TILINSIIRTO  " + maara +"\n" + tili1 + " \n" + tili2);
		String sql ="UPDATE tili SET saldo = saldo - ? WHERE tilinro=?";
		String sql2 = "UPDATE tili SET saldo = saldo + ? WHERE tilinro =?";
		Connection conn = null;
		PreparedStatement preparedStatement;
		int  lkm =0, i;
		int deadlock = 0;
		final int TOISTO= 3;
			
		if (tili1 != null && tili2 != null)
		{
			while (deadlock != TOISTO) {
			try
			{
				if (deadlock != 0)
					Thread.sleep(10000); // odota hiukan
				conn = yhdista(); // ota yhteys kantaan
				if (conn != null)
				{
					conn.setAutoCommit(false);// poista automaattinen transaktion hyväksyminen
					/*
						 * level - one of the following Connection constants:
						 * Connection.TRANSACTION_READ_UNCOMMITTED ,
						 * Connection.TRANSACTION_READ_COMMITTED ,
						 * Connection.TRANSACTION_REPEATABLE_READ or
						 * Connection.TRANSACTION_SERIALIZABLE.
						 */
						// eristyvyystason määritys 
					conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					preparedStatement = conn.prepareStatement(sql);
						
					preparedStatement.setDouble(1, maara);
					preparedStatement.setString(2, tili1.getTilinro()); 
					lkm = preparedStatement.executeUpdate();
					preparedStatement.close();
						
					if (lkm == 1 )
					{
						Thread.sleep(10000);
						preparedStatement = conn.prepareStatement(sql2);
						preparedStatement.setDouble (1, maara);
						preparedStatement.setString(2, tili2.getTilinro());
							
						lkm = preparedStatement.executeUpdate();
						preparedStatement.close();
					}
					if (lkm == 1) // jos jokainen rivi lisättiin
					{
						ok = true;
						conn.commit();  // hyväksy transaktio
						conn.close();	// lopetta yhteys kantaan
						deadlock = TOISTO; // kaikki hyvin, ei deadlockia
					}
					else
					{
						ok = false;
						conn.rollback(); //hylkää transaktio
						conn.close();
						deadlock = TOISTO; // ei deadlockia
					}
				}
			}
			catch (SQLException e)
		     {
		       	//e.printStackTrace();
		       	System.out.println("SQLState: " + e.getSQLState());
		    	System.out.println("errorCode " + e.getErrorCode());
		    	if ( e.getSQLState().equals("40001") && e.getErrorCode()== 1213)
		    	{
		    		conn.rollback();  // peruuta transaktio
		    		deadlock++;  // toistetaan trasnaktion uudelleen 
		    		
		    	}
		    	else {
		    		throw e;
		    	}
		     }
		     catch (Exception e)
		     {
		       	e.printStackTrace();
		       	throw new SQLException();
		     }
		     /*finally {
				if (conn != null &&  conn.isClosed() == false) 
				{
					try 
					{
						conn.rollback();  // peruuta transaktio
						conn.close();     // yhteys poikki
					} 
					catch(Exception e) 
					{
						e.printStackTrace();
			        	throw new SQLException();
					}
				}*/
			}// end of while
						
		}// end of if
		if (conn != null &&  conn.isClosed() == false) 
		{
		try 
		{
			conn.rollback();  // peruuta transaktio
			conn.close();     // yhteys poikki
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
        	throw new SQLException();
		}
	}
		return ok;
	}

	/**
	 * Hakee kannasta tilitaulun sisällön ArrayList-tyyppiseen kokoelmaan
	 * yhdessä transaktiossa.
	 *
	 * @return kaikki kannan sisältämät tilit yhdessä kokoelmassa
	 * @throws OdottamatonTietokantaPoikkeus
	 *             jos tapahtuu tietokantavirhe
	 * @throws KannassaEiTilejaPoikkeus
	 *             jos kannasta ei löydy vähintään kahta tiliä.
	 */
	public List<Tili> haeTilit() throws SQLException {

		List<Tili> lista = null;
		Connection conn = null;
		String sql = "SELECT tilinro, saldo, a.numero AS asiakasnumero, "
				+ "etunimi, sukunimi, osoite, p.postinro AS postinro, postitmp "
				+ " FROM tili t JOIN asiakas a ON t.omistaja=a.numero "
				+" JOIN postinumeroalue p ON p.postinro=a.postinro "
				+ "ORDER BY tilinro";
		PreparedStatement preparedStatement = null; // suoritettava SQL-lause
		ResultSet tulosjoukko = null; // SQL-kyselyn tulokset
		Tili tili = null;
		Asiakas asiakas;
		boolean jatkuu = false;
	
		try {
			conn = yhdista();
			if (conn != null) {
				conn.setAutoCommit(false);
				/*
				 * level - one of the following Connection constants:
				 * Connection.TRANSACTION_READ_UNCOMMITTED ,
				 * Connection.TRANSACTION_READ_COMMITTED ,
				 * Connection.TRANSACTION_REPEATABLE_READ or
				 * Connection.TRANSACTION_SERIALIZABLE.
				 */
				// eristyvyystason määritys
				conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				preparedStatement = conn.prepareStatement(sql);

				tulosjoukko = preparedStatement.executeQuery();
				preparedStatement.close();

				if (tulosjoukko != null) {
					conn.commit(); // lopeta transaktio hyväksymällä
					conn.close(); // sulje yhteys kantaan

					jatkuu = tulosjoukko.next();
					while (jatkuu) {
						tili = teeTili(tulosjoukko);
						
						if (lista == null)
							lista = new ArrayList<Tili>();
						lista.add(tili); // vie lainaus listaan

						jatkuu = tulosjoukko.next();
					}
					tulosjoukko.close();

				} else // lainauksia ei löytynyt
				{
					lista = null;
					conn.commit();
					conn.close();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException();
		} finally {
			if (conn != null && conn.isClosed() == false) {
				try {
					conn.rollback(); // peruuta transaktio
					conn.close(); // yhteys poikki
				} catch (Exception e) {
					e.printStackTrace();
					throw new SQLException();
				}
			}
		}

		return lista;

	}

}