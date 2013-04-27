CBA
===

College project. Class based Association algorithm implemented on a continuous data stream to mine frequent association patterns.
A data mining project to implement class based association algorithm , which generates frequent patterns and rules, with the help of support and confidence.
The dataset used is mushroom.dat . This consists of huge data of mushrooms whether it is edible or poisonous, depending on its 23 properties.

Steps to execute this porject: 

1.First start server and client to simulate a continous data stream .
compile the ServerSocketExample and Client java files.
execute them (remember ServerSocketExample first and then Client).
```java ServerSocketExample
   java Client
```
The data in mushroom.dat will be sent to f1.dat in a window size of 8.

2. Execute the algo.
Start the UI for algo.
```java AprioriUI 
```
this will start the front end of the algo.
Enter a support and confidence value in between 0-1(in decimals).
and click to start the process. After executing click "Frequent Patterns" to show the generated patters and "Generated Rules" to see the rules.

![Alt text]( "")