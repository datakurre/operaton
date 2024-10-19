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
import org.operaton.bpm.engine.variable.value.LongValue;
import org.operaton.bpm.engine.variable.value.TypedValue;

/**
 * Transform values of type {@link Number} and {@link String} into {@link LongValue}.
 *
 * @author Philipp Ossler
 *
 */
public class LongDataTypeTransformer implements DmnDataTypeTransformer {

    /**
   * Transforms the given value into a TypedValue based on its type.
   *
   * @param value the value to transform
   * @return the transformed TypedValue
   * @throws IllegalArgumentException if the value is not a Number or String
   */
  @Override
  public TypedValue transform(Object value) throws IllegalArgumentException {
    if (value instanceof Number) {
      long longValue = transformNumber((Number) value);
      return Variables.longValue(longValue);

    } else if (value instanceof String) {
      long longValue = transformString((String) value);
      return Variables.longValue(longValue);

    } else {
      throw new IllegalArgumentException();
    }
  }

    /**
   * Transforms a Number object into a long value if it is of type Long.
   * 
   * @param value the Number object to transform
   * @return the long value of the Number object
   * @throws IllegalArgumentException if the Number object is not of type Long
   */
  protected long transformNumber(Number value) {
    if(isLong(value)) {
      return value.longValue();
    } else {
      throw new IllegalArgumentException();
    }
  }

    /**
   * Checks if the given number is a long value without any decimal places.
   * 
   * @param value the Number to be checked
   * @return true if the Number is a long value, false otherwise
   */
  protected boolean isLong(Number value) {
    double doubleValue = value.doubleValue();
    return doubleValue == (long) doubleValue;
  }

    /**
   * Parses a given string value to a long data type.
   * 
   * @param value the string value to be parsed
   * @return the long value parsed from the input string
   */
  protected long transformString(String value) {
    return Long.parseLong(value);
  }

}
