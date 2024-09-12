package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Cart;
import entity.UserTable;
import java.io.IOException;
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

@WebServlet(name = "LoadCart", urlPatterns = {"/LoadCart"})
public class LoadCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isLoggedIn = (boolean) request.getAttribute("isLoggedIn");
        if (isLoggedIn) {
            // logged in get from , user cart
            int userId = ((UserTable) request.getSession().getAttribute("user")).getId();

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            Criteria cartCriteria = hibernateSession.createCriteria(Cart.class);
            cartCriteria.add(Restrictions.eq("id", userId));

            List<Cart> cartList = cartCriteria.list();

            Gson gson = new Gson();

            Response_DTO response_DTO;
            if (cartList.isEmpty()) {
                response_DTO = new Response_DTO(false, "No Cart Data");
            } else {
                response_DTO = new Response_DTO(true, gson.toJsonTree(cartList));
            }

            hibernateSession.close();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));

        } else {
            //get from session cart
            ArrayList<Cart> cartList = (ArrayList<Cart>) request.getSession().getAttribute("userSessionCart");

            Gson gson = new Gson();

            Response_DTO response_DTO;
            if (cartList.isEmpty()) {
                response_DTO = new Response_DTO(false, "No Cart Data");
            } else {
                response_DTO = new Response_DTO(true, gson.toJsonTree(cartList));
            }

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));

        }

    }

}
