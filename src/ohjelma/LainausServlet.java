package ohjelma;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kohdeluokat.Lainaus;
import dao.Dao;


@WebServlet("/LainausServlet")
public class LainausServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");

		System.out.println("action = " + action);

		if (action == null) {			
			haeLainausNumerot(request, response);
		} else if (action.equalsIgnoreCase("Hae kaikki lainaukset")) {
			lainausLista(request, response);
		} else if (action.equalsIgnoreCase("Hae")) {
			hae(request, response);
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
		System.out.println(lainat);
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
