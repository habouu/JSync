package FactoryMethod;

import Composite.DirectoryComposite;
import Composite.FileComponent;
import Composite.FileLeaf;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 * Implémentation de la méthode de fabrique pour retourner une instance
 * de {@link FileLeaf}
 */
public class FileCreator extends FileComponentCreator {
    // COMMANDES

    /**
     * Crée un composant représentant un fichier simple (feuille)
     * @param path chemin du fichier ou répertoire
     * @param lastModified date de dernière modification
     * @return un {@code FileLeaf}
     */
    @Override
    public FileComponent factoryMethod(Path path, Date lastModified) {
        try {
            if (Files.isDirectory(path)) {
                return new DirectoryComposite(path, lastModified);
            } else {
                return new FileLeaf(path, lastModified);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating file :" + path, e);
        }
    }
}
