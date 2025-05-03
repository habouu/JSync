package ProfileBuilder;

import java.nio.file.Path;

/**
 * Inmplémentation concréte de l'interface {@link BuilderProfileInterface}
 *
 * Construction d'un {@link Profile} étape par étape en définissant les
 *      différentes propriétés (nom, répertoires source et destination)
 *
 * @see BuilderProfileInterface
 * @see Profile
 */
public class ConcreteProfileBuilder implements BuilderProfileInterface {
    // ATTRIBUT

    private Profile profile;

    // CONSTRUCTEUR

    public ConcreteProfileBuilder() {
        this.profile = new Profile();
    }

    // MÉTHODES

    @Override
    public BuilderProfileInterface setProfileName(String name) {
        profile.setProfileName(name);
        return this;
    }

    @Override
    public BuilderProfileInterface setSourceDirectory(Path sourceDirectory) {
        profile.setSourceDirectory(sourceDirectory);
        return this;
    }

    @Override
    public BuilderProfileInterface setDestinationdirectory(
            Path destinationDirectory) {
        profile.setDestinationDirectory(destinationDirectory);
        return this;
    }

    // REQUÊTE

    @Override
    public Profile getResult() {
        return profile;
    }
}
