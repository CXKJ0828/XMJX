package com.zhjh.test;

import com.zhjh.bean.ComboboxEntity;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/1.
 */
public class DaoTest extends BasicTest{
    @Resource(name="processMapper")
    private ProcessMapper processMapper;
    @Resource(name="progressSearchMapper")
    private ProgressSearchMapper progressSearchMapper;
    @Resource(name="clientMapper")
    private ClientMapper clientMapper;
    @Resource(name="systemconfigMapper")
    private SystemconfigMapper systemconfigMapper;

    @Resource(name="blankProcessMapper")
    private BlankProcessMapper blankProcessMapper;
    @Resource(name="orderDetailsMapper")
    private OrderDetailsMapper orderDetailsMapper;
    @Resource(name="goodMapper")
    private GoodMapper goodMapper;
    @Resource(name="materialBuyOrderDetailsMapper")
    private MaterialBuyOrderDetailsMapper materialBuyOrderDetailsMapper;
    @Resource(name="toolOutMapper")
    private ToolOutMapper toolOutMapper;
    @Resource(name="goodproductCategoryMapper")
    private GoodproductCategoryMapper goodproductCategoryMapper;
    @Resource(name="workersubmitMapper")
    private WorkersubmitMapper workersubmitMapper;

    @Resource(name="heatTreatMapper")
    private HeatTreatMapper heatTreatMapper;

    @Resource(name="heattreatcheckMapper")
    private HeattreatcheckMapper heattreatcheckMapper;

    @Resource(name="workersubmitHeatTreatMapper")
    private WorkersubmitHeatTreatMapper workersubmitHeatTreatMapper;

    @Resource(name="blankMapper")
    private BlankMapper blankMapper;

    @Resource(name="employeeMapper")
    private EmployeeMapper employeeMapper;
    @Resource(name="userMapper")
    private UserMapper userMapper;

    @Resource(name="materialbuyorderdetailsBlankMapper")
    private   MaterialbuyorderdetailsBlankMapper materialbuyorderdetailsBlankMapper;


    @Test
    public void testHeatTreat(){
       boolean result=false;
        WorkersubmitHeatTreatFormMap workersubmitHeatTreatFormMap=workersubmitHeatTreatMapper.findMaxEstimateCompleteTimeAndIdByUserId("435");
       if(workersubmitHeatTreatFormMap==null){//未接活儿
           result=true;
       }else{
           String estimateCompleteTime=workersubmitHeatTreatFormMap.getStr("estimateCompleteTime");
          if(estimateCompleteTime==null){//未接活儿
              result=true;
          }else{
              String nowTime=ToolCommon.getNowTime();
              String hour=ToolCommon.getHourTimeDistance(nowTime,estimateCompleteTime);
              if(ToolCommon.StringToFloat(hour)<4){
                  result=true;
              }else{
                  result=false;
              }
          }

       }
    }

    @Test
    public void TestBlankProcess(){
        HeattreatcheckFormMap heattreatcheckFormMap=new HeattreatcheckFormMap();
        heattreatcheckFormMap.set("isCheck","是");
        List<HeattreatcheckFormMap> heattreatcheckFormMaps=heattreatcheckMapper.findByAllLike(heattreatcheckFormMap);
        System.out.println(heattreatcheckFormMaps);
    }

    @Test
    public void TestToolOut(){
//      HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
////      heatTreatFormMap.set("remarks","外圆粗磨");
//        heatTreatFormMap.set("origin","调质");
//        heatTreatFormMap.set("isDistribution","已分配");
//        int count=heatTreatMapper.findCountByAllLike(heatTreatFormMap);
        List<UserFormMap> userFormMapList=userMapper.findCheckByWages("检验确认人");
       System.out.println(userFormMapList);
    }


    @Test
    public void TestGood(){
        DecimalFormat formater = new DecimalFormat("#0.##");
        formater.setRoundingMode(RoundingMode.FLOOR);
        System.out.println(formater.format(123456.7897456));
    }

