/*
 * Copyright (C) 2017 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.loader.csv;

import static com.opengamma.strata.loader.csv.TradeCsvLoader.CURRENCY_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.DIRECTION_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.EXPIRY_DATE_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.EXPIRY_TIME_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.EXPIRY_ZONE_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.LONG_SHORT_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.NOTIONAL_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PAYMENT_DATE_CAL_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PAYMENT_DATE_CNV_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PAYMENT_DATE_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PREMIUM_AMOUNT_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PREMIUM_CURRENCY_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PREMIUM_DATE_CAL_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PREMIUM_DATE_CNV_FIELD;
import static com.opengamma.strata.loader.csv.TradeCsvLoader.PREMIUM_DATE_FIELD;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

import com.opengamma.strata.basics.currency.AdjustablePayment;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.Payment;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.BusinessDayConventions;
import com.opengamma.strata.basics.date.HolidayCalendarId;
import com.opengamma.strata.collect.Messages;
import com.opengamma.strata.collect.io.CsvRow;
import com.opengamma.strata.loader.LoaderUtils;
import com.opengamma.strata.product.TradeInfo;
import com.opengamma.strata.product.common.LongShort;
import com.opengamma.strata.product.common.PayReceive;
import com.opengamma.strata.product.fx.FxSingle;
import com.opengamma.strata.product.fx.FxSingleTrade;
import com.opengamma.strata.product.fxopt.FxVanillaOption;
import com.opengamma.strata.product.fxopt.FxVanillaOptionTrade;

/**
 * Loads FX vanilla option trades from CSV files.
 */
class FxVanillaOptionTradeCsvLoader {

  private static final String LEG_1_DIRECTION_FIELD = "Leg 1 " + DIRECTION_FIELD;
  private static final String LEG_1_PAYMENT_DATE_FIELD = "Leg 1 " + PAYMENT_DATE_FIELD;
  private static final String LEG_1_CURRENCY_FIELD = "Leg 1 " + CURRENCY_FIELD;
  private static final String LEG_1_NOTIONAL_FIELD = "Leg 1 " + NOTIONAL_FIELD;

  private static final String LEG_2_DIRECTION_FIELD = "Leg 2 " + DIRECTION_FIELD;
  private static final String LEG_2_PAYMENT_DATE_FIELD = "Leg 2 " + PAYMENT_DATE_FIELD;
  private static final String LEG_2_CURRENCY_FIELD = "Leg 2 " + CURRENCY_FIELD;
  private static final String LEG_2_NOTIONAL_FIELD = "Leg 2 " + NOTIONAL_FIELD;

  /**
   * Parses the data from a CSV row.
   *
   * @param row  the CSV row object
   * @param info  the trade info object
   * @param resolver  the resolver used to parse additional information. This is not currently used in this method.
   * @return the parsed trade, as an instance of {@link FxSingleTrade}
   */
  static FxVanillaOptionTrade parse(CsvRow row, TradeInfo info, TradeCsvInfoResolver resolver) {
    FxSingle single = FxSingleTradeCsvLoader.parseFxSingle(row, "");
    FxVanillaOptionTrade trade = parseRow(row, info, single);
    return resolver.completeTrade(row, trade);
  }

  // parses the trade
  private static FxVanillaOptionTrade parseRow(CsvRow row, TradeInfo info, FxSingle underlying) {
    LongShort longShort = LoaderUtils.parseLongShort(row.getValue(LONG_SHORT_FIELD));
    LocalDate expiryDate = LoaderUtils.parseDate(row.getValue(EXPIRY_DATE_FIELD));
    LocalTime expiryTime = LoaderUtils.parseTime(row.getValue(EXPIRY_TIME_FIELD));
    ZoneId expiryZone = LoaderUtils.parseZoneId(row.getValue(EXPIRY_ZONE_FIELD));
    AdjustablePayment premium = CsvLoaderUtils.parseAdjustablePayment(
        row,
        PREMIUM_CURRENCY_FIELD,
        PREMIUM_AMOUNT_FIELD,
        PREMIUM_DATE_FIELD,
        PREMIUM_DATE_CNV_FIELD,
        PREMIUM_DATE_CAL_FIELD);

    FxVanillaOption option = FxVanillaOption.builder()
        .longShort(longShort)
        .expiryDate(expiryDate)
        .expiryTime(expiryTime)
        .expiryZone(expiryZone)
        .underlying(underlying)
        .build();
    return FxVanillaOptionTrade.builder()
        .info(info)
        .product(option)
        .premium(premium)
        .build();
  }

