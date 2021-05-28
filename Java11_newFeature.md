# Java的版本演进

据国外网站2021-2-23发布的数据，在主要应用程序中使用Java 8作为编程语言的比例为69％。JavaScript紧随其后，占40％的受访者，比去年的报告大幅增加，只有2％的开发人员使用JavaScript。其次是Java 11（占36％）和Java 12或更高版本（占16％）。 Java8和Java11是LTS（Long-term support）版本，所以使用比例高。2021年9月最新的LTS版本Java 17就会release了。[[1]](https://www.163.com/dy/article/G809HV5S05371X3U.html)    [[2]](https://jaxenter.com/java-development-2021-173870.html)

![image-20210525201932739](https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F0420%2Fb8635759p00qrtp3y002hc000p100hsc.png&thumbnail=650x2147483647&quality=80&type=jpg)

大多数使用Oracle Java的企业（占50％），今年也不例外，其中使用Oracle Java的企业占59％。OpenJDK各大云厂商都有自己的发行版本，比如国内的腾讯、华为、阿里以及国外比较流行的RedHatOpenJDK、AdoptOpenJDK等等，所以完全不用为Oracle JDK 8 u191以后的版本收费担心。



![image-20210525201932739](https://nimg.ws.126.net/?url=http%3A%2F%2Fdingyue.ws.126.net%2F2021%2F0420%2F0b262e3bp00qrtp3z002cc000ou00h2c.png&thumbnail=650x2147483647&quality=80&type=jpg)

　　

# JAVA11的新特性

## 使用层面

### HTTP Client新用法

详细的例子可以参考 [[3] Java 11 reactive http client](https://downloads.ctfassets.net/oxjq45e8ilak/7frKPLnqmYownvOw4i8SBe/e8a6807ff431812953046e7723c54970/Chris_Hegarty_Java_11_reactive_HTTP_Client.pdf)

Java 11 对 Http Client API 进行了标准化，在前两个版本中进行孵化的同时，Http Client 几乎被完全重写，并且现在完全支持异步非阻塞。改动点如下：

1) 包名由 jdk.incubator.http 改为 java.net.http

2) 提供了对 HTTP/2 等业界前沿标准的支持，同时也向下兼容 HTTP/1.1，精简而又友好的 API 接口，与主流开源 API（如：Apache HttpClient、Jetty、OkHttp 等）类似甚至拥有更高的性能 ：以下是一个同步get请求的示例，可以看到api特别的简洁

```java
HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .version(HttpClient.Version.HTTP_2)//还可以选择HTTP_1_1
            .uri(URI.create(url))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response" + response.body());
    }
```



3) 它是 Java 在 Reactive-Stream 方面的第一个生产实践，其中广泛使用了 Java Flow API

4) 完成支持异步非阻塞：sendAsync方法返回CompletableFuture<HttpResponse>

```java

String url = "http://openjdk.java.net/";

HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
  .uri(URI.create(url))
  .build();
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
  .thenApply(HttpResponse::body)
  .thenAccept(System.out::println)
  .join();

```

### 局部变量类型可以使用var

从 Java 10 开始，便引入了局部变量类型推断这一关键特性。类型推断允许使用关键字 var 作为局部变量的类型而不是实际类型，编译器根据分配给变量的值推断出类型。这一改进简化了代码编写、节省了开发者的工作时间，因为不再需要显式声明局部变量的类型，而是可以使用关键字 var，且不会使源代码过于复杂。

Java11种允许lamda表达式中使用var，但是还有以下限制：

- 只能用于局部变量上
- 声明时必须初始化
- 不能用作方法参数

>  一句话就是：必须能够推断出类型的局部变量才能声明为var。

比如，以下代码是OK的：

```java
@Nonnull var foo = new Foo();
// 推断为String
var param = "a";
foo.setA(param);
var a = foo.getA();

(@Nonnull var x, @Nullable var y) -> x.process(y)
  
// 设别为Integer
var intb = 1;
foo.setB(1);


var doubleNumber = 20.5; // 推断为double
var floatNumber = 20.5F; // 推断为float
var productList = new ArrayList<>(); // 推断为ArrayList<Object>
var productList2 = new ArrayList<String>(); // 推断为ArrayList<String>
var numbers = new int[5];
numbers[0] = 1;

final var discount = 5;


Function<Integer, Integer> f = x -> x + 1;
```

