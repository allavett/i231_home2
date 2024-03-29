import java.util.*;

/**
 * Comparison of sorting methods. The same array of non-negative int values is
 * used for all methods.
 *
 * @author Jaanus
 * @version 3.0
 * @since 1.6
 */
public class IntSorting {

   /** maximal array length */
   static final int MAX_SIZE = 2560;

   /** number of competition rounds */
   static final int NUMBER_OF_ROUNDS = 4;

   /**
    * Main method.
    *
    * @param args
    *           command line parameters
    */
   public static void main(String[] args) {
      final int[] origArray = new int[MAX_SIZE];
      Random generator = new Random();
      for (int i = 0; i < MAX_SIZE; i++) {
         origArray[i] = generator.nextInt(1000);
      }
      int rightLimit = MAX_SIZE / (int) Math.pow(2., NUMBER_OF_ROUNDS);

      // Start a competition
      for (int round = 0; round < NUMBER_OF_ROUNDS; round++) {
         int[] acopy;
         long stime, ftime, diff;
         rightLimit = 2 * rightLimit;
         System.out.println();
         System.out.println("Length: " + rightLimit);

         acopy = Arrays.copyOf(origArray, rightLimit);
         stime = System.nanoTime();
         insertionSort(acopy);
         ftime = System.nanoTime();
         diff = ftime - stime;
         System.out.printf("%34s%11d%n", "Insertion sort: time (ms): ", diff / 1000000);
         checkOrder(acopy);

         acopy = Arrays.copyOf(origArray, rightLimit);
         stime = System.nanoTime();
         binaryInsertionSort(acopy);
         ftime = System.nanoTime();
         diff = ftime - stime;
         System.out.printf("%34s%11d%n", "Binary insertion sort: time (ms): ", diff / 1000000);
         checkOrder(acopy);

         acopy = Arrays.copyOf(origArray, rightLimit);
         stime = System.nanoTime();
         quickSort(acopy, 0, acopy.length);
         ftime = System.nanoTime();
         diff = ftime - stime;
         System.out.printf("%34s%11d%n", "Quicksort: time (ms): ", diff / 1000000);
         checkOrder(acopy);

         acopy = Arrays.copyOf(origArray, rightLimit);
         stime = System.nanoTime();
         Arrays.sort(acopy);
         ftime = System.nanoTime();
         diff = ftime - stime;
         System.out.printf("%34s%11d%n", "Java API  Arrays.sort: time (ms): ", diff / 1000000);
         checkOrder(acopy);

         acopy = Arrays.copyOf(origArray, rightLimit);
         stime = System.nanoTime();
         radixSort(acopy);
         ftime = System.nanoTime();
         diff = ftime - stime;
         System.out.printf("%34s%11d%n", "Radix sort: time (ms): ", diff / 1000000);
         checkOrder(acopy);
      }
   }

   /**
    * Insertion sort.
    *
    * @param a
    *           array to be sorted
    */
   public static void insertionSort(int[] a) {
      if (a.length < 2)
         return;
      for (int i = 1; i < a.length; i++) {
         int b = a[i];
         int j;
         for (j = i - 1; j >= 0; j--) {
            if (a[j] <= b)
               break;
            a[j + 1] = a[j];
         }
         a[j + 1] = b;
      }
   }

