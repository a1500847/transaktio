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
		Dao dao = new Dao();
		ArrayList<Integer> lainausNumerot = dao.haeLainausNrot();
		request.setAttribute("lainausnumerot", lainausNumerot);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/etusivu.jsp");
		dispatcher.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nroString = request.getParameter("lainausnumero");
		int lainausnro = new Integer(nroString);
		Dao dao = new Dao();
		Lainaus lainaus = dao.hae(lainausnro);
		request.setAttribute("lainaus", lainaus);
		
		response.sendRedirect("YksiLainausServlet");
	}

}
