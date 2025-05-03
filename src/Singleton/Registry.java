package Singleton;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton chargé de la gestion des données de synchronisation des fichiers
 * Le registre a la responsabilité de maintenir un suivi des dates de
 *      synchronisation pour chaque fichier dans le système. Essentiel pour la
 *      détection de modifications de fichier, la gestion de conflits éventuels
 *      Il garantit que chaque fichier est synchronisé au bon moment en effectuant
 *      une comparaison de sa date de synchronisation avec celle du répertoire
 *      cible, et, qu'il n'y ait qu'une seule instance
 */
public class Registry {
    // ATTRIBUTS

    private static Registry instance;
    private Map<String, Date> data;
    private final String registryFile = "registry.txt";

    // CONSTRUCTEUR

    private Registry() {
        data = new HashMap<>();
        loadFromFile();
    }

    // REQUÊTES

    /**
     * Récupère l'instance unique du registre
     * @return instance du registre {@code Registry}
     */
    public static Registry getInstance() {
        if (instance == null) {
            instance = new Registry();
        }
        return instance;
    }

    /**
     * Récupère la date à laquelle le fichier donné a été synchronisé pour la
     *      dernière fois. Si la date n'est pas connue, parce qu'il n'a pas été
     *      encore synchronisé, {@code null} est retourné
     * @param path chemin relatif du fichier donné
     * @return date de la dernière synchronisation, {@code null} sinon
     */
    public Date getLastSyncDate(String path) {
        return data.get(path);
    }

    // COMMANDES

    /**
     * Met à jour la date de synchronisation d'un fichier donné
     * @param path chemin relatif du fichier donné
     * @param date nouvelle date de synchronisation
     */
    public void updateDate(String path, Date date) {
        data.put(path, date);
    }

    /**
     * Supprime un fichier du registre
     * @param path chemin du fichier à supprimer
     */
    public void removePath(String path) {
        data.remove(path);
    }

    /**
     * Sérialise toutes les données du registre dans un fichier local
     * @throws IOException exception levée en cas d'erreur d'écriture
     */
    public void saveToFile() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(registryFile));
            outputStream.writeObject(data);
        } catch (IOException exception) {
            System.err.println("Error saved registry: "
                    + exception.getMessage());
        }
    }

    /**
     *
     */
    public void printAllData() {

    }

    // OUTILS

    /**
     * Charge les données de synchronisation depuis un fichier local.
     * Si le fichier n'existe pas, aucune donnée ne sera chargée
     * @throws IOException exception levée en cas d'erreur d'écriture
     * @throws ClassNotFoundException exception levée en cas de fichier
     *      non trouvé
     */
    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(registryFile);
        if (file.exists()) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(
                        new FileInputStream(file));
                data = (Map<String, Date>) inputStream.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                System.err.println("Error loading registry: "
                        + exception.getMessage());
                data = new HashMap<>();
            }
        }
    }
}
