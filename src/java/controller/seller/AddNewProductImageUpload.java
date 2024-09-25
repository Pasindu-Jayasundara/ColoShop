package controller.seller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(name = "AddNewProductImageUpload", urlPatterns = {"/AddNewProductImageUpload"})
public class AddNewProductImageUpload extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Part image1 =   (Part) request.getAttribute("img1");
        Part image2 =   (Part) request.getAttribute("img2");
        Part image3 =   (Part) request.getAttribute("img3");

        String applicationPath = request.getServletContext().getRealPath("");
        String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");

        File folder = new File(newApplicationPath + File.separator + "product-images" + File.separator + System.currentTimeMillis());
        folder.mkdir();

        File file1 = new File(folder, "image1.png");
        InputStream inputStream = image1.getInputStream();
        Files.copy(inputStream, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        File file2 = new File(folder, "image2.png");
        InputStream inputStream2 = image2.getInputStream();
        Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        File file3 = new File(folder, "image3.png");
        InputStream inputStream3 = image3.getInputStream();
        Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        request.setAttribute("img1Path", file1.toString());
        request.setAttribute("img2Path", file2.toString());
        request.setAttribute("img3Path", file3.toString());
        
        request.setAttribute("isImageUploadSuccess", true);

    }

}
