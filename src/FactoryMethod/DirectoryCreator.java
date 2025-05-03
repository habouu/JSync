package FactoryMethod;

import Composite.DirectoryComposite;
import Composite.FileComponent;

import java.nio.file.Path;
import java.util.Date;

/**
 * Fabrique concrète de répertoires (composites)
 */
public class DirectoryCreator extends FileComponentCreator {
    // COMMANDE

    /**
     * Crée un composant représentant un répertoire (composite)
     * @param path chemin du fichier ou répertoire
     * @param lastModified date de dernière modification
     * @return un {@code DirectoryComposite}
     */
    @Override
    public FileComponent factoryMethod(Path path, Date lastModified) {
        return new DirectoryComposite(path, lastModified);
    }
}
