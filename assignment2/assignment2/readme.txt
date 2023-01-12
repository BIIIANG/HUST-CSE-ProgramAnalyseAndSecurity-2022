1，记录了 branch coverage
2，使用 java 1.8
3，待插桩的类在为在 src/main 下，测试样例在 src/test 下，插桩代码在 src 下
4，由于 -process-dir 的特性，只能对所有类都进行插桩
5，输出的报告在 report/下

编译(暂时先分别编译)
javac -cp "lib/*;src/main/" -d bin/main src\main\org\apache\commons\math3\util\*
javac -cp "lib/*;src/" -d bin/ src\*.java
javac -cp "lib/*;src/main/;src/test/" -d bin/test src\test\org\apache\commons\math3\util\*

插桩(-process-dir 必须使用 bin/main/，否则将无法保留package信息，故对所有类都进行了插桩)
java -cp "bin;lib/*" MainDriver

运行
java -cp "sootOutput;lib/*;bin/main;bin/test;bin" -DtestSuiteName="FastMathTest" org.junit.runner.JUnitCore org.apache.commons.math3.util.FastMathTest org.apache.commons.math3.util.PrecisionTest org.apache.commons.math3.util.MathArraysTest
