/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.camunda.bpm.dmn.engine.impl.context;

import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.context.DmnContextFactory;
import org.camunda.bpm.dmn.engine.context.DmnDecisionContext;

public class DmnContextFactoryImpl implements DmnContextFactory {

  public DmnDecisionContext createDecisionContext(DmnEngineConfiguration configuration) {
    DmnDecisionContextImpl decisionContext = new DmnDecisionContextImpl();
    decisionContext.setHitPolicyHandlers(configuration.getHitPolicyHandlers());
    decisionContext.setScriptEngineResolver(configuration.getScriptEngineResolver());
    decisionContext.setFeelEngine(configuration.getFeelEngineProvider().createInstance());
    decisionContext.setDecisionTableListeners(configuration.getCustomDmnDecisionTableListeners());
    decisionContext.setDefaultAllowedValueExpressionLanguage(configuration.getDefaultAllowedValueExpressionLanguage());
    decisionContext.setDefaultInputEntryExpressionLanguage(configuration.getDefaultInputEntryExpressionLanguage());
    decisionContext.setDefaultInputExpressionExpressionLanguage(configuration.getDefaultInputExpressionExpressionLanguage());
    decisionContext.setDefaultOutputEntryExpressionLanguage(configuration.getDefaultOutputEntryExpressionLanguage());
    decisionContext.setElProvider(configuration.getElProvider());
    return decisionContext;
  }

}