    @Test
    public void TestProcess(){
        String[] namesXL = {"下料"};
        String processIdsXL = processMapper.findIdsByNames(namesXL);//下料

        String[] namesReCl = {"热调质","热正火"};
        String processIdsReCl = processMapper.findIdsByNames(namesReCl);//热处理

        String[] namesC = {"车","粗车","精车","双铣"};
        String processIdsC = processMapper.findIdsByNames(namesC);//车

        String[] namesX = {"铣","铣（二面）","铣（四面）"};
        String processIdsX = processMapper.findIdsByNames(namesX);//铣

        String[] namesQ = {"钳"};
        String processIdsQ = processMapper.findIdsByNames(namesQ);//钳

        String[] namesM = {"外圆粗","外圆精","内孔粗","内孔精","外磨","内磨","平磨","消差"};
        String processIdsM = processMapper.findIdsByNames(namesM);//磨

        String[] namesXQG = {"线切割","铣槽","台阶"};
        String processIdsXQG= processMapper.findIdsByNames(namesXQG);//线切割

        String[] namesYC= {"油槽"};
        String processIdsYC= processMapper.findIdsByNames(namesYC);//油槽
    }


    @Test
    public void TestWorkerSubmit(){
        List<String> processList=new ArrayList<>();
        String clientId="41";
        String month="2020-12";
        String[] namesXL = {"下料"};
        String processIdsXL = processMapper.findIdsByNames(namesXL);//下料
        processList.add(processIdsXL);

        String[] namesReCl = {"热调质","热正火"};
        String processIdsReCl = processMapper.findIdsByNames(namesReCl);//热处理
        processList.add(processIdsReCl);


        String[] namesC = {"车","粗车","精车","双铣"};
        String processIdsC = processMapper.findIdsByNames(namesC);//车
        processList.add(processIdsC);

        String[] namesX = {"铣","铣（二面）","铣（四面）"};
        String processIdsX = processMapper.findIdsByNames(namesX);//铣
        processList.add(processIdsX);

        String[] namesQ = {"钳"};
        String processIdsQ = processMapper.findIdsByNames(namesQ);//钳
        processList.add(processIdsQ);

        String[] namesM = {"外圆粗","外圆精","内孔粗","内孔精","外磨","内磨","平磨","消差"};
        String processIdsM = processMapper.findIdsByNames(namesM);//磨
        processList.add(processIdsM);

        String[] namesXQG = {"线切割","铣槽","台阶"};
        String processIdsXQG= processMapper.findIdsByNames(namesXQG);//线切割
        processList.add(processIdsXQG);

        String[] namesYC= {"油槽"};
        String processIdsYC= processMapper.findIdsByNames(namesYC);//油槽
        processList.add(processIdsYC);

        List<WorkersubmitFormMap> workersubmitFormMaps=new ArrayList<>();
        for(int i=0;i<processList.size();i++){
            String processIds=processList.get(i);
            String process="";
            switch (i){
                case 0:process="下料";break;
                case 1:process="热处理";break;
                case 2:process="车";break;
                case 3:process="铣";break;
                case 4:process="钳";break;
                case 5:process="磨";break;
                case 6:process="线切割";break;
                case 7:process="油槽";break;
            }
            if(i==3){
                System.out.print("aaa");
            }
            WorkersubmitFormMap workersubmitFormMap=new WorkersubmitFormMap();
            workersubmitFormMap.set("process",process);
            for(int j=1;j<2;j++){
                String day="";
                if(j<10){
                    day="0"+j;
                }else{
                    day=j+"";
                }
                String  showAmount=workersubmitMapper.findProductAmountByClinetAndProcessIdsAndDeliveryTime(processIds,month+"-"+day,clientId);
                workersubmitFormMap.set("amount"+j,showAmount);
            }
            workersubmitFormMaps.add(workersubmitFormMap);
        }
        System.out.print(workersubmitFormMaps);
    }
    @Test
    public void TestStr(){
        String money="1,2365";
        money=money.replace(",","");
        System.out.print(money);
    }

    @Test
    public void TestClient(){
        List<ClientFormMap> clientFormMaps=clientMapper.findByAllLike(new ClientFormMap());
    }

    @Test
    public void TestSystemConfit(){
        SystemconfigFormMap systemconfigFormMapResult=systemconfigMapper.findByName("taxRate");
        System.out.println(systemconfigFormMapResult);
    }


}
