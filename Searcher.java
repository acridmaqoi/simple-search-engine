package search;

import java.util.*;

public class Searcher { // context

    private Search type;

    public void setSearchType(Search type) {
        this.type = type;
    }

    public Set<String> runSearch(List<String> people, String query, Map<String, List<Integer>> index) {
        return this.type.search(people, query, index);
    }
}

interface Search { // strategy
    Set<String> search(List<String> people, String query, Map<String, List<Integer>> index);
}

class SearchAny implements Search { // concrete strategy


    @Override
    public Set<String> search(List<String> people, String query, Map<String, List<Integer>> indices) {
        String[] terms = query.toLowerCase().split(" ");
        Set<String> matchedPeople = new HashSet<>();

        for (String term : terms) {
            List<Integer> queryIndex = indices.get(term);

            if (queryIndex != null) {
                for (Integer index : queryIndex) {
                    matchedPeople.addAll(List.of(people.get(index)));
                }
            }
        }

        return matchedPeople;
    }
}

class SearchAll implements Search { // concrete strategy

    /***
     * Searches the List of people for the search term/query.
     * Only the lines containing all the words from the query will be printed.
     * @param people the List containing the lines to be searched
     * @param query the search term
     * @param indices the search index
     * @return - an array of lines which matched the search term
     */
    @Override
    public Set<String> search(List<String> people, String query, Map<String, List<Integer>> indices) {
        String[] terms = query.toLowerCase().split(" ");
        Set<String> matchedPeople = new HashSet<>();

        for (String term : terms) {
            List<Integer> queryIndex = indices.get(term);

            if (queryIndex != null) {
                for (Integer index : queryIndex) {
                    if (matchedPeople.isEmpty()) {
                        matchedPeople.addAll(List.of(people.get(index)));
                    } else {
                        matchedPeople.retainAll((List.of(people.get(index))));
                    }
                }
            }
        }

        return matchedPeople;
    }
}

class SearchNone implements Search { // concrete strategy

    @Override
    public Set<String> search(List<String> people, String query, Map<String, List<Integer>> indices) {
        String[] terms = query.toLowerCase().split(" ");
        Set<String> matchedPeople = new HashSet<>(people);

        for (String term : terms) {
            List<Integer> queryIndex = indices.get(term);

            if (queryIndex != null) {
                for (Integer index : queryIndex) {
                    matchedPeople.removeAll(List.of(people.get(index)));
                }
            }
        }

        return matchedPeople;
    }
}