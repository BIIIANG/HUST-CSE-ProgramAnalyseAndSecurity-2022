/**
 * @author: HUST-CSE-XBA
 */

//package instrument;

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import soot.*;
import soot.jimple.*;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

public class Instrumenter extends BodyTransformer {

    // TO DO  add necessary fields
    static HashSet<Integer> lineNumberSet;
    static HashMap<Key<Stmt, Unit>, Integer> branchMap;
    // static int totalStatement;
    static int totalBranch;
    // static HashMap<Key<String, String>, HashSet<Integer>> totalLineNumberMap;
    static SootClass counterClass;
    static SootMethod recordStatementFunc, recordBranchFunc;

    static {
        // totalLineNumberMap = new HashMap<>();
        lineNumberSet = new HashSet<>();
        branchMap = new HashMap<>();
        // totalStatement = 0;
        totalBranch = 0;
        counterClass = Scene.v().loadClassAndSupport("Counter");
        recordStatementFunc = counterClass.getMethod("void recordStatement(java.lang.String,java.lang.String,int)");
        recordBranchFunc = counterClass.getMethod("void recordBranch(java.lang.String,java.lang.String,int)");
    }

    @Override
    protected synchronized void internalTransform(Body body, String s, Map<String, String> map) {
        Chain<Unit> units = body.getUnits();
        String className = body.getMethod().getDeclaringClass().toString();
        String methodName = body.getMethod().getName();
        System.out.println("Instrumenting Method Body: " + body.getMethod().getSignature());

        // TODO: Regardless of the function overloading, we only take care about the class name and method name now.
        lineNumberSet = new HashSet<>();
        // lineNumberSet = totalLineNumberMap.getOrDefault(new Key<>(className, methodName), new HashSet<>());
        totalBranch = 0;

        // Statement coverage.
        Iterator<Unit> stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            if (!(stmt instanceof IdentityStmt)) {
                // Likely equal to stmt.getJavaSourceStartLineNumber();
                int lineNumber = getLineNumber(stmt);
                // TODO: Only care about stmt with new line number in source code.
                if (lineNumber != -1 && !lineNumberSet.contains(lineNumber)) {
                    lineNumberSet.add(lineNumber);
                    // Instrument before each statement.
                    InvokeExpr statementCountExpr = Jimple.v().newStaticInvokeExpr(recordStatementFunc.makeRef(),
                            StringConstant.v(className), StringConstant.v(methodName), IntConstant.v(lineNumber)
                    );
                    Stmt statementCountStmt = Jimple.v().newInvokeStmt(statementCountExpr);
                    units.insertBefore(statementCountStmt, stmt);
                }
            }
        }
        // TODO: Decide whether to add method line in statement coverage.
        // lineNumberSet.add(getLineNumber(body.getMethod()));
        // totalLineNumberMap.put(new Key<>(className, methodName), lineNumberSet);

        // Branch coverage.
        UnitGraph g = new BriefUnitGraph(body);
        stmtIt = units.snapshotIterator();
        while (stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            // IfStmt and SwitchStmt may have multiple successor.
            if (stmt instanceof IfStmt || stmt instanceof TableSwitchStmt || stmt instanceof LookupSwitchStmt) {
                // TODO: Check if current node has multiple successor.
                for (Unit u : g.getSuccsOf(stmt)) {
                    // If a new branch, give it a unique index and instrument at the beginning of the successor.
                    if (!branchMap.containsKey(new Key<>(stmt, u))) {
                        totalBranch++;
                        branchMap.put(new Key<>(stmt, u), branchMap.size());
                        InvokeExpr branchCountExpr = Jimple.v().newStaticInvokeExpr(
                                recordBranchFunc.makeRef(), StringConstant.v(className),
                                StringConstant.v(methodName), IntConstant.v(branchMap.get(new Key<>(stmt, u)))
                        );
                        Stmt branchCountStmt = Jimple.v().newInvokeStmt(branchCountExpr);
                        units.insertBefore(branchCountStmt, u);
                    }
                }
            }
        }

        // Save temporary result to file.
        try {
            File dir = new File("report");
            if (!dir.exists() && dir.mkdir())
                System.out.println("Create directory report successfully.");
            System.out.println("  totalLineNumber: " + lineNumberSet.size() + ";  totalBranch: " + totalBranch + ".");
            FileWriter fileWriter = new FileWriter("report/result.tmp", true);
            fileWriter.write(className + "\t" + methodName + "\t" + lineNumberSet.size() + "\t" + totalBranch + "\n");
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Could not write report to file: " + e);
        }

    }

	// TO DO you can add necessary member functions
    private static int getLineNumber(Unit unit) {
        List<Tag> tags = unit.getTags();
        for (Tag tag : tags) {
            if (tag instanceof LineNumberTag) {
                return ((LineNumberTag) tag).getLineNumber();
            }
        }
        return -1;
    }

    private static int getLineNumber(SootMethod method) {
        List<Tag> methodTags = method.getTags();
        for (Tag tag : methodTags) {
            if (tag instanceof LineNumberTag) {
                return ((LineNumberTag) tag).getLineNumber();
            }
        }
        return -1;
    }
}
