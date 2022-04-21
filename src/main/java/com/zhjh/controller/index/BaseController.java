package com.zhjh.controller.index;

import java.util.ArrayList;
import java.util.Enumeration;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.zhjh.bean.GeTuiMessage;
import com.zhjh.entity.*;
import com.zhjh.getui.AppPush;
import com.zhjh.mapper.CidRoleMapper;
import com.zhjh.mapper.HeatTreatMapper;
import com.zhjh.mapper.SystemconfigMapper;
import com.zhjh.tool.ToolCommon;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zhjh.mapper.ResourcesMapper;
import com.zhjh.mapper.base.BaseMapper;
import com.zhjh.plugin.PageView;
import com.zhjh.util.Common;
import com.zhjh.util.FormMap;

/**
 * 
 * @author lanyuan
 * Email：mmm333zzz520@163.com
 * date：2014-2-17
 */
public class BaseController {
	@Inject
	private ResourcesMapper resourcesMapper;

	@Inject
	public CidRoleMapper cidRoleMapper;

	@Inject
	public SystemconfigMapper systemconfigMapper;
	
	@Inject
	protected BaseMapper baseMapper;

	@Inject
	protected HeatTreatMapper heatTreatMapper;

	public String trsSuccess="";
	public String result="";
	public int total=0;

	public PageView pageView = null;
	public PageView getPageView(String pageNow,String pageSize,String orderby) {
		if (Common.isEmpty(pageNow)) {
			pageView = new PageView(1);
		} else {
			pageView = new PageView(Integer.parseInt(pageNow));
		}
		if (Common.isEmpty(pageSize)) {
			pageSize = "20";
		} 
		pageView.setPageSize(Integer.parseInt(pageSize));
		pageView.setOrderby(orderby);
		return pageView;
	}

	public String getCompanyName(){
		SystemconfigFormMap systemconfigFormMap=systemconfigMapper.findByName("companyName");
		String companyName=systemconfigFormMap.get("content")+"";
		return companyName;
	}

	public void addHeartTreatByBlankFormMap(String origin,BlankFormMap blankFormMap,String oprateUserId){
		try {
			HeatTreatFormMap heatTreatFormMap=new HeatTreatFormMap();
			if(origin.equals("调质")){
				heatTreatFormMap.set("hardnessId","42");
			}
			heatTreatFormMap.set("origin",origin);
			heatTreatFormMap.set("pickTime",ToolCommon.getNowDay());
			String contractNumber=blankFormMap.getStr("contractNumber");
			heatTreatFormMap.set("contractNumber",contractNumber);
			String goodId=blankFormMap.getStr("goodId");
			heatTreatFormMap.set("goodId",goodId);
			String blankSize=blankFormMap.getStr("blankSize");
			String goodWeight=goodWeight(blankSize);
			heatTreatFormMap.set("goodWeight",goodWeight);
			heatTreatFormMap.set("goodSize",blankSize);

			String amount=blankFormMap.getStr("amount");
			if(ToolCommon.isNull(amount)){
				amount="0";
			}
			heatTreatFormMap.set("amount",amount);
			String clientId=blankFormMap.getStr("clientId");
			heatTreatFormMap.set("clientId",clientId);
			heatTreatFormMap.set("userId",oprateUserId);
			String weight=ToolCommon.Double2NotIn(ToolCommon.StringToFloat(amount)*ToolCommon.StringToFloat(goodWeight));
			heatTreatFormMap.set("weight",weight);
			if(origin.equals("正火")||origin.equals("进度查询")){
				heatTreatFormMap.set("distributionAmount",amount);
			}
			if(origin.equals("中频")||origin.equals("调质")||origin.equals("渗碳")){
				heatTreatFormMap.set("distributionAmount",amount);
			}
			String materialQuality=blankFormMap.getStr("materialQuality")+"";
			heatTreatFormMap.set("materialQuality",materialQuality);

			String deliveryTime=blankFormMap.getStr("deliveryTime")+"";
			heatTreatFormMap.set("deliveryTime",deliveryTime);
			heatTreatFormMap.remove("id");
			heatTreatMapper.addEntity(heatTreatFormMap);
		}catch (Exception e){

		}
	}

	public String goodWeight(String goodSize){
	    try{
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
            return weightStrGood;
        }catch (Exception e){
	        return "";
        }
	}

