import java.math.BigInteger;
import java.util.Scanner;
import java.util.*;
import java.io.*;

public class Hashing_Quadratic_Probing
{

    static int hashTableSize;
    static int collisions;
    static String[] hashArray;
    static int numKeys = 0;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
       /* String[] words = new String[]{"tangy", "ball", "agreeable", "illegal", "faded", "pointless", "foamy", "purpose", "profit",
                "geese", "cut", "flippant", "needy", "loose", "downtown", "obese", "stem", "sack", "knotty", "detail", "offbeat",
                "poor", "nest", "dizzy", "lettuce", "level", "anger", "wander", "meddle", "roomy", "bushes", "ablaze", "greedy",
                "powder", "attract", "glue", "needless", "name", "spark","royal", "lock", "sudden", "rabbit", "apparel", "delicate",
                "parcel", "warm", "adamant", "waiting", "ajar", "proud", "precede", "direful"
                ,"imperfect","stiff","contain","mark","stimulating","secret","ubiquitous","stupendous","caring", "ugliest","permit"};
        */
        String[] addons = new String[]{"imperfect","stiff","contain","mark","stimulating","secret","ubiquitous","stupendous","caring", "ugliest","permit"};
        hashTableSize = 53;
        hashArray = new String[hashTableSize];
        Scanner scan = new Scanner(System.in);
        //String fileName = System.getProperty("user.dir")+"\\src\\wordlist.txt";
        System.out.println(">> Please enter the path of the file containing the list of words:");
        Scanner in = new Scanner(System.in);
        String fileName = in.nextLine();
        //System.out.println(fileName);
        ArrayList<String> wordsList = new ArrayList<String>();
        try
        {
            Scanner fileScan = new Scanner(new File(fileName));


            String word;

            for(; fileScan.hasNext(); wordsList.add(word))
                word = fileScan.next();
        }
        catch(FileNotFoundException ex) {
            System.out.println("File is not found!");
            System.exit(0);
        }
        //System.out.println(words.size());


        String[] words = new String[wordsList.size()]; //wordsList.toArray();
        words = new String[wordsList.size()];
        words = wordsList.toArray(words);
        //inserting elements into the hashtable;
        System.out.println("\n>> Inserting list of words in hash table\n");
        insert(words);
        System.out.println("\n>> Adding more 10 words to the hashtable \n");
        insert(addons);

        //searching for word
        String word ="";
        while(true)
        {
            System.out.println("\n>> Please enter the word to be searched. To quit type: quit()");
            word = scanner.nextLine();
            if  (word.equals("quit()"))
            {
                break;
            }
            //!(word.equals("quit()"))
            else
            {
                int searchResult = search(word);
                if (searchResult == -1)
                {
                    System.out.println(">> The word " + word + " is not present in the hashtable");
                }
                else
                {
                    System.out.println(">> Word found. Index of the word " + word + " : " + searchResult);
                }
            }
            //System.out.println();
        }


    }

    public static int hashFunction(String word, int size)
    {
        int hash = 0;
        for(int i=0; i < word.length();i++)
        {
            hash = hash + word.charAt(i);
        }
        return Math.abs(hash % size);
    }

    public static void insert (String[] array)
    {
        for (int i=0;i<array.length;i++)
        {
            //TODO: hashing function
            int initialIndex = hashFunction(array[i],hashTableSize);
            collisions =0;
            numKeys++;
            int QPIndex = quadraticProbing(hashArray,initialIndex,collisions);
            //System.out.println("flag 01");
            double loadFactor = (double) numKeys/hashTableSize;

            if (loadFactor > 0.5)
            {
                //System.out.println("flag 02");
                System.out.println("\n>> Load Factor became greater than 0.5 while inserting "+(i+1)+"th word. Hash table size: "
                        + hashTableSize +". No. of elements currently in the table: "+(numKeys-1));
                rehash(array);
                break;
            }
            else
            {
                //System.out.println("flag 03");
                hashArray[QPIndex] = array[i];
            }
            System.out.println(array[i]+"   "+ "Initial Index: "+initialIndex+"   "+"Quadratic Probing Index:"+QPIndex
                    + "   Collisions: "+collisions);
        }
    }

    public static int quadraticProbing(String[] hashArray, int initial_index, int collision)
    {
        int index = (initial_index + (collisions * collisions)) %hashTableSize;
        //System.out.println("Index "+ index);
        if(index == hashTableSize)
        {
            //System.out.println("flag 04");
            index = 0; // to wrap around the table
        }
        if(hashArray[index] == null)
        {
            //System.out.println("flag 05");
            return index; // insert possible at this location
        }
        collisions = ++collision;

        return quadraticProbing(hashArray,initial_index,collision); // recursive call
    }

    public static void rehash(String[] array)
    {
        //System.out.println("flag 06");
        numKeys =0; // reset it to zero since all the keys will be inserted again
        if (numKeys>=100) {
            ArrayList<String> wordsList1 = new ArrayList<String>();
            for (int i = 0; i < hashTableSize; i++) {
                if (hashArray[i] != null) {
                    wordsList1.add(hashArray[i]);
                }
            }
            String[] words1 = new String[wordsList1.size()];
            words1 = new String[wordsList1.size()];
            words1 = wordsList1.toArray(words1);
        }



        BigInteger x = new BigInteger(String.valueOf(hashTableSize*2));
        hashTableSize = x.nextProbablePrime().intValue();
        hashArray = new String[hashTableSize];
        System.out.println(">> New size of hash table after rehashing: "+ hashTableSize+"\n");
        insert(array); // re-insert elements
        //System.out.println("flag 07");
    }

    public static int search(String word)
    {
        int index = -1; //initial
        int inital_hashID = hashFunction(word,hashArray.length);
        for(int i =0;i<hashArray.length;i++)
        {
            int hashID = (inital_hashID + (i * i)) %hashTableSize;
            //System.out.println("hashid "+hashID);
            if(hashArray[hashID] !=null)
            {
                if(hashArray[hashID].equals(word))
                {
                    return hashID;
                }
            }
        }
        return index;
    }


}