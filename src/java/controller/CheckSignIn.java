package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
public class CheckSignIn extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        boolean isLoggedIn = true;
        if (request.getSession().getAttribute("user") == null) {
            //already not logedin user
            isLoggedIn = false;

        } 

        JsonObject jo = new JsonObject();
        jo.addProperty("isLoggedIn", isLoggedIn);
        
        Response_DTO response_DTO = new Response_DTO(isLoggedIn, jo);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
