package Visitor;

import Composite.DirectoryComposite;
import Composite.FileLeaf;

/**
 * Interface représentant un visiteur pour les composants de fichiers
 * Permet d'appliquer des opérations spécifiques sur les fichiers et dossiers
 */
public interface FileVisitor {
    /**
     * Applique une aopération spécifique sur un fichier
     * @param fileLeaf
     */
    void visitFile(FileLeaf fileLeaf);

    /**
     * Applique une opération spécifique sur un dossier
     * @param directoryComposite
     */
    void visitDirectory(DirectoryComposite directoryComposite);
}
