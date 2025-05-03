package Main;

import ProfileBuilder.BuilderProfileInterface;
import ProfileBuilder.ConcreteProfileBuilder;
import ProfileBuilder.Profile;

import java.nio.file.Path;
import java.nio.file.Paths;

public class NewProfile {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage:java new-profile <nom-du-profile> <source-dir> <dest-dir>");
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
