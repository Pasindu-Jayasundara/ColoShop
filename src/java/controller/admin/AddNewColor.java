package controller.admin;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Product_color;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "AddNewColor", urlPatterns = {"/AddNewColor"})
public class AddNewColor extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String productColor = request.getParameter("color");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Product_color product_color = new Product_color();
        product_color.setColor(productColor);

        hibernateSession.save(product_color);
        hibernateSession.beginTransaction().commit();
        hibernateSession.close();

        Response_DTO response_DTO = new Response_DTO(false, "Product Color Adding Successfull");
        Gson gson = new Gson();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
