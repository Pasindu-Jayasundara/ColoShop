package controller;

import entity.OrderDataTable;
import entity.Order_status;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Payhere;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "VerifyPayment", urlPatterns = {"/VerifyPayment"})
public class VerifyPayment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String merchant_id = request.getParameter("merchant_id");
        String order_id = request.getParameter("order_id");
        String payhere_amount = request.getParameter("payhere_amount");
        String payhere_currency = request.getParameter("payhere_currency");
        String status_code = request.getParameter("status_code");
        String md5sig = request.getParameter("md5sig");

        String merchant_secret = "MTM1MDE0NTg5OTIxMTcxNDkxOTAxMTcxMTU2MTc3MTY0NzAxODc3Ng==";
        String merchant_secret_md5hash = Payhere.generateMD5(merchant_secret);

        String generated_md5hash = Payhere.generateMD5(
                merchant_id
                + order_id
                + payhere_amount
                + payhere_currency
                + status_code
                + merchant_secret_md5hash
        );

        if (generated_md5hash.equals(md5sig) && status_code.equals("2")) {
            //update your order status paid

            System.out.println("payment completed ");

            Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

            Criteria orderStatusCriteria = hibernateSession.createCriteria(Order_status.class);
            orderStatusCriteria.add(Restrictions.eq("status", "Paid"));
            Order_status status = (Order_status) orderStatusCriteria.uniqueResult();

            // order 
            OrderDataTable order = (OrderDataTable) hibernateSession.get(OrderDataTable.class, Integer.parseInt(order_id));
            order.setOrder_status(status);

            hibernateSession.update(order);

            hibernateSession.beginTransaction().commit();
            hibernateSession.close();

        }

    }

}
