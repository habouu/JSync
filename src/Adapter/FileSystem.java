package Adapter;

import java.nio.file.Path;
import java.util.Date;
import java.util.List;

/**
 * Interface représentant les opérations courantes sur un système de fichier.
 * Définit les méthodes nécessaires pour manipuler les fichiers et les
 * répertoires :
 * <pre>
 *     La copie <br>
 *     La suppression <br>
 *     L'accès aux informations du fichier/dossier
 * </pre>
 * Les implémentations concrètes de l'interface devront intéragir avec un
 * système de fichiers spécifique (local, distant ...).
 */
public interface FileSystem {
    // REQUÊTES

    /**
     * Liste les fichiers contenue dans un répertoire donné.
     * @param directory chemin du répertoire
     * @return liste de chemin des fichiers du répertoire, liste vide si le
     * répertoire est vide, une exception est levée si le répertoire
     * n'existe pas
     */
    List<Path> listFiles(Path directory);

    /**
     * Récupère la date de la dernière modification d'un fichier.
     * @param file chemin du fichier
     * @return date de la dernière modification, une exception est levée si le
     * fichier n'existe pas
     */
    Date getFileLastModified(Path file);

    // COMMANDES

    /**
     * Copie un fichier d'un dossier source vers une destination.
     * @param source chemin du fichier source à copier
     * @param destination chemin de destination où le fichier sera placé
     */
    void copyFile(Path source, Path destination);

    /**
     * Supprime un fichier donné.
     * @param path chemin du fichier à supprimer
     */
    void deleteFile(Path path);

    /**
     * Vérifie si un fichier ou un répertoire existe dans le système de fichier.
     * @param path chemin du fichier ou répertoire à vérifier
     * @return {@code true} si le fichier ou répertoire existe, {@code false}
     * sinon
     */
    boolean exists(Path path);

    /**
     *  Créer un nouveau répertoire à l'endroit spécifié par {@code path}.
     * @param path chemin du répertoire à créer
     */
    void createDirectory(Path path);

    /**
     * Vérifie si le chemin spécifié correspond à un répertoire.
     * @param path chemin à vérifier
     * @return {@code true} si c'est un répertoire, {@code false} sinon
     */
    boolean isDirectory(Path path);
}
