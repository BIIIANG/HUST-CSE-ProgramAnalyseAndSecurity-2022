1，记录了 jimple 代码中的 goto 出现的次数
2，使用 java 1.8
3，待插桩的类和测试样例都在为在 src/ 下
4，FastMath 类只对主类进行了插桩，未对其他内部类插桩
5，输出的报告在 report/result.txt



编译
javac -cp lib/* -d bin src/*
插桩
java -cp "bin;lib/*" MainDriver Precision FastMath FastMathCalc
运行
java -cp "sootOutput;bin/;lib/*"  org.junit.runner.JUnitCore PrecisionTest

[如果是Mac或者Linux系统，请注意-cp参数中的“；”，需要改成“:”]