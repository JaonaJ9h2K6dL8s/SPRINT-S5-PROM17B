package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.monframework.FileUpload;
import com.monframework.ModelView;
import com.monframework.Url;

import jakarta.servlet.http.HttpServletRequest;

public class UploadController {
    
    @Url("/upload-form")
    public ModelView showUploadForm() {
        ModelView mv = new ModelView("uploadForm.jsp");
        return mv;
    }
    
    @Url("/upload")
    public ModelView uploadFile(FileUpload fichier, HttpServletRequest request) {
        ModelView mv = new ModelView("uploadSuccess.jsp");
        
        if (fichier != null) {
            mv.addObject("message", "Fichier uploadé avec succès!");
            mv.addObject("fileName", fichier.getNomFichier());
            mv.addObject("fileSize", fichier.getTaille());
            mv.addObject("uploadSuccess", true);
            
            // Ajouter le chemin du fichier sauvegardé
            String uploadPath = request.getServletContext().getRealPath("") + File.separator + "uploads";
            mv.addObject("filePath", uploadPath + File.separator + fichier.getNomFichier());
            
        } else {
            mv.addObject("message", "Échec de l'upload! Aucun fichier sélectionné.");
            mv.addObject("uploadSuccess", false);
        }
        
        return mv;
    }
    
    @Url("/list-files")
    public ModelView listFiles(HttpServletRequest request) {
        ModelView mv = new ModelView("fileList.jsp");
        
        String uploadPath = request.getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        
        List<FileInfo> files = new ArrayList<>();
        if (uploadDir.exists() && uploadDir.isDirectory()) {
            File[] fileList = uploadDir.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (file.isFile()) {
                        files.add(new FileInfo(file.getName(), file.length(), 
                                file.lastModified()));
                    }
                }
            }
        }
        
        mv.addObject("files", files);
        mv.addObject("fileCount", files.size());
        mv.addObject("uploadPath", uploadPath);
        
        return mv;
    }
    
    // Classe interne pour stocker les infos des fichiers
    public static class FileInfo {
        private String name;
        private long size;
        private long lastModified;
        
        public FileInfo(String name, long size, long lastModified) {
            this.name = name;
            this.size = size;
            this.lastModified = lastModified;
        }
        
        public String getName() { return name; }
        public long getSize() { return size; }
        public long getLastModified() { return lastModified; }
        
        public String getFormattedSize() {
            if (size < 1024) return size + " B";
            else if (size < 1024 * 1024) return (size / 1024) + " KB";
            else return String.format("%.2f MB", size / (1024.0 * 1024.0));
        }
        
        public String getFormattedDate() {
            return new Date(lastModified).toString();
        }
    }
}