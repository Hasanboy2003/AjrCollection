package com.yurakamri.ajrcollection.service;

import com.yurakamri.ajrcollection.entity.Attachment;
import com.yurakamri.ajrcollection.payload.ApiResponse;
import com.yurakamri.ajrcollection.repository.AttachmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    public static final String uploadedFiles = "attachment/";
//    public static final String uploadedFiles = "";

    private final AttachmentRepo attachmentRepo;

    public ApiResponse addFiles(MultipartHttpServletRequest request) throws IOException {
        Iterator<String> fileNames = request.getFileNames();
        List<UUID> attachmentIdList = new ArrayList<>();
        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            List<MultipartFile> files = request.getFiles(fileName);
            for (MultipartFile file : files) {
                if (file != null) {
                    Attachment attachment = new Attachment(file.getOriginalFilename(),
                            file.getContentType(), file.getSize());
                    String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                    String generateName = UUID.randomUUID() + "." + split[split.length - 1];
                    attachment.setGeneratedName(generateName);
                    attachment.setFileLocation(uploadedFiles);
                    Path path = Paths.get(uploadedFiles + generateName);
                    Files.copy(file.getInputStream(),path);
                    Attachment savedAttachment = attachmentRepo.save(attachment);
                    attachmentIdList.add(savedAttachment.getId());
                }
            }
        }
        return new ApiResponse(true, "Fayl(lar) saqlandi", attachmentIdList);
    }

    public ApiResponse getAttachmentById(UUID id, HttpServletResponse response) throws IOException {
        Optional<Attachment> attachmentOptional = attachmentRepo.findById(id);
        if (attachmentOptional.isPresent()) {
            Attachment attachment = attachmentOptional.get();
            // File ni name
            response.setHeader("Content-Disposition",
                    "attachment; filename=\""
                            + attachment.getOriginalName() + "\"");

            // File ni Content Type
            response.setContentType(attachment.getContentType());

            // inputStream va response.getOutputStream berishimiz kerak, endi shu yerda unikal name qilganimizni ishlatamiz
            // Buning uchun bitta FileInputStream ochvolamiz va uni ichiga olmoqchi bo'lgan file limizni yo'lini va
            // name ni bervoramiz va pasdagi FileCopyUtils.copy(); ==> methodiga bervoramiz:

            FileInputStream inputStream = new FileInputStream(attachment.getFileLocation() + attachment.getGeneratedName());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            return new ApiResponse(true, "Success");
        }
        return new ApiResponse(false, "Icon Not Found!");
    }

    public ApiResponse deleteAttachment(UUID id) {
        Optional<Attachment> optionalAttachment = attachmentRepo.findById(id);
        if (optionalAttachment.isPresent()) {
            try {
                File file = new File(optionalAttachment.get().getFileLocation() + optionalAttachment.get().getGeneratedName());
                file.delete();
                attachmentRepo.deleteById(id);
                return new ApiResponse(true, "Fayl o`chirildi");
            } catch (Exception e) {
                return new ApiResponse(false, "Faylni o`chirishni imkoni mavjud emas");
            }
        }
        return new ApiResponse(false, "Bunday file mavjud emas");
    }

}
