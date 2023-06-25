# 知识体系 - 人工智能
人工智能
  机器学习
    深度学习（现代神经网络）
    增强学习
## 体系

[技术体系](https://zhuanlan.zhihu.com/p/27920278)
[算法手抄](https://github.com/FavioVazquez/ds-cheatsheets)
深度学习做cv和nlp，传统机器学习做数据分析
[机器算法分类](https://machinelearningmastery.com/a-tour-of-machine-learning-algorithms/)
[](res/ai_map.png)
```
+-----------------------------------------------------------------------------------------------------------+
| Application                                                                                               |
|             Network/mobile transport and logistics robot/simulate/entertainment                           |
|             agriculture/education/medical treatment/finance/       Art/writing/translation/lawyer         |
+-----------------------------------------------------------------------------------------------------------+
|            recommend system                                                                               |
|                 Collaborative filtering                                                                   |
|                 latent factor model                                                                       |
+-----------------------------------------------------------------------------------------------------------+
| architecture                                  +-----------------------------------------------------------+
|                                               |Framework                                                  |
|                                               |       Tensorflow  ,keras                                  |
|                                               +-----------------------------------------------------------+
|                                               |lib                                                        |
|                                               |      scikit-learn                                         |
+--------+----------+---------------------------+-----------------------------------------------------------+
|        |          |  Others           Reinforcement_Learning      NLP  CV                                 |
|        |          |                   Feature_selection    Recommender Systems ...                        |
|      |(Similarity)| Deep Learning     CNN   RNNs DBN LSTMs  Stacked_Auto+Encoders  DBM                    |
|        |          |   (modern ANN)                                                                        |
|        |          | Ensemble          Boosting   Bagging  AdaBoost  Blending                  +-----------+
|        |          |                   Stacking   GBM     GBRT Random_Forest XGBoost           |           |
|        |          | Dimensionality    PCA   PCR  PLSR   Sammon_Mapping MDS  MDA    QDA FDA    |           |
|        |          |  Reduction        Projection_Pursuit  LDA                                 |           |
|        |          |                                                                           |           |MachineLearningAlgorithms
|ML      |          | artificial                                                               |           |
|Algorithms|        |   neural network  Back+Propagation Perceptron MLP Hopfield_Network        |           |
|        |          |   (classical ANN) Stochastic_Gradient_Descent      RBFN                    |           |
|        |          | Association Rule  Apriori  Eclat                                          |           |
|        |          |  Learning                                                                 | estimation|
|        |          | clustering       k+Means   k+Medians     EM    Hierarchical_Clustering    |  method   |    
|        |          | Bayesian         Naive_Bayes Gaussian_Naive_Bayes Multinomial_Naive_Bayes |           |
|        |          |                   AODE    BBN   BN                                        |           |    
|        |          | Decision Tree     CART  ID3   C4.5/C5.0   CHAID  Decision_Stump  M5       |           |
|        |          |                   Conditional_Decision_Trees                              | feature   |
|        |          | Regularization    Ridge LASSO    Elastic_Net    LARS                      |engineering|
|        |          | Instance+based    kNN   LVQ   SOM      LWL    SVM                         |           |
|        |          | regression        Linear    Logistic    Stepwise  OLSR   MARS   LOESS     |           |
|        |          |                                                                           |           |
|        +----------+--------------------------+------------------------------------------------------------|
|        | (learning| ⭐supervised  Learning   | Logistic Regression, Back Propagation Neural Network       |
|        |    style)| Semi-Supervised learning  | regression,  classification (image classification), Apriori|
|        |          | Transfer   learning      |                                                            |
|        |          | unsupervisedlearning     |      K+Means                                              |
|        |          | Reforcement Learning     |                                                            |
+--------+----------+--------------------------+------------------------------------------------------------|
| computer           programming language             distributed system                                    |
| technology                                          operating system                                      |
|                    Algorithmics(Hashing )           the principle of computers                            |
+-----------------------------------------------------------------------------------------------------------|
| mathematical       information theory                                                                     |
| modeling           probability and statistics             game theory                                     |
|                    calculous                                                                              |
|                    GraphTheory(combinatorics)             linear algebra                                  |
|                    Set theory                                                                             |
+-----------------------------------------------------------------------------------------------------------+

```

微积分，推荐系统需要对点击分布函数做大量的统计分析，分析长尾分布肯定要用到微积分。
离散数学，各种推荐内容的排列组合不就是离散数学的内容么。
线性代数，几乎所有的聚类算法都大量使用线性代数，比如在K-Means算法就用到一大堆矩阵运算。
概率论，著名的分词算法HMM就是一个条件概率模型，搜索系统里有多少地方要用分词就不用我多说了吧。


[作者：徐辰 ](https://www.zhihu.com/question/20572279/answer/15531917)


[各种算法和理论用到的数学知识](https://blog.csdn.net/SIGAI_CSDN/article/details/80733205https://blog.csdn.net/SIGAI_CSDN/article/details/80733205)
## 数学
《离散数学分册一-数理逻辑》
《离散数学分册二-集合与图论》
《离散数学分册三-代数结构与组合数学》
## 算法基础
[算法基础](https://blog.csdn.net/zhuxiaoping54532/article/details/53419040)
概率论与数理统计，矩阵分析，最优化理论，凸优化，数学分析，泛函分析等等，是人工智能科学必学的数学基础学科。
概率论、随机过程、数理统计构成了概率理论，为人工智能处理各种不确定问题奠定了基础。
人工智能学科诞生的时候，在概率论的基础上，出现了条件概率及贝叶斯定理，奠定了大多数人工智能系统中不确定推理的现代方法基础。
贝叶斯在人工智能领域的应用主要包括故障诊断，系统可靠性分析，航空交通管理，车辆类型分类等。
概率与统计推断、矩阵、凸优化三个方面来详述相关需要用到数学知识

支持向量机是人工智能的主要分类方法之一，其数学基础为核函数。
可计算理论是人工智能的重要理论基础和工具，为了回答是否存在不可判定的问题，数理逻辑学家提出了关于算法的定义（把一般数学推理形式化为逻辑演绎）。
### 函数
⭐拟合 Regression 得到最优解， 函数问题
     线性回归激活函数 y = mx + c 
     逻辑回归激活函数 sigmoid 
     Tanh激活函数 tanh(x)=2*sigmoid(2x)-1
⭐分类 Classification， 概率问题
聚类 clustering
异常检测 anomaly detection
⭐神经网络     
  ReLu(x)=max(0,x)
  正则化 避免过度拟合    
    ⭐卷积神经网络 CNN 空间相关(卷积层 conv,含很多滤波器 ;激活层 RELU; 采样层 POOL; 全链接层 FC)
    系综方法
    多任务学习
    半监督学习
    生成式对抗网络
  ⭐递归神经网络RNN 优化时间相关
    LSTM/GRU 优化梯度弥散和梯度爆炸
⭐增强学习 RL ,参数的量级限制了深度神经网络
### 机器学习
### 机器学习-ANN（DL）/GA
### 机器学习-监督学习-KNN
欧式距离
### 机器学习-深度学习（人工神经网络）
基础：统计学，生物学

## Python机器学习库sklearn



## [吴恩达](https://study.163.com/course/courseLearn.htm?courseId=1004570029#/learn/video?lessonId=1049050791&courseId=1004570029)

### 概念：
监督学习：回归任务，归类任务
非监督学习：聚类任务

octave

### 单变量线性回归 
模型
回归任务：预测离散值的输出
训练集，输入/特性，输出/target
假设函数h
线性，变量为一次的方程
#### 代价函数J：平方误差函数，平方误差代价函数

#### 梯度下降
a 学习率。梯度下降的速率
导数，微积分，偏导数

代价函数-凸函数，有一个最优解

Batch梯度下降

### 矩阵和向量

几维向量

矩阵运算与乘法特性
单位矩阵

逆与转置
方阵

### 多变量线性回归

特征缩放
多项式
正规方程（矩阵的相关运算）

### octave

### 逻辑回归
分类问题
概率

优化算法
- 梯度下降
- 共轭梯度法
- BFGS
- L-BFGS


多元分类

### 正则化

过度拟合

正规化线性回归


### 神经网络学习

非线性归类
- 特征向量多，如图片像素点

分类器

### 神经网络参数的反向传播

反向传播算法
梯度检测
随机初始化

应用：无人驾驶（先认为驾驶训练机器，再自动驾驶）

### 学习建议
模型选择，训练集，验证集，测试集

诊断偏差与方差

### 系统涉及
误差分析

### 支持向量机（大间距分类器）
非线性
大间距分类器的数学原理
核函数

### 非监督学习
为加标签的数据
k-means算法
优化目标函数
k均值法
肘部法则

### 无监督 - 降维
数据压缩


### 非监督 - 异常检测

### 应用 - 推荐系统

### 应用 - 学习大数据
 

### 应用 - 照片OCR



## NumPy
[quickly start](https://numpy.org/doc/stable/user/quickstart.html)


```

  001_scipy    f1a2d6376c430f65550efa235209b86c1a0967e3 factored out the scipy specific distutils code into a scipy_distutils package.
* 020_v0.2.0   e8c50dc6676e380e814c77927445ab3594382bc4 This commit was manufactured by cvs2svn to create tag 'release_0_2_0'.

                +------------------------------------------------------------------------+
                |   [weave]                                                              |
                |                                                                        |
                |                         __init__.py                                    |
                |                                __all__                                 |
                +------------------------------------------------------------------------+

  022_v0.2.2   d8e22e7e6479aa38b62c53f14ebaf98328131061 Prepearing to tag scipy tree (unified micro version numbers)
  030_v0.3.0   3cb02b56a10d3fe8cf6a7ae7b36e9a2f52661c2e Impl. a way to ignore software in the system, e.g. set ATLAS=None to pretend that Atlas libraries are not availiable.
  032_v0.3.2   957cf7970b0d2b2c600ae6459fb45db8ef498de1 When specifying env. var. then don't search elsewhere.
  042_v0.4.2b1 ac4db180f8cab1d302e699426a7257ec6b0df426 Merged changes from newcore
  092_v0.9.2   cbec6098bf702e19d2923d15d420e9410d94bf5c Removed svn for release.
  101_v1.0.b1  6dd69530d58ba65f4a37167ddec7c9cf19e44c18 Remove svn number from 1.0b1 version
                +------------------------------------------------------------------------------------------------+
                |   numpy.array():ndarray//numpy\core\src\arrayobject.c:PyArrayObject                            |
                +------------------------------------------------------------------------------------------------+
                |   [numpy]                                                                                      |
                |                       _import_tools.py                                                         |
                |                              __all__      //from numpy import array as narray                  |
                +-------------------------+----------------------+-----------+-------------+---------------------+
                |[core]                   |  [lib]               |  [dft]    |  [linalg]   |   [random]          |
                |   __init__.py info.py   |  __init__.py info.py |           |             |                     |
                |                         |                      |           |             |                     |
                +------------------------------------------------------------------------------------------------+
                |  [numeric.py]           | [type_check]         |           |             |                     |
                |                         | [index_tricks]       |           |             |                     |
                |  [defmatrix.py]         | [function_base]      |           |             |                     |
                |                         | [shape_base]         |           |             |                     |
                |  [defmatrix.py]         | [twodim_base]        |           |             |                     |
                |                         | [ufunclike]          |           |             |                     |
                +-------------------------+----------------------------------+-------------+---------------------+

  104_v1.0.b4  64be3e2a89c72a8b23ad11b3719bd66725658c61 Undoing changes mistakenly made on tag 1.0b4

```
封装数据类型 narray

## pandas
[getting_started（install）](https://pandas.pydata.org/getting_started.html) 
[getting_started (10min)   ](https://pandas.pydata.org/docs/getting_started/10min.html)
[《用于数据分析的 Python： 通过 Pandas、NumPy 和 IPython 进行数据整理》/《 Python for Data Analysis》]()
```
  001_initial 9d0080576446de475d34b0dbb58389b15cd4f529 Initial directory structure.
* 002_svn     c6b236db73ff81007909be6406f0e484edc4a9eb first commit with cleaned up code

            +----------------------------------------------------------------------------+
            |                frame.DataFrame                                             |
            |                         data:Series                                        |
            |                         index:Index                                        |
            +----------------------------------------------------------------------------+
            |                        //numpy.ndarray.view(this)                          |
            +----------------------------------------------------------------------------+
            |              series.Series                       index.Index               |
            |                                                      ndim:index            |
            |                                                      indexMap              |
            |                                                      _allDates             |
            |                                                                            |
            +----------------------------------------------------------------------------+
  030_v0.3.0  4906ee79d730f77be02ec586ec7f66c95a0433da version 0.3.0
  100_v1.0.0  d3f08566a80239a18a813ebda9a2ebb0368b1dc5 RLS: 1.0.0rc0

```
封装数据类型 Series

## Matplotlib

[Usage Guide](https://matplotlib.org/tutorials/introductory/usage.html#sphx-glr-tutorials-introductory-usage-py)
[支持的图表](https://matplotlib.org/tutorials/introductory/sample_plots.html#sphx-glr-tutorials-introductory-sample-plots-py)

```
  001_initial     48111d043ec52f9afb511ac447438877b236e7f3 The new matplotlib hierarchy
  002_example     967cb7017afc868557e1c504bc398f54cac08b42 adding examples
  003_gd2gtk      a9672eaa04773a1ca9e4d1b6879355d0fc6cce42 added gtkgd ext and gd win32 files
  004_agg         29dfcd2684c42ea0fa0634ead3878e911812ace9 finished agg backend
  004_agg_gcface  a1e3dba532b73669c6a6d0496ed505c84f807efb added agg backend, changed facecolor to gcface
  005_freetype2   a8b1850d2ad72a54f88cf88361c7c45b85805fa0 added freetype2 to agg
  006_TkAgg       f4be38cdaa7801496f84a9d9f2d32e082966e805 Added files for TkAgg backend.  Currently linux only.
  007_image       d0f4115f28cfe8f991faff313fa24b6c752d12a2 added basic image support to agg
  008_mpl         eeba06b04debdc244822757a5fbfa06774a25e02 added mpl src
  009_pfa         d129c38409ee728d7471b61a322337b6dbe3f24e Added the corresponding .pfa files for the Vera and CM TTF files for embedding in PS files.
  011_pylab       f210efb0aff44a108239496c877aefe6b7cab2e5 migrated matlab to pylab and more
  012_boilerplate ac7e2c0931e5fa8f78063f0c3a216d8ec6d9e47d added boilerplate generating code
  012_contour     f9b6b9597ce95cb2572cc0db321866588b498fdb added Nadia's contouring code
  014_enthought   456f33b8d6ec09e154a8a2d388c5cd056d41fb6c added enthought traits
  014_qt          3e315fbf6c1f45f4fce611f16f6780717d04b20a added refactored qt backend
  016_traits_ui   89ae6cd3a96d3fa748c90062c9721569f2458739 added traits null ui
  017_swig        307a75396b1d9b21232c2d8d555d23c1a15062c5 added initial swig wrapper
  018_agg23       854069db08ce77095166f5a42d70f9af3b6ad978 upgraded to agg23
  018_nib         ef8e81e4c3bae07be655abed57da36b3b9933dab Initial import of CocoaAgg. The Matplotlib.nib bundle is the gui as built in Interface Builder. The backend directory seemed the most logical place to put the nib. Edited setup.py to install the nib file only if on a mac.
* 091_v0.91.2     eb09eed0f9386609f2bd375ca57cac761b338f64 minor rev bump

                +----------------------------------------------------+-----------------------+
                |                                                    |                       |
                |           pyplot.py                                | image.py              |
                |              title()       annotate()              |     imread()          |
                |              matshow()                             |                       |
                |              ylabel():str                          |                       |
                |              text()                                |                       |
                |              grid()                                |                       |
                |                                                    |                       |
                |                                                    |                       |
                |     figure()                     plot():axes.py    |                       |
                |     subplots():                  bar()             |                       |
                |                                  scatter()         |                       |
                |                                  hist()            |                       |
                |                                                    |                       |
                +----------------------------------------------------+                       |
                |    Figure              axes.py                     |           AxesImage   |
                |       add_subplot()      plot()                    |                       | 
                +----------------------------------------------------------------------------+ 
* 300_v3.0.0rc2   b6563c09ce8c22d013179a101fde8f9229d7f1fd REL: v3.0.0rc2
```


### Seaborn
[介绍](http://seaborn.pydata.org/introduction.html)
```
  001_initial 36dd99374a1d6857af7652a9fb832a712a066e16 Initial commit
  010_v0.1    27afe0120138b8e97e44608d46448c84dbf497ef More README/setup changes
* 090_v0.9.0  3eb463a2cd32db196b6e71d1f72d64057804ed76 Bump version for v0.9.0 release
      +------------------------------------------------------------------------------+
      |  [seaborn]                                                                   |
      |                                                                              |
      |                                                                              |
      |      __init__.py           utils.py                 relational.py            |
      |         set()                load_dataset()           relplot()              |
      |                                                       scatterplot()          |
      |                                                     regression.py            |
      |                                                        lmplot()              |
      |                                                                              |
      |                                                     categorical.py           |
      |                                                        catplot()             |
      |                                                        boxplot()             |
      |                                                                              |
      |                                                                              |
      |                                                     axisgrid.py              |
      |                                                         jointplot()          |
      |                                                         pairplot()：PairGrid |
      +------------------------------------------------------------------------------+

```

### Sk-learn
[An introduction to machine learning with scikit-learn](https://scikit-learn.org/stable/tutorial/basic/tutorial.html)
```
  001_initial  92fbeab6ad94b6683377e26e8af7dd1cb86d615e Initial repository layout
  010_0.1-beta e6989efd71a2adddd03979d1fe7a2e82e37ea51f 0.1 beta release
                +---------------------------------------------------------------------------+
                |               [datasets]                [machine.svm]                     |
                |                   [iris.data.py]                                          |
                |                          load()                                           |
                +---------------------------------------------------------------------------+

* 090_0.9      3f1ea662ee1b1b08cee63cc31e4e3e36ec532208 Comment out doctest for fetch_mldata


```

### TensorFlow
[](https://tensorflow.google.cn/tutorials)
[keras 介绍](https://tensorflow.google.cn/guide/keras/overview)
[quickstart](https://tensorflow.google.cn/tutorials/quickstart/beginner)
```

```




### GPT
#### 数据推荐
《人工智能简史》《深度学习入门》
《深入浅出神经网络与深度学习》OpenAI 总裁兼董事长 Greg Brockman 的 AI 入门读物


问GPT结果
1.《Deep Learning》（深度学习）：这是一本经典的深度学习入门书籍，介绍了深度学习的基本概念、算法和应用，并详细介绍了神经网络、卷积神经网络和循环神经网络等基础模型。这本书可以帮助您建立深度学习的基础知识，为理解 GPT 模型打下基础。
2.《Attention Is All You Need》（注意力机制全文翻译）：这是一篇经典的论文，提出了使用纯注意力机制实现序列到序列学习的 Transformer 模型。GPT 模型就是基于 Transformer 模型进行改进而来的，因此了解 Transformer 模型的原理和基础知识对于理解 GPT 模型非常重要。
3.《The Illustrated Transformer》（可视化 Transformer 模型）：这是一篇可视化的 Transformer 模型教程，用图表和动画的方式解释了 Transformer 模型的原理和工作流程。这篇教程可以帮助您更直观地理解 Transformer 模型，为理解 GPT 模型提供帮助。
4.《GPT Explained: A Complete Guide to OpenAI's Language Model》（GPT 解析：OpenAI 语言模型完全指南）：这是一篇关于 GPT 模型的详细解析文章，介绍了 GPT 模型的原理、训练和应用等内容。这篇文章可以帮助您全面了解 GPT 模型，并了解如何在实际应用中使用 GPT 模型。
5.《Hands-On Machine Learning with Scikit-Learn and TensorFlow》
6. "神经网络方法在自然语言处理中的应用" 作者：Yoav Goldberg - 这本书提供了自然语言处理技术的概述，包括GPT中使用的Transformer模型。
7. "BERT：深度双向变压器的语言理解预训练" 作者：Devlin 等人 - 尽管这篇论文主要关注BERT模型，但它解释了预训练transformer的概念，这也适用于GPT。 


#### LLM
https://www.explainthis.io/zh-hans/ai-toolkit
https://www.aihub.cn/tools/chatbot/
https://www.askopenai.co/
#### ChatGpt
[wrtn](https://wrtn.ai/)
[perplexity](https://www.perplexity.ai)

https://poe.com/ChatGPT
https://unlimitedgpt.co/


