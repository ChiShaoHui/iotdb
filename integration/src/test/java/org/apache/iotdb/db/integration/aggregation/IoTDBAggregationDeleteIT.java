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
package org.apache.iotdb.db.integration.aggregation;

import org.apache.iotdb.db.conf.IoTDBDescriptor;
import org.apache.iotdb.integration.env.ConfigFactory;
import org.apache.iotdb.integration.env.EnvFactory;
import org.apache.iotdb.itbase.category.ClusterTest;
import org.apache.iotdb.itbase.category.LocalStandaloneTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.iotdb.db.constant.TestConstant.count;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Category({LocalStandaloneTest.class, ClusterTest.class})
public class IoTDBAggregationDeleteIT {

  private static String[] dataSet =
      new String[] {
        "INSERT INTO root.turbine.d1(timestamp,s1) values(1,1)",
        "INSERT INTO root.turbine.d1(timestamp,s1) values(2,2)",
        "INSERT INTO root.turbine.d1(timestamp,s1) values(3,3)",
        "INSERT INTO root.turbine.d1(timestamp,s1) values(4,4)",
        "INSERT INTO root.turbine.d1(timestamp,s1) values(5,5)",
        "flush",
        "delete from root.turbine.d1.s1 where time < 3"
      };
  private long prevPartitionInterval;

  @Before
  public void setUp() throws Exception {
    prevPartitionInterval = IoTDBDescriptor.getInstance().getConfig().getPartitionInterval();
    ConfigFactory.getConfig().setPartitionInterval(1000);
    EnvFactory.getEnv().initBeforeTest();
    prepareData();
  }

  @After
  public void tearDown() throws Exception {
    EnvFactory.getEnv().cleanAfterTest();
    ConfigFactory.getConfig().setPartitionInterval(prevPartitionInterval);
  }

  @Test
  public void countAfterDeleteTest() throws SQLException {
    try (Connection connection = EnvFactory.getEnv().getConnection();
        Statement statement = connection.createStatement()) {
      boolean hasResultSet = statement.execute("select count(*) from root");

      assertTrue(hasResultSet);
      try (ResultSet resultSet = statement.getResultSet()) {
        while (resultSet.next()) {
          assertEquals("3", resultSet.getString(count("root.turbine.d1.s1")));
        }
      }
    }
  }

  private void prepareData() throws SQLException {
    try (Connection connection = EnvFactory.getEnv().getConnection();
        Statement statement = connection.createStatement()) {

      for (String sql : dataSet) {
        statement.execute(sql);
      }
    }
  }
}
