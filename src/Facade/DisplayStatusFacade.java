package Facade;

import Adapter.FileSystem;
import Composite.DirectoryComposite;
import Composite.FileComponent;
import FactoryMethod.FileComponentCreator;
import FactoryMethod.FileCreator;
import ProfileBuilder.Profile;
import Singleton.Registry;
import Visitor.DisplayStatusVisitor;

import java.nio.file.Path;

/**
 * Affichage du status de la synchronisation d'un profil donné.
 *
 * Parcourt l'arborescence des fichiers d'un profil et compare leurs états
 *      avec les données du registre de synchronisation et affiche le statut
 *      complet du profil, en incluant les données et les fichiers modifiés
 */
public class DisplayStatusFacade {
    // ATTRIBUTS

    private Registry registry;
    private FileSystem fileSystem;
    private FileComponentCreator fileComponentCreator;
    private FileComponentCreator directoryComponentCreator;

    // CONSTRUCTEUR

    public DisplayStatusFacade(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.registry = Registry.getInstance();
        this.fileComponentCreator = new FileCreator();
        this.directoryComponentCreator = new FileCreator();
    }

    // COMMANDES

    /**
     * Affiche le statut complet d'un profil
     * @param profile profil à examiner
     */
    public void displayStatus(Profile profile) {
        System.out.println("Infos:");
        System.out.println("Name: " + profile.getProfileName());
        System.out.println("Source directory: "
                + profile.getSourceDirectory());
        System.out.println("Destination directory: "
                + profile.getDestinationDirectory());
        System.out.println();
        System.out.println("Content:");
        //registry.printRegitryData();
        System.out.println();
        Path sourceDirectory = profile.getSourceDirectory();
        FileComponent root = buildFileTree(sourceDirectory);
        System.out.println("File status:");
        DisplayStatusVisitor statusVisitor =
                new DisplayStatusVisitor(registry, sourceDirectory);
        root.accept(statusVisitor);
    }

    // OUTILS

    /**
     * Parcourt les fichiers à partir du chemin du dossier source fournit et
     *      construit une structure composite représentant l'arborescence
     *      complère des fichiers
     * @param sourceDirectory chemin du dossier source pour construire l'arbre
     *                        des fichiers
     * @return structure composite
     */
    private FileComponent buildFileTree(Path sourceDirectory) {
        DirectoryComposite root =
                (DirectoryComposite) directoryComponentCreator.factoryMethod(
                        sourceDirectory,
                        fileSystem.getFileLastModified(sourceDirectory));
        return root;
    }
}
