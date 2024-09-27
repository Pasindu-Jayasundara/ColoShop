package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product;
import entity.UserTable;
import entity.Wishlist;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddToWishlist", urlPatterns = {"/AddToWishlist"})
public class AddToWishlist extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = Integer.parseInt((String) request.getAttribute("pId"));
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        String message = "";
        boolean isSuccess = false;

        Product product = (Product) hibernateSession.get(Product.class, productId);
        if (product != null) {

            Criteria wishlistCriteria = hibernateSession.createCriteria(Wishlist.class);
            wishlistCriteria.add(Restrictions.eq("product", product));
            Wishlist w = (Wishlist) wishlistCriteria.uniqueResult();
            if (w == null) {

                UserTable user = (UserTable) request.getSession().getAttribute("user");

                Wishlist wishlist = new Wishlist();
                wishlist.setProduct(product);
                wishlist.setUser(user);

                hibernateSession.save(wishlist);
                hibernateSession.beginTransaction().commit();

                isSuccess = true;
                message = "Product Successfully Added To The Wishlist";
            } else {
                isSuccess = true;
                message = "Already In Wishlist";
            }

        } else {
            isSuccess = false;
            message = "Cannot Find The Product";
        }
        Response_DTO response_DTO = new Response_DTO(isSuccess, message);

        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
