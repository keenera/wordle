package org.cis120.wordle;

import java.io.BufferedReader;
import java.util.*;

/**
 * This class is inspired by TweetParser.java from the Twitterbot homework.
 * It is used to convert data in a CSV file into a set of five-letter words.
 */
public class WordListParser {
    /**
     * The regular expression {@code "^[a-zA-Z]+$"} matches a String solely
     * consisting of alphabetic characters (uppercase or lowercase letters.)
     */
    private static final String GOOD_WORD_REGEX = "^[a-zA-Z]+$";

    /**
     * Given a String that represents a CSV line extracted from a reader and an
     * int that represents the column of the String that we want
     * to extract from, return the contents of that column from the String.
     * Columns in the buffered reader are zero indexed.
     *
     * @param csvLine   - a line extracted from a buffered reader
     * @param csvColumn - the column of the CSV line whose contents ought to be
     *                  returned
     * @return the portion of csvLine corresponding to the column of csvColumn.
     *         If the csvLine is null or has no appropriate csvColumn, return null
     */
    static String extractColumn(String csvLine, int csvColumn) {
        if (csvLine == null) {
            return null;
        }
        String[] contents = csvLine.split(",");
        if (csvColumn < 0 || csvColumn >= contents.length) {
            return null;
        }
        return contents[csvColumn];
    }

    /**
     * Given a buffered reader and the column that the words are in,
     * use the extractColumn and a FileLineIterator to extract every word from
     * the reader, clean it, and add it to the set of words from the file.
     *
     * @param br          - a BufferedReader that represents words
     * @param column - the number of the column in the buffered reader
     *                    that contains the word
     * @return a Set of cleaned word Strings
     */
    static Set<String> csvDataToWords(BufferedReader br, int column) {
        FileLineIterator iter = new FileLineIterator(br);
        Set<String> wordSet = new TreeSet<>();
        while (iter.hasNext()) {
            String word = extractColumn(iter.next(), column);
            if (word != null && cleanWord(word) != null) {
                wordSet.add(cleanWord(word));
            }
        }
        return wordSet;
    }

    static Set<String> cleanWordSet(Set<String> ws) {
        Set<String> wordSet = new TreeSet<>();
        for (String word: ws) {
            String cw = cleanWord(word);
            if (cw != null) {
                wordSet.add(cw);
            }
        }
        return wordSet;
    }

    /**
     * Cleans a word by removing leading and trailing whitespace and converting
     * it to upper case. If the word does not match GOOD_WORD_REGEX (i.e. it
     * contains non-alphabetic characters), is not 5 letters in length,
     * or is the empty String, returns null instead.
     *
     * @param word - a (non-null) String to clean
     * @return - a trimmed, lowercase version of the word if it contains no
     *         illegal characters and is not empty, and null otherwise.
     */
    static String cleanWord(String word) {
        if (word == null) {
            return null;
        }
        String cleaned = word.trim().toUpperCase();
        if (!cleaned.matches(GOOD_WORD_REGEX) || cleaned.length() != 5) {
            return null;
        }
        return cleaned;
    }
}