但是以下代码会报编译错误，因为编译器无法推断出变量类型：

```java
// lambda表达式需要显式目标类型
// var f = x -> x + 1;

// 无法调用Foo#setC(Float c)
var b = 1; // 识别为int
foo.setC(b);

// 类型错误: 变量初始化为'null'
var message = null; 

class Statudent {
	 // 类的成员变量不可以声明为var
   private var age;
   // 方法参数不可以用var
   public void setAget(var age) {
   		this.age = age;
   }
}
```

## 开发效率

###  简化启动单个源代码文件的方法[3]

Java 11 版本中最令人兴奋的功能之一是增强 Java 启动器，使之能够运行单一文件的 Java 源代码。此功能允许使用 Java 解释器直接执行 Java 源代码。源代码在内存中编译，然后由解释器执行。唯一的约束在于所有相关的类必须定义在同一个 Java 文件中。

此功能对于开始学习 Java 并希望尝试简单程序的人特别有用，并且能与 jshell 一起使用，将成为任何初学者学习语言的一个很好的工具集。不仅初学者会受益，专业人员还可以利用这些工具来探索新的语言更改或尝试未知的 API。

如今单文件程序在编写小实用程序时很常见，特别是脚本语言领域。从中开发者可以省去用 Java 编译程序等不必要工作，以及减少新手的入门障碍。在基于 Java 10 的程序实现中可以通过三种方式启动：

- 作为 * .class 文件
- 作为 * .jar 文件中的主类
- 作为模块中的主类

而在最新的 Java 11 中新增了一个启动方式，即可以在源代码中声明类，例如：如果名为 HelloWorld.java 的文件包含一个名为 HelloWorld 的类，那么该命令：

```shell
$ java HelloWorld.java    
```

也等同于：

```shell
$ javac HelloWorld.java
$ java -cp . hello.World
```

## JVM诊断

### 飞行记录器 Java Flight Recorder(JFR)[[3]](https://downloads.ctfassets.net/oxjq45e8ilak/7frKPLnqmYownvOw4i8SBe/e8a6807ff431812953046e7723c54970/Chris_Hegarty_Java_11_reactive_HTTP_Client.pdf) [[4]](https://dzone.com/articles/using-java-flight-recorder-with-openjdk-11-1) [[5]](https://docs.oracle.com/javacomponents/jmc-5-4/jfr-runtime-guide/about.htm#JFRUH172) [[6]](https://my.oschina.net/u/3747772/blog/3216302)

Java 语言中的飞行记录器类似飞机上的黑盒子，是一种低开销的事件信息收集框架，主要用于对应用程序和 JVM 进行故障检查、分析。飞行记录器记录的主要数据源于应用程序、JVM 和 OS，这些事件信息保存在单独的事件记录文件中，故障发生后，能够从事件记录文件中提取出有用信息对故障进行分析。

**为什么用JFR** 

因为某些异常很难在开发测试阶段发现，需要在生产环境才会出这些问题。为了能在生产问题发生后，更好的定位生产问题，JDK 提供了这样一个可以长期开启，对应用影响很小的持续监控手段。官方说，目标是开启 JFR 监控（默认配置），对性能的影响在1%之内，对JVM Runtime 和 GC，OS 以及 Java 库进行全方位的监控。

JFR，具有以下关键的特性：

- 低开销（在配置正确的情况下），可在生产环境核心业务进程中始终在线运行。当然，也可以随时开启与关闭。
- 可以查看出问题时间段内进行分析，可以分析 Java 应用程序，JVM 内部以及当前Java进程运行环境等多因素。
- JFR基于事件采集，可以分析非常底层的信息，例如对象分配，方法采样与热点方法定位与调用堆栈，安全点分析与锁占用时长与堆栈分析，GC 相关分析以及 JIT 编译器相关分析（例如 CodeCache ）
- 完善的 API 定义，用户可以自定义事件生产与消费。

