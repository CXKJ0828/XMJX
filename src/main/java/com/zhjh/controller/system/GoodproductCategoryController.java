package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.ComboboxEntity;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolExcel;
import com.zhjh.util.Common;
import com.zhjh.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.tools.Tool;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/goodproductCategory/")
public class GoodproductCategoryController extends BaseController {
	@Inject
	private GoodproductCategoryMapper goodproductCategoryMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;

	@Inject
	private ClientMapper clientMapper;

	@Inject
	private GoodMapper goodMapper;
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		String id = getPara("id");
		String origin="";
		if(id.equals("752")){
			origin="销轴";
		}else if(id.equals("753")){
			origin="钢套";
		}else if(id.equals("754")){
			origin="垫片";
		}
		model.addAttribute("origin",origin);
		model.addAttribute("res", findByRes("752"));
		return Common.BACKGROUND_PATH + "/system/goodproductCategory/list";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="分类管理-新增编辑")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity,String origin) throws Exception {
		List<GoodproductCategoryFormMap> list = (List) ToolCommon.json2ObjectList(entity, GoodproductCategoryFormMap.class);
		for(int i=0;i<list.size();i++){
			GoodproductCategoryFormMap goodproductCategoryFormMap=list.get(i);
			String id=goodproductCategoryFormMap.get("id")+"";
			if(ToolCommon.isNull(id)){
				goodproductCategoryFormMap.remove("id");
				goodproductCategoryFormMap.set("origin",origin);
				goodproductCategoryFormMap.set("isComplete","否");
				goodproductCategoryMapper.addEntity(goodproductCategoryFormMap);
			}else{
				goodproductCategoryMapper.editEntity(goodproductCategoryFormMap);
			}
		}
			return "success";
	}

	@ResponseBody
	@RequestMapping("leaderSelect")
	public List<ComboboxEntity> leaderSelect(String q) throws Exception {
		if(q==null){
			q="";
		}
		List<GoodproductCategoryFormMap> goodproductCategoryFormMaps=goodproductCategoryMapper.findDistinctLeader(q);
		List<ComboboxEntity> comboboxEntityList=new ArrayList<>();
		for(int i=0;i<goodproductCategoryFormMaps.size();i++){
			GoodproductCategoryFormMap roleFormMap1=goodproductCategoryFormMaps.get(i);
			if(roleFormMap1!=null){
				ComboboxEntity entity=new ComboboxEntity();
				entity.id=roleFormMap1.get("leader")+"";
				entity.text=roleFormMap1.getStr("leader");
				comboboxEntityList.add(entity);
			}

		}
		ComboboxEntity entity=new ComboboxEntity();
		entity.id="不限";
		entity.text="不限";
		comboboxEntityList.add(entity);
		return comboboxEntityList;
	}
	@ResponseBody
	@RequestMapping("findClientByPage")
	@SystemLog(module="生产管理",methods="分类管理-获取客户列表内容")
	public List findClientByPage(String content,
								 String clientId,
								 String materialQuality,
								 String startTimeDelivery,
								 String endTimeDelivery,
								 String startTimeComplete,
								 String endTimeComplete,
                                 String startTimeBack,
                                 String endTimeBack,
                                 String startTimeReceipt,
                                 String endTimeReceipt,
								 String leader,
								 String isComplete,
								 String isOverTime,
								 String reason,
								 String overTimeUserId,
								 String badUserId,
								 String isBad,
                                 String isBack) throws Exception {
		String origin=getPara("origin");
		if(!ToolCommon.isNull(endTimeDelivery)){
			endTimeDelivery=ToolCommon.addDay(endTimeDelivery,1);
		}
		if(!ToolCommon.isNull(endTimeComplete)){
			endTimeComplete=ToolCommon.addDay(endTimeComplete,1);
		}
        if(!ToolCommon.isNull(endTimeBack)){
            endTimeBack=ToolCommon.addDay(endTimeBack,1);
        }
        if(!ToolCommon.isNull(endTimeReceipt)){
            endTimeReceipt=ToolCommon.addDay(endTimeReceipt,1);
        }
		GoodproductCategoryFormMap goodproductCategoryFormMap =new GoodproductCategoryFormMap();
		goodproductCategoryFormMap.set("reason",reason);
		goodproductCategoryFormMap.set("overTimeUserId",overTimeUserId);
		goodproductCategoryFormMap.set("badUserId",badUserId);
		goodproductCategoryFormMap.set("isBad",isBad);
		goodproductCategoryFormMap.set("origin",origin);
		goodproductCategoryFormMap.set("content",content);
		goodproductCategoryFormMap.set("clientId",clientId);
		goodproductCategoryFormMap.set("materialQuality",materialQuality);
		goodproductCategoryFormMap.set("startTimeDelivery",startTimeDelivery);
		goodproductCategoryFormMap.set("endTimeDelivery",endTimeDelivery);
		goodproductCategoryFormMap.set("startTimeComplete",startTimeComplete);
		goodproductCategoryFormMap.set("endTimeComplete",endTimeComplete);
        goodproductCategoryFormMap.set("startTimeBack",startTimeBack);
        goodproductCategoryFormMap.set("endTimeBack",endTimeBack);
        goodproductCategoryFormMap.set("startTimeReceipt",startTimeReceipt);
        goodproductCategoryFormMap.set("endTimeReceipt",endTimeReceipt);
		goodproductCategoryFormMap.set("leader",leader);
		goodproductCategoryFormMap.set("isComplete",isComplete);
        goodproductCategoryFormMap.set("isBack",isBack);
		goodproductCategoryFormMap.set("isOverTime",isOverTime);
		List<ClientFormMap> clientFormMaps=goodproductCategoryMapper.findClientByAllLike(goodproductCategoryFormMap);
		List<TreeClientEntity> treeClientEntities=new ArrayList<>();
		boolean isAddNullClient=true;
		try{
			for(int i=0;i<clientFormMaps.size();i++){
				ClientFormMap clientFormMap=clientFormMaps.get(i);
				if(clientFormMap!=null){
					String clientIdSelect=clientFormMap.get("clientId")+"";
					String fullName=clientFormMap.getStr("simpleName");
					TreeClientEntity treeClientEntity=new TreeClientEntity();
					if(ToolCommon.isNull(clientIdSelect)){
						if(isAddNullClient){
							isAddNullClient=false;
							treeClientEntity.id=clientIdSelect;
							treeClientEntity.text=fullName;
							treeClientEntities.add(treeClientEntity);
						}
					}else{
						treeClientEntity.id=clientIdSelect;
						treeClientEntity.text=fullName;
						treeClientEntities.add(treeClientEntity);
					}
				}
			}
			return treeClientEntities;
		}catch (Exception e){
			System.out.print(e);
			return treeClientEntities;
		}
	}
	public class TreeClientEntity {
		public String id;
		public String text;
		public String state;
	}
	@ResponseBody
	@RequestMapping("findByPage")
	@SystemLog(module="生产管理",methods="分类管理-获取列表内容")
	public GoodproductCategoryFormMap findByPage(String content,
												 String clientId,
												 String materialQuality,
												 String startTimeDelivery,
												 String endTimeDelivery,
												 String startTimeComplete,
												 String endTimeComplete,
                                                 String startTimeBack,
                                                 String endTimeBack,
                                                 String startTimeReceipt,
                                                 String endTimeReceipt,
												 String leader,
												 String isComplete,
												 String isOverTime,
												 String reason,
												 String overTimeUserId,
												 String badUserId,
												 String isBad,
                                                 String isBack
												 ) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		String origin=getPara("origin");

		String sort=getPara("sort");
		String order=getPara("order");
		if(sort==null){
			sort="deliveryTime";
			order="asc";
		}

		if(!ToolCommon.isNull(endTimeDelivery)){
			endTimeDelivery=ToolCommon.addDay(endTimeDelivery,1);
		}
		if(!ToolCommon.isNull(endTimeComplete)){
			endTimeComplete=ToolCommon.addDay(endTimeComplete,1);
		}
        if(!ToolCommon.isNull(endTimeBack)){
            endTimeBack=ToolCommon.addDay(endTimeBack,1);
        }
        if(!ToolCommon.isNull(endTimeReceipt)){
            endTimeReceipt=ToolCommon.addDay(endTimeReceipt,1);
        }
		GoodproductCategoryFormMap goodproductCategoryFormMap =new GoodproductCategoryFormMap();
		goodproductCategoryFormMap.set("reason",reason);
        goodproductCategoryFormMap.set("isBack",isBack);
		goodproductCategoryFormMap.set("overTimeUserId",overTimeUserId);
		goodproductCategoryFormMap.set("badUserId",badUserId);
		goodproductCategoryFormMap.set("isBad",isBad);
		goodproductCategoryFormMap.set("origin",origin);
		goodproductCategoryFormMap.set("content",content);
		goodproductCategoryFormMap.set("clientId",clientId);
		goodproductCategoryFormMap.set("materialQuality",materialQuality);
		goodproductCategoryFormMap.set("startTimeDelivery",startTimeDelivery);
		goodproductCategoryFormMap.set("endTimeDelivery",endTimeDelivery);
		goodproductCategoryFormMap.set("startTimeComplete",startTimeComplete);
		goodproductCategoryFormMap.set("endTimeComplete",endTimeComplete);
        goodproductCategoryFormMap.set("startTimeBack",startTimeBack);
        goodproductCategoryFormMap.set("endTimeBack",endTimeBack);
        goodproductCategoryFormMap.set("startTimeReceipt",startTimeReceipt);
        goodproductCategoryFormMap.set("endTimeReceipt",endTimeReceipt);
		goodproductCategoryFormMap.set("leader",leader);
		goodproductCategoryFormMap.set("isComplete",isComplete);
		goodproductCategoryFormMap.set("isOverTime",isOverTime);
		if(page.equals("1")){
			total=goodproductCategoryMapper.findCountByAllLike(goodproductCategoryFormMap);
		}
		goodproductCategoryFormMap.set("sort",sort);
		goodproductCategoryFormMap.set("order",order);
		GoodproductCategoryFormMap goodproductCategoryFormMapCount=goodproductCategoryMapper.findStatisticsByAllLike(goodproductCategoryFormMap);
		goodproductCategoryFormMap=toFormMap(goodproductCategoryFormMap, page, rows,goodproductCategoryFormMap.getStr("orderby"));
		List<GoodproductCategoryFormMap> departmentFormMapList=goodproductCategoryMapper.findByAllLike(goodproductCategoryFormMap);
		goodproductCategoryFormMap.set("sumEntity",goodproductCategoryFormMapCount);
		goodproductCategoryFormMap.set("rows",departmentFormMapList);
		goodproductCategoryFormMap.set("total",total);
		return goodproductCategoryFormMap;
	}

	@ResponseBody
	@RequestMapping("sureCompleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="分类管理-生成完成")//凡需要处理业务逻辑的.都需要记录操作日志
	public String sureCompleteEntity() throws Exception {
		String nowTime=ToolCommon.getNowDay();
		String isOverTime="";
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		List<GoodproductCategoryFormMap> goodproductCategoryFormMaps=goodproductCategoryMapper.findByIds(ids);
		for(int i=0;i<goodproductCategoryFormMaps.size();i++){
			GoodproductCategoryFormMap goodproductCategoryFormMap=goodproductCategoryFormMaps.get(i);
			String deliveryTime=goodproductCategoryFormMap.get("deliveryTime")+"";
			if(nowTime.compareTo(deliveryTime)>0){
				isOverTime="是";
			}else{
				isOverTime="否";
			}
			goodproductCategoryFormMap.set("isOverTime",isOverTime);
			goodproductCategoryFormMap.set("completeTime",nowTime);
			goodproductCategoryFormMap.set("isComplete","是");
			goodproductCategoryMapper.editEntity(goodproductCategoryFormMap);
		}
		return "success";

	}

    @ResponseBody
    @RequestMapping("sureBackEntity")
    @Transactional(readOnly=false)//需要事务操作必须加入此注解
    @SystemLog(module="生产管理",methods="分类管理-生成回单")//凡需要处理业务逻辑的.都需要记录操作日志
    public String sureBackEntity() throws Exception {
        String nowTime=ToolCommon.getNowDay();
        String ids = getPara("ids");
        ids=ids.substring(0,ids.length()-1);
        List<GoodproductCategoryFormMap> goodproductCategoryFormMaps=goodproductCategoryMapper.findByIds(ids);
        for(int i=0;i<goodproductCategoryFormMaps.size();i++){
            GoodproductCategoryFormMap goodproductCategoryFormMap=goodproductCategoryFormMaps.get(i);
            goodproductCategoryFormMap.set("backTime",nowTime);
            goodproductCategoryFormMap.set("isBack","是");
            goodproductCategoryMapper.editEntity(goodproductCategoryFormMap);
        }
        return "success";

    }

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="分类管理-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
						 String origin,
						 HttpServletRequest request) throws Exception {
		String errorMessage="";
		if(file!=null){
			boolean isAdd=false;
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				isAdd=true;
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					GoodproductCategoryFormMap goodproductCategoryFormMap=new GoodproductCategoryFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value= ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum+1;
									i=sheet1.getLastRowNum()+1;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
									j=lastColNum+1;
								}
							}
							switch (j){
								case 0:
									value=value.replace(" ","");
									List<ClientFormMap> clientFormMapList=clientMapper.findBySimpleName(value);
									if(clientFormMapList==null||clientFormMapList.size()==0){
										errorMessage=errorMessage+value+"客户不存在；";
										j=lastColNum+1;
										isAdd=false;
									}else{
										String clientId=clientFormMapList.get(0).get("id")+"";
										goodproductCategoryFormMap.set("clientId",clientId);
									}
									break;
								case 1:
									value=value.replace(" ","");
									value=value.replace(",","");
									List<GoodFormMap> goodFormMapList=goodMapper.findByMapNumber(value);
									if(goodFormMapList==null||goodFormMapList.size()==0){
										errorMessage=errorMessage+value+"产品不存在；";
										j=lastColNum+1;
										isAdd=false;
									}else{
										String goodId=goodFormMapList.get(0).get("id")+"";
										goodproductCategoryFormMap.set("goodId",goodId);
									}
									break;
								case 2:
									value=value.replace(" ","");
									goodproductCategoryFormMap.set("amount",value);;
									break;
								case 3:
									String deliveryTime=value;
									deliveryTime=deliveryTime.replace("//","-");
									deliveryTime=deliveryTime.replace("/","-");
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									String f = sdf.format(sdf.parse(deliveryTime));
									goodproductCategoryFormMap.set("deliveryTime",f);
									break;
								case 4:
									goodproductCategoryFormMap.set("leader",value);
									goodproductCategoryFormMap.set("isComplete","否");
									break;
                                case 5:
                                    String receiptTime=value;
                                    receiptTime=receiptTime.replace("//","-");
                                    receiptTime=receiptTime.replace("/","-");
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                                    String f1 = sdf1.format(sdf1.parse(receiptTime));
                                    goodproductCategoryFormMap.set("receiptTime",f1);
                                    break;
							}

						}else{
							i=sheet1.getLastRowNum()+1;
							j=lastColNum+1;
						}
					}
					if(isAdd){
						goodproductCategoryFormMap.remove("id");
						goodproductCategoryFormMap.set("origin",origin);
						goodproductCategoryMapper.addEntity(goodproductCategoryFormMap);
					}
				}else{
					i=sheet1.getLastRowNum()+1;
				}
			}
		}
		if(errorMessage.equals("")){
			return "success";
		}else{
			return errorMessage;
		}

	}
	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="生产管理",methods="分类管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap = new SystemconfigFormMap();
		systemconfigFormMap.set("name", "deletepassword");
		String content = getPara("password");
		systemconfigFormMap.set("content", content);
		List<SystemconfigFormMap> systemconfigFormMaps = systemconfigMapper.findByNames(systemconfigFormMap);
		if (systemconfigFormMaps.size() > 0) {
			String ids = getPara("ids");
			ids = ids.substring(0, ids.length() - 1);
			goodproductCategoryMapper.deleteByIds(ids);
			return "success";
		} else {
			return "删除密码错误";
		}
	}
}