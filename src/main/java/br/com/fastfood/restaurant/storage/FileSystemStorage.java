package br.com.fastfood.restaurant.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileSystemStorage implements Storage {
    @Override
    public String store(MultipartFile file, Path destDir) throws IOException {
        if (!Files.isDirectory(destDir)) {
            throw new IOException("destination is not a valid directory");
        }

        String filename = UUID.randomUUID().toString();
        Path uploadPath = destDir.resolve(filename);

        file.transferTo(uploadPath);
        return uploadPath.toString();
    }
}