**性能损耗** 

官方说JFR profile大概影响2%的性能，但是实际上，这个影响，尤其是频繁发生内存分配的微服务接口应用，影响**绝对不止2%**，而且profile的确采集的东西要比默认配置的多很多(这个我们后面会详细说，为什么负载会高的原因也会在后面说)，所以，**线上系统不推荐长期跑profile**

**如何实现的**

在 JFR中，一切皆为 Event：

- 任意JVM行为都是一个Event，例如类加载也是一个 Event，对应 Class Load Event
- 开启 JFR 记录的原因也是一个Event，对应的就是 Recording Reason Event
- 就算是有 Event 丢失，他也是一个 Event，对应 Data Loss Event

这些 Event 在某些特定的时间点产生，每个事件都有名称，产生时间戳还有 Event 数据体组成。Event 数据体不同的 Event 数据不同，例如 CPU负载，Event 发生之前还有之后的 Java 堆大小， 获取锁的线程 ID 等等。还有一点比较有意思的是，大部分的 Event，都有 Event 是在哪个线程发生的，Event 发生的时候这个线程的调用栈，Event 的持续时间。这就非常有用了，利用这些信息，我们可以回溯 Event 发生当时的情况。

由于 JFR 会采集很多很多的数据，为了效率，最好配置自己感兴趣的事件采集，并且对于 Duration Event 设置时间限制，一般我们对于时间短的事件并不关心。

Event 会被写入 .jfr 的二进制文件（二进制文件对于应用来说读写效率最高）中，以 little endian base 128 的形式编码，这里我们用一个 Event 举个例子：

Class Load Event

```
0000FC10 : 98 80 80 00 87 02 95 ae e4 b2 92 03 a2 f7 ae 9a 94 02 02 01 8d 11 00 00
```

**如何低延迟与低性能损耗小**

在 JFR 中，所有的 Event （包括通过JFR API产生的 Event 还有 JVM 产生的 EVENT），会先存储到每个线程自己的 Thread Buffer 中；在这个 Buffer 满了之后，会将 Buffer 的内容刷入 Global Buffer 中；Global Buffer 是一个环形 Buffer，保存着所有线程发送来的 Thread Buffer 中的内容。当这个环形 Buffer 存储到达上限之后，根据配置，会**选择丢弃或者刷入文件**。

event是多线程产生的，并且都是带时间戳的，每个线程的event是顺序产生的，多个线程可以看成一个又一个的有序集合，所以在生产的时候不要保证全局顺序性，在读取时只要针对多个有序集合排序一次，算法上快很多。

**JFR相关的内存占用到底有多大**？

主要是两部分，一部分是 global buffer，另一部分是 thread local buffer。 global buffer 总大小由memorysize自动计算得出，总大小就是 memorysize。所以， JFR 相关的占用内存大小为： thread 数量 * thread buffer 大小 + memory size。memorysize和threadbuffersize可以由-XX:FlightRecorderOptions指定，threadbuffersize默认8K，memorysize默认10M。

**如何启动**

两种方式：

方式一：通过JVM启动参数启用JFR参数如下：

```java
-XX:StartFlightRecording -XX:FlightRecorderOptions
```

一个负责启动，一个负责配置。

举个例子：

```shell
java \
   -Xmx1024m \
     -Xlog:gc*=debug:file=gc.log:utctime,uptime,tid,level:filecount=10,filesize=128m \
    -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heapdump.hprof \
    -XX:StartFlightRecording=disk=true,dumponexit=true,filename=recording.jfr,maxsize=1024m,maxage=1d,settings=profile \
      OOMEGenerator.java
```



方式二：使用jcmd 工具启动和配置飞行记录器：

飞行记录器启动、配置参数示例

```shell
   启动参数和-XX:StartFlightRecording一模一样
   $ jcmd <pid> JFR.start  
   $ jcmd <pid> JFR.dump filename=recording.jfr
   查看当前正在执行的 JFR 记录
   $ jcmd <pid> JFR.check
     停止 JFR 记录，需要传入名称
   $ jcmd <pid> JFR.stop name=***

```



