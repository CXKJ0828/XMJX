package com.zhjh.controller.system;

import com.zhjh.annotation.SystemLog;
import com.zhjh.bean.FileBean;
import com.zhjh.controller.index.BaseController;
import com.zhjh.entity.BlankSizeFormMap;
import com.zhjh.entity.ClientFormMap;
import com.zhjh.entity.GoodFormMap;
import com.zhjh.entity.UserFormMap;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
@RequestMapping("/blanksize/")
public class BlankSizeController extends BaseController {
	@Inject
	private BlankSizeMapper blankSizeMapper;

	@Inject
	private GoodMapper goodMapper;

	@Inject
	private ClientMapper clientMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		model.addAttribute("res", findByRes());
		return Common.BACKGROUND_PATH + "/system/blanksize/list";
	}


	@ResponseBody
	@RequestMapping("findByPage")
	public BlankSizeFormMap findByPage(String content) throws Exception {
		String page=getPara("page");

		String rows=getPara("rows");
		String clientId=getPara("clientId");
		System.out.println(clientId);
		BlankSizeFormMap blankSizeFormMap =new BlankSizeFormMap();
		blankSizeFormMap.set("content",content);
		blankSizeFormMap.set("clientId",clientId);
		if(page.equals("1")){
			total=blankSizeMapper.findCountByAllLike(blankSizeFormMap);
		}
		blankSizeFormMap=toFormMap(blankSizeFormMap, page, rows,blankSizeFormMap.getStr("orderby"));
		List<BlankSizeFormMap> departmentFormMapList=blankSizeMapper.findByAllLike(blankSizeFormMap);
		blankSizeFormMap.set("rows",departmentFormMapList);
		blankSizeFormMap.set("total",total);
		return blankSizeFormMap;
	}


	@RequestMapping("downExcelDemo")
	public void downExcelDemo(Model model,String url,HttpServletRequest request,HttpServletResponse response) throws Exception {
		String path = request.getRealPath("");//????????????????????????????????????????????????.
//		url=url.replace("/","\\");
		String fileUrl=path+url;
		System.out.print(fileUrl);
		File file=new File(fileUrl);
		String name=file.getName();
		ToolCommon.exportFile(file,name,response);
	}



	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String editEntity(String entity) throws Exception {
		List<BlankSizeFormMap> list = (List) ToolCommon.json2ObjectList(entity, BlankSizeFormMap.class);
		String nowtime=ToolCommon.getNowTime();
		UserFormMap userFormMap=getNowUserMessage();
		String userId=String.valueOf(userFormMap.getInt("id"));
		for(int i=0;i<list.size();i++){
			BlankSizeFormMap blankSizeFormMap=list.get(i);
			String id=blankSizeFormMap.get("id")+"";
			blankSizeFormMap.set("modifytime",nowtime);
			blankSizeFormMap.set("userId",userId);
			if(id==null||id.equals("")||id.equals("null")){
				blankSizeFormMap.remove("id");
				blankSizeMapper.addEntity(blankSizeFormMap);
			}else{
				blankSizeMapper.editEntity(blankSizeFormMap);
			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="????????????",methods="?????????-?????????")//??????????????????????????????.???????????????????????????
	public String deleteEntity() throws Exception {
		String ids = getPara("ids");
		ids=ids.substring(0,ids.length()-1);
		String[] idsStr=ids.split(",");
		for (String id : idsStr) {
			blankSizeMapper.deleteById(id);
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping("upload")
	@Transactional(readOnly=false)//???????????????????????????????????????
	@SystemLog(module="Excel????????????",methods="????????????-??????")//??????????????????????????????.???????????????????????????
	public String upload(@RequestParam(value = "file", required = false) MultipartFile file,
								  HttpServletRequest request) throws Exception {
		String errorMessage="";
		if(file!=null){
			boolean isAdd=false;
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
					BlankSizeFormMap blankSizeFormMap1=new BlankSizeFormMap();
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
								}else if(value.equals("??????")){
									isAdd=false;
									j=lastColNum;
									i=sheet1.getLastRowNum()+1;
								}
							}
							value= ToolExcel.replaceBlank(value);
							value=value.replace(" ","");
							if(isAdd){
								switch (j){
									case 0:
										List<ClientFormMap> clientFormMaps=clientMapper.findByFullName(value);
										if(clientFormMaps.size()>0){
											String clientId=clientFormMaps.get(0).get("id")+"";
											blankSizeFormMap1.set("clientId",clientId);
										}else{
											j=lastColNum;
											isAdd=false;
											errorMessage=errorMessage+"?????????"+value+"????????????;<br>";
										}
										break;
									case 1:
										GoodFormMap goodFormMap=goodMapper.findFirstByMapNumber(value);
										if(goodFormMap==null){
											errorMessage=errorMessage+"?????????"+value+"????????????;<br>";
										}else{
											blankSizeFormMap1.set("goodId",goodFormMap.get("id")+"");
										}
										break;
									case 2:
										blankSizeFormMap1.set("blankSize",value);
										String blankSize=value;
										if(blankSize.split("\\*").length>1){
											String outSize=blankSize.split("\\*")[0];
											outSize=outSize.replace("??","");
											String inside=blankSize.split("\\*")[1];
											String length="";
											if(ToolCommon.isContain(inside,"??")){
												inside=inside.replace("??","");
												length=blankSize.split("\\*")[2];
												String REGEX ="[^(0-9).]";
												length= Pattern.compile(REGEX).matcher(length).replaceAll("").trim();
											}else{
												if(blankSize.split("\\*").length==2){
													length=inside;
													inside="0";
												}else{
													length=blankSize.split("\\*")[2];
												}
											}
											float outsideF=ToolCommon.StringToFloat(outSize);
											float insideF=ToolCommon.StringToFloat(inside);
											float lengthF=ToolCommon.StringToFloat(length);
											double weight=outsideF*outsideF*lengthF*(0.00617/1000)-insideF*insideF*lengthF*(0.00617/1000);
											String weightStr =Common.formatDouble4(weight);
											blankSizeFormMap1.set("blankWeight",weightStr);
										}
										break;
									case 3:
										blankSizeFormMap1.set("remarks1",value);
										break;
									case 4:
										blankSizeFormMap1.set("remarks2",value);
										break;
									case 5:
										blankSizeFormMap1.set("remarks3",value);
										break;
									case 6:
										blankSizeFormMap1.set("isCheck",value);
										break;
								}
							}

						}

					}
					if(isAdd){
						blankSizeFormMap1.set("modifytime",ToolCommon.getNowTime());
						blankSizeFormMap1.set("userId",userId);
						blankSizeMapper.addEntity(blankSizeFormMap1);
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

}