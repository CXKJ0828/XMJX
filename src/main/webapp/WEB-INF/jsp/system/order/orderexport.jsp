<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/orderexport.js"></script>
<style>
    .tdShow{
        width:20%;
        text-align: left;
    }
    .spanShow{
        width: 30%!important;
    }
    .inputShow{
        width: 70%;
    }
</style>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'">
    <table id="tt" style="width:auto"></table>
</div>
<div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
    <select id="orderId" class="easyui-combogrid" style="width:200px" data-options="
			panelWidth: 800,
			idField: 'id',
			textField: 'orderId',
			url: '${pageContext.request.contextPath}/order/orderSelect.shtml',
			method: 'get',
			mode: 'remote',
			pagination:true,
           pagePosition:top,
			columns: [[
				{field:'id',title:'编号',hidden:true,width:150,align:'center',editor:'text'},
            {field:'orderId',title:'单号',width:270,align:'center',editor:'text'},
            {field:'time',title:'日期',width:180,align:'center',editor:'text'},
            {field:'name',title:'产品名称',width:150,align:'center',editor:'text'},
            {field:'amount',title:'数量',width:150,align:'center',editor:'text'},
            {field:'remarks',title:'备注',width:150,align:'center',editor:'text'},
            {field:'modifyTime',title:'制表时间',width:150,align:'center',editor:'text'},
            {field:'userName',title:'制表用户',width:150,align:'center',editor:'text'},
            {field:'state',title:'单据状态',width:150,align:'center',editor:'text'}
			]],
			fitColumns: true,
           onSelect:selectOrder
		">
    </select>
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
</div>
<script>
    function selectOrder(rowIndex, rowData) {
        orderCode=rowData.orderId;
        orderName=rowData.name;
    }

</script>
</body>
<script>
    initInputDataInput("starttime");
    initInputDataInput("endtime");
</script>