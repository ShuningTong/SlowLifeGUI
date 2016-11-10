# Deliverable 4: SlowLifeGUI
This is the fourth assignment for IS2545 Software Quality Assurance. It's focused on performance testing and testing efficiency-oriented indicators: Utilization. The source code is implementing Conway's Game of Life, in GUI Form.  It's deliberately non-performant.

I use VisualVM to determine CPU-intensive methods while I run GameOfLife in terminal. 

I have following findings:

* convertToInt method and runContinuous method of MainPanel class are the most CPU-intensive.

![](https://drive.google.com/open?id=0Byq0G7BliOfeN003QVYyTnlYRk0)

* In the process of running, if I hit stop button and then write button, the CPU usage will decrease first and then increase a lot. So I think the write function is also CPU-intensive. The corresponding method is toString method of Cell class.

![](https://drive.google.com/open?id=0Byq0G7BliOfeNzNKQmY5eTFhRTA)

I generated following pinning tests in PinningTest.java file:

* I added four pinning tests in the form of unit tests for convertToInt method in MainPanel class. One for zero, one for positive number, one for negative number, and the last for Integer.MAX_VALUE. They all passed with the legacy code.

* I added three pinning tests in the form of unit tests for runContinuous method in MainPanel class. One for all cells dead, one for corner square stable pattern, and the last for diagonal disppear pattern. They all passed with the legacy code.

![](/Users/shuning/Desktop/1st run/squarestable.png)
![](/Users/shuning/Desktop/1st run/diagonaldisappear.png)

* I added four pinning tests in the form of unit tests for toString method in Cell class. One for alive cell, one for dead cell, one for aliveToDead cell, and the last for deadToAlive cell. They all passed with the legacy code.

I made following changes:

* I changed the convertToInt method to let it return the integer passed in when it's not negative, but throw NumberFormatException when it's negative. Pinning tests still passed.

* I removed all _r and origR manipulation code from runContinuous method. Pinning tests still passed.

* I used getAlive to get the current state of a cell in toString method of Cell class and removed the code for concatenating toReturn variable. Pinning tests still passed.

After I made these changes, I profiled again using VisualVM with the same pattern I used when I did the first profile.

* convertToInt and runContinuous method are no longer the most CPU-intensive methods.

![](/Users/shuning/Desktop/1st run/convertToInt_runContinuous_After.png)

* In the process of running, if I hit stop and write button, the CPU usage will stay at a low level.

![](/Users/shuning/Desktop/1st run/write_After.png)

How to run the program code and test code:

* Clone using git
* Open terminal on your computer
* First compile GameOfLife.java file

```
javac GameOfLife.java
```
* Run program code

```
java GameOfLife 15
```
* Run the test file

```
java -cp .:/path/to/hamcrest-all-1.3.jar:/path/to/junit-4.12.jar org.junit.runner.JUnitCore PinningTest
```








