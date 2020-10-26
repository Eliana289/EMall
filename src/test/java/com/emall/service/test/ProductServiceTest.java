package com.emall.service.test;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.emall.common.ServerResponse;
import com.emall.service.IProductService;
import com.emall.test.TestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceTest extends TestBase {

    @Autowired
    private IProductService iProductService;

    @Test
    public void testIProductService(){
        ServerResponse<PageInfo> result =  iProductService.getProductByKeywordCategory("iphone",2,1,5,"price_desc");
        System.out.println(result);
    }

    public static void main(String[] args) {
        List<String> images = Lists.newArrayList();
        images.add("emall/aa.jpg");
        images.add("emall/bb.jpg");
        images.add("emall/cc.jpg");
        images.add("emall/dd.jpg");
        images.add("emall/ee.jpg");
//        ["emall/aa.jpg","emall/bb.jpg","emall/cc.jpg","emall/dd.jpg","emall/ee.jpg"]
    }
}
