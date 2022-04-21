package com.zhjh.test;

import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/10/22.
 */

public class ToolTest{

    @Test
    public void TestSelect(){
        String estimateCompleteTime="2022-02-24 12:00:00";
        String nowTime=ToolCommon.getNowTime();
        String hour=ToolCommon.getHourTimeDistance(nowTime,estimateCompleteTime);
        System.out.print(hour);
    }


}
