package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Product;
import entity.Review;
import entity.UserTable;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadReview", urlPatterns = {"/LoadReview"})
public class LoadReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = (int) request.getAttribute("id");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
        Product product = (Product) hibernateSession.get(Product.class, productId);

        //review for this product
        Criteria reviewCriteria = hibernateSession.createCriteria(Review.class);
        reviewCriteria.add(Restrictions.eq("product", product));
        reviewCriteria.addOrder(Order.desc("datetime"));
        List<Review> reviewList = reviewCriteria.list();

        boolean isLoggedIn = false;
        UserTable user = (UserTable) request.getSession().getAttribute("user");
        if (user != null) {
            isLoggedIn = true;
        }

        ArrayList<JsonObject> reviewArr = new ArrayList<>();
        if (reviewList != null && !reviewList.isEmpty()) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (Review review : reviewList) {

                JsonObject jo = new JsonObject();
                jo.addProperty("id", review.getId());
                jo.addProperty("review", review.getReview());
                jo.addProperty("datetime", sdf.format(review.getDatetime()));
                jo.addProperty("by", review.getUser().getFirst_name() + " " + review.getUser().getLast_name());

                if (isLoggedIn && (review.getUser().getId() == user.getId())) {
                    //this users reviews
                    jo.addProperty("isEditable", true);
                } else {
                    jo.addProperty("isEditable", false);
                }

                reviewArr.add(jo);
            }
        }

        hibernateSession.close();

        Gson gson = new Gson();

        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(reviewArr));
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
