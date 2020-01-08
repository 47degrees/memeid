# memeid

`memeid` is a Scala library for generating [RFC-compliant](https://www.ietf.org/rfc/rfc4122.txt) Universal Unique Identifiers (UUIDs).

> A universally unique identifier (UUID) is a 128-bit number used to identify information in computer systems.
>
> When generated according to the standard methods, UUIDs are for practical purposes unique. Their uniqueness does not depend on a central registration authority or coordination between the parties generating them, unlike most other numbering schemes.
-- [Wikipedia article on UUIDs](https://en.wikipedia.org/wiki/Universally_unique_identifier)

## Rationale

The UUID type that ships with the JVM `java.util.UUID` has a number of problems, namely:

 - A bug in the comparison function that will never be fixed https://bugs.java.com/bugdatabase/view_bug.do?bug_id=7025832
 - Only provides UUID generation for random (V4) and non-namespaced pseudo-V3 UUIDs

This library aims to solve the aforementioned issues and provide an RFC-compliant `UUID` type with coherent comparison, a rich API to get its different fields and constructors for the UUID variants.

## UUID construction

### Time-based (v1)

The time-based (V1) variant of UUIDs is the fastest to generate. It uses a monotonic clock and node information to generate UUIDs.

```scala
import memeid.UUID

UUID.V1.next
// res0: UUID = 123cfc70-17dd-1171-a5b1-9a72dd876511
UUID.V1.next
// res1: UUID = 123d0440-17dd-1171-a5b1-9a72dd876511
UUID.V1.next
// res2: UUID = 123d0441-17dd-1171-a5b1-9a72dd876511
```

### Random (v4)

The cryptographically random variant, equivalent to `java.util.UUID/randomUUID`.

```scala
import memeid.UUID

UUID.V4.random
// res3: UUID = 4ed7d541-bcb8-4d6a-bbfd-2771bf2da153
UUID.V4.random
// res4: UUID = b4f74362-1d74-4da8-a736-95a558dda492
UUID.V4.random
// res5: UUID = 1b5ef126-d782-427d-8c42-57022152c83f
```

### Namespaced (v3, v5)

Namespaced UUIDs are generated from a UUID (namespace) and a hashed value (name), V3 uses MD5 and V5 uses SHA1 hash.

```scala
import memeid.UUID

val namespace = UUID.V1.next
// namespace: UUID = 123d0ff8-17dd-1171-a5b1-9a72dd876511

UUID.V3(namespace, "my-secret-code")
// res6: UUID = e7ccb56a-9d12-3323-bbae-4af355b76903
UUID.V5(namespace, "my-secret-code")
// res7: UUID = cdfa5875-90d5-5983-176a-58508a2b4e68
```

### Semi-sequential, random (SQUUID)

SQUUIDs are a non-standard variaton of V4 UUIDs that are semi-sequential. They incorporate a time-component in their 32 most significant bits to generate UUIDs that don't fragment DB indexes.

```scala
import memeid.UUID

UUID.V4.squuid
// res8: UUID = 5e15a15b-7817-4990-a676-ddf5e36e8c81
UUID.V4.squuid
// res9: UUID = 5e15a15b-3e7d-4c83-a9aa-1dbcefc90477
UUID.V4.squuid
// res10: UUID = 5e15a15b-e2df-4e09-b6b6-42a7c41d64fb
```

## Install

## References

- [RFC 4122 - A Universally Unique Identifier](https://www.ietf.org/rfc/rfc4122.txt)
- JVM [UUID type](https://docs.oracle.com/javase/7/docs/api/java/util/UUID.html)
- [clj-uuid](https://github.com/danlentz/clj-uuid) Clojure implementation
