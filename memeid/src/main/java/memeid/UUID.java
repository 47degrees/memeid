package memeid;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static memeid.Bits.*;

/**
 * A class that represents an immutable universally unique identifier (UUID).
 * A UUID represents a 128-bit value.
 *
 * @see <a href="https://tools.ietf.org/html/rfc4122">RFC-4122</a>
 */
public class UUID implements Comparable<UUID> {

    /**
     * The nil UUID is special form of UUID that is specified to have all 128 bits set to zero.
     *
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.7">RFC-4122</a>
     */
    public static final UUID NIL = new UUID(new java.util.UUID(0, 0));

    /**
     * Creates a valid {@link UUID} from two {@code long} values representing
     * the most/least significant bits.
     *
     * @param msb Most significant bit in {@code long} representation
     * @param lsb Least significant bit in {@code long} representation
     * @return a new {@link UUID} constructed from msb and lsb
     */
    public static UUID from(long msb, long lsb) {
        return fromUUID(new java.util.UUID(msb, lsb));
    }

    /**
     * Creates a valid {@link UUID} from a {@link java.util.UUID}.
     *
     * @param juuid the {@link java.util.UUID}
     * @return a valid {@link UUID} created from a {@link java.util.UUID}
     */
    public static UUID fromUUID(java.util.UUID juuid) {
        if (juuid.version() == 0 && juuid == NIL.juuid)
            return NIL;

        if (juuid.version() == 1) return new V1(juuid);
        if (juuid.version() == 2) return new V2(juuid);
        if (juuid.version() == 3) return new V3(juuid);
        if (juuid.version() == 4) return new V4(juuid);
        if (juuid.version() == 5) return new V5(juuid);

        return new UnknownVersion(juuid);
    }

    /**
     * Creates a {@code UUID} from the string standard representation as
     * described in the {@link #toString} method.
     *
     * @param name A string that specifies a {@code UUID}
     * @return A {@code UUID} with the specified value
     * @throws IllegalArgumentException If name does not conform to the string representation as
     *                                  described in {@link #toString}
     */
    public static UUID fromString(String name) {
        return fromUUID(java.util.UUID.fromString(name));
    }

    /**
     * Returns the most significant 64 bits of this UUID's 128 bit value.
     *
     * @return The most significant 64 bits of this UUID's 128 bit value
     */
    public long getMostSignificantBits() {
        return this.juuid.getMostSignificantBits();
    }

    /**
     * Returns the most significant 64 bits of this UUID's 128 bit value.
     *
     * @return The most significant 64 bits of this UUID's 128 bit value
     */
    public long getLeastSignificantBits() {
        return this.juuid.getLeastSignificantBits();
    }

    /**
     * The variant field determines the layout of the {@link UUID}.
     * <p>
     * The variant field consists of a variable number of
     * the most significant bits of octet 8 of the {@link UUID}.
     * <p>
     * The variant number has the following meaning:
     *
     * <ul>
     * <li>'''0''': Reserved for NCS backward compatibility</li>
     * <li>'''2''': <a href="https://tools.ietf.org/html/rfc4122#section-4.1.1">RFC-4122</a></li>
     * <li>'''6''': Reserved, Microsoft Corporation backward compatibility</li>
     * <li>'''7''': Reserved for future definition</li>
     * </ul>
     * <p>
     * Interoperability, in any form, with variants other than the one
     * defined here is not guaranteed, and is not likely to be an issue in
     * practice.
     *
     * @return The variant of this {@link UUID}
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.1">RFC-4122</a>
     */
    public int variant() {
        return this.juuid.variant();
    }

    /**
     * The version number associated with this {@link UUID}.  The version
     * number describes how this {@link UUID} was generated.
     * <p>
     * The version number has the following meaning:
     *
     * <ul>
     * <li>'''1''': Time-based UUID</li>
     * <li>'''2''': DCE security UUID</li>
     * <li>'''3''': Name-based UUID</li>
     * <li>'''4''': Randomly generated UUID</li>
     * <li>'''5''': The name-based version that uses SHA-1 hashing</li>
     * </ul>
     *
     * @return The version number of this {@link UUID}
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC-4122</a>
     */
    public int version() {
        return this.juuid.version();
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(UUID.class.isAssignableFrom(obj.getClass())))
            return false;

