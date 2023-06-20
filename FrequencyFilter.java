import java.util.ArrayList;
import java.util.List;

public class FrequencyFilter {

    public static final String ILLEGAL_ARG_LENGTH = "List must be same length as number of trees";
    public static final String ILLEGAL_ARG_BAD_RANGE = "maxPrefixLen cannot be less than minPrefixLen";
    public static final String ILLEGAL_ARG_MIN_PREFIX = "minPrefixLen must be positive";

    // Array of BSTs that map prefixes to frequency (number of occurrences)
    private BST<String, Integer>[] frequencyTrees;


    /**
     * Builds this FrequencyFilter's frequencyTrees array of BSTs.
     * Each element in the array is a BST whose keys are the prefixes of a certain length
     * and values are the frequency of each prefix
     *
     * @param data         the data from which to parse prefixes
     * @param minPrefixLen the minimum prefix length, inclusive, equivalent to the prefix length of
     *                     the first BST in the this FrequencyFilter's frequencyTrees array
     * @param maxPrefixLen the maximum prefix length, inclusive, equivalent to the prefix length of
     *                     the last BST in the this FrequencyFilter's frequencyTrees array
     * @throws IllegalArgumentException if maxPrefixLen is less than minPrefixLen or minPrefixLen not positive
     */
    public void buildFrequencyTrees(String data, int minPrefixLen, int maxPrefixLen) throws IllegalArgumentException {
        if (!(minPrefixLen > 0))
            throw new IllegalArgumentException(ILLEGAL_ARG_MIN_PREFIX);
        else if (maxPrefixLen < minPrefixLen)
            throw new IllegalArgumentException(ILLEGAL_ARG_BAD_RANGE);
        if (data == null || data == "")
            return;

        frequencyTrees = (BST<String, Integer>[]) new BST<?, ?>[maxPrefixLen - minPrefixLen + 1];
        String[] wordsArray = data.split(" ");

        for (int degree = minPrefixLen; degree <= maxPrefixLen; degree++) {
            for (int i = 0; i < wordsArray.length - degree; i++) {
                String newPrefix = "";
                for (int j = i; j < i + degree; j++) {
                    newPrefix += wordsArray[j] + " ";
                }
                updateFrequencyTrees(newPrefix, degree - minPrefixLen);
            }
        }
    }

    // Adds new entries into frequencyTree Array based on specified prefix size.
    private boolean updateFrequencyTrees(String prefix, int index) {
        if (frequencyTrees[index] == null)
            frequencyTrees[index] = new BST<>();

        if (prefix == null || prefix == "")
            return false;
        if (index < 0 || index >= frequencyTrees.length)
            return false;

        String prefixToAdd = prefix.trim();
        BST<String, Integer> currentFreqTree = frequencyTrees[index];
        int frequency = 1;

        if (currentFreqTree.containsKey(prefixToAdd)) {
            frequency = currentFreqTree.get(prefixToAdd);
            return currentFreqTree.replace(prefixToAdd, frequency + 1);
        } else
            return currentFreqTree.put(prefixToAdd, frequency);

    }


    /**
     * Filters every BST in this FrequencyFilter's frequencyTrees array, removing any elements
     * whose frequency falls below the cutoff frequency. That is, if a prefix occurs less than the
     * cutoff frequency, it is removed
     */
    public List<String> filter(int freq) {
        if (freq < 0)
            return null;
        if (frequencyTrees == null)
            return null;

        List<String> toReturn = new ArrayList<>();

        for (BST<String, Integer> freqTree : frequencyTrees) {
            if (freqTree == null)
                return null;
            List<String> freqList = freqTree.keys();
            BST<Integer, String> freqSortTree = new BST<>();

            for (String currentPrefix : freqList) {
                if (freqTree.get(currentPrefix) < freq)
                    freqTree.remove(currentPrefix);
                else
                    freqSortTree.put(freqTree.get(currentPrefix), currentPrefix);
            }
            toReturn.add(generateOutput(freqSortTree));
        }

        return toReturn;
    }

    /**
     * Filters every BST as in filter(int freq) above, except that each BST is filtered with its
     * own cutoff frequency.
     */
    public List<String> filter(int[] freqs) throws IllegalArgumentException {
        if (freqs == null || !(freqs.length > 0))
            return null;
        if (freqs.length != frequencyTrees.length)
            throw new IllegalArgumentException(ILLEGAL_ARG_LENGTH);

        List<String> toReturn = new ArrayList<>();

        for (int treeNum = 0; treeNum < frequencyTrees.length; treeNum++) {
            List<String> freqList = frequencyTrees[treeNum].keys();
            BST<Integer, String> freqSortTree = new BST<>();
            for (String currentPrefix : freqList) {
                if (frequencyTrees[treeNum].get(currentPrefix) < freqs[treeNum])
                    frequencyTrees[treeNum].remove(currentPrefix);
                else {
                    freqSortTree.put(frequencyTrees[treeNum].get(currentPrefix), currentPrefix);
                }
            }
            toReturn.add(generateOutput(freqSortTree));
        }

        return toReturn;
    }

    // Helper method for filter(), generates the String output for a given tree.
    // Formatted according to the needs of filter().
    private String generateOutput(BST<Integer, String> tree) {
        String toReturn = "";
        List<Integer> frequencyList = tree.keys();

        toReturn += "\ntree -> {\n";

        for (int i = frequencyList.size() - 1; i >= 0; i--){
            int frequencyNum = frequencyList.get(i);
            toReturn += "\t\"" + tree.get(frequencyNum) + "\": " + frequencyNum + "\n";
        }

        return toReturn + "}";
    }
}
