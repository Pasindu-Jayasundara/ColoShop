package controller.seller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product;
import entity.Status;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "DeleteProduct", urlPatterns = {"/DeleteProduct"})
public class DeleteProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("productId"));

        boolean isDone = false;
        String message = "";

        Session hibernaSession = HibernateUtil.getSessionFactory().openSession();

        Criteria statusCriteria = hibernaSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("status", "Active"));
        Status activeStatus = (Status) statusCriteria.uniqueResult();

        Criteria productCriteria = hibernaSession.createCriteria(Product.class);
        productCriteria.add(Restrictions.and(
                Restrictions.eq("id", productId),
                Restrictions.eq("status", activeStatus)
        ));

        Product product = (Product) productCriteria.uniqueResult();

        if (product != null) {
            //have product

            statusCriteria.add(Restrictions.eq("status", "De-Active"));
            Status deActiveStatus = (Status) statusCriteria.uniqueResult();

            product.setStatus(deActiveStatus);
            hibernaSession.update(product);
            hibernaSession.beginTransaction().commit();

            isDone = true;
            message = "Product Deletion Success";
        } else {
            //no product
            message = "Cannot Find the Product";

        }

        hibernaSession.close();

        Response_DTO response_DTO = new Response_DTO(isDone, message);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
