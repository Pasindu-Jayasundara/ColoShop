package model.user;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Status;
import entity.UserTable;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UserRegister_VerifyDataFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Gson gson = new Gson();
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        User_DTO fromJson = gson.fromJson(httpServletRequest.getReader(), User_DTO.class);

        boolean isInvalid = false;
        String errorMessage = "";

        if (fromJson.getFirst_name() == null || fromJson.getFirst_name().trim() == "") {
            // no first name
            isInvalid = true;
            errorMessage = "Missing First Name";

        } else if (fromJson.getLast_name() == null || fromJson.getLast_name().trim() == "") {
            //no last name
            isInvalid = true;
            errorMessage = "Missing Last Name";

        } else if (fromJson.getEmail() == null || fromJson.getEmail().trim() == "") {
            //no email
            isInvalid = true;
            errorMessage = "Missing Email Address";

        } else if (fromJson.getPassword() == null || fromJson.getPassword().trim() == "") {
            //no password
            isInvalid = true;
            errorMessage = "Missing Password";

        } else if (fromJson.getRe_type_password() == null || fromJson.getRe_type_password().trim() == "") {
            //no retype password
            isInvalid = true;
            errorMessage = "Missing Re-Typed Password";

        } else {

            String first_name = fromJson.getFirst_name();
            String last_name = fromJson.getLast_name();
            String email = fromJson.getEmail();
            String password = fromJson.getPassword();
            String re_type_password = fromJson.getRe_type_password();

            if (first_name.length() > 45) {
                //first name too long
                isInvalid = true;
                errorMessage = "First Name Too Long";

            } else if (last_name.length() > 45) {
                //last  name too long
                isInvalid = true;
                errorMessage = "Last Name Too Long";

            } else if (email.length() > 60) {
                //email too long
                isInvalid = true;
                errorMessage = "Email Too Long";

            } else if (password.length() > 45) {
                //password too long
                isInvalid = true;
                errorMessage = "Password Too Long";

            } else if (re_type_password.length() > 45) {
                //retype password too long
                isInvalid = true;
                errorMessage = "Re-Type Password Too Long";

            } else {

                if (!Validation.isValidEmail(email)) {
                    //invalid email format 
                    isInvalid = true;
                    errorMessage = "Invalid Email Format";

                } else if (!Validation.isValidName(first_name)) {
                    //invalid first name
                    isInvalid = true;
                    errorMessage = "Invalid First Name Format";

                } else if (!Validation.isValidName(last_name)) {
                    //invalid last name
                    isInvalid = true;
                    errorMessage = "Invalid Last Name Format";

                } else if (!Validation.isValidPassword(password)) {
                    //invalid password
                    isInvalid = true;
                    errorMessage = "Invalid Password Format";

                } else if (!Validation.isValidPassword(re_type_password)) {
                    //invalid retype password
                    isInvalid = true;
                    errorMessage = "Invalid Re-Typed Password Format";

                } else {

                    if (!password.equals(re_type_password)) {
                        // 2 passwords dont match
                        isInvalid = true;
                        errorMessage = "Miss-match Passwords";

                    } else {
                        Session hibernateSession = HibernateUtil.getSessionFactory().openSession();

                        //status
                        Criteria statusCriteria = hibernateSession.createCriteria(Status.class);
                        statusCriteria.add(Restrictions.eq("name", "Active"));
                        Status activeStatus = (Status) statusCriteria.uniqueResult();

                        //find user
                        Criteria userCriteria = hibernateSession.createCriteria(UserTable.class);

                        userCriteria.add(Restrictions.and(
                                Restrictions.eq("email", email),
                                Restrictions.eq("status", activeStatus))
                        );

                        if (!userCriteria.list().isEmpty()) {
                            // already emailregistered
                            Response_DTO response_DTO = new Response_DTO(false, "User Already Registered");

                            hibernateSession.close();

                            response.setContentType("application/json");
                            response.getWriter().write(gson.toJson(response_DTO));
                        } else {

                            request.setAttribute("first_name", first_name);
                            request.setAttribute("last_name", last_name);
                            request.setAttribute("email", email);
                            request.setAttribute("password", password);
                            request.setAttribute("re_type_password", re_type_password);

                            chain.doFilter(request, response);
                        }

                    }
                }

            }
        }

        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, errorMessage);

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
