package controller.seller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Brand;
import entity.Category;
import entity.Product_color;
import entity.Size;
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
import org.hibernate.criterion.Order;

@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        Session hiberSession = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = hiberSession.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("id"));
        List<Category> categoryList = criteria1.list();

        Criteria criteria2 = hiberSession.createCriteria(Size.class);
        criteria2.addOrder(Order.asc("id"));
        List<Size> sizeList = criteria2.list();

        Criteria criteria3 = hiberSession.createCriteria(Product_color.class);
        criteria3.addOrder(Order.asc("id"));
        List<Product_color> colorList = criteria3.list();

        Criteria criteria4 = hiberSession.createCriteria(Brand.class);
        criteria4.addOrder(Order.asc("id"));
        List<Brand> brandList = criteria4.list();

        

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("sizeList", gson.toJsonTree(sizeList));
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        jsonObject.add("brandList", gson.toJsonTree(brandList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

        hiberSession.close();

    }

}
