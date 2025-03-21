package vn.hoidanit.jobhunter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.hoidanit.jobhunter.util.error.StorageException;

@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${hoidanit.upload-file.base-uri}")
    private String baseUri;

    public void createdDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(baseUri + folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + tmpDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException, StorageException {
        // validate before store
        if (file == null || file.isEmpty())
            throw new StorageException("file is empty, please upload again!");

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid)
            throw new StorageException("Invalid file extension, must be allow " + allowedExtensions);

        // create unique filename
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(baseUri + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }

    public Pair<InputStreamResource, Long> download(String fileName, String folder)
            throws URISyntaxException, StorageException, FileNotFoundException {
        URI uri = new URI(baseUri + folder + "/" + fileName);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        return Pair.of(new InputStreamResource(new FileInputStream(tmpDir)), getLengthFile(fileName, folder));
    }

    private long getLengthFile(String fileName, String folder) throws URISyntaxException, StorageException {
        URI uri = new URI(baseUri + folder + "/" + fileName);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());

        if (!tmpDir.exists() || tmpDir.isDirectory())
            throw new StorageException("File with name " + fileName + "not found!");
        return tmpDir.length();

    }
}
