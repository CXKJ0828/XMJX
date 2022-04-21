<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html lang="en" class="app">
<head>
	<%@include file="common/common.jspf"%>
	<link rel="stylesheet" type="text/css" href="themes/metro/easyui.css" id="swicth-style">
	<link rel="stylesheet" href="css/index.css" type="text/css">
	<script src="js/index.js"></script>
</head>
<body class="easyui-layout">
<div region="north" border="true" class="cs-north">
	<jsp:include page="top.jsp"></jsp:include>
</div>
<div region="west" border="true" style="width: 170px" split="true" title="导航" class="cs-west">
	<%@ include file="nav.jsp"%>
</div>
<div id="mainPanle" region="center" border="true" border="false">
	<div id="tabs" class="easyui-tabs"  fit="true" border="false" >
		<div title="主界面">
			<div >
				<div class="cs-home-remark">
					<span>产品导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/GoodExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>产品工艺导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/TechnologyExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>下料管理导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('blanksize/downExcelDemo.shtml?url=/excel/BlankSizeExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>订单导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('order/downOrderDemo.shtml?url=/excel/OrderExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>订单导入Excel表格模板2：</span>
					<a   onclick="downfileByUrl('order/downOrderDemo.shtml?url=/excel/OrderExcelDemo2.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>回料单导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/MaterialBackOrderExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>回料单(山东)导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/MaterialBackOrderMetersExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>工具入库导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/ToolInDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>进度查询导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/ProgressSearchDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>发货入库导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/DeliverInputGoodsExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>物料导入Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/MaterialDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>物料回料2Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/MaterialBack2ExcelDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>物料回料操作2Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/MaterialBack2ExcelOpreateDemo.xls')" href="#">点击下载</a>
				</div>
				<div class="cs-home-remark">
					<span>生产分类管理Excel表格模板：</span>
					<a   onclick="downfileByUrl('materialbackorder/downOrderDemo.shtml?url=/excel/GoodproductCategoryExcelOpreateDemo.xls')" href="#">点击下载</a>
				</div>
			</div>

		</div>
	</div>
</div>
<div region="south" border="false" id="south"><center>新明机械企业管理系统</center></div>

<div id="mm" class="easyui-menu cs-tab-menu">
	<div id="mm-tabupdate">刷新</div>
	<div class="menu-sep"></div>
	<div id="mm-tabclose">关闭</div>
	<div id="mm-tabcloseother">关闭其他</div>
	<div id="mm-tabcloseall">关闭全部</div>
</div>
</body>
</html>