	public void oprationSubmit(GeTuiMessage geTuiMessage){
		List<CidRoleFormMap> cidRoleFormMapList=cidRoleMapper.findCidNotInRole("操作部");
		List<String> cidList=new ArrayList<>();
		for(int i=0;i<cidRoleFormMapList.size();i++){
			cidList.add(cidRoleFormMapList.get(i).getStr("cid"));
		}
		if(!trsSuccess.equals("")){
			AppPush appPush=new AppPush();
			List<String> onLineList=appPush.getOnLineCID(cidList);
			if(onLineList!=null&&onLineList.size()>0){
				appPush.sendMessageByCid(onLineList,geTuiMessage);
			}
		}
	}
	public PageView getPageView(String pageNow,String pageSize) {
		if (Common.isEmpty(pageNow)) {
			pageView = new PageView(1);
		} else {
			pageView = new PageView(Integer.parseInt(pageNow));
		}
		if (Common.isEmpty(pageSize)) {
			pageSize = "10";
		} 
		pageView.setPageSize(Integer.parseInt(pageSize));
		return pageView;
	}

	/**
	 * 获取当前用户信息
	 * @return
	 */
	public UserFormMap getNowUserMessage(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
		return userFormMap;
	}


	//逻辑分页
	public void LuogicPaging(String pageNow, String pageSize, List<Map<String, Object>> list) throws Exception {
		int page = Integer.parseInt(pageNow == null ? "1" : pageNow);
		int intPageSize = Integer.parseInt(pageSize == null ? "10" : pageSize); 
		pageView = new PageView(page);
		pageView.setPageSize(intPageSize);

		int start = (page - 1) * pageView.getPageSize();
		int end = page * pageView.getPageSize();
		end = end > list.size() ? list.size() : end;

		List<Map<String, Object>> rs = new ArrayList<Map<String, Object>>();
		for (int i = start; i < end; i++) {
			rs.add(list.get(i));
		}
		pageView.setRowCount(list.size());
		pageView.setRecords(rs);
	}
	
	public <T> T toFormMap(T t,String pageNow,String pageSize,String orderby){
		@SuppressWarnings("unchecked")
		FormMap<String, Object> formMap = (FormMap<String, Object>) t;
		formMap.put("paging", getPageView(pageNow, pageSize,orderby));
		return t;
	}
	
	/**
	 * 获取返回某一页面的按扭组,
	 * <br/>
	 *<b>author：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsplanyuan</b><br/> 
	 *<b>date：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2015-04-01</b><br/> 
	 *<b>mod by：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbspEkko</b><br/> 
	 *<b>date：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2015-09-07</b><br/> 
	 *<b>version：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp3.0v</b>
	 * @return Class<T>
	 * @throws Exception
	 */
	public List<ResFormMap> findByRes(){
		// 资源ID
		String id = getPara("id");
		// 获取request
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		// 通过工具类获取当前登录的bean
		UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
		// user id
		int userId = userFormMap.getInt("id");
		ResFormMap resQueryForm = new ResFormMap();
		resQueryForm.put("parentId", id);
		resQueryForm.put("userId", userId);
		List<ResFormMap> rse = resourcesMapper.findRes(resQueryForm);
		//List<ResFormMap> rse = resourcesMapper.findByAttribute("parentId", id, ResFormMap.class);
		for (ResFormMap resFormMap : rse) {
			Object o =resFormMap.get("description");
			if(o!=null&&!Common.isEmpty(o.toString())){
				resFormMap.put("description",Common.stringtohtml(o.toString()));
			}
		}
		return rse;
	}
	public List<ResFormMap> findByRes(String id){

		// 获取request
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		// 通过工具类获取当前登录的bean
		UserFormMap userFormMap = (UserFormMap)Common.findUserSession(request);
		// user id
		int userId = userFormMap.getInt("id");
		ResFormMap resQueryForm = new ResFormMap();
		resQueryForm.put("parentId", id);
		resQueryForm.put("userId", userId);
		List<ResFormMap> rse = resourcesMapper.findRes(resQueryForm);
		//List<ResFormMap> rse = resourcesMapper.findByAttribute("parentId", id, ResFormMap.class);
		for (ResFormMap resFormMap : rse) {
			Object o =resFormMap.get("description");
			if(o!=null&&!Common.isEmpty(o.toString())){
				resFormMap.put("description",Common.stringtohtml(o.toString()));
			}
		}
		return rse;
	}
	/**
	 * 获取页面传递的某一个参数值,
	 * <br/>
	 *<b>author：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsplanyuan</b><br/> 
	 *<b>date：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2015-04-01</b><br/> 
	 *<b>version：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp3.0v</b>
	 */
	public String getPara(String key){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		return request.getParameter(key);
	}
	
