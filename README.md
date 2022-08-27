WordCleaner
=================================================

![Points](../../blob/badges/points.svg)

For this homework assignment, you will create a class that is able to clean and parse text into stemmed words using the `SnowballStemmer` class. Use `UTF_8` and try-with-resources when writing your files. Do not use the [`java.io.File`](https://www.cs.usfca.edu/~cs272/javadoc/api/java.base/java/io/File.html) class.

## Hints ##

Below are some hints that may help with this homework assignment:

  - You need to have use the third-party [Apache OpenNLP](http://opennlp.apache.org/) library. The library should be automatically setup in Eclipse by Maven. See the `main(String[])` method and the [Apache OpenNLP Tools Javadoc](https://opennlp.apache.org/docs/1.9.3/apidocs/opennlp-tools/index.html) for how to use this library.

  - When working with files, you should use try-with-resources, the `UTF-8` character encoding, and buffered readers and writers.

  - Look for opportunities to reduce duplicate code. For example, you could reuse some existing methods and/or create a new helper method that is reused in the methods that work with files.

These hints are *optional*. There may be multiple approaches to solving this homework.

## Instructions ##

Use the "Tasks" view in Eclipse to find the `TODO` comments for what need to be implemented and the "Javadoc" view to see additional details.

The tests are provided in the `src/test/` directory; do not modify any of the files in that directory. Check the run details on GitHub Actions for how many points each test group is worth. 

See the [Homework Guides](https://usf-cs272-fall2022.github.io/guides/homework/) for additional details on homework requirements and submission.
