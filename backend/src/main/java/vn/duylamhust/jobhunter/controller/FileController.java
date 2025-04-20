package vn.duylamhust.jobhunter.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.duylamhust.jobhunter.domain.response.file.ResUploadFileDTO;
import vn.duylamhust.jobhunter.service.FileService;
import vn.duylamhust.jobhunter.util.anotation.ApiMessage;
import vn.duylamhust.jobhunter.util.error.StorageException;

@RestController
@RequiredArgsConstructor
public class FileController extends BaseController {

    private final FileService fileService;

    @PostMapping("/files")
    @ApiMessage("upload single file")
    public ResponseEntity<ResUploadFileDTO> upload(@RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        // create directory
        fileService.createdDirectory(folder);
        // store file to directory
        String uploadFile = fileService.store(file, folder);

        var res = new ResUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.ok(res);

    }

    @GetMapping("/files")
    public ResponseEntity<Resource> download(@RequestParam(name = "fileName") String fileName,
            @RequestParam("folder") String folder) throws FileNotFoundException, URISyntaxException, StorageException {
        var res = fileService.download(fileName, folder);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(res.getSecond())
                .body(res.getFirst());
    }
}
