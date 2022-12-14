import java.util.Arrays;

import static java.lang.System.arraycopy;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k < 1) {
                return;
            }
            int j;
            for (int i = 1; i < k; i++) {
                int curr = array[i];
                for (j = i - 1; j >= 0; j--) {
                    if (array[j] < curr) {
                        break;
                    }
                    array[j + 1] = array[j];
                }
                array[j + 1] = curr;
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k < 1) {
                return;
            }
            for (int i = 0; i < k; i++) {
                int min = array[i];
                int minindex = i;
                for (int j = i + 1; j < k; j++) {
                    if (array[j] < min) {
                        min = array[j];
                        minindex = j;
                    }
                }
                swap(array, minindex, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array == null || k < 1) {
                return;
            }
            sortHelper(array, 0, k);
        }

        private void sortHelper(int[] array, int lower, int upper) {
            if (upper <= lower || upper - lower == 1) {
                return;
            }
            int mid = (lower + upper) / 2;
            sortHelper(array, lower, mid);
            sortHelper(array, mid, upper);
            mergeHelper(array, lower, mid, upper);
        }

        private void mergeHelper(int[] array, int lower, int mid, int upper) {
            int j;
            for (int i = mid; i < upper; i++) {
                int curr = array[i];
                for (j = i - 1; j >= lower; j--) {
                    if (array[j] < curr) {
                        break;
                    }
                    array[j + 1] = array[j];
                }
                array[j + 1] = curr;
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            if (a == null || k < 1) {
                return;
            }
            int digit = countMaxDigit(a, k);
            for (int i = 0; i < digit; i++) {
                sortHelper(a, k, i);
            }
        }
        private int countMaxDigit(int[] a, int k) {
            int max = a[0];
            for (int i = 1; i < k; i++) {
                max = Math.max(max, a[i]);
            }
            int num = 0;
            while (max > 0) {
                num++;
                max >>= 1;
            }
            return num;
        }

        private void sortHelper(int[] a, int k, int digit) {
            int MASK = (1 << (digit + 1)) - 1;
            int[] counts = new int[2];
            int[] output = new int[k];
            for (int i = 0; i < k; i++) {
                int c = (a[i] & MASK) >> digit;
                counts[c]++;
            }
            counts[1] += counts[0];
            for (int i = k - 1; i >= 0; i--) {
                int c = (a[i] & MASK) >> digit;
                output[counts[c]-- - 1] = a[i];
            }
            arraycopy(output, 0, a, 0, k);
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
