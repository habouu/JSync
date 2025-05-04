package FactoryMethod;

import Composite.FileComponent;

import java.nio.file.Path;
import java.util.Date;

/**
 * Classe abstraite définissant une fabrique de composants de fichiers.
 * Permet de créer dynamiquement des composants de type {@code FileComponent}:
 * fichiers, répertoires
 * @see FileComponent
 */
public abstract class FileComponentCreator {
    // COMMANDES

    /**
     * Méthode de fabrique de composant de fichier (feuille ou composite)
     * @param path chemin du fichier ou répertoire
     * @param lastModified date de dernière modification
     * @return composant de fichier
     */
    public abstract FileComponent factoryMethod(Path path, Date lastModified);
}
