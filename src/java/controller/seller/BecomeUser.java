package controller.seller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Account_type;
import entity.Order_item;
import entity.Product;
import entity.Seller;
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

@WebServlet(name = "BecomeUser", urlPatterns = {"/BecomeUser"})
public class BecomeUser extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserTable user = (UserTable) request.getSession().getAttribute("user");
        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status status = (Status) statusCriteria.uniqueResult();

        boolean hasNotDeliveredProducts = false;
        String message = "";
        // seller obj
        Criteria sellerCriteria = hibernateSession.createCriteria(Seller.class);
        sellerCriteria.add(Restrictions.and(
                Restrictions.eq("user", user),
                Restrictions.eq("status", status)
        ));
        Seller seller = (Seller) sellerCriteria.uniqueResult();
        if (seller != null) {
            //seller product obj
            Criteria productCriteria = hibernateSession.createCriteria(Product.class);
            productCriteria.add(Restrictions.and(
                    Restrictions.eq("seller", seller),
                    Restrictions.eq("status", status)
            ));
            List<Product> productList = productCriteria.list();

            if (!productList.isEmpty()) {
                outer:
                for (Product product : productList) {

                    Criteria orderItemCriteria = hibernateSession.createCriteria(Order_item.class);
                    orderItemCriteria.add(Restrictions.eq("product", product));
                    List<Order_item> orderItemList = (List<Order_item>) orderItemCriteria.list();

                    if (orderItemList != null) {
                        for (Order_item order_item : orderItemList) {

                            if (!order_item.getOrder().getOrder_status().getStatus().equals("Delivered")) {
                                //not delivered
                                hasNotDeliveredProducts = true;
                                message="Not Delivered Products Avaliable";
                                break outer;
                            }
                        }

                    }

                }
            }

        }else{
            message="Not A Seller";
            hasNotDeliveredProducts=true;
        }

        boolean isSuccess = false;
        if (!hasNotDeliveredProducts) {

            Criteria accountTypeCriteria = hibernateSession.createCriteria(Account_type.class);
            accountTypeCriteria.add(Restrictions.eq("type", "Buyer"));

            Account_type accountType = (Account_type) accountTypeCriteria.uniqueResult();

            user.setAccount_type(accountType);
            hibernateSession.merge(user);
            hibernateSession.beginTransaction().commit();
            
            isSuccess = true;
            message="Success";
        }

        hibernateSession.close();

        request.getSession().setAttribute("user", user);

        Response_DTO response_DTO = new Response_DTO(isSuccess, message);
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
