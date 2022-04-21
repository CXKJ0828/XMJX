package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.ComboboxEntity;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.*;
import com.zhjh.mapper.*;
import com.zhjh.tool.ToolCommon;
import com.zhjh.tool.ToolExcel;
import com.zhjh.tool.ToolProject;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author lanyuan 2014-11-19
 * @Email: mmm333zzz520@163.com
 * @version 3.0v
 */
@Controller
@RequestMapping("/good/")
public class GoodController extends BaseController {
	@Inject
	private GoodMapper goodMapper;

	@Inject
	private SystemconfigMapper systemconfigMapper;


	@Inject
	private ClientMapper clientMapper;

	@Inject
	private MaterialQualityTypeMapper materialQualityTypeMapper;

	@Inject
	private SystemconfigMapper systemConfigMapper;

	@Inject
	private GoodProcessMapper goodProcessMapper;

	@Inject
	private UserRoleMapper userRoleMapper;

	@Inject
	private UserMapper userMapper;

	@Inject
	private ProcessMapper processMapper;
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {

		SystemconfigFormMap systemconfigFormMap=systemConfigMapper.findByAttribute("name","taxRate",SystemconfigFormMap.class).get(0);
		model.addAttribute("taxRate",systemconfigFormMap.get("content")+"");
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/good/list";
	}

