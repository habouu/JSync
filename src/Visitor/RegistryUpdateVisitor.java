package Visitor;

import Composite.DirectoryComposite;
import Composite.FileComponent;
import Composite.FileLeaf;
import Singleton.Registry;

/**
 *
 */
public class RegistryUpdateVisitor implements FileVisitor {
    // ATTRIBUTS

    private Registry registry;

    // CONSTRUCTEUR

    public RegistryUpdateVisitor(Registry registry) {
        this.registry = registry;
    }

    // COMMANDES

    @Override
    public void visitFile(FileLeaf fileLeaf) {
        registry.updateDate(fileLeaf.getPath().toString(), fileLeaf.getLastModified());
    }

    @Override
    public void visitDirectory(DirectoryComposite directoryComposite) {
        for (FileComponent fileComponent : directoryComposite.getChildren()) {
            fileComponent.accept(this);
        }
    }
}
