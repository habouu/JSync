package Visitor;

import Composite.DirectoryComposite;
import Composite.FileComponent;
import Composite.FileLeaf;
import Singleton.Registry;

import java.nio.file.Path;
import java.util.Date;

public class DisplayStatusVisitor implements FileVisitor {
    // ATTRIBUTS

    private Registry registry;
    private Path source;

    // CONSTRUCTEUR

    public DisplayStatusVisitor(Registry registry, Path source) {
        this.registry = registry;
        this.source = source;
    }

    // COMMANDES

    @Override
    public void visitFile(FileLeaf fileLeaf) {
        Path pathRelatif = source.relativize(fileLeaf.getPath());
        Date lastSynchronization = registry.getLastSyncDate(pathRelatif.toString());

        System.out.println("File: " + fileLeaf.getPath());
        System.out.println("Last synchronization: " + fileLeaf.getLastModified());

        if (lastSynchronization != null) {
            System.out.println("Last synchronization: " + lastSynchronization);
            if (fileLeaf.getLastModified().after(lastSynchronization)) {
                System.out.println("status: MODIFIED");
            } else {
                System.out.println("status: up-to-date");
            }
        } else {
            System.out.println("status: never synchronized");
        }
    }

    @Override
    public void visitDirectory(DirectoryComposite directoryComposite) {
        System.out.println("Directory: " + directoryComposite.getPath());
        for (FileComponent fileComponent : directoryComposite.getChildren()) {
            fileComponent.accept(this);
        }
    }
}
