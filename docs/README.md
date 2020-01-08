# memeid

`memeid` is a Scala library for generating and using [RFC-compliant](https://www.ietf.org/rfc/rfc4122.txt) Universal Unique Identifiers (UUIDs).

> A universally unique identifier (UUID) is a 128-bit number used to identify information in computer systems.
>
> When generated according to the standard methods, UUIDs are for practical purposes unique. Their uniqueness does not depend on a central registration authority or coordination between the parties generating them, unlike most other numbering schemes.
-- [Wikipedia article on UUIDs](https://en.wikipedia.org/wiki/Universally_unique_identifier)

## Rationale

The UUID type that ships with the JVM `java.util.UUID` has a number of problems, namely:

 - A bug in the comparison function that will never be fixed https://bugs.java.com/bugdatabase/view_bug.do?bug_id=7025832
 - Only provides UUID generation for random (V4) and non-namespaced pseudo-V3 UUIDs

This library aims to solve the aforementioned issues and provide an RFC-compliant `UUID` type with coherent comparison, a rich API to get its different fields and constructors for the UUID variants:

- The time-based (V1) variant of UUIDs, which can be *significantly* faster to generate that UUIDs that require randomness
- The cryptographically random (V4) variant, roughly equivalent to `java.util.UUID/randomUUID`
- Namespaced (V3 & V5) identifiers, which are probably the most interesting and useful
- Squuids (sequential, random) which are a non-standard variaton of V4 UUIDs that have the property of increasing over time. For instance, using Squuids instead of V4 UUIDs for identifying values in a database can improve locality due to the time component.

## Goals

- Implement the RFC-compliant UUID type, providing a clear migration path from `java.util.UUID`
- Implement the RFC constructors with cats-effect for the UUIDs that need RNG, system time and/or system info
- Provide integration with http4s path parameters, Postgres UUID type trough Doobie

## References

- [RFC 4122](https://www.ietf.org/rfc/rfc4122.txt)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
- [java.util.UUID](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
