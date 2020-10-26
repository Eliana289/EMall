package com.emall.service.test;

import com.github.pagehelper.PageInfo;
import com.emall.common.ServerResponse;
import com.emall.pojo.Shipping;
import com.emall.service.impl.ShippingServiceImpl;
import com.emall.test.TestBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ShippingServiceTest extends TestBase {


    @Autowired
    private ShippingServiceImpl iShippingService;

    @Test
    public void testSelect(){
        ServerResponse sr = iShippingService.select(1, 3);
        Shipping shipping = (Shipping)sr.getData();
        log.info(ToStringBuilder.reflectionToString(shipping));

    }

    @Test
    public void testList(){
        ServerResponse sr = iShippingService.list(1,1,2);
        PageInfo<Shipping> p=new PageInfo<Shipping>((List<Shipping>)sr.getData());
        log.info(ToStringBuilder.reflectionToString(sr.getData()));
        log.info("======");

        ServerResponse x = ServerResponse.createBySuccess(p);
        log.info(ToStringBuilder.reflectionToString(x));

    }
}
