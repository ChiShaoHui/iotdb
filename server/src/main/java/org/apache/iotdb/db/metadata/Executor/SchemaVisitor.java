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

package org.apache.iotdb.db.metadata.Executor;

import org.apache.iotdb.common.rpc.thrift.TSStatus;
import org.apache.iotdb.commons.conf.IoTDBConstant;
import org.apache.iotdb.db.exception.metadata.MetadataException;
import org.apache.iotdb.db.metadata.schemaregion.ISchemaRegion;
import org.apache.iotdb.db.mpp.sql.planner.plan.node.PlanNode;
import org.apache.iotdb.db.mpp.sql.planner.plan.node.PlanVisitor;
import org.apache.iotdb.db.mpp.sql.planner.plan.node.metedata.write.CreateAlignedTimeSeriesNode;
import org.apache.iotdb.db.mpp.sql.planner.plan.node.metedata.write.CreateTimeSeriesNode;
import org.apache.iotdb.db.qp.physical.PhysicalPlan;
import org.apache.iotdb.db.qp.physical.sys.CreateAlignedTimeSeriesPlan;
import org.apache.iotdb.db.qp.physical.sys.CreateTimeSeriesPlan;
import org.apache.iotdb.rpc.RpcUtils;
import org.apache.iotdb.rpc.TSStatusCode;
import org.apache.iotdb.tsfile.exception.NotImplementedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Schema write PlanNode visitor */
public class SchemaVisitor extends PlanVisitor<TSStatus, ISchemaRegion> {
  private static final Logger logger = LoggerFactory.getLogger(SchemaVisitor.class);

  @Override
  public TSStatus visitCreateTimeSeries(CreateTimeSeriesNode node, ISchemaRegion schemaRegion) {
    try {
      PhysicalPlan plan = node.accept(new PhysicalPlanTransformer(), new TransformerContext());
      schemaRegion.createTimeseries((CreateTimeSeriesPlan) plan, -1);
    } catch (MetadataException e) {
      logger.error("{}: MetaData error: ", IoTDBConstant.GLOBAL_DB_NAME, e);
      return RpcUtils.getStatus(TSStatusCode.METADATA_ERROR, e.getMessage());
    }
    return RpcUtils.getStatus(TSStatusCode.SUCCESS_STATUS, "Execute successfully");
  }

  @Override
  public TSStatus visitPlan(PlanNode node, ISchemaRegion context) {
    return null;
  }

  private static class PhysicalPlanTransformer
      extends PlanVisitor<PhysicalPlan, TransformerContext> {
    @Override
    public PhysicalPlan visitPlan(PlanNode node, TransformerContext context) {
      throw new NotImplementedException();
    }

    public PhysicalPlan visitCreateTimeSeries(
        CreateTimeSeriesNode node, TransformerContext context) {
      return new CreateTimeSeriesPlan(
          node.getPath(),
          node.getDataType(),
          node.getEncoding(),
          node.getCompressor(),
          node.getProps(),
          node.getTags(),
          node.getAttributes(),
          node.getAlias());
    }

    public PhysicalPlan visitCreateAlignedTimeSeries(
        CreateAlignedTimeSeriesNode node, TransformerContext context) {
      return new CreateAlignedTimeSeriesPlan(
          node.getDevicePath(),
          node.getMeasurements(),
          node.getDataTypes(),
          node.getEncodings(),
          node.getCompressors(),
          node.getAliasList(),
          node.getTagsList(),
          node.getAttributesList());
    }
  }

  private static class TransformerContext {}
}
