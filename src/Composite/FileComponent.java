package Composite;

import Visitor.FileVisitor;

import java.nio.file.Path;
import java.util.Date;

/**
 * Interface de base pour tous les composants de fichiers
 */
public interface FileComponent {
    // REQUÊTES

    /**
     * Retourne le chemin absolu du répertoire ou du fichier sous la forme
     *      de {@link Path}
     * @return chemin absolu du répertoire
     */
    Path getPath();

    /**
     * Retourne la dernière modification du répertoire ou du fichier
     * @return date de dernière modification
     */
    Date getLastModified();

    // COMMANDES

    /**
     * Un visiteur applique une action spécifique:
     *      - {@code visitFile} si c'est un fichier
     *      - {@code visitDirectory} si un dossier
     * @param fileVisitor visiteur à appliquer
     */
    void accept(FileVisitor fileVisitor);

    /**
     * Si c'est un fichier:
     *      - affiche le chemin absolu du fichier
     * Si c'est un répertoire:
     *      - affiche récursivement la structure du répertoire
     */
    void print();
}
