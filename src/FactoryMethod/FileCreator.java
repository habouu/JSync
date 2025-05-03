package FactoryMethod;

import Composite.FileComponent;
import Composite.FileLeaf;

import java.nio.file.Path;
import java.util.Date;

/**
 * Implémentation de la méthode de fabrique pour retourner une instance
 *      de {@link FileLeaf}
 */
public class FileCreator extends FileComponentCreator {
    /**
     * Crée un composant représentant un fichier simple (feuille)
     * @param path chemin du fichier ou répertoire
     * @param lastModified date de dernière modification
     * @return un {@code FileLeaf}
     */
    @Override
    public FileComponent factoryMethod(Path path, Date lastModified) {
        return new FileLeaf(path, lastModified);
    }
}
