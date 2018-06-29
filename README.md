# Bytes

A small utility library for working with units of digital information, featuring:

* Conversion
* Comparison
* Parsing
* Formatting

Built for Java 8+ with a familiar and easy to use API.

## Usage

The `Bytes` class is the main focus of the library. It's an immutable container of a number and a unit.

```java
Bytes fromDouble = Bytes.valueOf(Math.PI, ByteUnits.SI.GIBIBYTE);
Bytes fromBigDecimal = Bytes.valueOf(BigDecimal.valueOf(Math.PI), ByteUnits.SI.GIBIBYTE); 
Bytes fromString = Bytes.parse("3.14 GiB"); 
```

It features simple & convenient methods for conversion, formatting and parsing:

```java
// Conversion
Bytes withNaturalUnit = Bytes.valueOf(1024, ByteUnits.BYTE).toNaturalUnit(); 
assertEquals(withNaturalUnit.getUnit(), ByteUnits.IEC.KIBIBYTE);

Bytes bytes = Bytes.valueOf(1024, ByteUnits.BYTE).toUnit(ByteUnits.SI.KILOBYTE);
assertEquals(bytes.getValue(), BigDecimal.valueOf(1.024));

// Formatting
String formatted = Bytes.format(withNaturalUnit, "#.##"); 
assertEquals(formatted, "1.00 KiB");

// Parsing
Bytes oneByte = Bytes.parse("1 B"); 
Bytes twoByte = Bytes.parse("2B"); 
```

See the JavaDoc for each class for detailed usage.

## Installation
Using Gradle, add this to your build script: 

```groovy
repositories {
    mavenCentral()
}
dependencies {
    compile 'io.aesy:bytes:1.0.0'
}
```

Using Maven, add this to your list of dependencies in `pom.xml`:

```xml
<dependency>
  <groupId>io.aesy</groupId>
  <artifactId>bytes</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Contribute
Use the [issue tracker](https://github.com/aesy/bytes/issues) to report bugs or make feature 
requests. Pull requests are welcome, but it may be a good idea to create an issue to discuss any 
changes beforehand.

## License
MIT, see [LICENSE](/LICENSE) file.