	@SystemLog(module="产品定量",methods="进入界面")//凡需要处理业务逻辑的.都需要记录操作日志
	@RequestMapping("goodprocesslist")
	public String goodprocesslistUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/good/goodprocesslist";
	}

	@RequestMapping("goodprocesslistByContent")
	public String goodprocesslistByContent(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/good/goodprocessContentlist";
	}

	@ResponseBody
	@RequestMapping("findByPage")
	public GoodFormMap findByPage(String content,String clientId) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		GoodFormMap goodFormMap =new GoodFormMap();
		goodFormMap.set("content",content);
		goodFormMap.set("clientId",clientId);
		if(page.equals("1")){
			total=goodMapper.findCountByAllLike(goodFormMap);
		}
		goodFormMap=toFormMap(goodFormMap, page, rows,goodFormMap.getStr("orderby"));
		List<GoodFormMap> departmentFormMapList=goodMapper.findByAllLike(goodFormMap);
		goodFormMap.set("rows",departmentFormMapList);
		goodFormMap.set("total",total);
		return goodFormMap;
	}


	@ResponseBody
	@RequestMapping("uploadGoodProcess")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadGoodProcess(@RequestParam(value = "file", required = false) MultipartFile file,
									HttpServletRequest request) throws Exception {
		String errorMessage="";
		String  orderId="";
		GoodProcessFormMap goodProcessFormMap=new GoodProcessFormMap();
		if(file!=null){
			boolean isAdd=false;
			String nowtime=ToolCommon.getNowTime();
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				int lastColNum=0;
				if(row!=null){
					lastColNum = row.getLastCellNum();
					GoodProcessFormMap goodProcessFormMap1=new GoodProcessFormMap();
					for(int j=0;j<lastColNum;j++){
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum;
								}else if(value.equals("结束")){
									i=sheet1.getLastRowNum()+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							switch (j){
								case 0:
									List<GoodFormMap> goodFormMaps=goodMapper.findByMapNumber(value);
									if(goodFormMaps.size()>0){
										String goodId=goodFormMaps.get(0).get("id")+"";
										goodProcessFormMap1.set("goodId",goodId);
									}
									break;
								case 1:
									List<ProcessFormMap> processFormMaps=processMapper.findByAttribute("name",value,ProcessFormMap.class);
									if(processFormMaps.size()>0){
										String processId=processFormMaps.get(0).get("id")+"";
										goodProcessFormMap1.set("processId",processId);
									}else {
										ProcessFormMap processFormMap=new ProcessFormMap();
										processFormMap.set("name",value);
										processMapper.addEntity(processFormMap);
										String processId=processFormMap.get("id")+"";
										goodProcessFormMap1.set("processId",processId);
									}
									break;
								case 2:
									goodProcessFormMap1.set("artificial",value);
									break;
								case 3:
									goodProcessFormMap1.set("remark",value);
									break;
								case 4:
									if(value.equals("True")){
										goodProcessFormMap1.set("isMust","是");
									}else{
										goodProcessFormMap1.set("isMust","否");
									}
									break;
								case 5:
									goodProcessFormMap1.set("multiple",value);
									break;
							}
						}

					}
					goodProcessFormMap1.set("modifyTime",ToolCommon.getNowTime());
					goodProcessFormMap1.set("userId",userId);
					goodProcessMapper.addEntity(goodProcessFormMap1);
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
	@RequestMapping("uploadGood")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadGood(@RequestParam(value = "file", required = false) MultipartFile file,
							 HttpServletRequest request) throws Exception {
		String errorMessage="";
		String  orderId="";
		GoodFormMap goodFormMap=new GoodFormMap();
		if(file!=null){
			boolean isAdd=false;
			String nowtime=ToolCommon.getNowTime();
			UserFormMap userFormMap=getNowUserMessage();
			String userId=String.valueOf(userFormMap.getInt("id"));
			FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
			Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
			for(int i=1;i<=sheet1.getLastRowNum();i++){
				Row row = null;
				row = sheet1.getRow(i);
				String nottaxPrice="0";
				int lastColNum=0;
				if(i==3){
					System.out.print(i);
				}
				if(row!=null){
					lastColNum = row.getLastCellNum();
					GoodFormMap goodFormMap1=new GoodFormMap();
					for(int j=0;j<lastColNum;j++){
						isAdd=true;
						Cell cell=row.getCell(j);
						if(cell!=null){
							String value=ExcelUtil.getCellValue(cell);
							if(value==null){
								value="";
							}
							if(j==0){
								if(value.equals("")){
									j=lastColNum;
									isAdd=false;
									i=sheet1.getLastRowNum()+1;
								}else if(value.equals("结束")){
									isAdd=false;
									j=lastColNum;
									i=sheet1.getLastRowNum()+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							value=value.replace(" ","");
							switch (j){
								case 0:
									List<GoodFormMap> goodFormMaps=goodMapper.findByMapNumber(value);
									if(goodFormMaps.size()>0){
										j=lastColNum;
										isAdd=false;
										errorMessage=errorMessage+"图号【"+value+"】已存在;<br>";
									}else{
										goodFormMap1.set("mapNumber",value);
									}
									break;
								case 1:
									goodFormMap1.set("name",value);
									break;
								case 2:
									goodFormMap1.set("roughcastSize",value);
									String roughcastSize=value;
									if(roughcastSize.split("\\*").length>1){
										String outSize=roughcastSize.split("\\*")[0];
										outSize=outSize.replace("Φ","");
										String inside=roughcastSize.split("\\*")[1];
										String length="";
										if(ToolCommon.isContain(inside,"Φ")){
											inside=inside.replace("Φ","");
											length=roughcastSize.split("\\*")[2];
											String REGEX ="[^(0-9).]";
											length= Pattern.compile(REGEX).matcher(length).replaceAll("").trim();
										}else{
											if(roughcastSize.split("\\*").length==2){
												length=inside;
												inside="0";
											}else{
												length=roughcastSize.split("\\*")[2];
											}
										}
										float outsideF=ToolCommon.StringToFloat(outSize);
										float insideF=ToolCommon.StringToFloat(inside);
										float lengthF=ToolCommon.StringToFloat(length);
										double weight=outsideF*outsideF*lengthF*(0.00617/1000)-insideF*insideF*lengthF*(0.00617/1000);
										String weightStr =Common.formatDouble4(weight);
										goodFormMap1.set("roughcastWeight",weightStr);
									}
									break;
								case 3:
									String materialQuality="";
									List<MaterialQualityTypeFormMap> materialQualityTypeFormMaps=materialQualityTypeMapper.findByAllLike(value);
									if(materialQualityTypeFormMaps.size()>0){
										materialQuality=materialQualityTypeFormMaps.get(0).getInt("id")+"";
									}
									goodFormMap1.set("materialQuality",materialQuality);
									break;
								case 4:
									goodFormMap1.set("goodSize",value);
									String goodSize=value;
									if(goodSize.split("\\*").length>1){
										String outSizeGood=goodSize.split("\\*")[0];
										outSizeGood=outSizeGood.replace("Φ","");
										String insideGood=goodSize.split("\\*")[1];
										String lengthGood="";
										if(ToolCommon.isContain(insideGood,"Φ")){
											insideGood=insideGood.replace("Φ","");
											lengthGood=goodSize.split("\\*")[2];
											String REGEX ="[^(0-9).]";
											lengthGood= Pattern.compile(REGEX).matcher(lengthGood).replaceAll("").trim();
										}else{
											if(goodSize.split("\\*").length==2){
												lengthGood=insideGood;
												insideGood="0";
											}else{
												lengthGood=goodSize.split("\\*")[2];
											}
										}
										float outsideFGood=ToolCommon.StringToFloat(outSizeGood);
										float insideFGood=ToolCommon.StringToFloat(insideGood);
										float lengthFGood=ToolCommon.StringToFloat(lengthGood);
										double weightGood=outsideFGood*outsideFGood*lengthFGood*(0.00617/1000)-insideFGood*insideFGood*lengthFGood*(0.00617/1000);
										String weightStrGood =Common.formatDouble4(weightGood);
										goodFormMap1.set("goodWeight",weightStrGood);
									}
									break;
								case 5:
									nottaxPrice=value;
									goodFormMap1.set("nottaxPrice",value);
									break;
								case 6:
									goodFormMap1.set("materialCode",value);
									break;
								case 7:
									goodFormMap1.set("remarks",value);
									break;
								case 8:
									List<ClientFormMap> clientFormMaps=clientMapper.findByFullName(value);
									if(clientFormMaps.size()>0){
										String clientId=clientFormMaps.get(0).get("id")+"";
										String taxRate=clientFormMaps.get(0).get("taxRate")+"";
										if(taxRate==null||taxRate=="null"||taxRate.equals("")){
											SystemconfigFormMap systemconfigFormMap=systemConfigMapper.findByAttribute("name","taxRate",SystemconfigFormMap.class).get(0);
											taxRate=systemconfigFormMap.get("content")+"";
										}
										goodFormMap1.set("clientId",clientId);
										float taxPrice=ToolCommon.StringToFloat(nottaxPrice)*(1+ToolCommon.StringToFloat(taxRate));
										taxPrice=ToolCommon.FloatToMoney(taxPrice);
										goodFormMap1.set("taxPrice",taxPrice);
									}else{
										j=lastColNum;
										isAdd=false;
										errorMessage=errorMessage+"客户【"+value+"】不存在存在;<br>";
									}
									break;
							}
						}

					}
					if(isAdd){
						goodFormMap1.set("modifyTime",ToolCommon.getNowTime());
						goodFormMap1.set("userId",userId);
						goodMapper.addEntity(goodFormMap1);
					}

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
	@RequestMapping("calculationEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-删除组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String calculationEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		boolean isUpdate=false;
		for(int i=0;i<idsStr.length;i++){
			isUpdate=false;
			GoodFormMap goodFormMap1=goodMapper.findbyFrist("id",idsStr[i],GoodFormMap.class);
			String roughcastSize=goodFormMap1.getStr("roughcastSize");
			if(roughcastSize.split("\\*").length>1){
				isUpdate=true;
				String outSize=roughcastSize.split("\\*")[0];
				outSize=outSize.replace("Φ","");
				String inside=roughcastSize.split("\\*")[1];
				String length="";
				if(ToolCommon.isContain(inside,"Φ")){
					inside=inside.replace("Φ","");
					length=roughcastSize.split("\\*")[2];
					String REGEX ="[^(0-9).]";
					length= Pattern.compile(REGEX).matcher(length).replaceAll("").trim();
				}else{
					if(roughcastSize.split("\\*").length==2){
						length=inside;
						inside="0";
					}else{
						length=roughcastSize.split("\\*")[2];
					}
				}
				float outsideF=ToolCommon.StringToFloat(outSize);
				float insideF=ToolCommon.StringToFloat(inside);
				float lengthF=ToolCommon.StringToFloat(length);
				double weight=outsideF*outsideF*lengthF*(0.00617/1000)-insideF*insideF*lengthF*(0.00617/1000);
				String weightStr =Common.formatDouble4(weight);
				goodFormMap1.set("roughcastWeight",weightStr);
			}


			String goodSize=goodFormMap1.getStr("goodSize");
			if(goodSize.split("\\*").length>1){
				isUpdate=true;
				String outSizeGood=goodSize.split("\\*")[0];
				outSizeGood=outSizeGood.replace("Φ","");
				String insideGood=goodSize.split("\\*")[1];
				String lengthGood="";
				if(ToolCommon.isContain(insideGood,"Φ")){
					insideGood=insideGood.replace("Φ","");
					lengthGood=goodSize.split("\\*")[2];
					String REGEX ="[^(0-9).]";
					lengthGood= Pattern.compile(REGEX).matcher(lengthGood).replaceAll("").trim();
				}else{
					if(goodSize.split("\\*").length==2){
						lengthGood=insideGood;
						insideGood="0";
					}else{
						lengthGood=goodSize.split("\\*")[2];
					}
				}
				float outsideFGood=ToolCommon.StringToFloat(outSizeGood);
				float insideFGood=ToolCommon.StringToFloat(insideGood);
				float lengthFGood=ToolCommon.StringToFloat(lengthGood);
				double weightGood=outsideFGood*outsideFGood*lengthFGood*(0.00617/1000)-insideFGood*insideFGood*lengthFGood*(0.00617/1000);
				String weightStrGood =Common.formatDouble4(weightGood);
				goodFormMap1.set("goodWeight",weightStrGood);
			}
			if(isUpdate){
				goodMapper.editEntity(goodFormMap1);
			}

		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("uploadGoodProcessContent")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadGoodProcessContent(@RequestParam(value = "files", required = false) MultipartFile[] files,
										   @RequestParam(value = "clientId", required = false) String clientId,
										   HttpServletRequest request) throws Exception {
		String errorMessage="";
		System.out.print(clientId);
		GoodProcessFormMap goodProcessFormMap=new GoodProcessFormMap();
		int successAmount=0;
		int failAmount=0;
		int allAmount=files.length;
		String failMessage="";
		String nowNumber="";
		if(files!=null){
			for(int n=0;n<files.length;n++){
				MultipartFile file=files[n];
				FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
				Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
				Row row1 =sheet1.getRow(0);
				if(row1==null){
					sheet1= ExcelUtil.readRowsAndColumsSheet2(fileBean.getPath());
					row1 =sheet1.getRow(0);
				}
				if(row1==null){
					sheet1= ExcelUtil.readRowsAndColumsSheet3(fileBean.getPath());
					row1 =sheet1.getRow(0);
				}
				if(row1==null){
					sheet1= ExcelUtil.readRowsAndColumsSheet4(fileBean.getPath());
					row1 =sheet1.getRow(0);
				}
				if(row1==null){
					failAmount=failAmount+1;
					failMessage=failMessage+file.getOriginalFilename()+"里面内容不存在、";
				}else{
					Cell cell1=row1.getCell(2);
//					String filename=file.getOriginalFilename();
//					String mapNumber=filename.substring(0,filename.lastIndexOf("."));
					String mapNumber=ExcelUtil.getCellValue(cell1);
					if(mapNumber.equals("06.1.3-8A")){
						System.out.print("aaa");
					}
					mapNumber=mapNumber.replace("　","");
					mapNumber=mapNumber.replace(",","");
					GoodFormMap goodFormMap=goodMapper.findFirstByMapNumber(mapNumber);
					String goodId="";
					if(goodFormMap!=null){
						goodId=goodFormMap.get("id")+"";
						if(goodId.equals("null")){
							System.out.print("aaa");
						}
//						goodFormMap.set("clientId",clientId);
//						goodMapper.editEntity(goodFormMap);
						boolean isAdd=true;
						String nowtime=ToolCommon.getNowTime();
						UserFormMap userFormMap=getNowUserMessage();
						String userId=String.valueOf(userFormMap.getInt("id"));
						for(int i=2;i<=sheet1.getLastRowNum();i++){
							Row row = null;
							row = sheet1.getRow(i);
							int lastColNum=0;
							if(row!=null){
								lastColNum = row.getLastCellNum();
								GoodProcessFormMap goodProcessFormMap1=new GoodProcessFormMap();
								for(int j=0;j<3;j++){
									Cell cell=row.getCell(j);
									if(cell!=null){
										String value=ExcelUtil.getCellValue(cell);
										if(value==null){
											value="";
										}
										if(j==0){
											if(value.equals("")){
												j=lastColNum;
												i=sheet1.getLastRowNum()+1;
												isAdd=false;
											}else if(value.equals("结束")){
												j=lastColNum;
												i=sheet1.getLastRowNum()+1;
												isAdd=false;
											}
										}
										value= ToolExcel.replaceBlank(value);
										switch (j){
											case 0:
												isAdd=true;
												nowNumber=value;
												goodProcessFormMap1.set("number",value);
												break;
											case 1:
												if(value.equals("下料")){
													System.out.print("aaa");
												}
												List<ProcessFormMap> processFormMaps=processMapper.findByAttribute("name",value,ProcessFormMap.class);
												if(processFormMaps.size()>0){
													String processId=processFormMaps.get(0).get("id")+"";
													goodProcessFormMap1.set("goodId",goodId);
													goodProcessFormMap1.set("processId",processId);
												}else{
													ProcessFormMap processFormMap=new ProcessFormMap();
													processFormMap.set("name",value);
													processMapper.addEntity(processFormMap);
													String processId=processFormMap.get("id")+"";
													goodProcessFormMap1.set("goodId",goodId);
													goodProcessFormMap1.set("processId",processId);
												}
												if(nowNumber.equals("14")){
													System.out.print("aaa");
												}
												List<GoodProcessFormMap> goodProcessFormMapList=goodProcessMapper.findByGoodIdAndProcessId(goodProcessFormMap1);
												if(goodProcessFormMapList.size()==2){
													GoodProcessFormMap goodProcessFormMap3=goodProcessFormMapList.get(0);
													GoodProcessFormMap goodProcessFormMap4=goodProcessFormMapList.get(1);
													String number3=goodProcessFormMap3.getStr("number");
													String number4=goodProcessFormMap4.getStr("number");
													if((number3==null&&number4!=null)||(number4==null&&number3!=null)){
														goodProcessMapper.deleteByGoodIdAndProcessIdAndNumberNotNull(goodProcessFormMap1);
													}
												}
												break;
											case 2:
												goodProcessFormMap1.set("content",value);
												break;
										}
									}else{
										j=3;
										i=sheet1.getLastRowNum()+1;
										isAdd=false;
									}
								}
								if(isAdd){
									if(i==12||i==13){
										System.out.print(i);
									}
									goodProcessFormMap1.set("modifyTime",ToolCommon.getNowTime());
									goodProcessFormMap1.set("userId",userId);
									String number=goodProcessFormMap1.get("number")+"";
									if(number.equals("13")){
										System.out.print("lll");
									}
									List<GoodProcessFormMap> goodProcessFormMapList=goodProcessMapper.findByGoodIdAndProcessId(goodProcessFormMap1);
									if(goodProcessFormMapList.size()>1){
										GoodProcessFormMap goodProcessFormMap2=goodProcessMapper.findByGoodIdAndProcessIdAndNumber(goodProcessFormMap1);

										if(goodProcessFormMap2!=null){
											String content=goodProcessFormMap1.get("content")+"";
											goodProcessFormMap2.set("content",content);
											goodProcessFormMap2.set("number",number);
											goodProcessMapper.editEntity(goodProcessFormMap2);


										}else{
											String goodId1=goodProcessFormMap1.get("goodId")+"";
											if(goodId1!=null&&!goodId1.equals("")&&!goodId1.equals("null")){
												goodProcessMapper.addEntity(goodProcessFormMap1);
											}
										}
									}else{
										if(goodProcessFormMapList.size()==0){
											String goodId1=goodProcessFormMap1.get("goodId")+"";
											if(goodId1!=null&&!goodId1.equals("")&&!goodId1.equals("null")){
												goodProcessMapper.addEntity(goodProcessFormMap1);
											}
										}else if(goodProcessFormMapList.size()==1){
											GoodProcessFormMap goodProcessFormMap2=goodProcessFormMapList.get(0);
											String numberSelect=goodProcessFormMap2.getStr("number");
											if(numberSelect==null||numberSelect.equals(nowNumber)){
												String content=goodProcessFormMap1.get("content")+"";
												goodProcessFormMap2.set("content",content);
												goodProcessFormMap2.set("number",number);
												goodProcessMapper.editEntity(goodProcessFormMap2);
											}else{
												String content=goodProcessFormMap1.get("content")+"";
												goodProcessFormMap2.set("content",content);
												goodProcessFormMap2.set("number",number);
												goodProcessFormMap2.remove("id");
												goodProcessMapper.addEntity(goodProcessFormMap2);
											}

										}
									}

								}

							}
						}
						successAmount=successAmount+1;
					}else{
						failAmount=failAmount+1;
						failMessage=failMessage+mapNumber+"、";
					}
				}
			}
		}else{
//			errorMessage="请选择上传文件";
		}
		if(errorMessage.equals("")&&failAmount==0){
			return "共上传【"+allAmount+"】;成功【"+successAmount+"】个;失败【"+failAmount+"】";
		}else{
			ToolCommon.writeToFile(failMessage);
			return "共上传【"+allAmount+"】;成功【"+successAmount+"】个;失败【"+failAmount+"】<br>"+failMessage+"图号不存在";
		}

	}

	@ResponseBody
	@RequestMapping("uploadGoodProcessContentContainMoney")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="产品定量",methods="产品定量上传-")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadGoodProcessContentContainMoney(@RequestParam(value = "files", required = false) MultipartFile[] files,
										   @RequestParam(value = "clientId", required = false) String clientId,
										   HttpServletRequest request) throws Exception {
		String errorMessage="";
		System.out.print(clientId);
		GoodProcessFormMap goodProcessFormMap=new GoodProcessFormMap();
		int successAmount=0;
		int failAmount=0;
		int allAmount=files.length;
		String failMessage="";
		String nowNumber="";
		if(files!=null){
			for(int n=0;n<files.length;n++){
				MultipartFile file=files[n];
				FileBean fileBean=ToolCommon.uploadExcelFile(file,request);
				Sheet sheet1= ExcelUtil.readRowsAndColumsSheet1(fileBean.getPath());
				Row row1 =sheet1.getRow(0);
				if(row1==null){
					sheet1= ExcelUtil.readRowsAndColumsSheet2(fileBean.getPath());
					row1 =sheet1.getRow(0);
				}
				if(row1==null){
					sheet1= ExcelUtil.readRowsAndColumsSheet3(fileBean.getPath());
					row1 =sheet1.getRow(0);
				}
				if(row1==null){
					sheet1= ExcelUtil.readRowsAndColumsSheet4(fileBean.getPath());
					row1 =sheet1.getRow(0);
				}
				if(row1==null){
					failAmount=failAmount+1;
					failMessage=failMessage+file.getOriginalFilename()+"里面内容不存在、";
				}else{
					Cell cell1=row1.getCell(2);
//					String filename=file.getOriginalFilename();
//					String mapNumber=filename.substring(0,filename.lastIndexOf("."));
					String mapNumber=ExcelUtil.getCellValue(cell1);
					if(mapNumber.equals("06.1.3-8A")){
						System.out.print("aaa");
					}
					mapNumber=mapNumber.replace("　","");
					mapNumber=mapNumber.replace(",","");
					GoodFormMap goodFormMap=goodMapper.findFirstByMapNumber(mapNumber);
					String goodId="";
					if(goodFormMap!=null){
						goodId=goodFormMap.get("id")+"";
						if(goodId.equals("null")){
							System.out.print("aaa");
						}
						List<GoodProcessFormMap> goodProcessFormMaps=goodProcessMapper.findByGoodId(goodId);
						if(goodProcessFormMaps!=null&&goodProcessFormMaps.size()>0){
							failAmount=failAmount+1;
							failMessage=failMessage+mapNumber+"信息已存在、";
						}else{
							boolean isAdd=true;
							String nowtime=ToolCommon.getNowTime();
							UserFormMap userFormMap=getNowUserMessage();
							String userId=String.valueOf(userFormMap.getInt("id"));
							for(int i=2;i<=sheet1.getLastRowNum();i++){
								if(i==4){
									System.out.print("aaa");
								}
								Row row = null;
								row = sheet1.getRow(i);
								if(row!=null){
									GoodProcessFormMap goodProcessFormMap1=new GoodProcessFormMap();
									for(int j=0;j<7;j++){
										Cell cell=row.getCell(j);
										if(cell!=null){
											String value=ExcelUtil.getCellValue(cell);
											if(value==null){
												value="";
											}
											if(j==0){
												if(value.equals("")){
													j=7;
													i=sheet1.getLastRowNum()+1;
													isAdd=false;
												}else if(value.equals("结束")){
													j=7;
													i=sheet1.getLastRowNum()+1;
													isAdd=false;
												}
											}
											if(j==3){
												System.out.print("aaa");
											}
											value= ToolExcel.replaceBlank(value);
											switch (j){
												case 0:
													isAdd=true;
													nowNumber=value;
													goodProcessFormMap1.set("number",value);
													break;
												case 1:
													List<ProcessFormMap> processFormMaps=processMapper.findByAttribute("name",value,ProcessFormMap.class);
													if(processFormMaps.size()>0){
														String processId=processFormMaps.get(0).get("id")+"";
														goodProcessFormMap1.set("goodId",goodId);
														goodProcessFormMap1.set("processId",processId);
													}else{
														ProcessFormMap processFormMap=new ProcessFormMap();
														processFormMap.set("name",value);
														processMapper.addEntity(processFormMap);
														String processId=processFormMap.get("id")+"";
														goodProcessFormMap1.set("goodId",goodId);
														goodProcessFormMap1.set("processId",processId);
													}
													break;
												case 2:
													goodProcessFormMap1.set("content",value);
													break;
												case 6:
													goodProcessFormMap1.set("artificial",value);
													break;
											}
										}else{

										}
									}
									if(isAdd){
										String number=goodProcessFormMap1.get("number")+"";
										if(!number.equals("")&&!number.equals("null")){
											goodProcessFormMap1.set("modifyTime",nowtime);
											goodProcessFormMap1.set("userId",userId);
											goodProcessMapper.addEntity(goodProcessFormMap1);
										}

									}

								}
							}
							successAmount=successAmount+1;
						}

					}else{
						failAmount=failAmount+1;
						failMessage=failMessage+mapNumber+"图号不存在";
					}
				}
			}
		}else{
//			errorMessage="请选择上传文件";
		}
		if(errorMessage.equals("")&&failAmount==0){
			return "共上传【"+allAmount+"】;成功【"+successAmount+"】个;失败【"+failAmount+"】";
		}else{
			ToolCommon.writeToFile(failMessage);
			return "共上传【"+allAmount+"】;成功【"+successAmount+"】个;失败【"+failAmount+"】<br>"+failMessage;
		}

	}


	@ResponseBody
	@RequestMapping("uploadGoodImg")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="Excel表格上传",methods="订单上传-上传")//凡需要处理业务逻辑的.都需要记录操作日志
	public String uploadGoodImg(@RequestParam(value = "filesImg", required = false) MultipartFile[] files,
								HttpServletRequest request) throws Exception {
		String errorMessage="";
		int successAmount=0;
		int failAmount=0;
		int allAmount=files.length;
		String failMessage="";
//		String url=ToolProject.getGoodImgUrl(request);
		String url=systemConfigMapper.findByAttribute("name","filepath",SystemconfigFormMap.class).get(0).getStr("content");
		if(files!=null){
			for(int n=0;n<files.length;n++){
				MultipartFile file=files[n];
				String filename=file.getOriginalFilename();
				String mapNumber=filename.substring(0,filename.lastIndexOf("."));
				GoodFormMap goodFormMap=goodMapper.findFirstByMapNumber(mapNumber);
				if(goodFormMap==null){
					failAmount=failAmount+1;
					failMessage=failMessage+mapNumber+"、";
				}else{
					FileBean fileBean= ToolCommon.uploadFileNotChangeName(url,file);
					goodFormMap.set("img","upload/goodimg/"+fileBean.getName());
					goodMapper.editEntity(goodFormMap);
					successAmount=successAmount+1;
				}
			}
		}else{
		}
		if(errorMessage.equals("")&&failAmount==0){
			return "共上传【"+allAmount+"】;成功【"+successAmount+"】个;失败【"+failAmount+"】";
		}else{
			ToolCommon.writeToFile(failMessage);
			return "共上传【"+allAmount+"】;成功【"+successAmount+"】个;失败【"+failAmount+"】<br>"+failMessage+"图号不存在";
		}
	}
	@RequestMapping("showImgUI")
	@SystemLog(module="产品图纸",methods="查看图纸")
	public String showImgUI(Model model,String goodId) throws Exception {
		List<SystemconfigFormMap> systemconfigFormMaps=systemConfigMapper.findByAttribute("name","imgURL",SystemconfigFormMap.class);
		String imgURL=systemconfigFormMaps.get(0).get("content")+"";
		GoodFormMap goodFormMap=goodMapper.findbyFrist("id",goodId,GoodFormMap.class);
		model.addAttribute("img",imgURL+goodFormMap.getStr("img"));
		model.addAttribute("mapNumber",goodFormMap.getStr("mapNumber"));
		return Common.BACKGROUND_PATH + "/system/good/imgShow";
	}


	@RequestMapping("addProcessUI")
	public String addProcessUI(Model model,String goodId) throws Exception {
		model.addAttribute("goodId",goodId);
		return Common.BACKGROUND_PATH + "/system/good/addprocess";
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="产品管理",methods="产品管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity(String entity) throws Exception {
		List<GoodFormMap> list = (List) ToolCommon.json2ObjectList(entity, GoodFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String errorMessage="";
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			GoodFormMap goodFormMap=list.get(i);
			String id=goodFormMap.get("id")+"";
			goodFormMap.set("modifytime",nowtime);
			goodFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				String mapNumber=goodFormMap.get("mapNumber")+"";
				if(mapNumber==null||mapNumber.equals("")||mapNumber.equals("null")){

				}else{
					List<GoodFormMap> goodFormMapList=goodMapper.findByMapNumber(mapNumber);
					if(goodFormMapList.size()==0){
						goodFormMap.remove("id");
						goodMapper.addEntity(goodFormMap);
					}else{
						errorMessage=errorMessage+mapNumber+"、";
					}
				}


			}else{
				String mapNumber=goodFormMap.get("mapNumber")+"";
				String OldMapNumber=goodFormMap.get("oldMapNumber")+"";
				List<GoodFormMap> goodFormMapList=goodMapper.findByMapNumber(mapNumber);
				if(mapNumber.equals(OldMapNumber)){
					goodMapper.editEntity(goodFormMap);
				}else{
					if(goodFormMapList.size()==1){
						errorMessage=errorMessage+mapNumber+"、";

					}else{
						goodMapper.editEntity(goodFormMap);
					}
				}


			}
		}
		if(errorMessage.equals("")){
			return "success";
		}else{
			return "图号"+errorMessage+"已存在";
		}

	}

	@ResponseBody
	@RequestMapping("materialCodeSelect")
	public GoodFormMap  materialCodeSelect(String q) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		GoodFormMap goodFormMap1=new GoodFormMap();
		goodFormMap1.set("content",q);
		if(page.equals("1")){
			total=goodMapper.findDistinctMaterialCodeCountLike(goodFormMap1);
			total=total+1;
		}
		goodFormMap1=toFormMap(goodFormMap1, page, rows,goodFormMap1.getStr("orderby"));
		List<GoodFormMap> goodFormMaps=goodMapper.findDistinctMaterialCodeLike(goodFormMap1);
		GoodFormMap goodFormMap2=new GoodFormMap();
		goodFormMap2.set("materialCode","不限");
		goodFormMaps.add(goodFormMap2);
		GoodFormMap goodFormMapAll=new GoodFormMap();
		goodFormMapAll.set("total",total);
		goodFormMapAll.set("rows",goodFormMaps);
		return goodFormMapAll;
	}

	@ResponseBody
	@RequestMapping("editGoodProcessEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="组织架构",methods="员工管理-新增")//凡需要处理业务逻辑的.都需要记录操作日志
	public String editGoodProcessEntity(String entity) throws Exception {
		String goodprocessList= ToolCommon.json2Object(entity).getString("goodprocessList");
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		List<GoodProcessFormMap> list = (List) ToolCommon.json2ObjectList(goodprocessList, GoodProcessFormMap.class);
		for(int i=0;i<list.size();i++){
			GoodProcessFormMap goodProcessFormMap=list.get(i);
			String goodprocessId=goodProcessFormMap.get("id")+"";
			goodProcessFormMap.set("userId",userId);
			goodProcessFormMap.set("modifyTime",nowtime);
			if(goodprocessId!=null&&!goodprocessId.equals("null")&&!goodprocessId.equals("")){
				goodProcessMapper.editEntity(goodProcessFormMap);
			}else{
				goodProcessFormMap.remove("id");
				goodProcessMapper.addEntity(goodProcessFormMap);
			}
		}
		return "success";
	}

	/**
	 * 验证资源是否存在
	 *
	 * @author lanyuan Email：mmm333zzz520@163.com date：2014-2-19
	 * @return
	 */
	@RequestMapping("goodMapNumber_isExist")
	@ResponseBody
	public boolean goodMapNumber_isExist(String mapNumber) {

		List<GoodFormMap> list = goodMapper.findByMapNumber(mapNumber);
		if (list == null || list.size()<=0) {
			return true;
		} else {
			return false;
		}
	}
	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="产品管理",methods="产品管理-删除")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		SystemconfigFormMap systemconfigFormMap=new SystemconfigFormMap();
		systemconfigFormMap.set("name","deletepassword");
		String content=getPara("password");
		systemconfigFormMap.set("content",content);
		List<SystemconfigFormMap> systemconfigFormMaps=systemconfigMapper.findByNames(systemconfigFormMap);
		if(systemconfigFormMaps.size()>0){
			String ids = getPara("ids");
			ids=ids.substring(0,ids.length()-1);
			String[] idsStr=ids.split(",");
			for (String id : idsStr) {
				goodMapper.deleteById(id);
			}
			return "success";
		}else{
			return "删除订单密码错误";
		}
	}

	@ResponseBody
	@RequestMapping("deleteGoodProcessEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="产品定量",methods="根据id删除产品定量")//凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteGoodProcessEntity(String id) throws Exception {
		goodProcessMapper.deleteById(id);
		return "success";
	}

	@ResponseBody
	@RequestMapping("goodSelect")
	public GoodFormMap goodSelect(String q) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		GoodFormMap  goodFormMap  = getFormMap(GoodFormMap .class);
		if(q==null){
			q="";
		}
		goodFormMap.set("content",q);
		if(page.equals("1")){
			total=goodMapper.findCountByAllLike(goodFormMap);
		}
		goodFormMap=toFormMap(goodFormMap, page, rows,goodFormMap.getStr("orderby"));
		List<GoodFormMap> departmentFormMapList=goodMapper.findByAllLike(goodFormMap);
		GoodFormMap bomFormMap=new GoodFormMap();
		bomFormMap.set("rows",departmentFormMapList);
		bomFormMap.set("total",total);
		return bomFormMap;
	}

	@ResponseBody
	@RequestMapping("goodSelectByClientId")
	public GoodFormMap goodSelectByClientId(String q,String clientId) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		GoodFormMap  goodFormMap  = getFormMap(GoodFormMap .class);
		if(q==null){
			q="";
		}
		goodFormMap.set("mapNumber",q);
		goodFormMap.set("clientId",clientId);
		if(page.equals("1")){
			total=goodMapper.findCountByMapNumberAndClientIdLike(goodFormMap);
		}
		goodFormMap=toFormMap(goodFormMap, page, rows,goodFormMap.getStr("orderby"));
		List<GoodFormMap> departmentFormMapList=goodMapper.findByMapNumberAndClientIdLike(goodFormMap);
		GoodFormMap bomFormMap=new GoodFormMap();
		bomFormMap.set("rows",departmentFormMapList);
		bomFormMap.set("total",total);
		return bomFormMap;
	}

	@ResponseBody
	@RequestMapping("getGoodProcessByGoodId")
	public GoodProcessFormMap getGoodProcessByGoodId(@RequestParam(required=true) String id) throws Exception {
		GoodProcessFormMap orderDetailsFormMap1 = getFormMap(GoodProcessFormMap.class);
		if(id.equals("")){
			List<GoodProcessFormMap> goodProcessFormMaps= new ArrayList<>();
			orderDetailsFormMap1.set("rows",goodProcessFormMaps);
		}else{
			String sum=goodProcessMapper.findSumArtificialByGoodId(id);
			List<GoodProcessFormMap> goodProcessFormMaps= goodProcessMapper.findByGoodId(id);
			orderDetailsFormMap1.set("rows",goodProcessFormMaps);
			orderDetailsFormMap1.set("sum",sum);
		}
		return orderDetailsFormMap1;
	}

	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = getPara("id");
		if(Common.isNotEmpty(id)){
			model.addAttribute("good", goodMapper.findbyFrist("id", id, GoodFormMap.class));
		}
		return Common.BACKGROUND_PATH + "/system/good/edit";
	}
	@ResponseBody
	@RequestMapping("getgoodById")
	public GoodFormMap getgoodById(@RequestParam(required=true) String id) throws Exception {
		GoodFormMap goodFormMap=new GoodFormMap();
		GoodFormMap goodFormMap1=goodMapper.findbyFrist("id",id,GoodFormMap.class);
		if(goodFormMap1!=null){
			goodFormMap.set("id","1");
			goodFormMap.set("goodId",goodFormMap1.getStr("goodId"));
		}else{
			goodFormMap.set("id","0");
			goodFormMap.set("result","所选员工不存在");
		}
		return goodFormMap;
	}

	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly=false)//需要事务操作必须加入此注解
	@SystemLog(module="系统管理",methods="组管理-修改组")//凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity() throws Exception {
		GoodFormMap goodFormMap = getFormMap(GoodFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		goodFormMap.set("modifytime",nowtime);
		goodFormMap.set("userId",userId);
		goodMapper.addEntity(goodFormMap);
		return "success";
	}


}