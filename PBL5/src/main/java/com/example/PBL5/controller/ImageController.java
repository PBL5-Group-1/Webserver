package com.example.PBL5.controller;

import com.example.PBL5.model.History;
import com.example.PBL5.service.IHistoryService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Controller
@RequestMapping("/image")
public class ImageController {
    public static final String URL = "http://172.20.10.2";
    private static final String IMAGE_FOLDER_PATH = "E:/image/history";
    @Autowired
    private IHistoryService historyService;

//    @PostMapping("/receive")
//    public ResponseEntity<String> receivePicture(MultipartFile file) {
//        try {
//            // Create a folder named "pictures" if it doesn't exist
//            String picturesFolder = "E:/image/history";
//            File folder = new File(picturesFolder);
//            folder.mkdirs();
//
//            // Generate a unique filename based on the current timestamp
//            LocalDateTime currentTime = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//            String timestamp = currentTime.format(formatter);
//            String timestamp1 = currentTime.format(formatter1);
//            String filename = timestamp + ".jpg";
//
//            // Save the image to the "pictures" folder
//            File imageFile = new File(folder, filename);
//            FileCopyUtils.copy(file.getBytes(), Files.newOutputStream(imageFile.toPath()));
//
//            String imagePath = imageFile.getAbsolutePath();
//            System.out.println("Image saved to: " + imagePath);
//
//            // Chuyển đổi byte thành chuỗi base64
//            Path path = Paths.get(imagePath);
//            byte[] imageBytes = Files.readAllBytes(path);
////            String base64String = Base64.encodeBase64String(imageBytes);
//            String base64String = Base64.getEncoder().encodeToString(imageBytes);
//
//            History history = new History();
//            history.setTime(timestamp1);
//            history.setImage(base64String);
//            history.setAmount(10);
//            historyService.save(history);
//
//            // In chuỗi base64
//            System.out.println(base64String);
//
//            return ResponseEntity.ok("Image received and saved successfully!");
//        } catch (IOException e) {
//            System.out.println("Error: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the image.");
//        }
//    }

    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }

    @PostMapping("/receive")
    public ResponseEntity<String> receiveImage(
            @RequestBody byte[] imageData,
            @RequestHeader("X-Extra-Data") int extraData) {

        try {
            // Create the folder if it doesn't exist
            Path imageFolderPath = Paths.get(IMAGE_FOLDER_PATH);
            if (!imageFolderPath.toFile().exists()) {
                imageFolderPath.toFile().mkdirs();
            }

            // Generate a unique filename based on the current timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = timestamp + ".jpg";


            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));


            // Save the image to the specified folder
            Path imagePath = Paths.get(IMAGE_FOLDER_PATH, filename);
            try (FileOutputStream fileOutputStream = new FileOutputStream(imagePath.toFile())) {
                fileOutputStream.write(imageData);
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Encode the byte array to Base64
            String base64Encoded = Base64.getEncoder().encodeToString(imageBytes);
            System.out.println(base64Encoded);

            History history = new History();
            history.setTime(time);
            history.setImage(base64Encoded);
            history.setAmount(extraData);
            history.setShowed(false);
            historyService.save(history);

            // Print the extra data to the console
            System.out.println("Received Extra Data: " + extraData);
            System.out.println("Image saved to: " + imagePath);

            return ResponseEntity.ok("Image received and saved successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the image.");
        }
    }

    @GetMapping("/capture")
    public ResponseEntity<String> capturePhotoFromESP32(Model model, RedirectAttributes redirectAttributes) throws IOException {
        try {
            // Tạo đối tượng HttpClient
            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = null;
            // Tạo yêu cầu POST tới ESP32
            try {
                HttpGet request = new HttpGet(URL + "/takepicture");
                // Gửi yêu cầu và nhận phản hồi
                response = httpClient.execute(request);
                System.out.println(response.getStatusLine());
            } catch (SocketTimeoutException e) {
                // Xử lý khi yêu cầu bị timeout
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Request timeout. Please try again later.");
            }

            // Đọc nội dung phản hồi
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);
            if (responseString.contains("OK")) {
                redirectAttributes.addFlashAttribute("mess", "Chụp ảnh thành công!");
            } else {
                redirectAttributes.addFlashAttribute("mess", "Đã có lỗi xảy ra!");
            }
//            return "redirect:/image";
            return ResponseEntity.ok("Capture request sent to ESP32.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("mess", "Đã có lỗi xảy ra!");
//            return "redirect:/image";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send capture request to ESP32.");
        }
    }

    @GetMapping("")
    public String showList(Model model,
                           @RequestParam(required = false, defaultValue = "") String timeBegin,
                           @RequestParam(required = false, defaultValue = "") String timeEnd,
                           @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5, Sort.by("time").descending());
        historyService.setAllHistoryShowed();
        model.addAttribute("historyList", historyService.findAll());
        model.addAttribute("mess", "");
        return "image_history";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam int id, RedirectAttributes redirectAttributes) {
        History history = historyService.findById(id);
        historyService.remove(history);
        redirectAttributes.addFlashAttribute("mess", "Đã xoá thành công");
        return "redirect:/image";
    }

    @GetMapping("/multiDelete")
    public String multiDelete(@RequestParam String idDeleteMore, RedirectAttributes redirectAttributes) {
        idDeleteMore = removeCharAt(idDeleteMore, 0);

        String[] idDelete = idDeleteMore.split("\\.");
        int[] arrId = new int[idDelete.length];
        for (int i = 0; i < idDelete.length; i++) {
            arrId[i] = Integer.parseInt(idDelete[i]);
        }

        for (int i : arrId) {
            History history = new History();
            history = historyService.findById(i);
            historyService.remove(history);
        }
        redirectAttributes.addFlashAttribute("mess", "Đã xoá thành công");
        return "redirect:/list";
    }
}
