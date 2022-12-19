<img align="left" width="80" height="80" src="./img/icon.svg">

# DataSize

[![maven-central][maven-central-image]][maven-central-url]
[![Build Status][github-actions-image]][github-actions-url]
[![Coverage Status][codecov-image]][codecov-url]
[![MIT license][license-image]][license-url]

[maven-central-image]: https://img.shields.io/maven-central/v/io.aesy/datasize?style=flat-square
[maven-central-url]: https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.aesy%22%20datasize

[github-actions-image]: https://img.shields.io/github/actions/workflow/status/aesy/datasize/ci.yml?branch=master&style=flat-square
[github-actions-url]: https://github.com/aesy/datasize/actions

[codecov-image]: https://img.shields.io/codecov/c/github/aesy/datasize?style=flat-square
[codecov-url]: https://codecov.io/github/aesy/datasize

[license-image]: https://img.shields.io/github/license/aesy/datasize?style=flat-square
[license-url]: https://github.com/aesy/datasize/blob/master/LICENSE

A small utility library for working with units of digital information, featuring:

* Conversion
* Comparison
* Parsing
* Formatting

Built for Java 8+.

### [API Reference](https://aesy.github.io/datasize/apidocs/)

## Usage

The `DataSize` class is the main focus of the library. It's an immutable container of a number and a unit.

```java
DataSize fromDouble = DataSize.of(Math.PI, ByteUnit.SI.GIBIBYTE);
DataSize fromBigDecimal = DataSize.of(BigDecimal.valueOf(Math.PI), ByteUnit.SI.GIBIBYTE); 
DataSize fromString = DataSize.parse("3.14 GiB"); 
```

It features simple & convenient methods for conversion, formatting and parsing:

```java
// Conversion
DataSize withNaturalUnit = DataSize.of(1024, ByteUnit.BYTE).toNaturalUnit(); 
assertEquals(withNaturalUnit.getUnit(), ByteUnit.IEC.KIBIBYTE);

DataSize dataSize = DataSize.of(1024, ByteUnit.BYTE).toUnit(ByteUnit.SI.KILOBYTE);
assertEquals(dataSize.getValue(), BigDecimal.valueOf(1.024));

// Formatting
DataSize dataSize = DataSize.of(1000, ByteUnit.BYTE); 
String formatted = dataSize.toString(); 
assertEquals(formatted, "1 kB");

// Parsing
DataSize oneByte = DataSize.parse("1 B"); 
DataSize twoByte = DataSize.parse("2B"); 
```

See the JavaDoc for each class for detailed usage.

## Installation
Using Gradle, add this to your build script: 

```groovy
repositories {
    mavenCentral()
}
dependencies {
    compile 'io.aesy:datasize:1.0.0'
}
```

Using Maven, add this to your list of dependencies in `pom.xml`:

```xml
<dependency>
  <groupId>io.aesy</groupId>
  <artifactId>datasize</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Contribute
Use the [issue tracker](https://github.com/aesy/datasize/issues) to report bugs or make feature 
requests. Pull requests are welcome, but it may be a good idea to create an issue to discuss any 
changes beforehand.

## License
MIT, see [LICENSE](/LICENSE) file.
