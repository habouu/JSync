package Main;

import Builder.BuilderProfileInterface;
import Builder.ConcreteProfileBuilder;
import Builder.Profile;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Programme de création d'un nouveau profil de synchronisation.
 * Il permet à l'utilisateur de spécifier un nom de profil, un répertoire
 * source et cible pour la synchronisation des fichiers.
 * <p>Exécution :</p>
 * <pre>
 *     java Main.NewProfile nom-du-profil source-dir cible-dir
 * </pre>
 *
 * @see BuilderProfileInterface interface de construction du profil
 * @see ConcreteProfileBuilder implémentation de l'interface de construction
 * @see Profile profil de synchronisation
 */
public class NewProfile {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(
                    "Usage:java Main.NewProfile nom-du-profil source-dir cible-dir"
            );
        }

        String profileName = args[0];
        Path sourceDir = Paths.get(args[1]);
        Path destDir = Paths.get(args[2]);

        BuilderProfileInterface builder = new ConcreteProfileBuilder();
        Profile profile = builder.setProfileName(profileName)
                .setSourceDirectory(sourceDir)
                .setDestinationdirectory(destDir)
                .getResult();

        profile.saveToFile();
        System.out.println("Profile " + profileName + " saved.");
        System.out.println("Profile Name: " + profileName);
        System.out.println("Source Directory: " + sourceDir);
        System.out.println("Destination Directory: " + destDir);
    }
}
