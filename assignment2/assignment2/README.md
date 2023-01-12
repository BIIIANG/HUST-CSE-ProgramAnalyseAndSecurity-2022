# 使用方法（Windows PowerShell）

```shell
# 1.检查JDK版本，应存在java及javac 1.8.x_xxx
# 若存在java不存在javac，请安装JDK而不是JRE
# 若仍有其他错误，请检查系统环境变量设置
.\script.bat check
# 2.清理已有结果
.\script.bat clean
# 3.编译（可能会出现使用过时API等提示，不出错即可忽略）
.\script.bat compile
# 4.插桩并得到代码数和分支数
.\script.bat instrument
# 5.执行测试程序并得到代码覆盖率
.\script.bat run
# 6.查看统计结果
cat .\report\result.txt
# or
.\report\result.csv
```

- `report`文件夹中为执行完如上操作后得到的统计结果，可供参考，或重新执行如上操作可得到相同结果。

# TODOs

- 为了满足Assignment 2的要求，按照类及函数名进行统计，没有细分至函数签名，类中的同名重载函数的数据相加作为该函数的整体数据；
- 在测试代码编译的过程中，由于编译时常量优化和一些其他优化，一些常量会被内嵌到代码当中，导致常量声明行被忽略，同时一些代码逻辑的实现方式也可能改变，可能给统计结果带来一定误差，如`\assignment2\src\main\org\apache\commons\math3\util\FastMath.java`中的`toRadians()`函数等。