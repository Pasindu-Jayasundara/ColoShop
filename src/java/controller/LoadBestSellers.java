package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

@WebServlet(name = "LoadBestSellers", urlPatterns = {"/LoadBestSellers"})
public class LoadBestSellers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Criteria productCriteria = hibernateSession.createCriteria(Product.class);

        productCriteria.addOrder(Order.desc("sold_count"));
        productCriteria.setMaxResults(10);

        List<Product> productList = productCriteria.list();

        Gson gson = new Gson();

        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(productList));

        hibernateSession.close();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }
}
