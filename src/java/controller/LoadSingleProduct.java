package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("id"));

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Product product = (Product) hibernateSession.get(Product.class, productId);

        hibernateSession.close();

        Gson gson = new Gson();

        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(product));
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
