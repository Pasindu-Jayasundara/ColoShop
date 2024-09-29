package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product;
import entity.UserTable;
import entity.Wishlist;
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
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "DeleteFromWishList", urlPatterns = {"/DeleteFromWishList"})
public class DeleteFromWishList extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserTable user = (UserTable) request.getSession().getAttribute("user");
        int pid = (int) request.getAttribute("id");

        boolean isDone = false;
        String message = "";

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Product product = (Product) hibernateSession.get(Product.class, pid);
        if (product != null) {

            Criteria wishlistCriteria = hibernateSession.createCriteria(Wishlist.class);
            wishlistCriteria.add(Restrictions.and(
                    Restrictions.eq("product", product),
                    Restrictions.eq("user", user)
            ));

            List<Wishlist> list = (List<Wishlist>) wishlistCriteria.list();
            if (list != null) {
                //review avaliable
                //delete 
                for (Wishlist wishlist : list) {

                    hibernateSession.delete(wishlist);
                }

                hibernateSession.beginTransaction().commit();
                isDone = true;
                message = "Wish Product Delete Success";

            } else {
                //no review
                message = "Wish Product Not Avaliable";
            }

        } else {
            message = "No Product Found";
        }

        Response_DTO response_DTO = new Response_DTO(isDone, message);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
