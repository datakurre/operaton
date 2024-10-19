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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.operaton.bpm.dmn.engine.DmnEngineException;
import org.operaton.bpm.dmn.engine.impl.spi.type.DmnDataTypeTransformer;
import org.operaton.bpm.engine.variable.Variables;
import org.operaton.bpm.engine.variable.value.DateValue;
import org.operaton.bpm.engine.variable.value.TypedValue;
import org.camunda.feel.syntaxtree.ZonedTime;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Transform values of type {@link Date} and {@link String} into
 * {@link DateValue} which contains date and time. A String should have the format
 * {@code yyyy-MM-dd'T'HH:mm:ss}.
 *
 * @author Philipp Ossler
 */
public class DateDataTypeTransformer implements DmnDataTypeTransformer {

  protected String formatPattern = "yyyy-MM-dd'T'HH:mm:ss";

    /**
   * Transforms the given object value into a TypedValue representing a Date.
   *
   * @param value the object value to transform
   * @return a TypedValue representing the Date
   * @throws IllegalArgumentException if the value is not a supported type
   */
  @Override
  public TypedValue transform(Object value) throws IllegalArgumentException {
    if (value instanceof Date) {
      return Variables.dateValue((Date) value);

    } else if (value instanceof String) {
      Date date = transformString((String) value);
      return Variables.dateValue(date);

    } if (value instanceof ZonedDateTime) {
      Instant instant = ((ZonedDateTime) value).toInstant();
      Date date = Date.from(instant);

      return Variables.dateValue(date);

    } else if (value instanceof LocalDateTime) {
      ZoneId defaultTimeZone = ZoneId.systemDefault();
      Instant instant = ((LocalDateTime) value)
        .atZone(defaultTimeZone)
        .toInstant();

      Date date = Date.from(instant);

      return Variables.dateValue(date);

    } else if (value instanceof LocalDate) {
      throw unsupportedType(value);

    } else if (value instanceof LocalTime) {
      throw unsupportedType(value);

    } else if (value instanceof Duration) {
      throw unsupportedType(value);

    } else if (value instanceof Period) {
      throw unsupportedType(value);

    } else if (value instanceof ZonedTime) {
      throw unsupportedType(value);

    } else {
      throw new IllegalArgumentException();
    }
  }

    /**
   * Parses a string value into a Date object using the specified format pattern.
   *
   * @param value the string value to be parsed
   * @return a Date object representing the parsed value
   * @throws IllegalArgumentException if the string value cannot be parsed
   */
  protected Date transformString(String value) {
    try {
      return new SimpleDateFormat(formatPattern).parse(value);
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
  }

    /**
   * Returns a DmnEngineException with a message stating that the provided value's type cannot be converted to java.util.Date.
   * 
   * @param value the object whose type is to be checked
   * @return a DmnEngineException with a message indicating the unsupported type
   */
  protected DmnEngineException unsupportedType(Object value) {
    String className = value.getClass().getName();
    return new DmnEngineException("Unsupported type: '" + className +
      "' cannot be converted to 'java.util.Date'");
  }

}
