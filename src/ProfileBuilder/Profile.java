package ProfileBuilder;

import java.io.*;
import java.nio.file.Path;

/**
 * Représente un profil de synchronisation entre un répertoire source et un
 *      répertoire de destination
 *
 * Le profile contient les informations sur la source, la destination des
 *      répertoires à synchroniser. IL permet de sauvegarder et charger ces
 *      informations depuis un fichier '.sync' à l'aide de la sérialisation.
 */
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;

    // ATTRIBUTS

    private String name;
    private Path sourcePath;
    private Path destinationPath;

    // REQUÊTES

    /**
     * Récupére le nom du profil de synchronisation
     * @return le nom du profil
     */
    public String getProfileName() {
        return name;
    }

    /**
     * Récupère le chemin du répertoire source associé au profil
     * @return le chemin du répertoire source
     */
    public Path getSourceDirectory() {
        return sourcePath;
    }

    /**
     * Récupère le chemin du répertoire de destination associé au profil
     * @return le chemin du répertoire destination
     */
    public Path getDestinationDirectory() {
        return destinationPath;
    }

    // MÉTHODES

    /**
     * Défini le nom du profile de synchronisation
     * @param name nom du profil
     */
    public void setProfileName(String name) {
        this.name = name;
    }

    /**
     * Défini le chemin du répertoire source à utiliser pour la synchronisation
     * @param source chemin du répertoire source
     */
    public void setSourceDirectory(Path source) {
        this.sourcePath = source;
    }

    /**
     * Défini le chemin du répertoire de destination dans lequel les fichiers
     * synchronisés sont stockés
     * @param destination chemin du répertoire de destination
     */
    public void setDestinationDirectory(Path destination) {
        this.destinationPath = destination;
    }

    // COMMANDES

    /**
     * Sauvegarde l'état actuel du profil dans un fichier '.sync'
     * @throws IOException exception levée en cas d'erreur d'écriture
     */
    public void saveToFile() {
        String filename = name + ".sync";
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(filename));
            outputStream.writeObject(this);
            System.out.println("Profile saved to " + filename);
        } catch (IOException exception) {
            System.err.println("Error saving profile to " + filename + ": "
                    + exception.getMessage());
        }
    }

    /**
     * Charge le profil sauvegardé dans le fichier '.sync'
     * @param profileName nom du profil à charger
     * @return l'objet {@link Profile} ou {@code null} en cas d'erreur
     * @throws IOException exception levée en cas d'érreur de lecture
     * @throws ClassNotFoundException exception levée si la classe Profile
     *      non trouvée
     */
    public static Profile loadFromFile(String profileName) {
        String filename = profileName + ".sync";
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename));
            return (Profile) inputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            System.err.println("Error loading profile from " + filename + ": " + exception.getMessage());
            return null;
        }
    }
}
