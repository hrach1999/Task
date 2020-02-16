package com.sflpro.chat.util;

import com.sflpro.chat.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Util {
    private static Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private static Pattern passwordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})");
    public static String IMAGE_FOLDER = System.getProperty("user.dir") + "/images/";
    public static String IMAGE = "images/";

    private Util() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static boolean isValidUser(User user) {
        if (user == null)
            return false;
        if (isBlank(user.getFirstName()) || isBlank(user.getLastName()))
            return false;
        if (!isValidEmail(user.getEmail()))
            return false;
        return passwordPattern.matcher(user.getPassword()).matches();
    }

    public static String saveImage(MultipartFile file) throws IOException {
        String imagePath = IMAGE_FOLDER;
        String realPath = Paths.get(imagePath, file.getOriginalFilename()).toString();
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(realPath)))) {
            out.write(file.getBytes());
            out.flush();
        }
        return "/" + IMAGE + file.getOriginalFilename();
    }

    public static boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }
}
