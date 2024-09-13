package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.AdminDetailTable;
import entity.OrderDataTable;
import entity.Order_status;
import entity.Product;
import entity.Status;
import entity.UserTable;
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

@WebServlet(name = "DeleteMyAccount", urlPatterns = {"/DeleteMyAccount"})
public class DeleteMyAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean isUser = (boolean) request.getAttribute("isUser");
        boolean isAdmin = (boolean) request.getAttribute("isAdmin");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isDone = true;
        String message = "";

        if (isUser) {

            //check if this user is a seller
            UserTable user = (UserTable) request.getSession().getAttribute("user");
            if (user.getAccount_type().getType().equals("Seller")) {
                //seller
                //check if have any undelivered products
                Criteria orderStatus = hibernateSession.createCriteria(Order_status.class);
                orderStatus.add(Restrictions.ne("status", "Delivered"));
                Order_status order_status = (Order_status) orderStatus.uniqueResult();

                Criteria orderCriteria = hibernateSession.createCriteria(OrderDataTable.class);
                orderCriteria.add(Restrictions.and(
                        Restrictions.eq("user", user),
                        Restrictions.eq("order_status", order_status)
                ));

                List<OrderDataTable> orderList = orderCriteria.list();
                if (!orderList.isEmpty()) {
                    isDone = false;
                    message = "You Have Undelivered Orders";

                } else {
                    //no pending orders
                    Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                    statusCriteria.add(Restrictions.eq("status", "De-Active"));

                    Status status = (Status) statusCriteria.uniqueResult();

                    user.setStatus(status);
                    hibernateSession.update(user);
                }

                deActivateProducts(hibernateSession, user);

            } else {
                //buyer
                //getStatus
                Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                statusCriteria.add(Restrictions.eq("status", "De-Active"));

                Status status = (Status) statusCriteria.uniqueResult();

                user.setStatus(status);
                hibernateSession.update(user);
            }

        } else if (isAdmin) {
            Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
            statusCriteria.add(Restrictions.eq("status", "De-Active"));

            Status status = (Status) statusCriteria.uniqueResult();

            AdminDetailTable admin = (AdminDetailTable) request.getSession().getAttribute("user");
            admin.setStatus(status);
            hibernateSession.update(admin);
        }

        if (isDone) {
            hibernateSession.beginTransaction().commit();
            message = "Account Delete Success";
        }
        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(true, message);
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

    private void deActivateProducts(Session hibernateSession, UserTable user) {

        //status
        Criteria statusDeactiveCriteria = hibernateSession.createCriteria(Status.class);
        statusDeactiveCriteria.add(Restrictions.eq("status", "De-Active"));

        Status deactiveStatus = (Status) statusDeactiveCriteria.uniqueResult();
        
        Criteria statusActiveCriteria = hibernateSession.createCriteria(Status.class);
        statusActiveCriteria.add(Restrictions.eq("status", "De-Active"));

        Status activeStatus = (Status) statusActiveCriteria.uniqueResult();

        //products
        Criteria productCriteria = hibernateSession.createCriteria(Product.class);
        productCriteria.add(Restrictions.and(
                Restrictions.eq("status", activeStatus),
                Restrictions.eq("user", user)
        ));

        List<Product> productList = productCriteria.list();
        if (!productList.isEmpty()) {

            for (Product product : productList) {
                product.setStatus(deactiveStatus);
                hibernateSession.update(product);

            }
        }

    }

}
