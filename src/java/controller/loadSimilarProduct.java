package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Category;
import entity.Product;
import entity.Status;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "loadSimilarProduct", urlPatterns = {"/loadSimilarProduct"})
public class loadSimilarProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = (int) request.getAttribute("id");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Product product = (Product) hibernateSession.get(Product.class, productId);

        Category category = (Category) hibernateSession.get(Category.class, product.getCategory().getId());

        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status statusActive = (Status) statusCriteria.uniqueResult();

        Criteria productCriteria = hibernateSession.createCriteria(Product.class);
        productCriteria.add(Restrictions.and(
                Restrictions.eq("category", category),
                Restrictions.eq("status", statusActive)
        ));
        productCriteria.setMaxResults(5);

        ArrayList<Product> productList = (ArrayList<Product>) productCriteria.list();
        if (!productList.isEmpty()) {
            for (Product product1 : productList) {
                product1.getSeller().setUser(null);
            }
        }

        hibernateSession.close();

        Gson gson = new Gson();

        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(productList));
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
