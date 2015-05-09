# idea-pitest
IDEA Pitest plugin

Pitest (aka PIT) is a state of the art mutation testing system for Java and the JVM.

Read all about it at http://pitest.org

The original Pitest logo was created by Ling Yeung. It is licensed under a [Creative Commons License](http://creativecommons.org/licenses/by-nc-sa/3.0/).

## Introduction

The idea of it is to make mutation testing as much easy as possible. The plugin allows you to run Pitest against existed unit test in one click. You don't need to manually setup options, they will be derived from JUnit configuration. Report will be shown using default IDEA Coverage functionality.

[Screenshot](https://www.dropbox.com/s/kptkw46suw1ijot/screenshot.png?dl=0), [binary](https://www.dropbox.com/s/34py3gvoh7aq74r/idea-pitest-plugin_1.1.3.0.zip?dl=0).

## Limitations of the current version

* IntelliJ IDEA 14 (IC or IU) via JRE 7+
* JUnit
* not fully correct coverage calculation
* only basic Pitest parameters

## Plans

* publish plugin to official repository
* TestNG support
* fancy JUnit like tests execution UI
* extended coverage information (like Pitest's HTML report)

## Hints how to use

* install plugin
* right click on the JUnit test
* select Run with Pitest
* open file which is covered by test in the editor

## How to build

You should have IntelliJ IDEA 14 installed. All project files like *.idea* directory and *.iml* are shared so you only need to perform several platform and IDE version dependant configuration steps.

1. Open root of the project using IDEA.
2. Open **Project Structure** configuration under **File** menu.
3. Choose **Modules** and select **idea-pitest-plugin** module. In **Dependencies** section configure **IntelliJ Plutform Plugin SDK** properly.
4. Choose **Libraries** and configure paths to **coverage** and **junit** lib directories. They are located in *$IDEA$/plugins*.
5. Create new **Plugin** run configuration using **idea-pitest-plugin** module. 

Now you should be able to run new instance of IDE with plugin installed in one click. Using this instance you can open **idea-pitest-example** module as a new project to check how the plugin works.