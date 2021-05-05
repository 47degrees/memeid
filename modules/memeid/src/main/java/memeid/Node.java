/*
 * Copyright 2019-2021 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package memeid;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;

import static memeid.Bits.fromBytes;

public class Node {

    private static Node instance;

    public static Node getInstance() {
        if (instance == null) {
            try {
                instance = new Node();
            } catch (SocketException | UnknownHostException | NoSuchAlgorithmException e) {
                throw new RuntimeException("Unable to instantiate Node", e);
            }
        }

        return instance;
    }

    /**
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.5">RFC-4122</a>
     */
    public final short clockSequence;

    /**
     * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.5">RFC-4122</a>
     */
    public final long id;

    public Node() throws SocketException, UnknownHostException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        Stream.Builder<String> builder = Stream.builder();

        for (String key : DATA_SOURCES) {
            builder.accept(System.getProperty(key));
        }

        InetAddress localHost = InetAddress.getLocalHost();
        String hostName = localHost.getCanonicalHostName();

        builder.accept(localHost.toString());
        builder.accept(hostName);

        for (InetAddress i : InetAddress.getAllByName(hostName)) {
            builder.accept(i.toString());
        }

        for (NetworkInterface n : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            builder.accept(n.toString());
        }

        builder.build()
                .filter(Objects::nonNull)
                .distinct()
                .map(String::getBytes)
                .forEach(messageDigest::update);

        byte[] digest = messageDigest.digest();

        byte[] bytes = {0, 0, digest[0], digest[1], digest[2], digest[3], digest[4], (byte) (digest[5] | 0x01)};

        clockSequence = (short) new Random().nextInt(Short.MAX_VALUE + 1);
        id = fromBytes(bytes);
    }

    public Node(short clockSequence, long id) {
        this.clockSequence = clockSequence;
        this.id = id;
    }

    private static final List<String> DATA_SOURCES = Arrays.asList("java.vendor", "java.vendor.url", "java.version", "os.arch", "os.name", "os.version");

}
