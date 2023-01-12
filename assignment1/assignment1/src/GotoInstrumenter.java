//package instrument;

import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JGotoStmt;
import soot.util.Chain;

import java.util.Iterator;
import java.util.Map;

public class GotoInstrumenter extends BodyTransformer {
    static SootClass  counterClass;
    static SootMethod recordGoto;


    static {
        // TODO: 注册Counter以及 ecordGoto
        counterClass = Scene.v().loadClassAndSupport("Counter");
        recordGoto = counterClass.getMethod("void recordGoto(java.lang.String)");
    }

    @Override
    protected synchronized void internalTransform(Body body, String s, Map<String, String> map) {
        // TODO: 在合适的位置插装Counter中的代码
        String className = body.getMethod().getDeclaringClass().getName();
        System.out.println("Instrumenting method: " + body.getMethod().getSignature());
        Chain<Unit> units = body.getUnits();

        Iterator<Unit> stmtIt = units.snapshotIterator();
        while(stmtIt.hasNext()) {
            Stmt stmt = (Stmt) stmtIt.next();
            if (stmt instanceof JGotoStmt) {
                InvokeExpr gotoCountExpr = Jimple.v().newStaticInvokeExpr(
                        recordGoto.makeRef(), StringConstant.v(className)
                );
                Stmt gotoStmt = Jimple.v().newInvokeStmt(gotoCountExpr);
                units.insertBefore(gotoStmt, stmt);
            }
        }
    }
}
