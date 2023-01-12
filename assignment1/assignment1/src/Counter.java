//package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Counter {

    // TODO: 需要自己定义合适的成员变量，来记录每一个class中GOTO语句被trigger的次数。
    public static HashMap<String, Integer> gotoCount = new HashMap<>();
    public static String[] classNames = new String[] {"FastMath", "FastMathCalc", "Precision"};

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Counter.generateReport();
            } catch (IOException e) {
                System.err.println("Could not write report to file: " + e);
            }
        }));
    }

    /**
     * reports the goto statement count
     */
    public static synchronized void recordGoto(String name) {
        // TODO: 定义recordGoto函数, 来统计每个类中Goto语句被trigger的次数
        gotoCount.merge(name, 1, Integer::sum);
    }

    /**
     * reports the counter content.
     */
    public static void generateReport() throws IOException {
        // TODO: 将插装统计内容写入文件。please write the results to the file "report/result.txt"
        File dir = new File("report");
        if (!dir.exists())
            dir.mkdir();
        FileWriter fileWriter = new FileWriter("report/result.txt");
        fileWriter.write("ClassName\tNumberOfGotoExecution\n");

        for (String className : classNames) {
            fileWriter.write(className + "\t" + gotoCount.getOrDefault(className, 0) + "\n");
        }

        fileWriter.close();
    }
}
