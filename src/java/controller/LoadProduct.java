package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.Product;
import entity.Product_color;
import entity.Size;
import entity.Status;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadProduct", urlPatterns = {"/LoadProduct"})
public class LoadProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int resultCount = (int) request.getAttribute("productCount");
        boolean isBrandFound = (boolean) request.getAttribute("isBrandFound");
        boolean isCategoryFound = (boolean) request.getAttribute("isCategoryFound");
        boolean isColorFound = (boolean) request.getAttribute("isColorFound");
        boolean isSizeFound = (boolean) request.getAttribute("isSizeFound");
        boolean isSortFound = (boolean) request.getAttribute("isSortFound");
        boolean isSearchFound = (boolean) request.getAttribute("isSearchFound");

        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
        statusCriteria.add(Restrictions.eq("name", "Active"));
        Status statusActive = (Status) statusCriteria.uniqueResult();

        Criteria productCriteria = hibernateSession.createCriteria(Product.class);

        //brand
        if (isBrandFound) {
            String brandId = (String) request.getAttribute("brandId");

            Criteria brandCriteria = hibernateSession.createCriteria(Brand.class);
            brandCriteria.add(Restrictions.and(
                    Restrictions.eq("id", brandId),
                    Restrictions.eq("status", statusActive)
            ));
            Brand brand = (Brand) brandCriteria.uniqueResult();

            if (brand != null) {
                productCriteria.add(Restrictions.eq("brand", brand));
            }
        }

        //category
        if (isCategoryFound) {
            String categoryId = (String) request.getAttribute("categoryId");

            Criteria categoryCriteria = hibernateSession.createCriteria(Category.class);
            categoryCriteria.add(Restrictions.and(
                    Restrictions.eq("id", categoryId),
                    Restrictions.eq("status", statusActive)
            ));
            Category category = (Category) categoryCriteria.uniqueResult();

            if (category != null) {
                productCriteria.add(Restrictions.eq("category", category));
            }
        }

        //color
        if (isColorFound) {
            String colorId = (String) request.getAttribute("colorId");

            Criteria colorCriteria = hibernateSession.createCriteria(Product_color.class);
            colorCriteria.add(Restrictions.and(
                    Restrictions.eq("id", colorId),
                    Restrictions.eq("status", statusActive)
            ));
            Product_color color = (Product_color) colorCriteria.uniqueResult();

            if (color != null) {
                productCriteria.add(Restrictions.eq("color", color));
            }
        }

        //size
        if (isSizeFound) {
            String id = (String) request.getAttribute("sizeId");

            Criteria criteria = hibernateSession.createCriteria(Size.class);
            criteria.add(Restrictions.and(
                    Restrictions.eq("id", id),
                    Restrictions.eq("status", statusActive)
            ));
            Size size = (Size) criteria.uniqueResult();

            if (size != null) {
                productCriteria.add(Restrictions.eq("size", size));
            }
        }

        //sort
        if (isSortFound) {
            String sortBy = (String) request.getAttribute("sortBy");

            if (sortBy.equals("default")) {
//                productCriteria.addOrder(Order.desc("RAND()"));
                productCriteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));

            } else if (sortBy.equals("new")) {
                productCriteria.addOrder(Order.desc("id"));

            } else if (sortBy.equals("priceasc")) {
                productCriteria.addOrder(Order.asc("unit_price"));

            } else if (sortBy.equals("pricedesc")) {
                productCriteria.addOrder(Order.desc("unit_price"));

            }

        } else {
            productCriteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
        }

        //search
        if (isSearchFound) {
            String txt = (String) request.getAttribute("search");

            if (!txt.isBlank()) {
                productCriteria.add(Restrictions.or(
                        Restrictions.like("name", txt, MatchMode.ANYWHERE),
                        Restrictions.like("description", txt, MatchMode.ANYWHERE)
                ));
            }
        }

        productCriteria.setMaxResults(resultCount);

        List<Product> productList = productCriteria.list();
        for (Product product : productList) {
            product.getSeller().setUser(null);
        }

        Gson gson = new Gson();
        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(productList));

        hibernateSession.close();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
