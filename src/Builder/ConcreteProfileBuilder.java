package Builder;

import java.nio.file.Path;

/**
 * Implementation concréte de l'interface {@link BuilderProfileInterface}.
 * Construction d'un objet de type {@link Profile} étape par étape en définissant
 * les différentes propriétés (nom, répertoires source et cible)
 *
 * @see BuilderProfileInterface interface de construction du profil
 * @see Profile profil de synchronisation
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
