# TileDB-Java
[![Build Status](https://travis-ci.org/TileDB-Inc/TileDB-Java.svg?branch=master)](https://travis-ci.org/TileDB-Inc/TileDB-Java)

## Dependencies

To build the JNI extension you need to install:

* Cmake (>=3.3)
* JDK (>=1.8)

## Build

To build the library with dependencies bundled in run:

`./gradlew oneJar`

This will create the TileDB JNI library `build/tiledb_jni/libtiledbjni.dylib`. This will also download and build the [TileDB](https://github.com/TileDB-Inc/TileDB) library first, if it is not found installed in a global system path, in which case the native library gets placed in `build/externals/install/lib/libtiledb.dylib`.

If you wish to build with a custom version of the native TileDB library, you can define the environment variable `TILEDB_HOME`, e.g.:

`env TILEDB_HOME=/path/to/TileDB/dist ./gradlew oneJar`
Note that if you build with a custom native TileDB library it will only be bundled into the jar if the native static library was produced.


### Non Bundled Jar

It is possible to build the native JNI library and produce a jar file that does not contain the dependencies.

`./gradlew assemble`

If you build the non-builded jar, before running the Java code you should copy the `libtiledbjni.dylib` file into your system library path, or add the build folder in your `LD_LIBRARY_PATH` ENV variable.

### Enabling TileDB Backends During Superbuild

If tiledb is not globally installed on the system where the JNI library is
being compiled, the native TileDB Library will be compiled. There are
multiple properties which can be configured including S3 and HDFS support.

See [grade.properties](gradle.properties) for all properties which can be
set for building.

The properties can be set via the `-P` option to gradlew:

```
./gradlew -P TILEDB_S3=ON -P TILEDB_VERBOSE=ON
```

## Tests

To run the tests use:

`./gradlew test`

## Examples

You can run the examples located in `src/main/java/examples` using you IDE or from a terminal.

To run an example from the terminal use:

`java -cp build/libs/tiledb-java-1.0-SNAPSHOT-standalone.jar examples.io.tiledb.libtiledb.TiledbArraySchema`

You may need to explicitly define the java library path if not using the bundled jar:

`java -Djava.library.path=".:<path/to/TileDB-Java/build/tiledb_jni>" -cp build/libs/tiledb-java-1.0-SNAPSHOT.jar examples.io.tiledb.libtiledb.TiledbArraySchema`

## Format 
 
To auto-format the Java source code run:

`./gradlew format`

to check Java source code formatting:

`./gradlew checkFormat`


## Generate JNI bindings for TileDB C-API

### Dependencies

* Swig (>=3.0)

For OSX swig is available using `brew install swig`

Installation instructions for other operating systems can be found here:
http://www.swig.org/Doc3.0/Preface.html#Preface_installation

### Genrate bindings

1) Set the ENV variable `TILEDB_HOME` to the install location of TileDB.

2) Generate the JNI code using

`env TILEDB_HOME=/path/to/TileDB/dist ./gradlew generateJNI`
