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
package org.apache.iotdb.db.mpp.sql.plan.node.source;

import org.apache.iotdb.commons.consensus.DataRegionId;
import org.apache.iotdb.commons.partition.RegionReplicaSet;
import org.apache.iotdb.db.exception.metadata.IllegalPathException;
import org.apache.iotdb.db.exception.query.QueryProcessException;
import org.apache.iotdb.db.metadata.path.AlignedPath;
import org.apache.iotdb.db.mpp.sql.plan.node.PlanNodeDeserializeHelper;
import org.apache.iotdb.db.mpp.sql.planner.plan.node.PlanNodeId;
import org.apache.iotdb.db.mpp.sql.planner.plan.node.source.SeriesScanNode;
import org.apache.iotdb.db.mpp.sql.statement.component.OrderBy;
import org.apache.iotdb.tsfile.read.filter.GroupByFilter;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SeriesScanNodeSerdeTest {

  @Test
  public void TestSerializeAndDeserialize() throws QueryProcessException, IllegalPathException {
    SeriesScanNode seriesScanNode =
        new SeriesScanNode(
            new PlanNodeId("TestSeriesScanNode"),
            new AlignedPath("s1"),
            new RegionReplicaSet(new DataRegionId(1), new ArrayList<>()));
    seriesScanNode.setTimeFilter(new GroupByFilter(1, 2, 3, 4));
    seriesScanNode.setScanOrder(OrderBy.TIMESTAMP_ASC);
    ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
    seriesScanNode.serialize(byteBuffer);
    byteBuffer.flip();
    assertEquals(PlanNodeDeserializeHelper.deserialize(byteBuffer), seriesScanNode);
  }
}
