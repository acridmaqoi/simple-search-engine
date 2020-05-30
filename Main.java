package search;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static List<String> people = new ArrayList<>();
    static Map<String, List<Integer>> index = new HashMap<>();

    public static void main(String[] args) {

        if (args.length > 0 && args[0].equals("--data")) {
            File file = new File(args[1]);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    people.add(scanner.nextLine());
                }
            } catch (IOException e) {
                System.out.println("invalid file");
                return;
            }
        } else {
            System.out.println("Enter the number of people:");
            int numPeople = Integer.parseInt(scanner.nextLine());

            System.out.println("Enter all people:");
            for (int i = 0; i < numPeople; i++) {
                people.add(scanner.nextLine());
            }
        }

        // index
        for (String str : people) {
            String[] terms = str.split(" ");

            for (String term : terms) {
                if (index.containsKey(term.toLowerCase())) {
                    index.get(term.toLowerCase()).add(people.indexOf(str));
                } else {
                    index.put(term.toLowerCase(), new ArrayList<>(List.of(people.indexOf(str))));
                }
            }
        }

        // menu
        boolean quit = false;
        while (!quit) {
            System.out.println("\n=== Menu ===\n" +
                    "1. Find a person.\n" +
                    "2. Print all people.\n" +
                    "0. Exit.");

            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    search();
                    break;
                case 2:
                    print();
                    break;
            }
        }
    }

    public static void print() {
        System.out.println("=== List of people ===");
        for (String str : people) {
            System.out.println(str);
        }
    }

    public static void search() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String input = scanner.nextLine();

        System.out.println("Enter a name or email to search all suitable people.");
        String query = scanner.nextLine();

        Searcher searcher = new Searcher();

        switch (input.toUpperCase()) {
            case "ALL":
                searcher.setSearchType(new SearchAll());
                break;
            case "ANY":
                searcher.setSearchType(new SearchAny());
                break;
            case "NONE":
                searcher.setSearchType(new SearchNone());
                break;
        }

        Set<String> result = searcher.runSearch(people, query, index);
        if (result.size() == 0) {
            System.out.println("No matching people found");
        } else {
            System.out.println(result.size() + " persons found:");
        }
        for (String line : result) {
            System.out.println(line);
        }
    }
}