	/**
	 * 获取页面传递的某一个数组值,
	 * <br/>
	 *<b>author：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsplanyuan</b><br/> 
	 *<b>date：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2015-04-01</b><br/> 
	 *<b>version：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp3.0v</b>
	 * @return Class<T>
	 * @throws Exception
	 */
	public String[] getParaValues(String key){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		return request.getParameterValues(key);
	}
	/*
	 * @ModelAttribute
	 * 这个注解作用.每执行controllor前都会先执行这个方法
	 * @author lanyuan
	 * Email：mmm333zzz520@163.com
	 * date：2015-4-05
	 * @param request
	 * @throws Exception 
	 * @throws  
	 */
	/*@ModelAttribute
	public void init(HttpServletRequest request){
		String path = Common.BACKGROUND_PATH;
		Object ep = request.getSession().getAttribute("basePath");
		if(ep!=null){
			if(!path.endsWith(ep.toString())){
				Common.BACKGROUND_PATH = "/WEB-INF/jsp/background"+ep;
			}
		}
		
	}*/
	
	/**
	 * 获取传递的所有参数,
	 * 反射实例化对象，再设置属性值
	 * 通过泛型回传对象.
	 * <br/>
	 *<b>author：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsplanyuan</b><br/> 
	 *<b>date：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2015-04-01</b><br/> 
	 *<b>version：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp3.0v</b>
	 * @return Class<T>
	 * @throws Exception
	 */
	public <T> T getFormMap(Class<T> clazz){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Enumeration<String> en = request.getParameterNames();
		T t = null;
		try {
			t = clazz.newInstance();
			@SuppressWarnings("unchecked")
			FormMap<String, Object> map = (FormMap<String, Object>) t;
			String order = "",sort="",page="",rows="";
			while (en.hasMoreElements()) {
				String nms = en.nextElement().toString();
				if(nms.endsWith("[]")){
					String[] as = request.getParameterValues(nms);
//					String asStr=as
					if(as!=null&&as.length!=0&&as.toString()!="[]"){
						String mname = t.getClass().getSimpleName().toUpperCase();
						if(nms.toUpperCase().startsWith(mname)){
							nms=nms.replace("[]","");
							nms=nms.substring(mname.length()+1);
							map.put( nms,ArrayStrToStr(as));
						}
					}
				}else{
					String as = request.getParameter(nms);
//					if(!Common.isEmpty(as)){
						String mname = t.getClass().getSimpleName().toUpperCase();
						if(nms.toUpperCase().startsWith(mname)){
							nms=nms.substring(mname.length()+1);
							map.put( nms, as);
						}
						if(nms.toLowerCase().equals("column"))order = as;
						if(nms.toLowerCase().equals("sort"))sort = as;
						if(nms.toLowerCase().equals("page"))page = as;
						if(nms.toLowerCase().equals("rows"))rows = as;
//					}
				}
			}
			if(!Common.isEmpty(page)){
				map.put("page",page);
			}
			if(!Common.isEmpty(rows)){
				map.put("rows",rows);
			}
			if(!Common.isEmpty(order) && !Common.isEmpty(sort))
				map.put("orderby", " order by " + order + " " + sort);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return  t;
	}

	public String ArrayStrToStr(String[] lytype){
		StringBuilder sb = new StringBuilder();
		if (lytype != null && lytype.length > 0) {
			for (int i = 0; i < lytype.length; i++) {
				if (i < lytype.length - 1) {
					sb.append(lytype[i] + ",");
				} else {
					sb.append(lytype[i]);
				}
			}
		}
		String lytype1 = sb.toString();
		return lytype1;
	}
	
	/**
	 * 获取传递的所有参数,
	 * 再设置属性值
	 * 通过回传Map对象.
	 * <br/>
	 *<b>author：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsplijianning</b><br/> 
	 *<b>date：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2015-04-01</b><br/> 
	 *<b>version：</b><br/> 
	 *<b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp1.0v</b>
	 * @return Class<T>
	 * @throws Exception
	 */
	public <T> T findHasHMap(Class<T> clazz){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Enumeration<String> en = request.getParameterNames();
		T t = null;
		try {
			t = clazz.newInstance();
			@SuppressWarnings("unchecked")
			FormMap<String, Object> map = (FormMap<String, Object>) t;
			while (en.hasMoreElements()) {
				String nms = en.nextElement().toString();
				if(!"_t".equals(nms)){
					if(nms.endsWith("[]")){
						String[] as = request.getParameterValues(nms);
						if(as!=null&&as.length!=0&&as.toString()!="[]"){
							map.put( nms,as);
						}
					}else{
						String as = request.getParameter(nms);
						if(!Common.isEmpty(as)){
							map.put( nms, as);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
}