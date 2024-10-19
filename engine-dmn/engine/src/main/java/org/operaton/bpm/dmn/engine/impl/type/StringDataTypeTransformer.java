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
package org.operaton.bpm.dmn.engine.impl.type;

import org.operaton.bpm.dmn.engine.impl.spi.type.DmnDataTypeTransformer;
import org.operaton.bpm.engine.variable.Variables;
import org.operaton.bpm.engine.variable.value.StringValue;
import org.operaton.bpm.engine.variable.value.TypedValue;

/**
 * Transform values of type {@link Object} into {@link StringValue}.
 *
 * @author Philipp Ossler
 */
public class StringDataTypeTransformer implements DmnDataTypeTransformer {

    /**
   * Transforms the given object value into a TypedValue object.
   * 
   * @param value the object value to transform
   * @return the TypedValue object representing the transformed value
   * @throws IllegalArgumentException if the transformation fails
   */
  @Override
  public TypedValue transform(Object value) throws IllegalArgumentException {
    String stringValue = String.valueOf(value);
    return Variables.stringValue(stringValue);
  }

}
