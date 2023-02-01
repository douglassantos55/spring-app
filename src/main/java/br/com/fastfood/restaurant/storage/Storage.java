package br.com.fastfood.restaurant.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface Storage {
    public String store(MultipartFile file, Path destination) throws IOException;

    public boolean delete(Path file) throws IOException;
}
