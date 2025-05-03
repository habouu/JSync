package Facade;

import Adapter.FileSystem;
import Composite.DirectoryComposite;
import Composite.FileComponent;
import FactoryMethod.FileComponentCreator;
import FactoryMethod.FileCreator;
import ProfileBuilder.BuilderProfileInterface;
import ProfileBuilder.ConcreteProfileBuilder;
import ProfileBuilder.Director;
import ProfileBuilder.Profile;
import Singleton.Registry;
import Visitor.SyncVisitor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


/**
 * Simplification de l'utilisation du système de synchronisation.
 * @see Profile profil de synchronisation
 * @see Visitor.SyncVisitor visiteur qui parcourt les fichiers et dossiers pour
 *      effectuer les actions de synchronisation
 * @see Registry registre qui gère les profils
 */
public class SynchronizationFacade {
    // ATTRIBUTS

    private Registry registry;
    private FileSystem fileSystem;
    private FileComponentCreator fileComponentCreator;
    private FileComponentCreator directoryCreator;

    // CONSTRUCTEUR

    public SynchronizationFacade(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
        this.registry = Registry.getInstance();
        this.fileComponentCreator = new FileCreator();
        this.directoryCreator = new FileCreator();
    }

    // COMMANDES

    /**
     * Création d'un nouveau profil de synchronisation
     * @param name nom du profil à créer
     * @param source chemin du répertoire source
     * @param destination chemin du répertoire de destination
     * @return profil de synchronisation créé
     */
    public Profile createProfile(String name, Path source, Path destination) {
        BuilderProfileInterface builder = new ConcreteProfileBuilder();
        Director director = new Director();
        director.setBuilder(builder);
        director.construct(name, source, destination);
        return builder.getResult();
    }

    /**
     * Lance la synchronisation entre les répertoires source et destination en
     *      fonction du profil donné en parcourant et appliquant les
     *      modifications et les règles de synchronisation nécessaires dans le
     *      répertoire cible
     * @param profile profil à utiliser pour la synchronisation
     */
    public void synchronize(Profile profile) {
        Path source = profile.getSourceDirectory();
        Path destination = profile.getDestinationDirectory();
        FileComponent fileComponent = buildFileTree(source);
        SyncVisitor syncVisitor = new SyncVisitor(
                source, destination, registry, fileSystem);
        fileComponent.accept(syncVisitor);
        registry.saveToFile();
    }

    // OUTILS

    /**
     * Parcourt le répertoire donné et crée récursivement une structure
     *      composite représentant tous les fichiers et répertoires du
     *      dossier source.
     * @param source chemin du répertoire source
     * @return structure composite représentant l'arborescence des fichiers du
     *      répertoire source
     */
    private FileComponent buildFileTree(Path source) {
        DirectoryComposite directoryComposite =
                (DirectoryComposite) directoryCreator.factoryMethod(
                        source, fileSystem.getFileLastModified(source));
        buildFileTreeRec(directoryComposite, source);
        return directoryComposite;
    }

    /**
     * Construit récursivement l'arborescence des fichiers sous forme de
     *      structure composite à partir d'un répertoire donné. Parcourt les
     *      sous-répertoires et ajoute chaque fichier et répertoire à la
     *      structure composite.
     * @param directoryParentComposite composant parent auquel sera ajouté les
     *                                 fichiers et sous-répertoires
     * @param curPath chemin actuel du répertoire ou fichier à ajouter
     */
    private void buildFileTreeRec(
            DirectoryComposite directoryParentComposite, Path curPath) {
        List<Path> data = fileSystem.listFiles(curPath);
        for (Path path : data) {
            if (Files.isDirectory(path)) {
                DirectoryComposite dirComposite =
                        (DirectoryComposite) directoryCreator.factoryMethod(
                                path, fileSystem.getFileLastModified(path));
                buildFileTreeRec(dirComposite, path);
            } else {
                FileComponent fileComponent =
                        fileComponentCreator.factoryMethod(
                                path, fileSystem.getFileLastModified(path));
                directoryParentComposite.add(fileComponent);
            }
        }
    }
}
