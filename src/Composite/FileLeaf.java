package Composite;

import Visitor.FileVisitor;

import java.nio.file.Path;
import java.util.Date;

/**
 * Représente une feuille de l'arbre des fichiers.
 * Implémente l'interface {@link FileComponent}, pour s'intégrer dans la
 *      structure composite des fichiers et répertoire
 *
 * @see FileComponent
 */
public class FileLeaf implements FileComponent {
    // ATTRIBUTS

    private Path path;
    private Date lastModified;

    // CONSTRUCTEUR

    public FileLeaf(Path path, Date lastModified) {
        this.path = path;
        this.lastModified = lastModified;
    }

    // REQUÊTES

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    // COMMANDES

    @Override
    public void accept(FileVisitor fileVisitor) {
        fileVisitor.visitFile(this);
    }

    @Override
    public void print() {
        System.out.println("file: " + path.toString());
    }
}
