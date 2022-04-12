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

include "common.thrift"
namespace java org.apache.iotdb.service.rpc.thrift

typedef i32 int
typedef i64 long

struct CreateSchemaRegionReq {
    1: required common.TRegionReplicaSet regionReplicaSet
    2: required string storageGroup
}

struct CreateDataRegionReq {
    1: required common.TRegionReplicaSet regionReplicaSet
    2: required string storageGroup
    3: optional long ttl
}

struct CreateDataPartitionReq{
    1: required list<int> dataNodeID
    2: required int dataRegionID
    3: required long timeInterval
}

struct MigrateSchemaRegionReq{
    1: required int sourceDataNodeID
    2: required int targetDataNodeID
    3: required int schemaRegionID
}
struct MigrateDataRegionReq{
    1: required int sourceDataNodeID
    2: required int targetDataNodeID
    3: required int dataRegionID
}

service ManagementIService {
    /**
      * Config node will create a schema region on a list of data nodes.
      *
      * @param data nodes of the schema region, and schema region id generated by config node
    **/
    common.TSStatus createSchemaRegion(CreateSchemaRegionReq req)

    /**
      * Config node will create a data region on a list of data nodes.
      *
      * @param data nodes of the data region, and data region id generated by config node
    **/
    common.TSStatus createDataRegion(CreateDataRegionReq req)

    /**
      * Config node will create a new data partition on a existing data region
      *
      * @param data nodes of the data region, data region id, and a new time interval
      * of data partition
    **/
    common.TSStatus createDataPartition(CreateDataPartitionReq req)

    /**
      * Config node will migrate a schema region from one data node to another
      *
      * @param previous data node in the schema region, new data node, and schema region id
    **/
    common.TSStatus migrateSchemaRegion(MigrateSchemaRegionReq req)

    /**
      * Config node will migrate a data region from one data node to another
      *
      * @param previous data node in the data region, new data node, and dataregion id
    **/
    common.TSStatus migrateDataRegion(MigrateDataRegionReq req)

}