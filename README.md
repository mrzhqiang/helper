[helper]:https://github.com/mrzhqiang/helper/tree/master/helper
[database]:https://github.com/mrzhqiang/helper/tree/master/database
[oauth2]:https://github.com/mrzhqiang/helper/tree/master/oauth2
[samples]:https://github.com/mrzhqiang/helper/tree/master/samples
[mvnrepository]:https://mvnrepository.com/artifact/com.github.mrzhqiang.helper/helper
[helper in jitpack.io]:https://jitpack.io/#mrzhqiang/helper

# helper
助手有很多通用代码，在此封装成库，不用再拷贝来拷贝去。

[![Release](https://img.shields.io/github/release/mrzhqiang/helper.svg)](https://github.com/mrzhqiang/helper/releases/latest)
[![Release](https://jitpack.io/v/mrzhqiang/helper.svg)](https://jitpack.io/#mrzhqiang/helper)


## 有什么功能？
- datetime：对 `Date` 类进行格式化与解析，以及计算时间间距和友好显示
- random：指定长度与长度区间内随机字符串，包括数字（首字符不为0）、英文（可以区分大小写）、常用汉字、中文姓氏（仅百家姓）
- username：提取首字符，根据昵称生成专属颜色，检查名字是否有效（中英文+数字），检查是否为纯中文，以及可传递正则表达式的字符串检测

## 如何使用？
1. 添加依赖：[helper in jitpack.io] 或 [mvnrepository]（从 `sonatype` 同步略有延迟）。
2. 参考 [samples] 模块（待实现）。

## Licenses
```
   Copyright 2018 mrzhqiang

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
