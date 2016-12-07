package ohjelma;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kohdeluokat.Asiakas;
import kohdeluokat.Lainaus;
import kohdeluokat.Nide;
import dao.Dao;


@WebServlet("/LainausServlet")
public class LainausServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher disp;
		String action = request.getParameter("action");

		System.out.println("action = " + action);

		if (action == null 
				|| (action!= null && action.equalsIgnoreCase("Etusivu"))
				|| (action!= null && action.equalsIgnoreCase("Peruuta lainaus")) 
				|| (action!= null && action.equalsIgnoreCase("Peruuta"))) {	
			haeLainausNumerot(request, response);
		} else if (action.equalsIgnoreCase("Hae kaikki lainaukset")) {
			lainausLista(request, response);
		} else if (action.equalsIgnoreCase("Hae lainaus")) {
			hae(request, response);
		} else if (action.equalsIgnoreCase("Tee lainaus")) {
			try {
				teeLainaus(request, response);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
		} else if (action.equalsIgnoreCase("Vahvista lainaus")) {
			vahvistaLainaus(request, response);
		} else if (action.equalsIgnoreCase("Talleta lainaus")) {
			talletaLainaus(request, response);
		} 
	}		
		
	private void hae(HttpServletRequest request, HttpServletResponse response) {
		String nroString = request.getParameter("lainausnumero");
		int lainausnro = new Integer(nroString);
		Dao dao = new Dao();
		Lainaus lainaus = dao.hae(lainausnro);
		request.setAttribute("lainaus", lainaus);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/yksi_lainaus.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
		
	}

	private void lainausLista(HttpServletRequest request,
			HttpServletResponse response) {
		Dao dao = new Dao();
		ArrayList<Lainaus> lainat = dao.haeKaikki();
		request.setAttribute("lainauslista", lainat);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/kaikki_lainaukset.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	private void haeLainausNumerot(HttpServletRequest request, HttpServletResponse response) {
		Dao dao = new Dao();
		ArrayList<Integer> lainausNumerot = dao.haeLainausNrot();
		request.setAttribute("lainausnumerot", lainausNumerot);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/etusivu.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void teeLainaus(HttpServletRequest request, HttpServletResponse response) throws SQLException{
		RequestDispatcher dispatcher;
		Dao dao = new Dao();
		String jsp = "/WEB-INF/lisaaLainaus.jsp";
		ArrayList<Asiakas> asiakkaat = null;
		ArrayList<Nide> kirjat = null;
		
		try {
			asiakkaat = dao.haeAsiakkaat();
			kirjat = dao.haeNiteet();
			
			if (asiakkaat != null && kirjat != null) {
				
				request.setAttribute("asiakkaat", asiakkaat);
				request.setAttribute("kirjat", kirjat);
				dispatcher = request.getRequestDispatcher(jsp);
				dispatcher.forward(request, response);
			} 
		}
		catch (ServletException | IOException e) {
			e.printStackTrace();
			System.out.println("Virhe!");
		}
	}
	
	private void vahvistaLainaus(HttpServletRequest request, HttpServletResponse response){
		System.out.println("VAHVISTALAINAUSTA KUTSUTTU!!!!!");
		//Tässä pitäisi näyttää syötetty lainaus ennen kuin se on viety kantaan

		String asiakas = request.getParameter("asiakas");
		String nide = request.getParameter("nide");
		System.out.println(asiakas + nide);
		request.setAttribute("asiakas", asiakas);
		request.setAttribute("nide", nide);
		
		valmistaLainaus(request, response);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/varmistus.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void valmistaLainaus(HttpServletRequest request,
			HttpServletResponse response) {
		Dao dao = new Dao();
		String strAsNumero = request.getParameter("asiakas");
		int numero = Integer.parseInt(strAsNumero);
		
		try {
			Asiakas asiakas = dao.haeAsiakas(numero);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String strIsbn = request.getParameter("nide");
		
		
	}

	private void talletaLainaus(HttpServletRequest request, HttpServletResponse response){
		System.out.println("VAHVISTALAINAUSTA KUTSUTTU!!!!!");
		//Vie uusi lainaus tietokantaan ja vie takaisin etusivulle
		haeLainausNumerot(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
