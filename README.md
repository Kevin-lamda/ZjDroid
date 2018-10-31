# ZjDroid
[![release](https://img.shields.io/badge/download-v1.0.2(3)-brightgreen.svg)](https://github.com/KB5201314/ZjDroid/releases)

凑齐了ZjDroid的源码，并在原版的基础上进行了一些改进。

# 有什么不同？
1. 通过源码可以编译出多个平台的so文件，使得在Android x86虚拟机上脱壳成为可能。
2. 修改源码，解决了原来的版本**拖出来之后拿到的是壳子的问题**（原版原理上其实是没有问题的，就是一个小细节没有处理好）。\
详细原因请看我的文章：\
[https://blog.csdn.net/u010746456/article/details/80150250](https://blog.csdn.net/u010746456/article/details/80150250)
3. **增加dexinfo时显示mCookie**。
4. **dump时使用mCookie作为参数。（dumpdex文件等操作都以mCookie这串数字作为唯一标识）**
5. **增加了对art虚拟机上dump dexfile的支持**

# 局限（TODO）
- [x] ~**只能在dalvik虚拟机上使用（Android4.4及以下）**~
- [ ] [**非常有用的功能**] **监控应用敏感行为**模块 不支持高AndroidSDK版本的API，需要修复(简单但是繁琐)\
相关类所在项目文件夹：\
[https://github.com/KB5201314/ZjDroid/tree/master/app/src/main/java/com/android/reverse/apimonitor](https://github.com/KB5201314/ZjDroid/tree/master/app/src/main/java/com/android/reverse/apimonitor)
- [ ] **动态反编译指定DEX**模块 由于使用的库版本过旧，不支持ART虚拟机，需要换用新版库，但是大部分接口已改，需要时间去学习新版库的使用
- [ ] [**非常有用的功能**] 增加**反Xposed检测**
- [ ] [**非常有用的功能**] 增加**反模拟器检测**
- [ ] [**调试方便**] 增加**查找指定类所在的dex文件**
- [ ] **支持hook多ClassLoader**
- [ ] **动态hook**

# 用法
**除了将索引dex文件的方式改为用mCookie,在用法上基本和原版无差别**

1.获取APK当前加载DEX文件信息：
```
adb shell am broadcast -a com.zjdroid.invoke --ei target <target-pid> --es cmd '{"action":"dump_dexinfo"}'
```
    结果示例：
    06-30 21:22:59.211 20298 23084 E zjdroid-shell-com.browser2345: filepath:/data/app/com.browser2345-1/base.apk dexElementToString:dex file "/data/app/com.browser2345-1/base.apk" mCookie:3013523232
    分3个部分：filepath 文件路径
              dexElementToString 文件路径及相关信息（描述这是个dex文件还是zip文件）
              mCookie 关键参数，后面用到

2.获取指定mCookie对应的DEX文件包含的可加载类名：
```
adb shell am broadcast -a com.zjdroid.invoke --ei target <target-pid> --es cmd '{"action":"dump_class","mCookie":"*****"}'
```
3.根据Dalvik相关内存指针动态反编译指定DEX，并以文件形式保存。
```
adb shell am broadcast -a com.zjdroid.invoke --ei target <target-pid> --es cmd '{"action":"backsmali","mCookie":"*****"}'
```
4.Dump指定DEX内存中的数据并保存到文件（数据为odex格式，可在pc上反编译）。
```
adb shell am broadcast -a com.zjdroid.invoke --ei target <target-pid> --es cmd '{"action":"dump_dexfile","mCookie":"*****"}'
```
5.Dump指定内存空间区域数据到文件。
```
adb shell am broadcast -a com.zjdroid.invoke --ei target <target-pid> --es cmd '{"action":"dump_mem","start":1234567,"length":123}'
```
6.Dump Dalvik堆栈信息到文件，文件可以通过java heap分析工具分析处理。
```
adb shell am broadcast -a com.zjdroid.invoke --ei target <target-pid> --es cmd '{"action":"dump_heap"}'
```
7.运行时动态调用Lua脚本 该功能可以通过Lua脚本动态调用java代码。 使用场景： 可以动态调用解密函数，完成解密。 可以动态触发特定逻辑。
luajava相关使用方法： http://www.keplerproject.org/luajava/
```
adb shell am broadcast -a com.zjdroid.invoke --ei target <target-pid> --es cmd '{"action":"invoke","filepath":"****"}'
```
8.敏感API调用自动监控

# 执行结果查看：

1.命令执行结果： 
```
adb shell logcat -s zjdroid-shell-{package name}
```
2.敏感API调用监控输出结果： 
```
adb shell logcat -s zjdroid-apimonitor-{package name}
```

# 相关开源项目
halfkiss/ZjDroid：\
[https://github.com/halfkiss/ZjDroid](https://github.com/halfkiss/ZjDroid)\
mikusjelly/HeyGirl:\
[https://github.com/mikusjelly/HeyGirl](https://github.com/mikusjelly/HeyGirl)\
mkottman/AndroLua:\
[https://github.com/mkottman/AndroLua](https://github.com/mkottman/AndroLua)

# LICENSE
Apache License, Version 2.0
