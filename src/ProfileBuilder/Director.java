package ProfileBuilder;

import java.nio.file.Path;

/**
 * Rerésente le Directeur qui ochestre la construction du profil par
 *      l'intermédiaire de l'interface {@link BuilderProfileInterface}
 * Le directeur coordonne l'ordre des étapes de construction à appliquer
 *
 * @see BuilderProfileInterface
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
     *      construction dans un ordre spécifique:
     *      1. nom
     *      2. répertoire source
     *      3. répertoire de destination
     * @param name nom du profil à créer
     * @param source chemin du répertoire source
     * @param destination chemin du répertoire de destination
     */
    public void construct(String name, Path source, Path destination) {
        builder.setProfileName(name)
                .setSourceDirectory(source)
                .setDestinationdirectory(destination);
    }
}
