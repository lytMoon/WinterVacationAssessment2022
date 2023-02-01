# WinterVacationAssessment2022
红岩网校2022-2023年的寒假考核



## APP的简单介绍（version：1.0）：

这款app简单的实现了从网络上下载小说并以翻页的形式阅读小说，并且可以实现对小说阅读的进度追查（只保留到了特定几位数）

## 功能展示

1.开始时有三秒的打开时间

![1675251457451](https://user-images.githubusercontent.com/117186626/216035184-f9a45263-0618-41b9-81b1-a51c85b46b48.gif)

2.主要

![1675251457437](https://user-images.githubusercontent.com/117186626/216035355-badbb8c8-5c72-43be-9482-4dcb6a90a5a6.gif)


## 技术亮点

1.实现了打开软件的splash界面，通过handler来实现倒计时。

2.利用了okp3.0来实现对网络数据的请求，并使用Gson将网络请求的数据对应封装。

3.利用get的异步请求将数据下载到本地

4.借鉴了网络的自定义view（主要来实现翻页的动画）

5.利用帮助类对象进行相关数据的配置

6.目前已经可以实现了对网络请求得到的书本的打开。并且实现了翻页效果和总进度百分比（不是很精确）

## 心得体会

通过这次任务，让我明白了自学能力的重要性，我们需要在网络上主动地去学习，也要善于请教他人。例如我在完成APP的时候遇到了很多的问题，一个是在我进行网络请求下载文件的时候，网络请求可以正常进行，但是文件却没有下载，因为最新的安卓版本默认给我们制订了文件的下载位置，我在向学长们请教完之后完成了任务。（或者采用更为高级的弹窗）通过这次考核，我觉得我对安卓开发有了新的认识，在写寒假考核期间，我不断的提高自己的耐心和信心，极大提高了自学的能力。有很多的概念由刚上大一的懵懂畏惧变得慢慢接受，有了重新的认识。

## 需要提升的地方

1.书藉的展示和下载界面不够完美

2.看小说的阅读模式功能不够完善

3.没有尝试写自己的自定义view

  

