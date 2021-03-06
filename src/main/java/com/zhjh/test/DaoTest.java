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
       if(workersubmitHeatTreatFormMap==null){//????????????
           result=true;
       }else{
           String estimateCompleteTime=workersubmitHeatTreatFormMap.getStr("estimateCompleteTime");
          if(estimateCompleteTime==null){//????????????
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
        heattreatcheckFormMap.set("isCheck","???");
        List<HeattreatcheckFormMap> heattreatcheckFormMaps=heattreatcheckMapper.findByAllLike(heattreatcheckFormMap);
        System.out.println(heattreatcheckFormMaps);
    }

    @Test
    public void TestToolOut(){
//      HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
////      heatTreatFormMap.set("remarks","????????????");
//        heatTreatFormMap.set("origin","??????");
//        heatTreatFormMap.set("isDistribution","?????????");
//        int count=heatTreatMapper.findCountByAllLike(heatTreatFormMap);
        List<UserFormMap> userFormMapList=userMapper.findCheckByWages("???????????????");
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
        String[] namesXL = {"??????"};
        String processIdsXL = processMapper.findIdsByNames(namesXL);//??????

        String[] namesReCl = {"?????????","?????????"};
        String processIdsReCl = processMapper.findIdsByNames(namesReCl);//?????????

        String[] namesC = {"???","??????","??????","??????"};
        String processIdsC = processMapper.findIdsByNames(namesC);//???

        String[] namesX = {"???","???????????????","???????????????"};
        String processIdsX = processMapper.findIdsByNames(namesX);//???

        String[] namesQ = {"???"};
        String processIdsQ = processMapper.findIdsByNames(namesQ);//???

        String[] namesM = {"?????????","?????????","?????????","?????????","??????","??????","??????","??????"};
        String processIdsM = processMapper.findIdsByNames(namesM);//???

        String[] namesXQG = {"?????????","??????","??????"};
        String processIdsXQG= processMapper.findIdsByNames(namesXQG);//?????????

        String[] namesYC= {"??????"};
        String processIdsYC= processMapper.findIdsByNames(namesYC);//??????
    }


    @Test
    public void TestWorkerSubmit(){
        List<String> processList=new ArrayList<>();
        String clientId="41";
        String month="2020-12";
        String[] namesXL = {"??????"};
        String processIdsXL = processMapper.findIdsByNames(namesXL);//??????
        processList.add(processIdsXL);

        String[] namesReCl = {"?????????","?????????"};
        String processIdsReCl = processMapper.findIdsByNames(namesReCl);//?????????
        processList.add(processIdsReCl);


        String[] namesC = {"???","??????","??????","??????"};
        String processIdsC = processMapper.findIdsByNames(namesC);//???
        processList.add(processIdsC);

        String[] namesX = {"???","???????????????","???????????????"};
        String processIdsX = processMapper.findIdsByNames(namesX);//???
        processList.add(processIdsX);

        String[] namesQ = {"???"};
        String processIdsQ = processMapper.findIdsByNames(namesQ);//???
        processList.add(processIdsQ);

        String[] namesM = {"?????????","?????????","?????????","?????????","??????","??????","??????","??????"};
        String processIdsM = processMapper.findIdsByNames(namesM);//???
        processList.add(processIdsM);

        String[] namesXQG = {"?????????","??????","??????"};
        String processIdsXQG= processMapper.findIdsByNames(namesXQG);//?????????
        processList.add(processIdsXQG);

        String[] namesYC= {"??????"};
        String processIdsYC= processMapper.findIdsByNames(namesYC);//??????
        processList.add(processIdsYC);

        List<WorkersubmitFormMap> workersubmitFormMaps=new ArrayList<>();
        for(int i=0;i<processList.size();i++){
            String processIds=processList.get(i);
            String process="";
            switch (i){
                case 0:process="??????";break;
                case 1:process="?????????";break;
                case 2:process="???";break;
                case 3:process="???";break;
                case 4:process="???";break;
                case 5:process="???";break;
                case 6:process="?????????";break;
                case 7:process="??????";break;
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
