package Visitor;

import Adapter.FileSystem;
import Composite.DirectoryComposite;
import Composite.FileComponent;
import Composite.FileLeaf;
import Singleton.Registry;

import java.nio.file.Path;
import java.util.Date;
import java.util.Scanner;

/**
 * Visiteur chargé d'appliquer la logique de synchronisation entre le répertoire
 *      source et le répertoire de destination
 * Implémente le patron Visitor pour appliquer cette logique lors du parcours
 *      de l'arborescence des fichiers. Elle vérifie si les fichiers ont été
 *      modifiés depuis la dernière synchronisation et les synchronise en
 *      conséquence
 *      
 * @see FileVisitor
 * @see FileComponent
 * @see Registry
 * @see FileSystem
 */
public class SyncVisitor implements FileVisitor {
    // ATTRIBUTS

    private Path source;
    private Path destination;
    private Registry registry;
    private FileSystem fileSystem;
    private Scanner scanner;

    // CONSTRUCTEUR

    public SyncVisitor(
            Path source,
            Path destination,
            Registry registry,
            FileSystem fileSystem) {
        this.source = source;
        this.destination = destination;
        this.registry = registry;
        this.fileSystem = fileSystem;
        scanner = new Scanner(System.in);
    }

    // COMMANDES

    @Override
    public void visitFile(FileLeaf fileLeaf) {
        Path pathRelatif = source.relativize(fileLeaf.getPath());
        Path pathDestination = destination.resolve(pathRelatif);
        Date lastDateSynchronization =
                registry.getLastSyncDate(pathRelatif.toString());

        // si le fichier existe
        if (fileSystem.exists(pathDestination)) {
            Date destinationLastModified =
                    fileSystem.getFileLastModified(pathDestination);

            // cas où les deux fichiers source et destination ont été modifié
            if (lastDateSynchronization != null
                    && fileLeaf.getLastModified().after(lastDateSynchronization)
                    && destinationLastModified.after(lastDateSynchronization)) {
                // ça veut dire conflict
                handleConflict(
                        fileLeaf.getPath(),
                        pathDestination,
                        pathRelatif);
            }
            // cas où c'est le fichier source qui a été modifié
            else if (lastDateSynchronization == null
                    || fileLeaf.getLastModified().after(lastDateSynchronization)) {
                // ça veut dire, on fait la copie
                System.out.println(
                        "copy of: " + fileLeaf.getPath()
                                + " to " + pathDestination);
                registry.updateDate(
                        pathRelatif.toString(), fileLeaf.getLastModified());
            }
            // cas où c'est le fichier de destination qui a été modifié
            else if (destinationLastModified.after(lastDateSynchronization)) {
                System.out.println(
                        "copy of: " + pathDestination
                                + " to " + fileLeaf.getPath());
                fileSystem.copyFile(pathDestination, fileLeaf.getPath());
                registry.updateDate(
                        pathRelatif.toString(), destinationLastModified);
            }
        }
        // cas où le fichier de destination n'existe pas
        else {
            System.out.println(
                    "copy of: " + fileLeaf.getPath()
                            + " to " + pathDestination);
            fileSystem.copyFile(fileLeaf.getPath(), pathDestination);
            registry.updateDate(
                    pathRelatif.toString(), fileLeaf.getLastModified());
        }
    }

    @Override
    public void visitDirectory(DirectoryComposite directoryComposite) {
        Path pathRelatif = source.relativize(directoryComposite.getPath());
        Path destionation = destination.resolve(pathRelatif);

        // le répertoire de destination doit exister avant toute chose
        if (!fileSystem.exists(destionation)) {
            fileSystem.createDirectory(destionation);
            System.out.println("created directory" + destionation);
        }

        for (FileComponent childDirectory : directoryComposite.getChildren()) {
            childDirectory.accept(this);
        }
    }

    // OUTILS

    /**
     * Gére les conflits de modification entre deux fichiers, demande à
     *      l'utilisateur de choisir entre quels fichiers il faut garder
     *      - source
     *      - destination
     *      - ignoré le fichier
     * @param source chemin du fichier source
     * @param destination chemin du fichier cible
     * @param relatif chemin relatif utilisé dans le registre
     */
    private void handleConflict(Path source, Path destination, Path relatif) {
        System.out.println("conflict: " + relatif);
        System.out.println(
                "source: " + source
                        + " last modified:"
                        + fileSystem.getFileLastModified(source));
        System.out.println(
                "destination: " + destination
                        + " last modified:"
                        + fileSystem.getFileLastModified(destination));
        System.out.println("actions ?");
        System.out.println("1 - use the source file");
        System.out.println("2 - use the destination file");
        System.out.println("3 - ignore");
        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.println("your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("the choice must be between 1 and 3");
            }
        }
        Date now = new Date();
        switch (choice) {
            case 1:
                System.out.println("copy of: " + source + " to " + destination);
                fileSystem.copyFile(source, destination);
                registry.updateDate(relatif.toString(), now);
                break;
                case 2:
                    System.out.println("copy of: " + destination + " to " + source);
                    fileSystem.copyFile(destination, source);
                    registry.updateDate(relatif.toString(), now);
                    break;
                    case 3:
                        System.out.println("ignore");
                        break;
        }
    }

    /**
     * Méthode récursive pour synchroniser les fichiers présents uniquement
     *      dans le répertoire de destination vers celui source
     * @param destinationPath répertoire cible
     * @param sourcePath répertoire source
     */
    private void mustSynchronizeDestinationToSource(Path destinationPath, Path sourcePath) {

    }
}
