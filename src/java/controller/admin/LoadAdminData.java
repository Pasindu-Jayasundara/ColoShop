package controller.admin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.AdminDetailTable;
import entity.Brand;
import entity.Category;
import entity.Message;
import entity.Message_status;
import entity.Product;
import entity.Product_color;
import entity.Seller;
import entity.Size;
import entity.Status;
import entity.UserTable;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadAdminData", urlPatterns = {"/LoadAdminData"})
public class LoadAdminData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status status = (Status) statusCriteria.uniqueResult();

        //  total sellers
        Criteria sellerCriteria = hibernateSession.createCriteria(Seller.class);
        sellerCriteria.add(Restrictions.eq("status", status));
        sellerCriteria.setProjection(
                Projections.count("id")
        );
        String sellerCount = (String) sellerCriteria.uniqueResult();

        // total users
        Criteria userCriteria = hibernateSession.createCriteria(UserTable.class);
        userCriteria.add(Restrictions.eq("status", status));
        userCriteria.setProjection(
                Projections.count("id")
        );
        String userCount = (String) userCriteria.uniqueResult();

        // total buyers
        int buyerCount = Integer.parseInt(userCount) - Integer.parseInt(sellerCount);

        // total products
        Criteria productCriteria = hibernateSession.createCriteria(Product.class);
        productCriteria.add(Restrictions.eq("status", status));
        productCriteria.setProjection(
                Projections.count("id")
        );
        String productCount = (String) productCriteria.uniqueResult();

        //username
        AdminDetailTable admin = (AdminDetailTable) request.getSession().getAttribute("admin");
        String name = admin.getFirst_name() + " " + admin.getLast_name();

        //not replyed message status
        Criteria messageStatusCriteria = hibernateSession.createCriteria(Message_status.class);
        messageStatusCriteria.add(Restrictions.ne("id", 3));
        messageStatusCriteria.setProjection(
                Projections.count("id")
        );
        Message_status messageStatus = (Message_status) messageStatusCriteria.uniqueResult();

        // message
        Criteria messageCriteria = hibernateSession.createCriteria(Message.class);
        messageCriteria.add(Restrictions.eq("message_status", messageStatus));
        ArrayList<Message> messageList = (ArrayList<Message>) messageCriteria.list();

//        JsonArray jsonMessages = new JsonArray();
        for (Message message : messageList) {
            
            message.getUser().setAccount_type(null);
            message.getUser().setEmail(null);
            message.getUser().setFirst_name(null);
            message.getUser().setLast_name(null);
            message.getUser().setPassword(null);
            message.getUser().setStatus(null);
            message.getUser().setToken(null);
            message.getUser().setVerified_status(null);

        }
        
        //features
        
        //color
        Criteria colorCriteria = hibernateSession.createCriteria(Product_color.class);
        colorCriteria.add(Restrictions.eq("status", status));
        ArrayList<Product_color> colorList = (ArrayList<Product_color>) colorCriteria.list();

//        JsonArray jsonColors = new JsonArray();
//        for (Product_color color : colorList) {
//            
//            JsonObject colorJson = new JsonObject();
//            
//            colorJson.addProperty("id", color.getId()); 
//            colorJson.addProperty("color", color.getColor()); 
//            
//            jsonColors.add(colorJson);
//        }

        
        //category
        Criteria categoryCriteria = hibernateSession.createCriteria(Category.class);
        categoryCriteria.add(Restrictions.eq("status", status));
        ArrayList<Category> categoryList = (ArrayList<Category>) categoryCriteria.list();

//        JsonArray jsonCategory = new JsonArray();
//        for (Category category : categoryList) {
//            
//            JsonObject categoryJson = new JsonObject();
//            
//            categoryJson.addProperty("id", category.getId()); 
//            categoryJson.addProperty("category", category.getCategory()); 
//            
//            jsonCategory.add(categoryJson);
//        }

        
        //brand
        Criteria brandCriteria = hibernateSession.createCriteria(Brand.class);
        brandCriteria.add(Restrictions.eq("status", status));
        ArrayList<Brand> brandList = (ArrayList<Brand>) brandCriteria.list();

//        JsonArray jsonBrand = new JsonArray();
//        for (Brand brand : brandList) {
//            
//            JsonObject categoryJson = new JsonObject();
//            
//            categoryJson.addProperty("id", brand.getId()); 
//            categoryJson.addProperty("brand", brand.getBrand()); 
//            
//            jsonBrand.add(categoryJson);
//        }
        
        
        
        //size
        Criteria sizeCriteria = hibernateSession.createCriteria(Size.class);
        sizeCriteria.add(Restrictions.eq("status", status));
        ArrayList<Size> sizeList = (ArrayList<Size>) sizeCriteria.list();

//        JsonArray jsonSize = new JsonArray();
//        for (Size size : sizeList) {
//            
//            JsonObject sizeJson = new JsonObject();
//            
//            sizeJson.addProperty("id", size.getId()); 
//            sizeJson.addProperty("size", size.getSize()); 
//            
//            jsonSize.add(sizeJson);
//        }
        
        
        
        
                Gson gson = new Gson();

        JsonObject jo = new JsonObject();
        jo.addProperty("sellerCount", sellerCount);
        jo.addProperty("buyerCount", buyerCount);
        jo.addProperty("productCount", productCount);
        jo.addProperty("userName", name);
        jo.add("messageArr", gson.toJsonTree(messageList));
        jo.add("colorArr", gson.toJsonTree(colorList));
        jo.add("categoryArr", gson.toJsonTree(categoryList));
        jo.add("brandArr",gson.toJsonTree(brandList) );
        jo.add("sizeArr", gson.toJsonTree(sizeList));

        Response_DTO response_DTO = new Response_DTO(true, jo);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
