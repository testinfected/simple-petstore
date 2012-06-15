package org.testinfected.petstore;

import java.io.File;

public class Launcher {

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        File webRoot = new File(args[1]);

        final PetStore petStore = PetStore.rootedAt(webRoot);
        // todo optional command line parameter
        petStore.setEncoding("utf-8");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    petStore.stop();
                    System.out.println("Stopped");
                } catch (Exception ignored) {
                }
            }
        });
        System.out.println("Starting web application at http://localhost:" + port);
        System.out.println("Serving files from " + webRoot.getAbsolutePath());
        petStore.start(port);
    }

}
