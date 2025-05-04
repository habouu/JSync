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
     * de {@link Path}
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
     * Un visiteur applique une action spécifique : <br>
     * - {@code visitFile} si c'est un fichier <br>
     * - {@code visitDirectory} si un dossier <br>
     * @param fileVisitor visiteur à appliquer
     */
    void accept(FileVisitor fileVisitor);

    /**
     * Affiche le chemin absolu si l'objet manipulé est un fichier, affiche
     * récursivement la structure du répertoire sinon
     */
    void print();
}
