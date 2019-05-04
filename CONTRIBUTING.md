
# Contributing Guide

 - [The repository](#the-repository)
	 - [Getting started](#getting-started)
	 - [Adding a feature](#adding-a-feature)
	 - [Fixing a bug](#fixing-a-bug)
 - [Testing](#testing)
 - [Documentation](#documentation)
 - [Changes to this guide](#changes-to-this-guide)

## The repository

### Getting started
First things first, you're going to want to clone the repository and then `cd` into it:

```sh
git clone https://github.com/mdpoulter/nis-prac.git
cd nis-prac
```

### Adding a feature
To get started on adding a new feature, first checkout and pull master:

```sh
git checkout master
git pull origin master
```

Then create a new branch describing your feature:

```sh
git checkout -b feature/feature-name
```

While developing, remember to commit regularly and use appropriate commit messages:

```sh
git add .                           # or git add <file>
git commit -m "Commit message"
```

You can also push it to the remote repository to keep your branch backed up. The first time you do this, you will need to run:

```sh
git push -u origin feature/feature-name
```

For future pushes however, you can simply run:

```sh
git push origin
```

Finally, once your feature is complete, all the tests are still passing, and you have pushed to origin, you can create a pull request on Github.

Navigate to the repository on Github and you’ll see that your branch should be listed at the top with a useful "Compare & pull request" button. Click it! Make sure to set the base to `master` and provide a little bit of context in the pull request before submitting it.

Once submitted, the pull request (currently) requires approval from 2 other contributors before it can be merged into the `master` branch so let us know on the WhatsApp group to get on that.

### Fixing a big

Bug fixes can be done in almost exactly the same way, however please name your branches accordingly:

```sh
git checkout -b bugfix/bugfix-name
```
## Testing

`JUnit` is the test framework used for this project. `JUnit` is simple to use and has a vast amount of documentation available. 

**Note:** There are some packages you *may* need to add to the classpath in order to get `JUnit` to work with IntelliJ. [Here](https://www.jetbrains.com/help/idea/configuring-testing-libraries.html) is a walkthrough on how to add them if you run into difficulty.

Here is the basic format of a `JUnit` test:
```java
class FirstJUnitTest {

    @Test
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }

}
```

Here are some resources to help!
* [User Guide](https://junit.org/junit5/docs/current/user-guide/)
* [Documentation](https://junit.org/junit5/docs/current/api/overview-summary.html)
* [Vogella Tutorial](http://www.vogella.com/tutorials/JUnit/article.html#junittesting)

## Documentation
We will use javadocs in order to create and maintain documentation for all our code. Please use the javadocs [notation](https://www.oracle.com/technetwork/java/javase/tech/index-137868.html) when writing all your code. For example:
```java
/**
  * Lorem ipsum dolor sit amet, consectetur adipiscing elit...
  * @author John Doe
  * @version 07/08/2018
  */
public class foo{
  // example
}
```

The beginning of the class has a brief description - *Lorem ipsum* etc, the writer of the class, `@author` , and the version, `30/04/2019`, which is the date

```java
/**
  * Lorem ipsum dolor sit amet, consectetur adipiscing...
  * @param foo the variable fizzbuzz takes
  * @return the string and FizzBuzz
  */
public String FizzBuzz(String foo){
  String bar = foo + " FizzBuzz!";
  return bar
}
```
There are a few other javadocs keywords we might use, like `@exception`, `@see`, etc. Please use them where necessary.

Please take a look at the [docs](/docs) folder for more information.

## Changes to this guide
Feel free to make any changes to this guide that you see fit and submit a pull request for it.
