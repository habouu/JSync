package Main;

import Adapter.FileSystem;
import Adapter.LocalFileSystemAdapter;
import Facade.DisplayStatusFacade;
import Builder.Profile;

import java.io.IOException;


/**
 * Programme permettant d'afficher l'état de synchronisation des fichiers
 * d'un profil spécifique. Ce programme charge un profil à partir d'un fichier
 * de sauvegarde et utilise un système de fichiers local pour afficher le statut
 * de synchronisation entre les répertoires source et destination.
 *
 * <p>Exécution :</p>
 * <pre>
 *     java Main.SyncStat <nom-du-profil>
 * </pre>
 *
 * @see FileSystem Interface pour les opérations sur le système de fichiers
 * @see LocalFileSystemAdapter Implémentation concrète de FileSystem
 * @see DisplayStatusFacade Simplification de l'affichage du statut
 * @see Profile Profil de synchronisation
 */
public class SyncStat {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 1) {
            System.out.println("Usage: java Main.SyncStat nom-du-profil");
            return;
        }

        String profileName = args[0];

        Profile profile = Profile.loadFromFile(profileName);

        FileSystem fileSystem = new LocalFileSystemAdapter();
        DisplayStatusFacade statusFacade = new DisplayStatusFacade(fileSystem);
        System.out.println("Synchronization status profile: " + profileName);
        System.out.println("Source: " + profile.getSourceDirectory());
        System.out.println("Destination: " + profile.getDestinationDirectory());
        System.out.println("----------------------------------------");
        statusFacade.displayStatus(profile);
    }
}
