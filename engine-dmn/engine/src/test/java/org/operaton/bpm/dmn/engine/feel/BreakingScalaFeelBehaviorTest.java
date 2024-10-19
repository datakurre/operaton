/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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
package org.operaton.bpm.dmn.engine.feel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import org.operaton.bpm.dmn.engine.DmnDecisionResult;
import org.operaton.bpm.dmn.engine.DmnEngine;
import org.operaton.bpm.dmn.engine.DmnEngineConfiguration;
import org.operaton.bpm.dmn.engine.feel.helper.TestPojo;
import org.operaton.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.operaton.bpm.dmn.engine.test.DecisionResource;
import org.operaton.bpm.dmn.engine.test.DmnEngineTest;
import org.operaton.bpm.dmn.feel.impl.FeelException;
import org.operaton.bpm.dmn.feel.impl.scala.ScalaFeelEngineFactory;
import org.operaton.bpm.engine.variable.Variables;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BreakingScalaFeelBehaviorTest extends DmnEngineTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

    /**
   * Returns the DMN engine configuration with a default configuration and a Scala FEEL engine factory set.
   * 
   * @return the DMN engine configuration
   */
  @Override
  public DmnEngineConfiguration getDmnEngineConfiguration() {
    DefaultDmnEngineConfiguration configuration = new DefaultDmnEngineConfiguration();
    configuration.setFeelEngineFactory(new ScalaFeelEngineFactory());
    return configuration;
  }

  // https://jira.operaton.com/browse/CAM-11304
    /**
   * Test method to compare a short untyped number in a decision table result.
   */
  @Test
  @DecisionResource(resource = "breaking_unary_test_compare_short_untyped.dmn")
  public void shouldCompareShortUntyped() {
    variables.putValue("numberInput", (short)5);

    assertThatDecisionTableResult()
      .hasSingleResult()
      .hasSingleEntry(true);
  }

  @Test
  @DecisionResource(resource = "breaking_unary_test_boolean.dmn")
  public void shouldEqualBoolean() {
    DefaultDmnEngineConfiguration configuration = (DefaultDmnEngineConfiguration) getDmnEngineConfiguration();
    DmnEngine engine = configuration.buildEngine();

    DmnDecisionResult decisionResult = engine.evaluateDecision(decision, Variables.createVariables());

    assertThat((String)decisionResult.getSingleEntry()).isEqualTo("foo");
  }

    /**
   * This method tests if a decision in a DMN model returns a boolean value equal to "foo".
   */
   @Test
    @DecisionResource(resource = "breaking_unary_test_boolean.dmn")
    public void shouldEqualBoolean() {
      DefaultDmnEngineConfiguration configuration = (DefaultDmnEngineConfiguration) getDmnEngineConfiguration();
      DmnEngine engine = configuration.buildEngine();
  
      DmnDecisionResult decisionResult = engine.evaluateDecision(decision, Variables.createVariables());
  
      assertThat((String)decisionResult.getSingleEntry()).isEqualTo("foo");
    }

    /**
   * This method tests the evaluation of a decision table involving timezone comparison with a typed value.
   */
  @Test
  @DecisionResource(resource = "breaking_compare_date_with_time_zone_untyped.dmn")
  public void shouldEvaluateTimezoneComparisonWithTypedValue() {
    // given a date typed value
    variables.putValue("date1", Variables.dateValue(new Date()));

    // when it is compared against timezone
    var evaluationResult = evaluateDecisionTable(dmnEngine);

    // then the evaluation is handled gracefully and empty results are returned despite the type mismatch
    assertThat(evaluationResult).isEmpty();
  }

    /**
   * This method tests the evaluation of timezone comparison with a date. 
   */
  @Test
    @DecisionResource(resource = "breaking_compare_date_with_time_zone_untyped.dmn")
    public void shouldEvaluateTimezoneComparisonWithDate() {
      // given a date
      variables.putValue("date1", new Date());
  
      // when it is compared against timezone
      var evaluationResult = evaluateDecisionTable(dmnEngine);
  
      // then the evaluation is handled gracefully and empty results are returned despite the type mismatch
      assertThat(evaluationResult).isEmpty();
    }

    /**
   * Test method to verify that single quotes are used correctly in string literals when evaluating a decision.
   */
  @Test
  @DecisionResource(resource = "breaking_single_quotes.dmn")
  public void shouldUseSingleQuotesInStringLiterals() {
    // given
    DefaultDmnEngineConfiguration configuration = (DefaultDmnEngineConfiguration) getDmnEngineConfiguration();
    DmnEngine engine = configuration.buildEngine();

    // then
    thrown.expect(FeelException.class);
    thrown.expectMessage("FEEL/SCALA-01008 Error while evaluating expression: failed to parse expression ''Hello World'': "
      + "Expected (start-of-input | negation | positiveUnaryTests | anyInput):1:1, found \"'Hello Wor\"");

    // when
    engine.evaluateDecision(decision, Variables.createVariables().putValue("input", "Hello World"));
  }

    /**
   * This method tests the comparison of two instances of TestPojo using a decision table.
   */
  @Ignore("CAM-11319")
  @Test
  @DecisionResource(resource = "breaking_pojo_comparison.dmn")
  public void shouldComparePojo() {
    // given
    variables.putValue("pojoOne", new TestPojo())
      .putValue("pojoTwo", new TestPojo());

    // then
    assertThatDecisionTableResult()
      .hasSingleResult()
      .hasSingleEntry("foo");
  }

}
