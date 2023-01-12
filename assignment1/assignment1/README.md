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
```

- `report`文件夹中为执行完如上操作后得到的统计结果，可供参考，或重新执行如上操作可得到相同结果。
