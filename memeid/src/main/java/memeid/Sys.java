/*
 * Copyright 2019-2020 47 Degrees, LLC. <http://www.47deg.com>
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;

import static java.util.function.Function.identity;

public class Sys {

    public static Set<String> getAddresses() throws UnknownHostException, SocketException {
        InetAddress localHost = InetAddress.getLocalHost();

        String hostName = localHost.getCanonicalHostName();

        Stream<String> local = Stream.of(localHost.toString(), hostName);
        Stream<String> inetAddresses = stream(InetAddress.getAllByName(hostName)).map(InetAddress::toString);
        Stream<String> networkInterfaces = Collections
                .list(NetworkInterface.getNetworkInterfaces())
                .stream()
                .map(NetworkInterface::toString);

        return concat(local, concat(inetAddresses, networkInterfaces)).collect(Collectors.toSet());
    }


    public static Map<String, String> getProperties() {
        Properties properties = System.getProperties();

        return properties.stringPropertyNames()
                .stream()
                .collect(Collectors.toMap(identity(), properties::getProperty));
    }

}
