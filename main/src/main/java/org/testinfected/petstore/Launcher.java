package org.testinfected.petstore;

public class Launcher {

    public static void main(String[] args) throws Exception {
        int port = parse(args);
        final PetStore petStore = new PetStore(port);
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
        petStore.start();
    }

    private static int parse(String... args) {
        return Integer.parseInt(args[0]);
    }
}
