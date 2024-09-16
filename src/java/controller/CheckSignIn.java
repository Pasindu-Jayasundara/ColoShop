package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import netscape.javascript.JSObject;

@WebServlet(name = "CheckSignIn", urlPatterns = {"/CheckSignIn"})
public class CheckSignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject fj = gson.fromJson(request.getReader(), JsonObject.class);

        String address = fj.get("address").getAsString();
        String text = fj.get("text").getAsString();

        String message = "";
        boolean isTextOk = true;
        boolean isAddressOk = true;

        boolean isLoggedIn = true;
        if (request.getSession().getAttribute("user") == null) {
            //already not logedin user
            isLoggedIn = false;

        } else {

            if (address.isBlank()) {
                message = "Missing Address";
                isAddressOk = false;
            } else if (text.isBlank()) {
                message = "Missing Text";
                isTextOk = false;
            } else if (text.length() > 250) {
                message = "Text Too long";
                isTextOk = false;
            }
        }

        JsonObject jo = new JsonObject();
        jo.addProperty("text", isTextOk);
        jo.addProperty("address", isAddressOk);
        jo.addProperty("message", message);
        jo.addProperty("isLoggedIn", isLoggedIn);
        
        Response_DTO response_DTO = new Response_DTO(true, jo);

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));

    }

}
