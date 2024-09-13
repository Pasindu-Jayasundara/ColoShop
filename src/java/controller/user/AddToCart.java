package controller.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Cart;
import entity.Product;
import entity.UserTable;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        boolean isOnlyOneProduct = (boolean) request.getAttribute("isOnlyOneProduct");
        boolean isLoggedIn = (boolean) request.getAttribute("isLoggedIn");

        String message = "";

        if (!isLoggedIn && isOnlyOneProduct) {

            //not logged in
            //have only one product id
            //add to session cart
            int productId = (int) request.getAttribute("productId");
            Product product = (Product) hibernateSession.get(Product.class, productId);

            boolean isNewTOCart = true;
            ArrayList<Product> productList;
            if (request.getSession(false).getAttribute("userSessionCart") != null) {

                //already have session cart
                productList = (ArrayList<Product>) request.getSession().getAttribute("userSessionCart");
                if (!productList.isEmpty()) {
                    for (Product oneProduct : productList) {
                        if (oneProduct.getId() == productId) {
                            message = "Product Already In Cart";
                            isNewTOCart = false;

                            break;
                        }
                    }
                }

            } else {
                // If the session cart does not exist, create a new ArrayList
                productList = new ArrayList<>();
                request.getSession().setAttribute("userSessionCart", productList);
            }
            if (isNewTOCart) {
                productList = (ArrayList<Product>) request.getSession().getAttribute("userSessionCart");
                boolean add = productList.add(product);
                System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
                if (add) {
                    message = "Product Successfully Added";
                } else {
                    message = "Product Adding Faild";

                }
                System.out.println("sssssssssssssssssssssssssssssssss");
                System.out.println("sssssssssssssssssssssssssssssssss");
                System.out.println("sssssssssssssssssssssssssssssssss");
                System.out.println("sssssssssssssssssssssssssssssssss");
            }

        } else if (isLoggedIn && isOnlyOneProduct) {
            //logged in user
            //have only one product id
            //add to db cart
            int productId = Integer.parseInt(request.getParameter("productId"));
            UserTable user = (UserTable) request.getSession().getAttribute("user");

            if (hibernateSession.get(Cart.class, productId) == null) {
                //not avaliable in db cart

                Product product = (Product) hibernateSession.get(Product.class, productId);

                Cart cart = new Cart();
                cart.setProduct(product);
                cart.setUser(user);

                hibernateSession.save(cart);
                hibernateSession.beginTransaction().commit();

                message = "Product Successfully Added";
            } else {
                message = "Product Already In Cart";
            }

        } else if (isLoggedIn && !isOnlyOneProduct) {
            //logged in user
            //have arraylist of cart objects
            //add to db cart
            UserTable user = (UserTable) request.getSession().getAttribute("user");

            ArrayList<Product> productArrayList = (ArrayList<Product>) request.getSession().getAttribute("userSessionCart");
            for (Product product : productArrayList) {

                Cart cart = new Cart();
                cart.setProduct(product);
                cart.setUser(user);

                hibernateSession.save(cart);

            }
            hibernateSession.beginTransaction().commit();

        }

        hibernateSession.close();

        Gson gson = new Gson();

        Response_DTO response_DTO = new Response_DTO(true, message);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
