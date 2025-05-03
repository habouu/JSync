package Main;

import Adapter.FileSystem;
import Adapter.LocalFileSystemAdapter;
import Facade.SynchronizationFacade;
import ProfileBuilder.Profile;

public class Synchronization {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java sync <nom-du-profile>");
        }

        String profileName = args[0];

        Profile profile = Profile.loadFromFile(profileName);

        if (profile == null) {
            System.err.println("Profile " + profileName + " not found");
        }

        FileSystem fileSystem = new LocalFileSystemAdapter();
        SynchronizationFacade synchronizationFacade = new SynchronizationFacade(fileSystem);

        System.out.println("Synchronization started for " + profileName);
        System.out.println("Source: " + profile.getSourceDirectory());
        System.out.println("Destination: " + profile.getDestinationDirectory());
        System.out.println("----------------------------------------");

        synchronizationFacade.synchronize(profile);

        System.out.println("----------------------------------------");
        System.out.println("Synchronization stopped");
    }
}