**JFR 使用测试**：

 JFR 使用示例，被测程序如下

```java
 public class OOMEGenerator {

  static BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

  public static void main(String[] args) {
6
    new Thread(new Consumer()).start();
    new Thread(new Producer()).start();
  }

  static class Producer implements Runnable {

    public void run() {
      while (true) {
        queue.offer(new byte[3 * 1024 * 1024]);

        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

  }

  static class Consumer implements Runnable {
    public void run() {
      while (true) {
        try {
          queue.take();
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
    
```



2. 分析上一步中生成的 JFR 文件：recording.jfr

1）代码分析

```java
    public void readRecordFile() throws IOException {
        final Path path = Paths.get("/uses/apple/recording.jfr");
        final List<RecordedEvent> recordedEvents = RecordingFile.readAllEvents(path);
        for (RecordedEvent event : recordedEvents) {
            System.out.println(event.getStartTime() + "," + event.getValue("message"));
        }
    }
```

2）可视化分析：最新的JDK11中已经移除了jmc工具包，需要自己下载https://github.com/JDKMissionControl/jmc代码编译。

jmc待运行，找不到可以打开上述jfr的工具T_T。。

### 低开销（low-overhead）的 Heap Profiling [[4]](http://openjdk.java.net/jeps/331)

对于开发者来讲，了解程序的堆内存使用情况非常重要，糟糕的堆管理可能会导致堆耗尽并且GC奔溃。目前有几种工具可以了解堆的使用情况：Java Flight Recorder, jmap, visualvm, yourkit。 但是这些工具有以下弊端：无法得到对象的分配位置，headp dump 以及 heap histogram 中都没有包含对象分配的具体信息，但是这些信息对于调试内存问题至关重要，因为它能够告诉开发人员他们的代码中发生的高内存分配的确切位置，并根据实际源码来分析具体问题，这也是 Java 11 中引入这种低开销堆分配采样方法的原因。

引入这个低开销内存分析工具是为了达到如下目的：

- 足够低的采样开销，可以默认且一直开启
- 能通过定义好的程序接口访问
- 能够对所有堆分配区域进行采样
- 能给出活动对象和无效对象的信息

提供给用户的API为:

```java
void JNICALL SampledObjectAlloc(jvmtiEnv *jvmti_env,
            JNIEnv* jni_env,
            jthread thread,
            jobject object,
            jclass object_klass,
            jlong size)
 
jvmtiError  SetHeapSamplingInterval(jvmtiEnv* env, jint sampling_interval)

```

请注意，采样间隔不精确。 每次采样发生时，在选择下一个采样之前的字节数将是具有给定平均间隔的伪随机数。 这是为了避免采样偏差。 例如，如果每512KB发生一次相同的分配，则512KB的采样间隔将始终对相同的分配进行采样。 因此，尽管采样间隔不会总是选择的间隔，但是在进行大量采样之后，采样间隔会趋向于此。

应用程序性能监视（APM）供应商开始使用此新功能，Java工程组正在研究其与Azure性能监视工具的潜在用途。

启用该功能可能会导致性能/内存损失（禁用不会）。 目前Dacapo benchmark对此评估的开销为：

- 禁用此功能时为0％

- 以默认的512KB间隔启用该功能但未执行任何回调操作（即SampledAllocEvent方法为空但已注册到JVM）时为1％。

- 采样回调的3％开销，该回调实现了简单的实现来存储数据（在测试中使用一个）



和JFR相比，low-overhead heap profile的优势如下：

首先，JFR不允许设置采样大小或提供回调。 其次，当缓冲区用尽时，JFR使用缓冲区系统可能会导致分配丢失。 最后，JFR事件系统没有提供跟踪已被垃圾收集的对象的方法，这意味着无法使用它来提供有关活动和垃圾收集对象的信息。

### GC

从Java9开始，CMS垃圾回收器被废弃，在java11中支持的垃圾回收器有： Serial, Parallel, G1, ZGC  and Epsilon，默认的是G1回收器。

#### ZGC 

