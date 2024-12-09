package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Brand;
import entity.Category;
import entity.Product;
import entity.Product_color;
import entity.Size;
import entity.Status;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadProduct", urlPatterns = {"/LoadProduct"})
public class LoadProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int from = (int) request.getAttribute("from");
        int to = (int) request.getAttribute("to");
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
        productCriteria.add(Restrictions.eq("status", statusActive));
        
        //brand
        if (isBrandFound) {
            int brandId = Integer.parseInt((String) request.getAttribute("brandId"));

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
            int categoryId = Integer.parseInt((String) request.getAttribute("categoryId"));

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
            int colorId = Integer.parseInt((String) request.getAttribute("colorId"));

            Criteria colorCriteria = hibernateSession.createCriteria(Product_color.class);
            colorCriteria.add(Restrictions.and(
                    Restrictions.eq("id", colorId),
                    Restrictions.eq("status", statusActive)
            ));
            Product_color color = (Product_color) colorCriteria.uniqueResult();

            if (color != null) {
                productCriteria.add(Restrictions.eq("product_color", color));
            }
        }

        //size
        if (isSizeFound) {
            int id = Integer.parseInt((String) request.getAttribute("sizeId"));

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

            if (!txt.isEmpty()) {
                productCriteria.add(Restrictions.or(
                        Restrictions.like("name", txt, MatchMode.ANYWHERE),
                        Restrictions.like("description", txt, MatchMode.ANYWHERE)
                ));
            }
        }

//        productCriteria.setFirstResult(from);
//        productCriteria.setMaxResults(to);
        int allProductCount = 0;
        List<Product> productList1 = productCriteria.list();
        ArrayList<Product> productList = new ArrayList<>();

        for (Product product : productList1) {

            if (product.getSeller().getUser().getAccount_type().getType().equals("Seller")) {
                allProductCount += 1;
                productList.add(product);
            }
        }

        for (Product product : productList) {
            product.getSeller().setUser(null);
        }

        int searchTO;
        if (to > productList.size()) {
            searchTO = productList.size();
        } else {
            searchTO = to;
        }
        ArrayList<Product> sendProductList = new ArrayList<>();
        for (int i = from; i < searchTO; i++) {
            sendProductList.add(productList.get(i));
        }

        

        //size
        Criteria criteria2 = hibernateSession.createCriteria(Size.class);
        criteria2.add(Restrictions.eq("status", statusActive));
        criteria2.addOrder(Order.asc("id"));
        criteria2.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Size> sizeList = criteria2.list();
        
        
        //color
        Criteria criteria3 = hibernateSession.createCriteria(Product_color.class);
        criteria3.add(Restrictions.eq("status", statusActive));
        criteria3.addOrder(Order.asc("id"));
        criteria3.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Product_color> colorList = criteria3.list();
        
        
        //brand
        Criteria criteria4 = hibernateSession.createCriteria(Brand.class);
        criteria4.add(Restrictions.eq("status", statusActive));
        criteria4.addOrder(Order.asc("id"));
        criteria4.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Brand> brandList = criteria4.list();
        
        
        // category
        Criteria criteria1 = hibernateSession.createCriteria(Category.class);
        criteria1.add(Restrictions.eq("status", statusActive));
        criteria1.addOrder(Order.asc("id"));
        criteria1.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Category> categoryList = criteria1.list();
        
        
        Gson gson = new Gson();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("allProductCount", allProductCount);
        jsonObject.add("productList", gson.toJsonTree(sendProductList));
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("brandList", gson.toJsonTree(brandList));
        jsonObject.add("colorList", gson.toJsonTree(colorList));
        jsonObject.add("sizeList", gson.toJsonTree(sizeList));

        Response_DTO response_DTO = new Response_DTO(true, gson.toJsonTree(jsonObject));

        hibernateSession.close();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

}
