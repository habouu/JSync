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
 * source et le répertoire de destination
 * Implémente le patron Visitor pour appliquer cette logique lors du parcours
 * de l'arborescence des fichiers. Elle vérifie si les fichiers ont été
 * modifiés depuis la dernière synchronisation et les synchronise en conséquence
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
        Date lastSync = registry.getLastSyncDate(pathRelatif.toString());

        Date sourceLastModified = fileLeaf.getLastModified();
        Date destLastModified = fileSystem.exists(pathDestination)
                ? fileSystem.getFileLastModified(pathDestination)
                : null;

        if (sourceLastModified == null) {
            System.err.println("last modified not found:" + fileLeaf.getPath());
            return;
        }

        boolean sourceModified = (lastSync == null) || sourceLastModified.after(lastSync);
        boolean destModified = (destLastModified != null) && (lastSync != null) && destLastModified.after(lastSync);

        if (destLastModified != null) {
            if (sourceModified && destModified) {
                // Conflit : les deux fichiers ont été modifiés
                handleConflict(fileLeaf.getPath(), pathDestination, pathRelatif);
            } else if (sourceModified) {
                // Source modifiée : copie vers destination
                System.out.println("copy of: " + fileLeaf.getPath() + " to " + pathDestination);
                fileSystem.copyFile(fileLeaf.getPath(), pathDestination);
                registry.updateDate(pathRelatif.toString(), sourceLastModified);
            } else if (destModified) {
                // Destination modifiée : copie vers source
                System.out.println("copy of: " + pathDestination + " to " + fileLeaf.getPath());
                fileSystem.copyFile(pathDestination, fileLeaf.getPath());
                registry.updateDate(pathRelatif.toString(), destLastModified);
            }
        } else {
            // Le fichier n'existe pas en destination : copie depuis la source
            System.out.println("copy of: " + fileLeaf.getPath() + " to " + pathDestination);
            fileSystem.copyFile(fileLeaf.getPath(), pathDestination);
            registry.updateDate(pathRelatif.toString(), sourceLastModified);
        }
    }


    @Override
    public void visitDirectory(DirectoryComposite directoryComposite) {
        Path pathRelatif = source.relativize(directoryComposite.getPath());
        Path destination = this.destination.resolve(pathRelatif);

        // le répertoire de destination doit exister avant toute chose
        if (!fileSystem.exists(destination)) {
            fileSystem.createDirectory(destination);
            System.out.println("created directory" + destination);
        }

        for (FileComponent childDirectory : directoryComposite.getChildren()) {
            childDirectory.accept(this);
        }

        if (fileSystem.exists(destination)) {
            for (Path destinationChild : fileSystem.listFiles(destination)) {
                Path destinationRelative = destination.relativize(destinationChild);
                Path src = directoryComposite.getPath().resolve(destinationRelative.toString());
                if (!fileSystem.exists(src)) {
                    if (fileSystem.isDirectory(destinationChild)) {
                        System.out.println("directory created: " + src);
                        mustSynchronizeDestinationToSource(destinationChild, src);
                    } else {
                        System.out.println("copy from:" + destinationChild + " to " + src);
                        fileSystem.copyFile(destinationChild, src);
                        Path p = source.relativize(destinationChild);
                        registry.updateDate(p.toString(), fileSystem.getFileLastModified(destinationChild));
                    }
                }
            }
        }
    }

    // OUTILS

    /**
     * Gére les conflits de modification entre deux fichiers, demande à
     * l'utilisateur de choisir entre quels fichiers il faut garder : la source
     * celui du dossier cible ou de l'ignorer
     * @param source chemin du fichier source
     * @param destination chemin du fichier cible
     * @param relatif chemin relatif utilisé dans le registre
     */
    private void handleConflict(Path source, Path destination, Path relatif) {
        System.out.println("detected conflict: " + relatif);

        Date sourceLastModified = fileSystem.getFileLastModified(source);
        Date destinationLastModified = fileSystem.getFileLastModified(destination);

        if (sourceLastModified == null || destinationLastModified == null) {
            System.out.println("Unable to resolve conflict due to missing file dates.");
            return;
        }

        System.out.println(
                "source: " + source
                        + " last modified:"
                        + fileSystem.getFileLastModified(source));
        System.out.println(
                "destination: " + destination
                        + " last modified:"
                        + fileSystem.getFileLastModified(destination));

        int choice = -1;
        while (choice < 1 || choice > 3) {
            System.out.println("possible actions: ");
            System.out.println("1 - use the source file");
            System.out.println("2 - use the destination file");
            System.out.println("3 - ignore");
            System.out.print("choice (1-3): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("the choice must be between 1 and 3");
            }
        }
        Date now = new Date();
        switch (choice) {
            case 1:
                System.out.println("copy from: " + source + " to " + destination);
                fileSystem.copyFile(source, destination);
                registry.updateDate(relatif.toString(), now);
                break;
                case 2:
                    System.out.println("copy from: " + destination + " to " + source);
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
     * dans le répertoire de destination vers celui source
     * @param destinationPath répertoire cible
     * @param sourcePath répertoire source
     */
    private void mustSynchronizeDestinationToSource(Path destinationPath, Path sourcePath) {
        for (Path targetChild : fileSystem.listFiles(destinationPath)) {
            Path targetRelative = destinationPath.relativize(targetChild);
            Path sourceEquivalent = sourcePath.resolve(targetRelative);

            if (fileSystem.isDirectory(targetChild)) {
                if (!fileSystem.exists(sourceEquivalent)) {
                    System.out.println("directory created: " + sourceEquivalent);
                    fileSystem.createDirectory(sourceEquivalent);
                }
                mustSynchronizeDestinationToSource(targetChild, sourceEquivalent);
            } else {
                if (!fileSystem.exists(sourceEquivalent)) {
                    System.out.println("copy from: " + targetChild + " to " + sourceEquivalent);
                    fileSystem.copyFile(targetChild, sourceEquivalent);
                    Path relPath = sourcePath.relativize(sourceEquivalent);
                    registry.updateDate(relPath.toString(), fileSystem.getFileLastModified(targetChild));
                }
            }
        }
    }
}
