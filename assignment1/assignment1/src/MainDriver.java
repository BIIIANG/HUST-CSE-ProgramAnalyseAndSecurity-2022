//package instrument;

import soot.PackManager;
import soot.Scene;
import soot.Transform;
import soot.options.Options;

import java.util.Arrays;

public class MainDriver {

    public static void main(String[] args) {
        // mac或linux系统中，';'应被换为':'
        Options.v().set_soot_classpath(Scene.v().defaultClassPath() + ";./bin");
        Options.v().set_keep_line_number(true);
        Options.v().set_output_dir("./sootOutput");
        

        // insert a phase to jtp
        PackManager.v().getPack("jtp").add(
                new Transform("jtp.myTransform", new GotoInstrumenter()));

        // these args will be passed into soot.
        String[] sootArgs = Arrays.copyOf(args, args.length);
        soot.Main.main(sootArgs);
    }
}
