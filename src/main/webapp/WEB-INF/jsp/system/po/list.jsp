<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/po/list.js"></script>
<body class="easyui-layout" id="content">
<script>
    var poId="";
</script>
<table style="width: 100%;height: 100%" border="1">
    <tr style="height: 30%;">
        <td colspan="2">
                <table id="tt" style="width:auto"></table>
        </td>
    </tr>
    <tr style="height: 70%;">
        <td style="width: 50%">
            <table class="easyui-datagrid" id="podetails"
                   data-options="title:'PO明细',fit:true,fitColumns:true,
                 url:'${pageContext.request.contextPath}/po/findpoByPoId.shtml?poId='+poId">
                <thead>
                <tr>
                    <th data-options="field:'factoryNumber'" width="80">工厂号</th>
                    <th data-options="field:'makeTime'" width="100">确定订单时间</th>
                    <th data-options="field:'startPort',align:'right'" width="80">起运港</th>
                    <th data-options="field:'endPort',align:'right'" width="80">目的港</th>
                    <th data-options="field:'sailingTime'" width="150">开船时间</th>
                    <th data-options="field:'cabinetType',align:'center'" width="50">柜型</th>
                    <th data-options="field:'productFactory',align:'right'" width="80">生产工厂</th>
                    <th data-options="field:'cx'" width="150">型号</th>
                    <th data-options="field:'amount',align:'center'" width="50">数量</th>
                </tr>
                </thead>
            </table>

        </td>
        <td style="width: 50%">
            <table class="easyui-datagrid" id="cabinetdDtails"
                   data-options="title:'装柜明细',toolbar: '#cabinetBar',fit:true,fitColumns:true">
                <thead>
                <tr>
                    <th data-options="field:'itemid'" width="80">Item ID</th>
                    <th data-options="field:'productid'" width="100">Product ID</th>
                    <th data-options="field:'listprice',align:'right'" width="80">List Price</th>
                    <th data-options="field:'unitcost',align:'right'" width="80">Unit Cost</th>
                    <th data-options="field:'attr'" width="150">Attribute</th>
                    <th data-options="field:'status',align:'center'" width="50">Status</th>
                </tr>
                </thead>
            </table>
        </td>
    </tr>
</table>

<div id="add" title="添加产品" style="background: ghostwhite">
</div>
<div id="tb" style="height:auto">
    <form id="form" name="form"  method="post" enctype="multipart/form-data"
    >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input name="file" id="file" type="file" style="width:300px">
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </form>
</div>

<div id="cabinetBar" style="height:auto">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">抛单</a>
</div>
</body>