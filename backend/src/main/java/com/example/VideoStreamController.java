package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VideoStreamController {

    @Autowired
    VideoStreamService vid;

    private String bucket = "aygo-bucket-project-test";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Welcome to the Video Streamer!");
    }

    @GetMapping("/watch")
    public String designer() {
        return "video";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    // Upload a MP4 to an Amazon S3 bucket
    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam String description) {
        try {
            byte[] bytes = file.getBytes();
            String name = file.getOriginalFilename();
            String desc2 = description;

            // Put the MP4 file into an Amazon S3 bucket.
            vid.putVideo(bytes, bucket, name, desc2);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("File uploaded successfully!");
    }

    // Returns items to populate the Video menu.
    @RequestMapping(value = "/items", method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> getItems(HttpServletRequest request, HttpServletResponse response) {
        return vid.getTags(bucket);
    }

    // Returns the video in the bucket specified by the ID value.
    @RequestMapping(value = "/{id}/stream", method = RequestMethod.GET)
    public Mono<ResponseEntity<byte[]>> streamVideo(@PathVariable String id) {

        String fileName = id;
        return Mono.just(vid.getObjectBytes(bucket, fileName));
    }
}
