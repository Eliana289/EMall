package com.alipay.demo.trade;

import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.*;
import com.alipay.demo.trade.model.hb.*;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayMonitorService;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayMonitorServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.service.impl.AlipayTradeWithHBServiceImpl;
import com.alipay.demo.trade.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    private static Log log = LogFactory.getLog(Main.class);

    private static AlipayTradeService   tradeService;

    private static AlipayTradeService   tradeWithHBService;

    // from Alipay api
    private static AlipayMonitorService monitorService;

    static {
        Configs.init("zfbinfo.properties");
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
            .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
            .setFormat("json").build();
    }

    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.test_trade_precreate();
    }

    public void test_monitor_schedule_logic() {
        DemoHbRunner demoRunner = new DemoHbRunner(monitorService);
        demoRunner.setDelay(5); 
        demoRunner.setDuration(10); 
        demoRunner.schedule();

        while (Math.random() != 0) {
            test_trade_pay(tradeWithHBService);
            Utils.sleep(5 * 1000);
        }

        demoRunner.shutdown();
    }

    public void test_monitor_sys() {
        List<SysTradeInfo> sysTradeInfoList = new ArrayList<SysTradeInfo>();
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000001", 5.2, HbStatus.S));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000002", 4.4, HbStatus.F));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000003", 11.3, HbStatus.P));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000004", 3.2, HbStatus.X));
        sysTradeInfoList.add(SysTradeInfo.newInstance("00000005", 4.1, HbStatus.X));

        List<ExceptionInfo> exceptionInfoList = new ArrayList<ExceptionInfo>();
        exceptionInfoList.add(ExceptionInfo.HE_SCANER);

        Map<String, Object> extendInfo = new HashMap<String, Object>();

        String appAuthToken = "authentication";

        AlipayHeartbeatSynRequestBuilder builder = new AlipayHeartbeatSynRequestBuilder()
            .setAppAuthToken(appAuthToken).setProduct(Product.FP).setType(Type.CR)
            .setEquipmentId("cr1000001").setEquipmentStatus(EquipStatus.NORMAL)
            .setTime(Utils.toDate(new Date())).setStoreId("store10001").setMac("0a:00:27:00:00:00")
            .setNetworkType("LAN").setProviderId("2088911212323549") 
            .setSysTradeInfoList(sysTradeInfoList) 
            .setExtendInfo(extendInfo) 
        ;

        MonitorHeartbeatSynResponse response = monitorService.heartbeatSyn(builder);
        dumpResponse(response);
    }

    public void test_monitor_pos() {
        List<PosTradeInfo> posTradeInfoList = new ArrayList<PosTradeInfo>();
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.S, "1324", 7));
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.X, "1326", 15));
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.S, "1401", 8));
        posTradeInfoList.add(PosTradeInfo.newInstance(HbStatus.F, "1405", 3));

        List<ExceptionInfo> exceptionInfoList = new ArrayList<ExceptionInfo>();
        exceptionInfoList.add(ExceptionInfo.HE_PRINTER);

        Map<String, Object> extendInfo = new HashMap<String, Object>();

        AlipayHeartbeatSynRequestBuilder builder = new AlipayHeartbeatSynRequestBuilder()
            .setProduct(Product.FP)
            .setType(Type.SOFT_POS)
            .setEquipmentId("soft100001")
            .setEquipmentStatus(EquipStatus.NORMAL)
            .setTime("2015-09-28 11:14:49")
            .setManufacturerPid("2088000000000009")
            .setStoreId("store200001").setEquipmentPosition("31.2433190000,121.5090750000")
            .setBbsPosition("2869719733-065|2896507033-091").setNetworkStatus("gggbbbgggnnn")
            .setNetworkType("3G").setBattery("98").setWifiMac("0a:00:27:00:00:00")
            .setWifiName("test_wifi_name").setIp("192.168.1.188")
            .setPosTradeInfoList(posTradeInfoList) 
            .setExtendInfo(extendInfo) 
        ;

        MonitorHeartbeatSynResponse response = monitorService.heartbeatSyn(builder);
        dumpResponse(response);
    }



    