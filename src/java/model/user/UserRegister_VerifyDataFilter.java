package model.user;

import com.Check;
import com.google.gson.Gson;
import dto.Response_DTO;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class UserRegister_VerifyDataFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        boolean isInvalid = false;
        String errorMessage = "";

        if (request.getParameter("first_name") == null && request.getParameter("first_name").isBlank()) {
            // no first name
            isInvalid = true;
            errorMessage = "Missing First Name";

        } else if (request.getParameter("last_name") == null && request.getParameter("last_name").isBlank()) {
            //no last name
            isInvalid = true;
            errorMessage = "Missing Last Name";

        } else if (request.getParameter("email") == null && request.getParameter("email").isBlank()) {
            //no email
            isInvalid = true;
            errorMessage = "Missing Email Address";

        } else if (request.getParameter("password") == null && request.getParameter("password").isBlank()) {
            //no password
            isInvalid = true;
            errorMessage = "Missing Password";

        } else if (request.getParameter("re_type_password") == null && request.getParameter("re_type_password").isBlank()) {
            //no retype password
            isInvalid = true;
            errorMessage = "Missing Re-Typed Password";

        } else {

            String first_name = request.getParameter("first_name");
            String last_name = request.getParameter("last_name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String re_type_password = request.getParameter("re_type_password");

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

                if (!Check.isValidEmail(email)) {
                    //invalid email format 
                    isInvalid = true;
                    errorMessage = "Invalid Email Format";

                } else if (!Check.isValidName(first_name)) {
                    //invalid first name
                    isInvalid = true;
                    errorMessage = "Invalid First Name Format";

                } else if (!Check.isValidName(last_name)) {
                    //invalid last name
                    isInvalid = true;
                    errorMessage = "Invalid Last Name Format";

                } else if (!Check.isValidPassword(password)) {
                    //invalid password
                    isInvalid = true;
                    errorMessage = "Invalid Password Format";

                } else if (!Check.isValidPassword(re_type_password)) {
                    //invalid retype password
                    isInvalid = true;
                    errorMessage = "Invalid Re-Typed Password Format";

                } else {

                    if (!password.equals(re_type_password)) {
                        // 2 passwords dont match
                        isInvalid = true;
                        errorMessage = "Miss-match Passwords";

                    } else {
                        chain.doFilter(request, response);

                    }
                }

            }
        }

        if (isInvalid) {
            Response_DTO response_DTO = new Response_DTO(false, errorMessage);
            Gson gson = new Gson();

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
        }

    }

    @Override
    public void destroy() {
    }

}
