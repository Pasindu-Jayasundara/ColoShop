package controller.seller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.AdminDetailTable;
import entity.Brand;
import entity.Category;
import entity.Message;
import entity.Message_status;
import entity.Message_to_seller;
import entity.OrderDataTable;
import entity.Order_item;
import entity.Order_status;
import entity.Product;
import entity.Product_color;
import entity.Seller;
import entity.Size;
import entity.Status;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session hiberSession = HibernateUtil.getSessionFactory().openSession();

        Criteria statusCriteria = hiberSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status status = (Status) statusCriteria.uniqueResult();

        // category
        Criteria criteria1 = hiberSession.createCriteria(Category.class);
        criteria1.add(Restrictions.eq("status", status));
        criteria1.addOrder(Order.asc("id"));
        List<Category> categoryList = criteria1.list();

        //size
        Criteria criteria2 = hiberSession.createCriteria(Size.class);
        criteria2.add(Restrictions.eq("status", status));
        criteria2.addOrder(Order.asc("id"));
        List<Size> sizeList = criteria2.list();

        //color
        Criteria criteria3 = hiberSession.createCriteria(Product_color.class);
        criteria3.add(Restrictions.eq("status", status));
        criteria3.addOrder(Order.asc("id"));
        List<Product_color> colorList = criteria3.list();

        //brand
        Criteria criteria4 = hiberSession.createCriteria(Brand.class);
        criteria4.add(Restrictions.eq("status", status));
        criteria4.addOrder(Order.asc("id"));
        List<Brand> brandList = criteria4.list();

        //username
        UserTable user = (UserTable) request.getSession().getAttribute("user");
        String name = user.getFirst_name() + " " + user.getLast_name();

        //account type
        boolean isBuyer = false;
        boolean isSeller = false;
        String accountType = user.getAccount_type().getType();
        if (accountType.equals("Buyer")) {
            isBuyer = true;
        } else if (accountType.equals("Seller")) {
            isSeller = true;
        }

        // if seller, my products
        List<Product> productList = null;
        Seller seller = null;
        if (isSeller) {

            // seller obj
            Criteria sellerCriteria = hiberSession.createCriteria(Seller.class);
            sellerCriteria.add(Restrictions.and(
                    Restrictions.eq("user", user),
                    Restrictions.eq("status", status)
            ));
            seller = (Seller) sellerCriteria.uniqueResult();

            if (seller != null) {
                //seller product obj
                Criteria productCriteria = hiberSession.createCriteria(Product.class);
                productCriteria.add(Restrictions.and(
                        Restrictions.eq("seller", seller),
                        Restrictions.eq("status", status)
                ));
                productList = productCriteria.list();

                for (Product product : productList) {
                    product.setSeller(null);
                }
            }
        }

        //product messages
//        List<Message_to_seller> msgToSellerList = null;
        List<Message_to_seller> msgToSellerList = new ArrayList<>();
        if (productList != null) {

            //not replyed message status
            Criteria messageStatusCriteria = hiberSession.createCriteria(Message_status.class);
            messageStatusCriteria.add(Restrictions.ne("id", 3));
            List<Message_status> messageStatusList = messageStatusCriteria.list();

            Criteria msgToSellerCriteria = hiberSession.createCriteria(Message_to_seller.class);
            msgToSellerCriteria.add(Restrictions.and(
                    Restrictions.eq("seller", seller),
                    Restrictions.in("message_status", messageStatusList)
            ));
            msgToSellerList = msgToSellerCriteria.list();

            if (msgToSellerList != null) {
                for (Message_to_seller message : msgToSellerList) {

                    message.setUser(null);
                    message.getSeller().setUser(null);
                    message.getProduct().setSeller(null);
                }
            }

        }

        //delivered order status
        Criteria orderStatusCriteria = hiberSession.createCriteria(Order_status.class);
        orderStatusCriteria.add(Restrictions.eq("status", "Delivered"));
        Order_status deliveredOrderStatus = (Order_status) orderStatusCriteria.uniqueResult();

        //orders
        //if seller its others that should be delivered
        //if buyer its the item they orered
        //iterate over the product list
        //get the order items of that product
        //check whether order status is not delivered
        ArrayList<Order_item> orderList = new ArrayList<>();
        if (productList != null) {
            //seller
            for (Product product : productList) {

                Criteria orderItemCriteria = hiberSession.createCriteria(Order_item.class);
                orderItemCriteria.add(Restrictions.eq("product", product));
                Order_item orderItem = (Order_item) orderItemCriteria.uniqueResult();

                if (orderItem != null) {
                    if (!orderItem.getOrder().getOrder_status().getStatus().equals("Delivered")) {
                        //not delivered
                        orderItem.getOrder().setUser(null);
                        orderList.add(orderItem);
                    }
                }

            }
        } else {
            //buyer
            Criteria orderCriteria = hiberSession.createCriteria(OrderDataTable.class);
            orderCriteria.add(Restrictions.and(
                    Restrictions.eq("user", user),
                    Restrictions.ne("order_status", deliveredOrderStatus)
            ));
            List<OrderDataTable> orderList2 = orderCriteria.list();

            if (orderList2 != null) {

                for (OrderDataTable orderDataTable : orderList2) {
                    Criteria orderItemCriteria = hiberSession.createCriteria(Order_item.class);
                    orderItemCriteria.add(Restrictions.eq("order", orderDataTable));
                    List<Order_item> orderItems = orderItemCriteria.list();

                    for (Order_item item : orderItems) {

                        item.getProduct().getSeller().setUser(null);

                        orderList.add(item); // Add items to the order list
                    }
                }
            }

        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("sizeList", gson.toJsonTree(sizeList));
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        jsonObject.add("brandList", gson.toJsonTree(brandList));
        jsonObject.addProperty("userName", name);
        jsonObject.addProperty("isBuyer", isBuyer);
        jsonObject.addProperty("isSeller", isSeller);
        if (productList != null) {
            jsonObject.add("productList", gson.toJsonTree(productList));
        }
        if (productList != null) {
            jsonObject.add("msgToSellerList", gson.toJsonTree(msgToSellerList));
        }
        jsonObject.add("orderList", gson.toJsonTree(orderList));

        Response_DTO dTO = new Response_DTO(true, gson.toJsonTree(jsonObject));
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(dTO));

        hiberSession.close();

    }

}
