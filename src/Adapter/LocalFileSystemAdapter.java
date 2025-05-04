package Adapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * Implémentation concrète de l'interface {@link FileSystem} en utilisant l'API
 * Java {@code java.nio.file} pour un système de fichiers local
 */
public class LocalFileSystemAdapter implements FileSystem {
    // REQUÊTES

    @Override
    public List<Path> listFiles(Path directory) {
        List<Path> files = new ArrayList<>();
        try {
            Stream<Path> stream = Files.list(directory);
            for (Path path : stream.toList()) {
                files.add(path);
            }
        } catch (IOException exception) {
            System.out.println("error with the lists of files "
                    + exception.getMessage());
        }
        return files;
    }

    @Override
    public Date getFileLastModified(Path file) {
        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(file,
                    BasicFileAttributes.class);
            return new Date(fileAttributes.lastModifiedTime().toMillis());
        } catch (IOException exception) {
            System.out.println("error with the getFileLastModified of file "
                    + exception.getMessage());
            return new Date();
        }
    }

    // COMMANDES

    @Override
    public void copyFile(Path source, Path destination) {
        try {
            Files.createDirectories(destination.getParent());
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            FileTime fileTime = Files.getLastModifiedTime(source);
            Files.setLastModifiedTime(destination, fileTime);
        } catch (IOException exception) {
            System.out.println("error with the copy of file "
                    + exception.getMessage());
        }
    }

    @Override
    public void deleteFile(Path path) {
        try {
            Files.delete(path);
            System.out.println("successfully deleted file " + path);
        } catch (IOException exception) {
            System.out.println("error with the delete of file" + exception.getMessage());
        }
    }

    @Override
    public boolean exists(Path path) {
        return Files.exists(path);
    }

    @Override
    public void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException exception) {
            System.out.println("error with the createDirectory of file" + exception.getMessage());
        }
    }

    @Override
    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }
}
