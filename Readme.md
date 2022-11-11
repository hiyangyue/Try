Implementation of the Try-Success-Failure Scala API for Java

---
The `Try` type give us a ability to write safety and clean code without pay attention to `try-catch` blocks,
 it represents a computation that the result may either in an exception or a success value and inspired by
 Scala's <a href="https://javadoc.io/doc/org.scala-lang/scala-library/2.11.0-M4/scala/util/Try.html">Try.</a>
 
##### Why need `Try` replace "try-catch" blocks
Exception would breaks the program's flow, which makes code unclean and poor readability.
 And here are some additional thought on matter:
 <ul>
     <li>
      <a href="https://radio-weblogs.com/0122027/stories/2003/04/01/JavasCheckedExceptionsWereAMistake.html">
     Java's checked exceptions were a mistake</a>
     </li>
     <li>
     <a href="https://www.artima.com/articles/the-trouble-with-checked-exceptions">
         The Trouble with Checked Exceptions A Conversation with Anders Hejlsberg</a>
     </li>

 </ul>

 Examples
 ```
 int requestCount() {
         return 1;
 }
 
 Try.of(() -> requestCount())
     .map(x -> 1 * 2)
     .onSuccess(System.out::println)
     .onFailure(System.out::println) // if exception need handle
     .recover(NullPointerException.class, nullPointerException -> doSomething()) // if specify exception need handle
     .getOrElse(-1);

```

Note: only non-fatal exception are caught by the combinators.The following exception will be thrown.
* InterruptedException
* LinkageError
* ThreadDeath
* VirtualMachineError(OutOfMemoryError/StackOverflowError)
