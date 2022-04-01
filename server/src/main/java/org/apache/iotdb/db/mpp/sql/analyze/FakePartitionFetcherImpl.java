/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.iotdb.db.mpp.sql.analyze;

import org.apache.iotdb.commons.partition.*;

import java.util.*;

public class FakePartitionFetcherImpl implements IPartitionFetcher {
  @Override
  public DataPartitionInfo fetchDataPartitionInfo(DataPartitionQueryParam parameter) {
    return null;
  }

  @Override
  public DataPartitionInfo fetchDataPartitionInfos(List<DataPartitionQueryParam> parameterList) {
    String device1 = "root.sg.d1";
    String device2 = "root.sg.d22";
    String device3 = "root.sg.d333";

    DataPartitionInfo dataPartitionInfo = new DataPartitionInfo();
    Map<String, Map<DeviceGroupId, Map<TimePartitionId, List<RegionReplicaSet>>>> dataPartitionMap =
        new HashMap<>();
    Map<DeviceGroupId, Map<TimePartitionId, List<RegionReplicaSet>>> sgPartitionMap =
        new HashMap<>();

    List<RegionReplicaSet> d1DataRegions = new ArrayList<>();
    d1DataRegions.add(
        new RegionReplicaSet(
            new ConsensusGroupId(GroupType.DataRegion, 1),
            Arrays.asList(new Endpoint("192.0.1.1", 9000), new Endpoint("192.0.1.2", 9000))));
    d1DataRegions.add(
        new RegionReplicaSet(
            new ConsensusGroupId(GroupType.DataRegion, 2),
            Arrays.asList(new Endpoint("192.0.2.1", 9000), new Endpoint("192.0.2.2", 9000))));
    Map<TimePartitionId, List<RegionReplicaSet>> d1DataRegionMap = new HashMap<>();
    d1DataRegionMap.put(new TimePartitionId(), d1DataRegions);

    List<RegionReplicaSet> d2DataRegions = new ArrayList<>();
    d2DataRegions.add(
        new RegionReplicaSet(
            new ConsensusGroupId(GroupType.DataRegion, 3),
            Arrays.asList(new Endpoint("192.0.3.1", 9000), new Endpoint("192.0.3.2", 9000))));
    Map<TimePartitionId, List<RegionReplicaSet>> d2DataRegionMap = new HashMap<>();
    d2DataRegionMap.put(new TimePartitionId(), d2DataRegions);

    List<RegionReplicaSet> d3DataRegions = new ArrayList<>();
    d3DataRegions.add(
        new RegionReplicaSet(
            new ConsensusGroupId(GroupType.DataRegion, 1),
            Arrays.asList(new Endpoint("192.0.1.1", 9000), new Endpoint("192.0.1.2", 9000))));
    d3DataRegions.add(
        new RegionReplicaSet(
            new ConsensusGroupId(GroupType.DataRegion, 4),
            Arrays.asList(new Endpoint("192.0.4.1", 9000), new Endpoint("192.0.4.2", 9000))));
    Map<TimePartitionId, List<RegionReplicaSet>> d3DataRegionMap = new HashMap<>();
    d3DataRegionMap.put(new TimePartitionId(), d3DataRegions);

    sgPartitionMap.put(new DeviceGroupId(device1.length()), d1DataRegionMap);
    sgPartitionMap.put(new DeviceGroupId(device2.length()), d2DataRegionMap);
    sgPartitionMap.put(new DeviceGroupId(device3.length()), d3DataRegionMap);

    dataPartitionMap.put("root.sg", sgPartitionMap);

    dataPartitionInfo.setDataPartitionMap(dataPartitionMap);

    return dataPartitionInfo;
  }

  @Override
  public SchemaPartitionInfo fetchSchemaPartitionInfo(String devicePath) {
    return null;
  }

  @Override
  public SchemaPartitionInfo fetchSchemaPartitionInfos(List<String> devicePath) {
    return null;
  }

  @Override
  public PartitionInfo fetchPartitionInfo(DataPartitionQueryParam parameter) {
    return null;
  }

  @Override
  public PartitionInfo fetchPartitionInfos(List<DataPartitionQueryParam> parameterList) {
    return null;
  }
}
