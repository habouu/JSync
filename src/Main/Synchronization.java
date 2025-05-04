package Main;

import Adapter.FileSystem;
import Adapter.LocalFileSystemAdapter;
import Facade.SynchronizationFacade;
import Builder.Profile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Programme pour démarrer la synchronisation des fichiers en utilisant un
 * profil spécifique. Ce programme charge un profil à partir d'un fichier de
 * sauvegarde et utilise un système de fichiers local pour effectuer la
 * synchronisation entre les répertoires source et destination.
 *
 * <p>Exécution :</p>
 * <pre>
 *     java Main.SyncStat nom-du-profil
 * </pre>
 *
 * @see FileSystem interface pour les opérations sur le système de fichiers
 * @see LocalFileSystemAdapter implémentation concrète pour le système de
 * fichiers local
 * @see SynchronizationFacade simplification de la synchronisation des fichiers
 * @see Profile profil de synchronisation
 */
public class Synchronization {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Min.Synchronization nom-du-profil");
        }

        String profileName = args[0];

        Profile profile = Profile.loadFromFile(profileName);
        Path sourceDirectory = profile.getSourceDirectory();
        Path destinationDirectory = profile.getDestinationDirectory();

        if (!Files.exists(sourceDirectory)) {
            System.err.println("Source directory does not exist: " + sourceDirectory);
            return;
        }
        if (!Files.exists(destinationDirectory)) {
            System.err.println("Target directory does not exist: " + destinationDirectory);
            return;
        }

        FileSystem fileSystem = new LocalFileSystemAdapter();
        SynchronizationFacade synchronizationFacade = new SynchronizationFacade(fileSystem);

        System.out.println("Synchronization started for " + profileName);
        System.out.println("Source: " + profile.getSourceDirectory());
        System.out.println("Destination: " + profile.getDestinationDirectory());
        System.out.println("========================================");

        synchronizationFacade.synchronize(profile);

        System.out.println("========================================");
        System.out.println("Synchronization finished");
    }
}
