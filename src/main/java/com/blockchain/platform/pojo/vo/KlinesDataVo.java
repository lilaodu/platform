package com.blockchain.platform.pojo.vo;

import lombok.Data;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

/*
 * @author:wx
 * @description:
 * @create:2018-08-29  23:47
 */
@Measurement(name = "kline_new")
@Data
public class KlinesDataVo {
    @Column(name = "time")
    Instant time;
    @Column(name="first")
    double first;
    @Column(name="max")
    double max;
    @Column(name="min")
    double min;
    @Column(name="last")
    double last;
    @Column(name="sum")
    double sum;
}