ZGC 即 Z Garbage Collector（垃圾收集器或垃圾回收器），这应该是 Java 11 中最为瞩目的特性，没有之一。ZGC 是一个可伸缩的、低延迟的垃圾收集器，主要为了满足如下目标进行设计：

- GC 停顿时间不超过 10ms
- 即能处理几百 MB 的小堆，也能处理几个 TB 的大堆
- 应用吞吐能力不会下降超过 15%（与 G1 回收算法相比）
- 方便在此基础上引入新的 GC 特性和利用 colord
- 针以及 Load barriers 优化奠定基础
- 当前只支持 Linux/x64 位平台 停顿时间在 10ms 以下，10ms 其实是一个很保守的数据，即便是 10ms 这个数据，也是 GC 调优几乎达不到的极值。根据 SPECjbb 2015 的基准测试，128G 的大堆下最大停顿时间才 1.68ms，远低于 10ms，和 G1 算法相比，改进非常明显。

![img](https://pdai.tech/_images/java/java-11-1.png)



不过目前 ZGC 还处于实验阶段，目前只在 Linux/x64 上可用，如果有足够的需求，将来可能会增加对其他平台的支持。同时作为实验性功能的 ZGC 将不会出现在 JDK 构建中，除非在编译时使用 configure 参数：`--with-jvm-features=zgc` 显式启用。

在实验阶段，编译完成之后，已经迫不及待的想试试 ZGC，需要配置以下 JVM 参数，才能使用 ZGC，具体启动 ZGC 参数如下：

```shell
-XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Xmx1g    
```

其中参数： -Xmx 是 ZGC 收集器中最重要的调优选项，大大解决了程序员在 JVM 参数调优上的困扰。ZGC 是一个并发收集器，必须要设置一个最大堆的大小，应用需要多大的堆，主要有下面几个考量：

- 对象的分配速率，要保证在 GC 的时候，堆中有足够的内存分配新对象。

- 一般来说，给 ZGC 的内存越多越好，但是也不能浪费内存，所以要找到一个平衡。

  

#### G1和ZGC对比[7]](https://tech.meituan.com/2020/08/06/new-zgc-practice-in-meituan.html)

在使用G1的时候，在回收垃圾的时候，必须要保证应用程序当中所有的线程停下来，不在内存中制造混乱，然后GC才会开始工作，会造成停顿。这一个过程，有一个专属名词来解释：STW（stop the world）。
ZGC的目标就是缩短STW的时间：ZGC是一个并发，基于region， 压缩型的垃圾收集器，只有root扫描阶段会STW， 因此GC停顿时间不会随着堆的增长和存活对象的增长而变长。下面就比较下G1 GC和ZGC在停顿时间的区别。

1）G1 GC

G1 GC的设计目的是在延迟和吞吐量之间找到一个平衡，尽量的将暂停时间缩短到最小，尽量避免full GC，但是当并行的GC回收无法足够快时，还是会触发full GC。

下面通过G1中标记-复制算法过程（G1的Young GC和Mixed GC均采用该算法），分析G1停顿耗时的主要瓶颈。G1垃圾回收周期如下图所示：

![img](https://p0.meituan.net/travelcube/2f56a9a249bc8d74f4f455782abce6be147997.png@1832w_848h_80q)

G1的混合回收过程可以分为标记阶段、清理阶段和复制阶段。

**标记阶段停顿分析**

- 初始标记阶段：初始标记阶段是指从GC Roots出发标记全部直接子节点的过程，该阶段是STW的。由于GC Roots数量不多，通常该阶段耗时非常短。
- 并发标记阶段：并发标记阶段是指从GC Roots开始对堆中对象进行可达性分析，找出存活对象。该阶段是并发的，即应用线程和GC线程可以同时活动。并发标记耗时相对长很多，但因为不是STW，所以我们不太关心该阶段耗时的长短。
- 再标记阶段：重新标记那些在并发标记阶段发生变化的对象。该阶段是STW的。

**清理阶段停顿分析**

- 清理阶段清点出有存活对象的分区和没有存活对象的分区，该阶段不会清理垃圾对象，也不会执行存活对象的复制。该阶段是STW的。

**复制阶段停顿分析**

- 复制算法中的转移阶段需要分配新内存和复制对象的成员变量。转移阶段是STW的，其中内存分配通常耗时非常短，但对象成员变量的复制耗时有可能较长，这是因为复制耗时与存活对象数量与对象复杂度成正比。对象越复杂，复制耗时越长。

四个STW过程中，初始标记因为只标记GC Roots，耗时较短。再标记因为对象数少，耗时也较短。清理阶段因为内存分区数量少，耗时也较短。转移阶段要处理所有存活的对象，耗时会较长。因此，G1停顿时间的瓶颈主要是标记-复制中的转移阶段STW。为什么转移阶段不能和标记阶段一样并发执行呢？主要是G1未能解决转移过程中准确定位对象地址的问题。



2）全并发的ZGC

与CMS中的ParNew和G1类似，ZGC也采用标记-复制算法，不过ZGC对该算法做了重大改进：ZGC在标记、转移和重定位阶段几乎都是并发的，这是ZGC实现停顿时间小于10ms目标的最关键原因。

ZGC垃圾回收周期如下图所示：

![img](https://p0.meituan.net/travelcube/40838f01e4c29cfe5423171f08771ef8156393.png@1812w_940h_80q)

ZGC只有三个STW阶段：**初始标记**，**再标记**，**初始转移**。其中，初始标记和初始转移分别都只需要扫描所有GC Roots，其处理时间和GC Roots的数量成正比，一般情况耗时非常短；再标记阶段STW时间很短，最多1ms，超过1ms则再次进入并发标记阶段。即，ZGC几乎所有暂停都只依赖于GC Roots集合大小，停顿时间不会随着堆的大小或者活跃对象的大小而增加。与ZGC对比，G1的转移阶段完全STW的，且停顿时间随存活对象的大小增加而增加。

#### ZGC关键技术

ZGC通过着色指针和读屏障技术，解决了转移过程中准确访问对象的问题，实现了并发转移。大致原理描述如下：并发转移中“并发”意味着GC线程在转移对象的过程中，应用线程也在不停地访问对象。假设对象发生转移，但对象地址未及时更新，那么应用线程可能访问到旧地址，从而造成错误。而在ZGC中，应用线程访问对象将触发“读屏障”，如果发现对象被移动了，那么“读屏障”会把读出来的指针更新到对象的新地址上，这样应用线程始终访问的都是对象的新地址。那么，JVM是如何判断对象被移动过呢？就是利用对象引用的地址，即着色指针。下面介绍着色指针和读屏障技术细节。

**着色指针**

> 着色指针是一种将信息存储在指针中的技术。

ZGC仅支持64位系统，它把64位虚拟地址空间划分为多个子空间，如下图所示：

![img](https://p0.meituan.net/travelcube/f620aa44eb0a756467889e64e13ee86338446.png@1568w_322h_80q)

其中，[0~4TB) 对应Java堆，[4TB ~ 8TB) 称为M0地址空间，[8TB ~ 12TB) 称为M1地址空间，[12TB ~ 16TB) 预留未使用，[16TB ~ 20TB) 称为Remapped空间。

当应用程序创建对象时，首先在堆空间申请一个虚拟地址，但该虚拟地址并不会映射到真正的物理地址。ZGC同时会为该对象在M0、M1和Remapped地址空间分别申请一个虚拟地址，且这三个虚拟地址对应同一个物理地址，但这三个空间在同一时间有且只有一个空间有效。ZGC之所以设置三个虚拟地址空间，是因为它使用“空间换时间”思想，去降低GC停顿时间。“空间换时间”中的空间是虚拟空间，而不是真正的物理空间。后续章节将详细介绍这三个空间的切换过程。

与上述地址空间划分相对应，ZGC实际仅使用64位地址空间的第0~41位，而第42~45位存储元数据，第47~63位固定为0。

![img](https://p0.meituan.net/travelcube/507f599016eafffa0b98de7585a1c80b338346.png@2080w_624h_80q)

ZGC将对象存活信息存储在42~45位中，这与传统的垃圾回收并将对象存活信息放在对象头中完全不同。

**读屏障**

> 读屏障是JVM向应用代码插入一小段代码的技术。当应用线程从堆中读取对象引用时，就会执行这段代码。需要注意的是，仅“从堆中读取对象引用”才会触发这段代码。

读屏障示例：

```Java
Object o = obj.FieldA   // 从堆中读取引用，需要加入屏障
<Load barrier>
Object p = o  // 无需加入屏障，因为不是从堆中读取引用
o.dosomething() // 无需加入屏障，因为不是从堆中读取引用
int i =  obj.FieldB  //无需加入屏障，因为不是对象引用
```

ZGC中读屏障的代码作用：在对象标记和转移过程中，用于确定对象的引用地址是否满足条件，并作出相应动作。

**ZGC并发处理演示**

接下来详细介绍ZGC一次垃圾回收周期中地址视图的切换过程：

- **初始化**：ZGC初始化之后，整个内存空间的地址视图被设置为Remapped。程序正常运行，在内存中分配对象，满足一定条件后垃圾回收启动，此时进入标记阶段。
- **并发标记阶段**：第一次进入标记阶段时视图为M0，如果对象被GC标记线程或者应用线程访问过，那么就将对象的地址视图从Remapped调整为M0。所以，在标记阶段结束之后，对象的地址要么是M0视图，要么是Remapped。如果对象的地址是M0视图，那么说明对象是活跃的；如果对象的地址是Remapped视图，说明对象是不活跃的。
- **并发转移阶段**：标记结束后就进入转移阶段，此时地址视图再次被设置为Remapped。如果对象被GC转移线程或者应用线程访问过，那么就将对象的地址视图从M0调整为Remapped。

其实，在标记阶段存在两个地址视图M0和M1，上面的过程显示只用了一个地址视图。之所以设计成两个，是为了区别前一次标记和当前标记。也即，第二次进入并发标记阶段后，地址视图调整为M1，而非M0。

着色指针和读屏障技术不仅应用在并发转移阶段，还应用在并发标记阶段：将对象设置为已标记，传统的垃圾回收器需要进行一次内存访问，并将对象存活信息放在对象头中；而在ZGC中，只需要设置指针地址的第42~45位即可，并且因为是寄存器访问，所以速度比访问内存更快。

![img](https://p0.meituan.net/travelcube/a621733099b8fda2a0f38a8859e6a114213563.png@2070w_806h_80q)

文章[[新一代垃圾回收器ZGC的探索与实践](https://tech.meituan.com/2020/08/06/new-zgc-practice-in-meituan.html)中有关于ZGC的调优实践，感兴趣的可以阅读一下

#### Epsilon GC

Epsilon 垃圾回收器的目标是开发一个控制内存分配，但是不执行任何实际的垃圾回收工作。它提供一个完全消极的 GC 实现，分配有限的内存资源，最大限度的降低内存占用和内存吞吐延迟时间。

Java 版本中已经包含了一系列的高度可配置化的 GC 实现。各种不同的垃圾回收器可以面对各种情况。但是有些时候使用一种独特的实现，而不是将其堆积在其他 GC 实现上将会是事情变得更加简单。

下面是 no-op GC 的几个使用场景：

- **性能测试**：什么都不执行的 GC 非常适合用于 GC 的差异性分析。no-op （无操作）GC 可以用于过滤掉 GC 诱发的性能损耗，比如 GC 线程的调度，GC 屏障的消耗，GC 周期的不合适触发，内存位置变化等。此外有些延迟者不是由于 GC 引起的，比如 scheduling hiccups, compiler transition hiccups，所以去除 GC 引发的延迟有助于统计这些延迟。
- **内存压力测试**：在测试 Java 代码时，确定分配内存的阈值有助于设置内存压力常量值。这时 no-op 就很有用，它可以简单地接受一个分配的内存分配上限，当内存超限时就失败。例如：测试需要分配小于 1G 的内存，就使用-Xmx1g 参数来配置 no-op GC，然后当内存耗尽的时候就直接 crash。
- **VM 接口测试**：以 VM 开发视角，有一个简单的 GC 实现，有助于理解 VM-GC 的最小接口实现。它也用于证明 VM-GC 接口的健全性。
- **极度短暂 job 任务**：一个短声明周期的 job 任务可能会依赖快速退出来释放资源，这个时候接收 GC 周期来清理 heap 其实是在浪费时间，因为 heap 会在退出时清理。并且 GC 周期可能会占用一会时间，因为它依赖 heap 上的数据量。 延迟改进：对那些极端延迟敏感的应用，开发者十分清楚内存占用，或者是几乎没有垃圾回收的应用，此时耗时较长的 GC 周期将会是一件坏事。
- **吞吐改进**：即便对那些无需内存分配的工作，选择一个 GC 意味着选择了一系列的 GC 屏障，所有的 OpenJDK GC 都是分代的，所以他们至少会有一个写屏障。避免这些屏障可以带来一点点的吞吐量提升。

Epsilon 垃圾回收器和其他 OpenJDK 的垃圾回收器一样，可以通过参数 `-XX:+UseEpsilonGC` 开启。

Epsilon 线性分配单个连续内存块。可复用现存 VM 代码中的 TLAB 部分的分配功能。非 TLAB 分配也是同一段代码，因为在此方案中，分配 TLAB 和分配大对象只有一点点的不同。Epsilon 用到的 barrier 是空的(或者说是无操作的)。因为该 GC执行任何的 GC 周期，不用关系对象图，对象标记，对象复制等。引进一种新的 barrier-set 实现可能是该 GC 对 JVM 最大的变化。

**用法**

```shell
-XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC
```

**示例代码**

```java
public class Epsilon {
    public static void main(String[] args) {
        boolean flag = true;
        List<Garbage> garbageList = new ArrayList<>();
        int count = 0;
        while (flag) {
            garbageList.add(new Garbage());
            if (count ++ == 500) {
                garbageList.clear();
            }
        }
    }
}

class Garbage {
    private double d1 = 1;
    private double d2 = 2;

    /**
     * 在GC清除对象时会调用一次
     */
    @Override
    protected void finalize() throws Throwable {
        System.out.println(this + " collecting");
    }
}
```

1）直接运行，使用的默认的垃圾回收器：

```
java11.Garbage@37c7d031 collecting
java11.Garbage@71a19bf9 collecting
java11.Garbage@3b2df791 collecting
java11.Garbage@61441b29 collecting
java11.Garbage@680b1968 collecting
java11.Garbage@158829c3 collecting
java11.Garbage@414dc59c collecting
java11.Garbage@1103cf collecting
......
```

从运行打印的结果可以看出：有对象被回收了，触发了GC【默认用的是G1】
因为只限定回收了500个，500个之后的对象会不断加到内存中，内存就会不够用。

2）如果使用Epsilon GC，会是什么样的结果呢？

执行命令

```shell
java -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC EpsilonGC.java
```

运行代码，发现控制台没有任何的输出，即EpsilonGC不会做任何的回收，并且程序很快就因为堆空间不足而退出。

```shell
Terminating due to java.lang.OutOfMemoryError: Java heap space
```

## 其他特性

### docker容器增强

从Java 10开始，JVM使用容器控制组（cgroup）设置的约束来设置内存和CPU限制。 例如，默认的最大堆大小是容器的内存限制的1/4（例如-m2G为500MB）。另外增加了JVM Options，以使Docker容器用户可以细粒度控制将用于Java堆的系统内存量。

### 模块化

以前如果要依赖外部库需要把jar包放在classpath中就可以了，但是容易引发版本冲突。java11可以在程序组件里明确声明依赖哪些方法，并且可以指定哪些自己的公共类型可以被外部访问到。这样带来的其他好处是：减少应用程序内存占用量以及加快应用启动时间。

### jar文件包含多个版本的class文件

这样不必依赖多个jar文件



# 结语

本文的涉及到的demo可以https://github.com/apppppple/java 下载

其他参考资料：

[Reasons to move to Java 11](https://docs.microsoft.com/en-us/azure/developer/java/fundamentals/reasons-to-move-to-java-11)

