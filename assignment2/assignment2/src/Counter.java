/**
 * @author: HUST-CSE-XBA
 */


//package src;

import java.io.*;
import java.util.*;

public class Counter {

    static HashMap<Key<String, String>, HashSet<Integer>> coveredLineNumberMap;
    static HashMap<Key<String, String>, HashSet<Integer>> coveredBranchMap;

    static {
        coveredLineNumberMap = new HashMap<>();
        coveredBranchMap = new HashMap<>();
    }

    static {
        // hook addShutdownHook，类似于junit的@AfterClass
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Counter.generateReport();
            } catch (IOException e) {
                System.err.println("Could not write report to file: " + e);
            }
        }));
    }

    public static void generateReport() throws IOException {
        System.out.println("generateReport...");

        // Read result.tmp for totalStatement/totalBranch and merge overloading functions.
        File tmp = new File("report/result.tmp");
        if (!tmp.exists()) {
            System.out.println("No result.tmp!");
            return;
        }
        HashMap<Key<String, String>, Integer> totalLineNumberMap = new HashMap<>(), totalBranchMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader("report/result.tmp"));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            String[] strings = line.split("\t");
            if (strings.length < 4) {
                System.out.println("Read result.tmp error!");
                return;
            }
            totalLineNumberMap.merge(new Key<>(strings[0], strings[1]), Integer.parseInt(strings[2]), Integer::sum);
            totalBranchMap.merge(new Key<>(strings[0], strings[1]), Integer.parseInt(strings[3]), Integer::sum);
        }

        // Sort by class name and method name.
        TreeMap<Key<String, String>, Integer> totalLineNumberTreeMap = new TreeMap<>(new Comparator<Key<String, String>>() {
            @Override
            public int compare(Key<String, String> o1, Key<String, String> o2) {
                return o1.key1.compareTo(o2.key1) != 0 ? o1.key1.compareTo(o2.key1) : o1.key2.compareTo(o2.key2);
            }
        });
        totalLineNumberTreeMap.putAll(totalLineNumberMap);

        // Generate result.txt with format in assignment2.pdf and result.csv with csv format.
        FileWriter fileWriter_txt = new FileWriter("report/result.txt");
        fileWriter_txt.write("MethodName\tCoveredStatement\tTotalStatement\tStatementCoverage\t" +
                "CoveredBranch\tTotalBranch\tBranchCoverage\n");
        FileWriter fileWriter_csv = new FileWriter("report/result.csv");
        fileWriter_csv.write("MethodName,CoveredStatement,TotalStatement,StatementCoverage," +
                "CoveredBranch,TotalBranch,BranchCoverage\n");

        // Write result to 2 files.
        for (Map.Entry<Key<String, String>, Integer> entry : totalLineNumberTreeMap.entrySet()) {
            String methodName = entry.getKey().key1 + "." + entry.getKey().key2;
            int coveredStatement = coveredLineNumberMap.getOrDefault(entry.getKey(), new HashSet<>()).size();
            int totalStatement = entry.getValue();
            double statementCoverage = coveredStatement * 100.0 / totalStatement;
            int coveredBranch = coveredBranchMap.getOrDefault(entry.getKey(), new HashSet<>()).size();
            int totalBranch = totalBranchMap.getOrDefault(entry.getKey(), 0);
            double branchCoverage = coveredBranch * 100.0 / totalBranch;
            fileWriter_txt.write(methodName + "\t" + coveredStatement + "\t" + totalStatement + "\t" +
                    statementCoverage + "%\t" + coveredBranch + "\t" + totalBranch + "\t" + branchCoverage + "%\n");
            fileWriter_csv.write(methodName + "," + coveredStatement + "," + totalStatement + "," +
                    statementCoverage + "%," + coveredBranch + "," + totalBranch + "," + branchCoverage + "%\n");
        }
        fileWriter_txt.close();
        fileWriter_csv.close();
    }

	// Add necessary member functions
    public static void recordStatement(String className, String methodName, int lineNumber) {
        coveredLineNumberMap.computeIfAbsent(new Key<>(className, methodName), k -> new HashSet<>()).add(lineNumber);
    }

    public static void recordBranch(String className, String methodName, int branchIndex) {
        coveredBranchMap.computeIfAbsent(new Key<>(className, methodName), k -> new HashSet<>()).add(branchIndex);
    }
}
