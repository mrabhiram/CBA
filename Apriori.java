import java.io.*;
import java.util.*;

public class Apriori {


    public static void main(String[] args) throws Exception {
        Apriori ap = new Apriori(args);
    }

    /** the list of current itemsets */
    private List<int[]> itemsets ;
    /** the name of the transcation file */
    private String transaFile; 
    /** number of different items in the dataset */
    private int numItems; 
    /** total number of transactions in transaFile */
    private int numTransactions; 
    /** minimum support for a frequent itemset in percentage, e.g. 0.8 */
    private double minSup; 
    private int classLabel;        
    private int[] class_label;
	private int[] class_labels = new int[2000000];
    private List<int[]> store_itemsets = new ArrayList<int[]>(); //the previously stored frequent candidates for the current itemset
    private int[][] count;
	private int[][] counttemp = new int[2000000][2];
    private double confidence;
    private HashMap<Integer,String> ht;
    public  Apriori(String[] args) throws Exception
    {
        configure(args);
        go();
    }

    /** starts the algorithm after configuration */
	private void go() throws Exception {
		long start = System.currentTimeMillis();
        createItemsetsOfSize1();        
        int itemsetNumber=1; //the current itemset being looked at
        int nbFrequentSets=0;
        while (itemsets.size()>0)
        {
            calculateFrequentItemsets();
            if(itemsets.size()!=0)
            {
                nbFrequentSets+=itemsets.size();
                log("Found "+itemsets.size()+" frequent itemsets of size " + itemsetNumber + " (with support "+(minSup*100)+"%)");;
                createNewItemsetsFromPreviousOnes();
            }

            itemsetNumber++;
        } 
       // log(""+ Arrays.toString(store_itemsets.s(0)));
        //display the execution time
        long end = System.currentTimeMillis();
        log("Execution time is: "+((double)(end-start)/1000) + " seconds.");
        log("Found "+nbFrequentSets+ " frequents sets for support "+(minSup*100)+"% (absolute "+Math.round(numTransactions*minSup)+")");
        log("Done");
        write_itemsets(store_itemsets);
    }

