package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Cart;
import entity.Product;
import entity.UserTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "DeleteFromCart", urlPatterns = {"/DeleteFromCart"})
public class DeleteFromCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        int pid = (int) request.getAttribute("id");
        boolean isLoggedIn = (boolean) request.getAttribute("isLoggedIn");
        String message = "";

        if (isLoggedIn) {
            //db cart

            UserTable user = (UserTable) request.getSession().getAttribute("user");

            Product product = (Product) hibernateSession.get(Product.class, pid);

            Criteria cartCriteria = hibernateSession.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.and(
                    Restrictions.eq("user", user),
                    Restrictions.eq("product", product)
            ));

            Cart cartResult = (Cart) cartCriteria.uniqueResult();
            if (cartResult != null) {

                hibernateSession.delete(cartResult);
                hibernateSession.beginTransaction().commit();
                message = "Delete Success";

            } else {
                message = "Not In Cart";
            }

        } else {
            // session cart
            HttpSession session = request.getSession(false);
            if (session != null) {
                ArrayList<Product> list = (ArrayList<Product>) session.getAttribute("userSessionCart");

                if (list != null && !list.isEmpty()) {
                    // Use an iterator to safely remove elements while iterating
                    for (Iterator<Product> iterator = list.iterator(); iterator.hasNext();) {
                        Product product = iterator.next();
                        if (product.getId() == pid) {
                            iterator.remove(); // Remove the product with matching ID
                            message = "Delete Success";
                        }
                    }

                    // Update the session attribute after modification
                    session.setAttribute("userSessionCart", list);

                } else {
                    message = "Cart is empty or session cart not found";
                }
            }

        }

        hibernateSession.close();

        Gson gson = new Gson();

        Response_DTO response_DTO = new Response_DTO(true, message);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }
}
