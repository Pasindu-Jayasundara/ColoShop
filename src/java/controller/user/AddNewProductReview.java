package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.OrderDataTable;
import entity.Order_item;
import entity.Product;
import entity.Review;
import entity.UserTable;
import java.io.IOException;
import java.util.Date;
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

@WebServlet(name = "AddNewReview", urlPatterns = {"/AddNewReview"})
public class AddNewProductReview extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int productId = (int) request.getAttribute("id");
        String reviewText = (String) request.getAttribute("review");

        UserTable user = (UserTable) request.getSession().getAttribute("user");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        
        String message = "";
        boolean isDone = false;
        
        //get user orders
        Criteria orderCriteria = hibernateSession.createCriteria(OrderDataTable.class);
        orderCriteria.add(Restrictions.eq("user", user));

        List<OrderDataTable> orderList = orderCriteria.list();
        if (!orderList.isEmpty()) {
            //have orders
            Product product = (Product) hibernateSession.load(Product.class, productId);

            orderLoop:
            for (OrderDataTable order : orderList) {
                //get order items of these orders
System.out.println("bbbbbbbbbbbbbbbb");
System.out.println("bbbbbbbbbbbbbbbb");
System.out.println("bbbbbbbbbbbbbbbb");
System.out.println("bbbbbbbbbbbbbbbb");
                Criteria orderItemCriteria = hibernateSession.createCriteria(Order_item.class);
                orderItemCriteria.add(Restrictions.and(
                        Restrictions.eq("orders", order),
                        Restrictions.eq("product", product)
                ));

                List<Order_item> orderItemList = orderItemCriteria.list();
                if (!orderItemList.isEmpty()) {
                    //have order items
System.out.println("cccccccccccccccccc");
System.out.println("cccccccccccccccccc");
System.out.println("cccccccccccccccccc");
System.out.println("cccccccccccccccccc");
                    for (Order_item order_item : orderItemList) {
                        //check product id match to this id

                        System.out.println("dddddddddddddddddddddddd");
                        System.out.println("dddddddddddddddddddddddd");
                        System.out.println("dddddddddddddddddddddddd");
                        System.out.println("dddddddddddddddddddddddd");
                        if (order_item.getProduct().getId() == productId) {
                            //have bought this product

                            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeee");
                            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeee");
                            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeee");
                            System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeee");
                            Review review = new Review();
                            review.setReview(reviewText);
                            review.setDatetime(new Date());
                            review.setProduct(product);
                            review.setUser(user);

                            hibernateSession.save(review);
                            hibernateSession.beginTransaction().commit();

                            isDone=true;
                            message="Review Adding Success";
                            
                            break orderLoop;

                        } else {
                            //did not bought this product
                            message="Sorry, You Havent Purchased This Product Yet!";
                        }

                    }

                }else{
                    message="You Need To Buy This Product First";
                }
                
            }
        } else {
            //no orders
            message="You Need To Order This Product First";
        }

        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(isDone, message);
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
