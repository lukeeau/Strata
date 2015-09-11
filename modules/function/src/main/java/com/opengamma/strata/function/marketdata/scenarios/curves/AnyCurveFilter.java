/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.function.marketdata.scenarios.curves;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.strata.engine.marketdata.scenarios.MarketDataFilter;
import com.opengamma.strata.market.curve.Curve;
import com.opengamma.strata.market.id.CurveId;

/**
 * A market data filter that matches any curve.
 * <p>
 * The {@link #matches} method always returns true.
 */
@BeanDefinition(builderScope = "private")
public final class AnyCurveFilter
    implements MarketDataFilter<Curve, CurveId>, ImmutableBean {

  /**
   * The single shared instance.
   */
  public static final AnyCurveFilter INSTANCE = new AnyCurveFilter();

  //-------------------------------------------------------------------------
  @Override
  public boolean matches(CurveId marketDataId, Curve marketData) {
    return true;
  }

  @Override
  public Class<?> getMarketDataIdType() {
    return CurveId.class;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code AnyCurveFilter}.
   * @return the meta-bean, not null
   */
  public static AnyCurveFilter.Meta meta() {
    return AnyCurveFilter.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(AnyCurveFilter.Meta.INSTANCE);
  }

  private AnyCurveFilter() {
  }

  @Override
  public AnyCurveFilter.Meta metaBean() {
    return AnyCurveFilter.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(32);
    buf.append("AnyCurveFilter{");
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code AnyCurveFilter}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null);

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    public BeanBuilder<? extends AnyCurveFilter> builder() {
      return new AnyCurveFilter.Builder();
    }

    @Override
    public Class<? extends AnyCurveFilter> beanType() {
      return AnyCurveFilter.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code AnyCurveFilter}.
   */
  private static final class Builder extends DirectFieldsBeanBuilder<AnyCurveFilter> {

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      throw new NoSuchElementException("Unknown property: " + propertyName);
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      throw new NoSuchElementException("Unknown property: " + propertyName);
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public AnyCurveFilter build() {
      return new AnyCurveFilter();
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      return "AnyCurveFilter.Builder{}";
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