  // parse an FxSingle
  static FxSingle parseFxSingle(CsvRow row, String prefix) {
    PayReceive direction1 = LoaderUtils.parsePayReceive(row.getValue(prefix + LEG_1_DIRECTION_FIELD));
    Currency currency1 = Currency.of(row.getValue(prefix + LEG_1_CURRENCY_FIELD));
    double notional1 = LoaderUtils.parseDouble(row.getValue(prefix + LEG_1_NOTIONAL_FIELD));
    LocalDate paymentDate1 = row.findValue(prefix + LEG_1_PAYMENT_DATE_FIELD)
        .map(str -> LoaderUtils.parseDate(str))
        .orElseGet(() -> LoaderUtils.parseDate(row.getValue(prefix + PAYMENT_DATE_FIELD)));
    PayReceive direction2 = LoaderUtils.parsePayReceive(row.getValue(prefix + LEG_2_DIRECTION_FIELD));
    Currency currency2 = Currency.of(row.getValue(prefix + LEG_2_CURRENCY_FIELD));
    double notional2 = LoaderUtils.parseDouble(row.getValue(prefix + LEG_2_NOTIONAL_FIELD));
    LocalDate paymentDate2 = row.findValue(prefix + LEG_2_PAYMENT_DATE_FIELD)
        .map(str -> LoaderUtils.parseDate(str))
        .orElseGet(() -> LoaderUtils.parseDate(row.getValue(prefix + PAYMENT_DATE_FIELD)));
    Optional<BusinessDayAdjustment> paymentAdj = parsePaymentDateAdjustment(row);
    if (direction1.equals(direction2)) {
      throw new IllegalArgumentException(Messages.format(
          "FxSingle legs must not have the same direction: {}, {}",
          direction1.toString(),
          direction2.toString()));
    }
    Payment payment1 = Payment.of(currency1, direction1.normalize(notional1), paymentDate1);
    Payment payment2 = Payment.of(currency2, direction2.normalize(notional2), paymentDate2);
    return paymentAdj
        .map(adj -> FxSingle.of(payment1, payment2, adj))
        .orElseGet(() -> FxSingle.of(payment1, payment2));
  }

  // parses the payment date adjustment, which consists of two linked optional fields
  static Optional<BusinessDayAdjustment> parsePaymentDateAdjustment(CsvRow row) {
    Optional<BusinessDayAdjustment> paymentAdj = Optional.empty();
    Optional<String> paymentDateCnv = row.findValue(PAYMENT_DATE_CNV_FIELD); // Optional field with Business day adjustment
    if (paymentDateCnv.isPresent()) {
      BusinessDayConvention bdCnv = LoaderUtils.parseBusinessDayConvention(paymentDateCnv.get());
      if (!bdCnv.equals(BusinessDayConventions.NO_ADJUST)) {
        Optional<String> paymentDateCalOpt = row.findValue(PAYMENT_DATE_CAL_FIELD);
        if (paymentDateCalOpt.isPresent()) {
          paymentAdj = Optional.of(BusinessDayAdjustment.of(
              LoaderUtils.parseBusinessDayConvention(paymentDateCnv.get()), HolidayCalendarId.of(paymentDateCalOpt.get())));
        }
      }
    }
    return paymentAdj;
  }

}
