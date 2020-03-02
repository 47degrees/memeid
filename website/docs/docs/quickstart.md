---
layout: docs
title: Quickstart
permalink: docs/
---

# memeid

`memeid` is a JVM library for generating [RFC-compliant](https://www.ietf.org/rfc/rfc4122.txt) Universal Unique Identifiers (UUIDs).

`A universally unique identifier (UUID) is a 128-bit number used to identify information in computer systems.
When generated according to the standard methods, UUIDs are for practical purposes unique. Their uniqueness does not depend on a central registration authority or coordination between the parties generating them, unlike most other numbering schemes.`
-- [Wikipedia article on UUIDs](https://en.wikipedia.org/wiki/Universally_unique_identifier)

## Rationale

The UUID type that ships with the JVM `java.util.UUID` has a number of problems, namely:

 - [A bug in the comparison function that will never be fixed](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=7025832) 
 - Only provides UUID generation for random (V4) and non-namespaced pseudo-V3 UUIDs

This library aims to solve the aforementioned issues and provide an RFC-compliant `UUID` type with coherent comparison, a rich API to get its different fields and constructors for the UUID variants.

## Install

### Java

#### Using maven

```xml
<dependency>
    <groupId>com.47deg</groupId>
    <artifactId>memeid</artifactId>
    <version>0.1</version>
</dependency>
```

#### Using gradle

```groovy
compile group: 'com.47deg', name: 'memeid', version: '@VERSION@'
```

### Scala

Add this to your `build.sbt` file:

```scala
libraryDependencies += "com.47deg" %% "memeid4s" % "@VERSION@"
```

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation