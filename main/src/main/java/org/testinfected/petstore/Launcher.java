package org.testinfected.petstore;

public class Launcher {

    private static final int WEB_ROOT = 1;
    private static final int PORT = 0;

    public static void main(String[] args) throws Exception {
        String webRoot = args[WEB_ROOT];

        final PetStore petStore = PetStore.at(webRoot);
        // todo optional command line parameters
        petStore.encodeOutputAs("utf-8");
        petStore.logToConsole();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    petStore.stop();
                    System.out.println("Stopped");
                } catch (Exception ignored) {
                }
            }
        });

        int port = Integer.parseInt(args[PORT]);
        System.out.println("Starting web application at http://localhost:" + port);
        System.out.println("Serving from " + webRoot);
        petStore.start(port);
    }

}
