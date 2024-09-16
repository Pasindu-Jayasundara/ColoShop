package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
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

@WebServlet(name = "LoadWishlist", urlPatterns = {"/LoadWishlist"})
public class LoadWishlist extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        UserTable user = (UserTable) request.getSession().getAttribute("user");
        
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Criteria wishlistCriteria = hibernateSession.createCriteria(Wishlist.class);
        
        wishlistCriteria.add(Restrictions.eq("user", user.getId()));
        
        Gson gson = new Gson();
        Response_DTO response_DTO;
        
        List<Wishlist> wishList = wishlistCriteria.list();
        
        if (wishList.isEmpty()) {
            //no data
            response_DTO = new Response_DTO(false, "No Items");
            
        } else {
            response_DTO = new Response_DTO(true, gson.toJsonTree(wishList));
        }
        
        hibernateSession.close();
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        
    }
    
}