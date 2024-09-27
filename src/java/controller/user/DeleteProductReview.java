package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Review;
import entity.UserTable;
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

@WebServlet(name = "DeleteReview", urlPatterns = {"/DeleteReview"})
public class DeleteProductReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserTable user = (UserTable) request.getSession().getAttribute("user");
        if (user != null) {

            int reviewId = (int) request.getAttribute("id");

            boolean isDone = false;
            String message = "";

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();
            Criteria reviewCriteria = hibernateSession.createCriteria(Review.class);
            reviewCriteria.add(Restrictions.and(
                    Restrictions.eq("id", reviewId),
                    Restrictions.eq("user", user)
            ));

            Review review = (Review) reviewCriteria.uniqueResult();
            if (review != null) {
                //review avaliable
                //delete review

                hibernateSession.delete(review);
                hibernateSession.beginTransaction().commit();

                isDone=true;
                message = "Review Delete Success";

            } else {
                //no review
                message = "Review Not Avaliable";
            }

            Response_DTO response_DTO = new Response_DTO(isDone, message);
            Gson gson = new Gson();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

}
