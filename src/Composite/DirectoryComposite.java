package Composite;

import Visitor.FileVisitor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Représente un répertoire dans la structure composite. Le répertoire peut
 *      contenir d'autres fichiers ou des sous-répertoires, ce qui permet
 *      d'avoir une structure hiérarchique du système du fichier et une gestion
 *      récursive de la structure
 * Implémente l'interface {@link FileComponent} qui permet d'appliquer le
 *      patron Composite, pour avoir un traitement des répertoires et des
 *      fichiers uniforme
 *
 * @see FileComponent
 */
public class DirectoryComposite implements FileComponent {
    // ATTRIBUTS

    private Path path;
    private Date lastModified;
    private List<FileComponent> children;

    // CONSTRUCTEUR

    public DirectoryComposite(Path path, Date lastModified) {
        this.path = path;
        this.children = new ArrayList<>();
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

    /**
     * Retourne la liste des enfants (fichier, répertoires) contenu dans le
     *      répertoire
     * @return liste des composants enfants du répertoire
     */
    public List<FileComponent> getChildren() {
        return children;
    }

    // COMMANDES

    /**
     * Ajoute un enfant (fichier ou sous-répertoire) à un répertoire
     * @param child fichier ou sous-répertoire enfant
     */
    public void add(FileComponent child) {
        children.add(child);
    }

    @Override
    public void accept(FileVisitor fileVisitor) {
        fileVisitor.visitDirectory(this);
    }

    @Override
    public void print() {
        System.out.println("DirectoryComposite: " + path.toString());
        for (FileComponent child : children) {
            child.print();
        }
    }
}
