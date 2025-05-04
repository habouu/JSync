package Builder;

import java.nio.file.Path;

/**
 * Représente le Directeur qui orchestre la construction du profil par
 * l'intermédiaire de l'interface {@link BuilderProfileInterface}.
 * Le directeur coordonne l'ordre des étapes de construction à appliquer
 *
 * @see BuilderProfileInterface interface de construction du profil
 */
public class Director {
    // ATTRIBUT

    private BuilderProfileInterface builder;

    // MÉTHODE

    public void setBuilder(BuilderProfileInterface builder) {
        this.builder = builder;
    }

    // COMMANDE

    /**
     * Construit le profil complet via le builder en appliquant des étapes de
     * construction dans un ordre spécifique :
     *      1. Nom <br>
     *      2. Répertoire source <br>
     *      3. répertoire cible <br>
     * @param name nom du profil à créer
     * @param source chemin du répertoire source
     * @param destination chemin du répertoire cible
     */
    public void construct(String name, Path source, Path destination) {
        builder.setProfileName(name)
                .setSourceDirectory(source)
                .setDestinationdirectory(destination);
    }
}
