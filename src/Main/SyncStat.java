package Main;

import Adapter.FileSystem;
import Adapter.LocalFileSystemAdapter;
import Facade.DisplayStatusFacade;
import ProfileBuilder.Profile;

public class SyncStat {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java syncstat <nom-du-profile>");
            return;
        }

        String profileName = args[0];

        Profile profile = Profile.loadFromFile(profileName);

        if (profile == null) {
            System.err.println("Profile " + profileName + " not found");
            return;
        }

        FileSystem fileSystem = new LocalFileSystemAdapter();
        DisplayStatusFacade statusFacade = new DisplayStatusFacade(fileSystem);
        System.out.println("Synchronization status profile: " + profileName);
        System.out.println("Source: " + profile.getSourceDirectory());
        System.out.println("Destination: " + profile.getDestinationDirectory());
        System.out.println("----------------------------------------");
        statusFacade.displayStatus(profile);
    }
}
