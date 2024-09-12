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
@WebServlet(name = "UpdateProductImageUpload", urlPatterns = {"/UpdateProductImageUpload"})
public class UpdateProductImageUpload extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String imageFolderParentFolder = request.getServletContext().getRealPath("").replace("build" + File.separator + "web", "web");
        if(!request.getParameter("image1").startsWith(imageFolderParentFolder)){
            //new image 1
            deleteExistingImage(imageFolderParentFolder, "image1");
            uploadImage(imageFolderParentFolder,"image1",request.getPart("image1"),request);
        }
        
        if(!request.getParameter("image2").startsWith(imageFolderParentFolder)){
            //new image 2
            deleteExistingImage(imageFolderParentFolder, "image2");
            uploadImage(imageFolderParentFolder,"image2",request.getPart("image2"),request);
        }
        
        if(!request.getParameter("image3").startsWith(imageFolderParentFolder)){
            //new image 3
            deleteExistingImage(imageFolderParentFolder, "image3");
            uploadImage(imageFolderParentFolder,"image3",request.getPart("image3"),request);
        }

        request.setAttribute("isImageUpdateSuccess", true);
        
    }
    private void deleteExistingImage(String imageFolderParentFolder, String imageName) {
        File existingFile = new File(imageFolderParentFolder + File.separator + imageName);
        if (existingFile.exists()) {
            existingFile.delete(); 
        }
    }
    
    private void uploadImage(String imageFolderParentFolder,String imageNumber,Part image,HttpServletRequest request) throws ServletException, IOException{

        File file = new File(imageFolderParentFolder, imageNumber+".png");
        InputStream inputStream = image.getInputStream();
        Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
        request.setAttribute(imageNumber, file.toString());
    }

}
