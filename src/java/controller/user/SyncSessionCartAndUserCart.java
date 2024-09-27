package controller.user;

import entity.Cart;
import entity.Product;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

@WebServlet(name = "SyncSessionAndUserCart", urlPatterns = {"/SyncSessionAndUserCart"})
public class SyncSessionCartAndUserCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        ArrayList<Product> newCartProduct = new ArrayList<>();
        ArrayList<Product> sessionCartProductList = (ArrayList<Product>) request.getSession().getAttribute("userSessionCart");

        if (sessionCartProductList != null) {

            for (Product product : sessionCartProductList) {

                int productId = product.getId();
                Cart cartProduct = (Cart) hibernateSession.get(Cart.class, productId);
                if (cartProduct == null) {
                    //cart doesnot have this product
                    // add to cart
                    newCartProduct.add(product);
                }

            }
        }

        hibernateSession.close();
        request.getSession().removeAttribute("userSessionCart");

        if (!newCartProduct.isEmpty()) {
            //have new products to be added
            
            request.setAttribute("newCartProduct", newCartProduct);
            request.setAttribute("isLoggedIn",true);
            request.setAttribute("isOnlyOneProduct",false);
            request.getRequestDispatcher("/AddToCart").include(request, response);
        }

    }

}
