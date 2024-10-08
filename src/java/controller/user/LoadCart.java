package controller.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Cart;
import entity.UserTable;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadCart", urlPatterns = {"/LoadCart"})
public class LoadCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isLoggedIn = (boolean) request.getAttribute("isLoggedIn");
        if (isLoggedIn) {
            // logged in get from , user cart

            UserTable userTable = ((UserTable) request.getSession().getAttribute("user"));

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            Criteria cartCriteria = hibernateSession.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("user", userTable));
            cartCriteria.addOrder(Order.desc("id"));

            ArrayList<Cart> cartList1 = (ArrayList<Cart>) cartCriteria.list();
            ArrayList<Cart> cartList = new ArrayList<>();
            if (cartList1 != null && !cartList1.isEmpty()) {

                for (Cart cart : cartList1) {

                    if (cart.getProduct().getSeller().getUser().getAccount_type().getType().equals("Seller")) {

                        cartList.add(cart);
                    }

                }

                for (Cart cart : cartList) {
                    cart.getProduct().setSeller(null);
                }
            }

            Gson gson = new Gson();

            Response_DTO response_DTO;
            if (cartList.isEmpty()) {
                response_DTO = new Response_DTO(false, "No Cart Data");
            } else {
                JsonObject jo = new JsonObject();
                jo.add("cartList", gson.toJsonTree(cartList));
                jo.addProperty("isLoggedIn", isLoggedIn);

                response_DTO = new Response_DTO(true, gson.toJsonTree(jo));
            }

            hibernateSession.close();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));

        } else {
            //get from session cart
            ArrayList<Cart> cartList = (ArrayList<Cart>) request.getSession().getAttribute("userSessionCart");

            Gson gson = new Gson();

            Response_DTO response_DTO;
            if (cartList == null || cartList.isEmpty()) {
                response_DTO = new Response_DTO(false, "No Cart Data");
            } else {
                JsonObject jo = new JsonObject();
                jo.add("cartList", gson.toJsonTree(cartList));
                jo.addProperty("isLoggedIn", isLoggedIn);

                response_DTO = new Response_DTO(true, gson.toJsonTree(jo));
            }

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));

        }

    }

}