   /**
    * Binary insertion sort.
    * Used materials:
    * http://enos.itcollege.ee/~jpoial/algoritmid/searchsort.html
    * Algoritmid Ja Andmestruktuurid J.Kiho Tartu 2003
    * https://www.geeksforgeeks.org/binary-insertion-sort/
    * TODO!! Not finished! poor performance and ugly code D:
    *
    * @param a
    *           array to be sorted
    */
   public static void binaryInsertionSort(int[] a) {
      // TODO!!! Your method here!
      int len = a.length - 1;


      int i; // Item that is looking for it's place
      int j = -1;
      for (i = 0; i <= len; i++) {
         int left = 0;
         int right = len;
         while (left <= right) {
            int k = ((left + right) % 2) == 1 ? 1 : 0;
            j = (left + right) / 2 + k;
            // comparing item with itself is useless, move on with the search to the left
            if (i == j && j > left) {
               j--;
            }

            if (a[i] > a[j]) {
               left = j + 1;
            } else if (a[i] < a[j]) {
               right = j - 1;
               if (left > right && j != 0) {
                  left = right;
               }
            } else {
               break;
            }

         }
         //System.out.println(j);
         if (i != j) {
            //int a_old[] = a.clone();
            //System.arraycopy(a, 0, a_old, 0, a.length -1 );
            //System.out.println(Arrays.toString(a_old));
            if (a[i] == a[j] && j - i == 1) {
               while (a[i] == a[j]){
                  i++;
                  j++;
               }


            } else {
               int sortee = a[i];
               if (j > i) {
                  System.arraycopy(a, i + 1, a, i, j - i); //move down/left - length = mitu itemit, srcPos - kus kohast võtma hakata, desPos - kuhu
                  if (i != 0) {
                     i--;
                  } // to recheck the value of the item that was pushed on that index
               } else {
                  System.arraycopy(a, j, a, j + 1, i - j); // move up/right
                  if (sortee > a[j]) {
                     j++;
                  }
               }
               a[j] = sortee;
               //System.out.println(Arrays.toString(a));
            }
         }
      }
      //System.arraycopy();
   }

   /**
    * Sort a part of the array using quicksort method.
    *
    * @param array
    *           array to be changed
    * @param l
    *           starting index (included)
    * @param r
    *           ending index (excluded)
    */
   public static void quickSort (int[] array, int l, int r) {
      if (array == null || array.length < 1 || l < 0 || r <= l)
         throw new IllegalArgumentException("quickSort: wrong parameters");
      if ((r - l) < 2)
         return;
      int i = l;
      int j = r - 1;
      int x = array[(i + j) / 2];
      do {
         while (array[i] < x)
            i++;
         while (x < array[j])
            j--;
         if (i <= j) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            i++;
            j--;
         }
      } while (i < j);
      if (l < j)
         quickSort(array, l, j + 1); // recursion for left part
      if (i < r - 1)
         quickSort(array, i, r); // recursion for right part
   }

   /** frequency of the byte */
   public static int[] freq = new int[256];

   /** number of positions */
   public static final int KEYLEN = 4;

   /** Get the value of the position i. */
   public static int getValue(int key, int i) {
      return (key >>> (8 * i)) & 0xff;
   }

   /** Sort non-negative keys by position i in a stable manner. */
   public static int[] countSort(int[] keys, int i) {
      if (keys == null)
         return null;
      int[] res = new int[keys.length];
      for (int k = 0; k < freq.length; k++) {
         freq[k] = 0;
      }
      for (int key : keys) {
         freq[getValue(key, i)]++;
      }
      for (int k = 1; k < freq.length; k++) {
         freq[k] = freq[k - 1] + freq[k];
      }
      for (int j = keys.length - 1; j >= 0; j--) {
         int ind = --freq[getValue(keys[j], i)];
         res[ind] = keys[j];
      }
      return res;
   }

   /** Radix sort for non-negative integers. */
   public static void radixSort(int[] keys) {
      if (keys == null)
         return;
      int[] res = keys;
      for (int p = 0; p < KEYLEN; p++) {
         res = countSort(res, p);
      }
      System.arraycopy(res, 0, keys, 0, keys.length);
   }

   /**
    * Check whether an array is ordered.
    *
    * @param a
    *           sorted (?) array
    * @throws IllegalArgumentException
    *            if an array is not ordered
    */
   static void checkOrder(int[] a) {
      if (a.length < 2)
         return;
      for (int i = 0; i < a.length - 1; i++) {
         if (a[i] > a[i + 1]) {
            System.out.println(Arrays.toString(a));
            throw new IllegalArgumentException(
                    "array not ordered: " + "a[" + i + "]=" + a[i] + " a[" + (i + 1) + "]=" + a[i + 1]);

         }
      }
   }

}

