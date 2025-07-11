package Builder;

import java.nio.file.Path;

/**
 * Interface du patron Builder pour la construction d'un objet {@link Profile}
 *
 * Cette interface fournit les méthodes à implémenter pour la construction d'un
 * profil. Un profil est constitué d'un
 * <pre>
 *     Nom
 *     Répertoire source
 *     Répertoire cible
 * </pre>
 */
public interface BuilderProfileInterface {
    /**
     * Défini le nom du profil qu'on souhaite créer.
     * @param name nom du profil
     * @return builder courant
     */
    BuilderProfileInterface setProfileName(String name);

    /**
     * Défini le chemin du répertoire source du profil en construction.
     * @param sourceDirectory chemin vers le répertoire source
     * @return builder courant
     */
    BuilderProfileInterface setSourceDirectory(Path sourceDirectory);

    /**
     * Défini le chemin du répertoire destination pour le profil en construction.
     * @param destinationDirectory chemin vers le répertoire de destination
     * @return builder courant
     */
    BuilderProfileInterface setDestinationdirectory(Path destinationDirectory);

    /**
     * Récupère l'objet {@link Profile} final construit étape par étape.
     * @return objet {@link Profile} construit
     */
    Profile getResult();
}
