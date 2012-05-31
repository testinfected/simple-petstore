package org.testinfected.petstore;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = parse(args);
        final PetStore petStore = new PetStore(port);
        System.out.println("Starting web application at http://localhost:" + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override public void run() {
                try {
                    petStore.stop();
                    System.out.println("Stopped");
                } catch (Exception ignored) {
                }
            }
        });
        petStore.start();
    }

    private static int parse(String... args) {
        return Integer.parseInt(args[0]);
    }
}