        return compareTo((UUID) obj) == 0;
    }

    @Override
    public int compareTo(UUID o) {
        int comparison = Long.compareUnsigned(getMostSignificantBits(), o.getMostSignificantBits());

        if (comparison == 0) {
            return Long.compareUnsigned(getLeastSignificantBits(), o.getLeastSignificantBits());
        } else return comparison;
    }

    @Override
    public int hashCode() {
        return this.juuid.hashCode();
    }

    /**
     * Returns a {@code String} object representing this {@code UUID}.
     *
     * <p> The UUID string representation is as described by this BNF:
     * <blockquote><pre>
     * {@code
     * UUID                   = <time_low> "-" <time_mid> "-"
     *                          <time_high_and_version> "-"
     *                          <variant_and_sequence> "-"
     *                          <node>
     * time_low               = 4*<hexOctet>
     * time_mid               = 2*<hexOctet>
     * time_high_and_version  = 2*<hexOctet>
     * variant_and_sequence   = 2*<hexOctet>
     * node                   = 6*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               =
     *       "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
     *       | "a" | "b" | "c" | "d" | "e" | "f"
     *       | "A" | "B" | "C" | "D" | "E" | "F"
     * }</pre></blockquote>
     *
     * @return A string representation of this {@code UUID}
     */
    @Override
    public String toString() {
        return this.juuid.toString();
    }

    /**
     * Returns this {@link UUID} as a {@link java.util.UUID}.
     *
     * @return this {@link UUID} as a {@link java.util.UUID}
     */
    public java.util.UUID asJava() {
        return this.juuid;
    }

    /**
     * Returns this {@link UUID} as a {@link V1} if versions match;
     * otherwise, returns {@link Optional#empty}.
     *
     * @return this {@link UUID} as a {@link V1} if versions match;
     * otherwise, returns {@link Optional#empty}.
     */
    public Optional<V1> asV1() {
        if (isV1()) return Optional.of((V1) this);
        else return Optional.empty();
    }

    /**
     * Returns this {@link UUID} as a {@link V2} if versions match;
     * otherwise, returns {@link Optional#empty}.
     *
     * @return this {@link UUID} as a {@link V2} if versions match;
     * otherwise, returns {@link Optional#empty}.
     */
    public Optional<V2> asV2() {
        if (isV2()) return Optional.of((V2) this);
        else return Optional.empty();
    }

    /**
     * Returns this {@link UUID} as a {@link V3} if versions match;
     * otherwise, returns {@link Optional#empty}.
     *
     * @return this {@link UUID} as a {@link V3} if versions match;
     * otherwise, returns {@link Optional#empty}.
     */
    public Optional<V3> asV3() {
        if (isV3()) return Optional.of((V3) this);
        else return Optional.empty();
    }

    /**
     * Returns this {@link UUID} as a {@link V4} if versions match;
     * otherwise, returns {@link Optional#empty}.
     *
     * @return this {@link UUID} as a {@link V4} if versions match;
     * otherwise, returns {@link Optional#empty}.
     */
    public Optional<V4> asV4() {
        if (isV4()) return Optional.of((V4) this);
        else return Optional.empty();
    }

    /**
     * Returns this {@link UUID} as a {@link V5} if versions match;
     * otherwise, returns {@link Optional#empty}.
     *
     * @return this {@link UUID} as a {@link V5} if versions match;
     * otherwise, returns {@link Optional#empty}.
     */
    public Optional<V5> asV5() {
        if (isV5()) return Optional.of((V5) this);
        else return Optional.empty();
    }

    /**
     * Returns {@code true} if this UUID is a
     * <a href="https://tools.ietf.org/html/rfc4122#section-4.1.7">NIL UUID</a>; otherwise,
     * returns {@code false}.
     *
     * @return {@code true} if this {@link UUID} is a {@code NIL UUID}; {@code false} otherwise
     */
    public boolean isNil() {
        return this == NIL;
    }

    /**
     * Returns {@code true} if this UUID is a {@link V1}; otherwise,
     * returns {@code false}.
     *
     * @return {@code true} if this {@link UUID} is a {@link V1}; {@code false} otherwise
     */
    public boolean isV1() {
        return this instanceof V1;
    }

    /**
     * Returns {@code true} if this UUID is a {@link V2}; otherwise,
     * returns {@code false}.
     *
     * @return {@code true} if this {@link UUID} is a {@link V2}; {@code false} otherwise
     */
    public boolean isV2() {
        return this instanceof V2;
    }

    /**
     * Returns {@code true} if this UUID is a {@link V3}; otherwise,
     * returns {@code false}.
     *
     * @return {@code true} if this {@link UUID} is a {@link V3}; {@code false} otherwise
     */
    public boolean isV3() {
        return this instanceof V3;
    }

    /**
     * Returns {@code true} if this UUID is a {@link V4}; otherwise,
     * returns {@code false}.
     *
     * @return {@code true} if this {@link UUID} is a {@link V4}; {@code false} otherwise
     */
    public boolean isV4() {
        return this instanceof V4;
    }

    /**
     * Returns {@code true} if this UUID is a {@link V5}; otherwise,
     * returns {@code false}.
     *
     * @return {@code true} if this {@link UUID} is a {@link V5}; {@code false} otherwise
     */
    public boolean isV5() {
        return this instanceof V5;
    }


    /**
     * Version 1 UUIDs are those generated using a timestamp and the MAC address of the
     * computer on which it was generated.
     *
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC-4122</a>
     */
    public final static class V1 extends UUID {

        /**
         * Get the time_low component of the timestamp field
         *
         * @return time_low component of the timestamp field
         * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.2">RFC-4122</a>
         */
        public final long timeLow() {
            return Bits.readByte(Mask.TIME_LOW, Offset.TIME_LOW, this.asJava().timestamp());
        }

        /**
         * Get the time_mid component of the timestamp field
         *
         * @return time_mid component of the timestamp field
         * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.2">RFC-4122</a>
         */
        public final long timeMid() {
            return Bits.readByte(Mask.TIME_MID, Offset.TIME_MID, this.asJava().timestamp());
        }

        /**
         * Get the time_high component of the timestamp field
         *
         * @return time_high component of the timestamp field
         * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.2">RFC-4122</a>
         */
        public final long timeHigh() {
            return Bits.readByte(Mask.TIME_HIGH, Offset.TIME_HIGH, this.asJava().timestamp());
        }

        /**
         * Get the clock_seq_low component of the clock sequence field
         *
         * @return clock_seq_low component of the clock sequence field
         * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.2">RFC-4122</a>
         */
        public final long clockSeqLow() {
            return Bits.readByte(Mask.CLOCK_SEQ_LOW, Offset.CLOCK_SEQ_LOW, this.asJava().clockSequence());
        }

        /**
         * Get the clock_seq_high component of the clock sequence field
         *
         * @return clock_seq_high component of the clock sequence field
         * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.2">RFC-4122</a>
         */
        public final long clockSeqHigh() {
            return Bits.readByte(Mask.CLOCK_SEQ_HIGH, Offset.CLOCK_SEQ_HIGH, this.asJava().clockSequence());
        }

        public V1(java.util.UUID uuid) {
            super(uuid);
        }

    }


    /**
     * DCE Security version, with embedded POSIX UIDs.
     *
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC-4122</a>
     */
    public final static class V2 extends UUID {

        public V2(java.util.UUID uuid) {
            super(uuid);
        }

    }

    /**
     * Version 3 UUIDs are those generated by hashing a namespace identifier and name using
     * MD5 as the hashing algorithm.
     *
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC-4122</a>
     */
    public final static class V3 extends UUID {

        /**
         * Construct a namespace name-based {@link V3} UUID. Uses MD5 as a hash algorithm
         *
         * @param namespace   {@link UUID} used for the {@link V3} generation
         * @param name        name used for the {@link V3} generation
         * @param nameToBytes function used to convert the name to a byte array
         * @return a {@link V3} UUID
         */
        public static <A> UUID from(UUID namespace, A name, Function<A, byte[]> nameToBytes) throws NoSuchAlgorithmException {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(toBytes(namespace.getMostSignificantBits()));
            messageDigest.update(toBytes(namespace.getLeastSignificantBits()));
            messageDigest.update(nameToBytes.apply(name));
            byte[] bytes = messageDigest.digest();

            long rawMsb = fromBytes(Arrays.copyOfRange(bytes, 0, 8));
            long rawLsb = fromBytes(Arrays.copyOfRange(bytes, 8, 16));

            long msb = writeByte(Mask.VERSION, Offset.VERSION, rawMsb, 3);
            long lsb = writeByte(Mask.HASHED, Offset.HASHED, rawLsb, 0x2);

            return new V3(new java.util.UUID(msb, lsb));
        }

        public V3(java.util.UUID uuid) {
            super(uuid);
        }

    }


    /**
     * Version 4 UUIDs are those generated using random numbers.
     *
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC-4122</a>
     */
    public final static class V4 extends UUID {

        /**
         * Construct a {@link V4} (random) UUID from the given `msb` and `lsb`.
         *
         * @param msb Most significant bit in long representation
         * @param lsb Least significant bit in long representation
         */
        public V4(long msb, long lsb) {
            this(new java.util.UUID(
                    writeByte(Mask.VERSION, Offset.VERSION, msb, 0x4),
                    writeByte(Mask.V4_LSB, Offset.V4_LSB, lsb, 0x2)));
        }

        /**
         * Construct a {@link V4} random UUID.
         *
         * @return the {@link V4} random UUID
         */
        public static UUID random() {
            return new V4(java.util.UUID.randomUUID());
        }

        /**
         * Constructs a SQUUID (random, time-based) {@link V4} UUID.
         *
         * @param posix the posix timestamp to be used to construct the UUID
         * @return a {@link V4} SQUUID.
         */
        public static UUID squuid(long posix) {
            final UUID uuid = random();

            final long msb = (posix << 32) | (uuid.getMostSignificantBits() & Mask.UB32);

            return new UUID.V4(new java.util.UUID(msb, uuid.getLeastSignificantBits()));
        }

        /**
         * Constructs a SQUUID (random, time-based) {@link V4} UUID using
         * the result of {@link System#currentTimeMillis()} as posix timestamp.
         *
         * @return a {@link V4} SQUUID.
         */
        public static UUID squuid() {
            return squuid(MILLISECONDS.toSeconds(System.currentTimeMillis()));
        }

        public V4(java.util.UUID uuid) {
            super(uuid);
        }

    }


    /**
     * Version 5 UUIDs are those generated by hashing a namespace identifier and name using
     * SHA-1 as the hashing algorithm.
     *
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC-4122</a>
     */
    public final static class V5 extends UUID {

        /**
         * Construct a namespace name-based {@link V5} UUID. Uses MD5 as a hash algorithm
         *
         * @param namespace   {@link UUID} used for the {@link V5} generation
         * @param name        name used for the {@link V5} generation
         * @param nameToBytes function used to convert the name to a byte array
         * @return a {@link V5} UUID
         */
        public static <A> UUID from(UUID namespace, A name, Function<A, byte[]> nameToBytes) throws NoSuchAlgorithmException {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(toBytes(namespace.getMostSignificantBits()));
            messageDigest.update(toBytes(namespace.getLeastSignificantBits()));
            messageDigest.update(nameToBytes.apply(name));
            byte[] bytes = messageDigest.digest();

            long rawMsb = fromBytes(Arrays.copyOfRange(bytes, 0, 8));
            long rawLsb = fromBytes(Arrays.copyOfRange(bytes, 8, 16));

            long msb = writeByte(Mask.VERSION, Offset.VERSION, rawMsb, 5);
            long lsb = writeByte(Mask.HASHED, Offset.HASHED, rawLsb, 0x2);

            return new V5(new java.util.UUID(msb, lsb));
        }

        public V5(java.util.UUID uuid) {
            super(uuid);
        }

    }


    /**
     * Not standard-version UUIDs. Includes the extracted version from the most significant bits.
     *
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC-4122</a>
     */
    public final static class UnknownVersion extends UUID {

        private UnknownVersion(java.util.UUID uuid) {
            super(uuid);
        }

    }

    private final java.util.UUID juuid;

    private UUID(java.util.UUID juuid) {
        this.juuid = juuid;
    }

}