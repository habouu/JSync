package Facade;

import Adapter.FileSystem;
import Composite.DirectoryComposite;
import Composite.FileComponent;
import FactoryMethod.FileComponentCreator;
import FactoryMethod.FileCreator;
import Builder.Profile;
import Singleton.Registry;
import Visitor.DisplayStatusVisitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Affichage du status de la synchronisation d'un profil donné.
 * Parcourt l'arborescence des fichiers d'un profil et compare leurs états
 * avec les données du registre de synchronisation et affiche le statut
 * complet du profil, en incluant les données et les fichiers modifiés
 */
public class DisplayStatusFacade {
    // ATTRIBUTS

    private Registry registry;
    private FileSystem fileSystem;
    private FileComponentCreator componentCreator;

    // CONSTRUCTEUR

    public DisplayStatusFacade(FileSystem fileSystem) throws IOException, ClassNotFoundException {
        this.fileSystem = fileSystem;
        this.registry = Registry.getInstance();
        this.componentCreator = new FileCreator();
    }

    // COMMANDES

    /**
     * Affiche le statut complet d'un profil
     * @param profile profil à examiner
     */
    public void displayStatus(Profile profile) {
        System.out.println("Infos:");
        System.out.println("Name: " + profile.getProfileName());
        System.out.println("Source directory: " + profile.getSourceDirectory());
        System.out.println("Destination directory: " + profile.getDestinationDirectory());
        System.out.println();
        System.out.println("Content:");
        registry.printAllData();
        System.out.println();
        Path sourceDirectory = profile.getSourceDirectory();
        FileComponent root = buildFileTree(sourceDirectory);
        System.out.println("File status:");
        DisplayStatusVisitor statusVisitor = new DisplayStatusVisitor(registry, sourceDirectory);
        root.accept(statusVisitor);
    }

    // OUTILS

    /**
     * Parcourt les fichiers à partir du chemin du dossier source fournit et
     * construit une structure composite représentant l'arborescence
     * complète des fichiers
     * @param sourceDirectory chemin du dossier source pour construire l'arbre
     * des fichiers
     * @return structure composite
     */
    private FileComponent buildFileTree(Path sourceDirectory) {
        FileComponent rootComponent = componentCreator.factoryMethod(
                sourceDirectory,
                fileSystem.getFileLastModified(sourceDirectory)
        );
        if (!(rootComponent instanceof DirectoryComposite root)) {
            throw new IllegalArgumentException("Root is not a DirectoryComposite");
        }
        buildFileTreerec(root, sourceDirectory);
        return root;
    }

    /**
     * Construction récursive de l'arborescence des fichiers et répertoires à
     * partie du répertoire source
     * @param root composant parent auquel on ajoute fichiers et sous-répertoires
     * @param curPath chemin actuel du répertoire ou fichier
     */
    private void buildFileTreerec(DirectoryComposite root, Path curPath) {
        List<Path> data = fileSystem.listFiles(curPath);
        for (Path path : data) {
            FileComponent component = componentCreator.factoryMethod(
                    path, fileSystem.getFileLastModified(path)
            );

            root.add(component);

            if (component instanceof DirectoryComposite directoryComposite) {
                buildFileTreerec(directoryComposite, path);
            }
        }
    }

}