    private void write_itemsets(List<int[]> store_itemsets) throws Exception{
        BufferedReader data_in = new BufferedReader(new InputStreamReader(new FileInputStream(transaFile)));
        boolean[] trans = new boolean[numItems];
        int count_1[] = new int[store_itemsets.size()]; //the number of successful matches, initialized by zeros        
        boolean match;
        ht = new HashMap<Integer,String>();
        ht.put(3 ,"flat");ht.put(4, "knobbed");ht.put(5, "sunken");ht.put(6, "convex");
        ht.put(7, "fibrous");ht.put(8, "grooves");ht.put(9, "smooth");ht.put(10, "scaly");
        ht.put(11, "buff");ht.put(12, "cinnamon");ht.put(13, "red");ht.put(14, "gray");
        ht.put(15, "brown");ht.put(16, "pink");ht.put(17, "green");ht.put(18, "purple");
        ht.put(19, "white");ht.put(20, "yellow");ht.put(21, "no");ht.put(22, "bruises");
        ht.put(23, "almond");ht.put(24, "creosote");ht.put(25, "foul");ht.put(26, "anise");
        ht.put(27, "musty");ht.put(28, "none");ht.put(29, "pungent");ht.put(30, "spicy");
        ht.put(31, "fishy");ht.put(32, "attached");ht.put(33, "descending");ht.put(34, "free");
        ht.put(35, "notched");ht.put(36, "close");ht.put(37, "distant");ht.put(38, "crowded");
        ht.put(39, "broad");ht.put(40, "narrow");ht.put(41, "buff");ht.put(42, "red");ht.put(43, "gray");
        ht.put(44, "chocolate");ht.put(45, "black");ht.put(46, "brown");ht.put(47, "orange");
        ht.put(48, "pink");ht.put(49, "green");ht.put(50, "purple");ht.put(51, "white");
        ht.put(52, "yellow");ht.put(53, "enlarging");ht.put(54, "tapering");ht.put(55, "fibrous");
        ht.put(56, "silky");ht.put(57, "smooth");ht.put(58, "smooth");ht.put(59, "fibrous");
        ht.put(60, "silky");ht.put(61, "smooth");ht.put(62, "scaly");ht.put(63, "buff");
        ht.put(64, "cinnamon");ht.put(65, "red");ht.put(66, "gray");ht.put(67, "brown");
        ht.put(68, "orange");ht.put(69, "pink");ht.put(70, "white");ht.put(71, "yellow");
        ht.put(72, "buff");ht.put(73, "cinnamon");ht.put(74, "red");ht.put(75, "gray");
        ht.put(76, "brown");ht.put(77, "orange");ht.put(78, "pink");ht.put(79, "white");
        ht.put(80, "yellow");ht.put(81, "partial");ht.put(82, "universal");ht.put(83, "brown");
        ht.put(84, "orange");ht.put(85, "white");ht.put(86, "yellow");ht.put(87, "none");
        ht.put(88, "one");ht.put(89, "two");ht.put(90, "cobwebby");ht.put(91, "evanescent");
        ht.put(92, "flaring");ht.put(93, "large");ht.put(94, "none");ht.put(95, "pendant");
        ht.put(96, "sheathing");ht.put(97, "zone");ht.put(98, "buff");ht.put(99, "chocolate");
        ht.put(100, "black");ht.put(101, "brown");ht.put(102, "orange");ht.put(103, "green");
        ht.put(104, "purple");ht.put(105, "white");ht.put(106, "yellow");ht.put(107, "abundant");
        ht.put(108, "clustered");ht.put(109, "numerous");ht.put(110, "scattered");ht.put(111, "several");
        ht.put(112, "solitary");ht.put(113, "woods");ht.put(114, "grasses");ht.put(115, "leaves");
        ht.put(116, "meadows");ht.put(117, "paths");ht.put(118, "urban");ht.put(119, "waste");        
        for (int i = 0; i < numTransactions; i++) {
            // boolean[] trans = extractEncoding1(data_in.readLine());
            String line = data_in.readLine();
            line2booleanArray1(line, trans);
            // check each candidate
            for (int c = 0; c < store_itemsets.size(); c++) {
                match = true; 
                int[] cand = store_itemsets.get(c);
                //log(""+Arrays.toString(store_itemsets.get(c)));
                //int[] cand = candidatesOptimized[c];
                // check each item in the itemset to see if it is present in the
                // transaction
                for (int xx : cand) {
                    if (trans[xx] == false) {
                        match = false;
                        break;
					}
				}
                if (match) { // if at this point it is a match, increase the count
                    count_1[c]++;
                                        //log(Arrays.toString(cand)+" is contained in trans "+i+" ("+line+")");
                }
            }
        }
		data_in.close();   
		for(int i=0;i<store_itemsets.size();i++){
			log(Arrays.toString(store_itemsets.get(i)));
		}
		//   log(""+trans.length);
		log("Yup"+counttemp[0][0]);
		File file = new File("output.txt");
        if (!file.exists()) {
			file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuffer content = new StringBuffer();

        File file1 = new File("rules.txt");
        if (!file1.exists()) {
            file1.createNewFile();
        }
		FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
		BufferedWriter bw1 = new BufferedWriter(fw1);    
		StringBuffer content1 =  new StringBuffer();
		for(int i=0;i<store_itemsets.size();i++){
			double confidence_temp = (float)counttemp[i][class_labels[i]-1]/count_1[i];
			int attr[] = store_itemsets.get(i);
			StringBuffer att = new StringBuffer();
			att.append("[");
			for(int a=0;a<attr.length;a++) {
				att.append(ht.get(attr[a]));
				att.append(" ");
			}
			att.append("]");
			content.append("Confidence of "+ att.toString() + " with class label " +class_labels[i] + " is " + confidence_temp+"\n");
			System.out.println(" "+content);
            //log(" "+content);
			    
            if(confidence_temp>=confidence) {
			    content1.append(" "+ att.toString() + " => " +(class_labels[i]==1 ? "Edible" : "Poisonous") + "\n");
		    }
		}
		bw.write(content.toString());
        bw.close();
		bw1.write(content1.toString());
		bw1.close();
            
	}

    /** triggers actions if a frequent item set has been found  */
    private void foundFrequentItemSet(int[] itemset, int support, int i) {
		System.out.println(Arrays.toString(itemset) + " Class Label :"+classLabel+"  ("+ ((support / (double) numTransactions))+" "+support+")");
        class_label[i] = classLabel;
    }

    /** outputs a message in Sys.err if not used as library */
    private void log(String message) {
        System.out.println(message);
    }

    /** computes numItems, numTransactions, and sets minSup */
    private void configure(String[] args) throws Exception
    {        
        // setting transafile
        if (args.length!=0) transaFile = args[0];
        else transaFile = "mushroom1.dat"; // default
      
        // setting minsupport
        if (args.length>=2) minSup=(Double.valueOf(args[1]).doubleValue());     
        else minSup = 0.1;// by default
        if (minSup>1 || minSup<0) throw new Exception("minSup: bad value");
        
        
        // going thourgh the file to compute numItems and  numTransactions
        numItems = 0;
        numTransactions=0;
        BufferedReader data_in = new BufferedReader(new FileReader(transaFile));
        while (data_in.ready()) {           
            String line=data_in.readLine();
            if (line.matches("\\s*")) continue; //  ignore empty lines
            numTransactions++;
            StringTokenizer t = new StringTokenizer(line," ");
            while (t.hasMoreTokens()) {
                int x = Integer.parseInt(t.nextToken());
                //log(x);
                numItems++;
            }           
        }  
        System.out.println(""+numItems);
        outputConfig();

    }

   /** outputs the current configuration
     */ 
    private void outputConfig() {
        //output config info to the user
         log("Input configuration: "+numItems+" items, "+numTransactions+" transactions, ");
         log("minsup = "+minSup*100+"%");
    }

    /** puts in itemsets all sets of size 1, 
     * i.e. all possibles items of the datasets
     */
    private void createItemsetsOfSize1() {
        itemsets = new ArrayList<int[]>();
        for(int i=0; i<numItems; i++)
        {
            int[] cand = {i};
            itemsets.add(cand);
        }
        /*Iterator<int[]> it = itemsets.iterator();
        while(it.hasNext())
        {
           System.out.print("{");
           for(int i : it.next()) {
                System.out.print(""+i+",");
           }
           System.out.println("}");
        }*/
    }
            
    /**
     * if m is the size of the current itemsets,
     * generate all possible itemsets of size n+1 from pairs of current itemsets
     * replaces the itemsets of itemsets by the new ones
     */
    private void createNewItemsetsFromPreviousOnes()
    {
        // by construction, all existing itemsets have the same size
        int currentSizeOfItemsets = itemsets.get(0).length;
        log("Creating itemsets of size "+(currentSizeOfItemsets+1)+" based on "+itemsets.size()+" itemsets of size "+currentSizeOfItemsets);
            
        HashMap<String, int[]> tempCandidates = new HashMap<String, int[]>(); //temporary candidates
        
        // compare each pair of itemsets of size n-1
        for(int i=0; i<itemsets.size(); i++)
        {
            for(int j=i+1; j<itemsets.size(); j++)
            {
                int[] X = itemsets.get(i);
                int[] Y = itemsets.get(j);

                assert (X.length==Y.length);
                
                //make a string of the first n-2 tokens of the strings
                int [] newCand = new int[currentSizeOfItemsets+1];
                for(int s=0; s<newCand.length-1; s++) {
                    newCand[s] = X[s];
                }
                    
                int ndifferent = 0;
                // then we find the missing value
                for(int s1=0; s1<Y.length; s1++)
                {
                    boolean found = false;
                    // is Y[s1] in X?
                    for(int s2=0; s2<X.length; s2++) {
                        if (X[s2]==Y[s1]) { 
                            found = true;
                            break;
                        }
                    }
                    if (!found){ // Y[s1] is not in X
                        ndifferent++;
                        // we put the missing value at the end of newCand
                        newCand[newCand.length -1] = Y[s1];
                    }
                
                }
                
                // we have to find at least 1 different, otherwise it means that we have two times the same set in the existing candidates
                assert(ndifferent>0);
                
                
                if (ndifferent==1) {
                    // HashMap does not have the correct "equals" for int[] :-(
                    // I have to create the hash myself using a String :-(
                    // I use Arrays.toString to reuse equals and hashcode of String
                    Arrays.sort(newCand);
                    tempCandidates.put(Arrays.toString(newCand),newCand);
                }
            }
        }
        
        //set the new itemsets
        itemsets = new ArrayList<int[]>(tempCandidates.values());
        log("Created "+itemsets.size()+" unique itemsets of size "+(currentSizeOfItemsets+1));

    }



    /** put "true" in trans[i] if the integer i is in line */
    private void line2booleanArray(String line, boolean[][] trans) {
        for (int a=0;a<trans.length;a++){
            for(int b=0;b<2;b++){
                    trans[a][b]=false;
            }
        }
        StringTokenizer stFile = new StringTokenizer(line, " "); //read a line from the file to the tokenizer
        //put the contents of that line into the transaction array
        classLabel = Integer.parseInt(stFile.nextToken());
		while (stFile.hasMoreTokens())
        {           
            int parsedVal = Integer.parseInt(stFile.nextToken());
            trans[parsedVal][classLabel-1]=true; //if it is not a 0, assign the value to true
        }
    }
   private void line2booleanArray1(String line, boolean[] trans) {
        for (int a=0;a<trans.length;a++){
                     trans[a]=false;
        }
        StringTokenizer stFile = new StringTokenizer(line, " "); //read a line from the file to the tokenizer
        //put the contents of that line into the transaction array
        classLabel = Integer.parseInt(stFile.nextToken());
        while (stFile.hasMoreTokens())
        {           
            int parsedVal = Integer.parseInt(stFile.nextToken());
            trans[parsedVal]=true;
 // /           log("trans["+parsedVal+"]"+ "="+ trans[parsedVal]); //if it is not a 0, assign the value to true
        }
    }

    
    /** passes through the data to measure the frequency of sets in {@link itemsets},
     *  then filters thoses who are under the minimum support (minSup)
     */
    private void calculateFrequentItemsets() throws Exception
    {
        
        log("Passing through the data to compute the frequency of " + itemsets.size()+ " itemsets of size "+itemsets.get(0).length);

        List<int[]> frequentCandidates = new ArrayList<int[]>(); //the frequent candidates for the current itemset

        boolean match; //whether the transaction has all the items in an itemset
        count = new int[itemsets.size()][2]; //the number of successful matches, initialized by zeros
        class_label = new int[itemsets.size()];

        // load the transaction file
        BufferedReader data_in = new BufferedReader(new InputStreamReader(new FileInputStream(transaFile)));

        boolean[][] trans = new boolean[numItems+1][2];        
        
        // for each transaction
        for (int i = 0; i < numTransactions; i++) {

            // boolean[] trans = extractEncoding1(data_in.readLine());
            String line = data_in.readLine();
            line2booleanArray(line, trans);
            /*if(i==1){for (int a=0;a<numItems;a++){
                for(int b=0;b<2;b++){
                    log(""+trans[a][b]);
                }
            }}*/

            // check each candidate
            for (int c = 0; c < itemsets.size(); c++) {
                match = true; 
                int[] cand = itemsets.get(c);

                //int[] cand = candidatesOptimized[c];
                // check each item in the itemset to see if it is present in the
                // transaction
         

                for (int xx : cand ) {
                 match = match&&trans[xx][classLabel-1];
                }
                if (match) {
                    count[c][classLabel-1]++;
//                    log(Arrays.toString(cand)+" is contained in trans "+i+" ("+line+")"+match);
                }
            }

        }
        
        data_in.close();
        int j=0,set=0,k=0;
        for (int i = 0; i < itemsets.size(); i++) {
            // if the count% is larger than the minSup%, add to the candidate to
            // the frequent candidates
			//log("itemsets size :"+itemsets.size());
            if ((count[i][0] / (double) (numTransactions)) >= minSup) {
                classLabel = 1;
                foundFrequentItemSet(itemsets.get(i),count[i][0], j);
                frequentCandidates.add(itemsets.get(i));
                counttemp[k][0] = count[i][0];
				set=1;
				j++;
            }
            if ((count[i][1] / (double) (numTransactions)) >= minSup) {
                classLabel=2;
                foundFrequentItemSet(itemsets.get(i),count[i][1], j);
                frequentCandidates.add(itemsets.get(i));
                counttemp[k][1] = count[i][1];
				set=1;
				j++;
            }
			if(set==1){
				k++;
				set=0;
			}
            //else log("-- Remove candidate: "+ Arrays.toString(candidates.get(i)) + "  is: "+ ((count[i] / (double) numTransactions)));
        }

        //new candidates are only the frequent candidates
		if(frequentCandidates.size()>0)
		{
			store_itemsets.clear();
			for(int a=0;a<frequentCandidates.size();a++) {
				store_itemsets.add(frequentCandidates.get(a));
			}
			//store_itemsets = new ArrayList(frequentCandidates);
			log("FRequentr"+Arrays.toString(frequentCandidates.get(0)));
			for(int a=0;a<class_label.length;a++) {
				class_labels[a] = class_label[a];
			}
			System.arraycopy(class_label,0,class_labels,0,class_label.length);
			/*for(int a=0;a<counttemp.length;a++) {
				log(""+counttemp[a][0]+"\t"+counttemp[a][1]);
			}*/
		}
        itemsets = frequentCandidates;
       // log("itemsets"+itemsets.size());
    }

}
