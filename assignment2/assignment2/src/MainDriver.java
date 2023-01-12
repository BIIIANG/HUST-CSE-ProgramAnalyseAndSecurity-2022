//package instrument;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class MainDriver {

    public static void main(String[] args) {
        // mac或linux系统中，';'应被换为':'
        Options.v().set_soot_classpath(Scene.v().defaultClassPath() + ";./bin;bin/main");
        Options.v().set_keep_line_number(true);

        Options.v().set_process_dir(Collections.singletonList("./bin/main/"));
        Options.v().set_output_dir("./sootOutput");
        Options.v().set_hierarchy_dirs(true);
//        Options.v().set_output_dir("./bin/main/org/apache/commons/math3/util");
        // insert a phase to jtp
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.myTransform", new Instrumenter()));
        Scene.v().loadNecessaryClasses();
        PackManager.v().runPacks();
        PackManager.v().writeOutput();
    }
}
