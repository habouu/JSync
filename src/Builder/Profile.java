package Builder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Représente un profil de synchronisation entre un répertoire source et un
 * répertoire de destination.
 * Le profile contient les informations sur la source, la destination des
 * répertoires à synchroniser. Il permet de sauvegarder et charger ces
 * informations depuis un fichier d'extension '.sync' à l'aide de la sérialisation.
 */
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;

    // ATTRIBUTS

    /**
     * Nom du profil
     */
    private String name;

    /**
     * Chemin du répertoire source
     */
    private String sourcePath;

    /**
     * Chemin du répertoire cible
     */
    private String ciblePath;

    // REQUÊTES

    /**
     * Récupère le nom du profil de synchronisation.
     * @return le nom du profil
     */
    public String getProfileName() {
        return name;
    }

    /**
     * Récupère le chemin du répertoire source associé au profil.
     * @return le chemin du répertoire source
     */
    public Path getSourceDirectory() {
        return Paths.get(sourcePath);
    }

    /**
     * Récupère le chemin du répertoire cible associé au profil.
     * @return le chemin du répertoire cible
     */
    public Path getDestinationDirectory() {
        return Paths.get(ciblePath);
    }

    // MÉTHODES

    /**
     * Défini le nom du profil de synchronisation
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
        this.sourcePath = source.toString();
    }

    /**
     * Défini le chemin du répertoire cible dans lequel les fichiers
     * synchronisés sont stockés
     * @param destination chemin du répertoire cible
     */
    public void setDestinationDirectory(Path destination) {
        this.ciblePath = destination.toString();
    }

    // COMMANDES

    /**
     * Sauvegarde l'état actuel du profil dans un fichier d'extension '.sync'
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
     * Charge le profil sauvegardé dans le fichier d'extension '.sync'
     * @param profileName nom du profil à charger
     * @return l'objet {@link Profile} ou {@code null} en cas d'erreur
     * @throws IOException exception levée en cas d'érreur de lecture
     * @throws ClassNotFoundException exception levée si la classe Profile n'est pas trouvée
     */
    public static Profile loadFromFile(String profileName) throws IOException, ClassNotFoundException {
        String filename = profileName + ".sync";

            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(filename));
            Profile profile = (Profile) inputStream.readObject();
            profile.setSourceDirectory(Paths.get(profile.sourcePath));
            profile.setDestinationDirectory(Paths.get(profile.ciblePath));
            return profile;

    }
}
