<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/expectClientGood.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true" style="width:auto"></table>
    <table id="ttselect" style="display: none"></table>
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input id="clientId" style="display: none" type="text" >
          客户：<input id="clientName" style="width: 250px" type="text"   >
        产品名称:
        <select class="easyui-combobox" id='goodName' style="width:80px;">
            <option value="不限"></option>
            <option value="轴">轴</option>
            <option value="套">套</option>
            <option value="垫/片">垫/片</option>
            <option value="其他">其他</option>
            <option value="不限">不限</option>
        </select>
        <input  id="mapNumber" class="inputShow" placeholder="请输入图号">
        交货月份:
        <input  id="month" type="text" value="${month}">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <span id="all">合计:</span>
    </div>
</div>
<script>
    initInputMonthInput("month");
    $('#clientName').combogrid({
        panelWidth:400,
        idField:'fullName',
        textField:'fullName',
        url: '${pageContext.request.contextPath}/client/clientSelect.shtml',
        columns:[[
            {field:'id',hidden:true,title:'客户编码',width:100},
            {field:'fullName',title:'客户全称',width:350},
        ]],
        onSelect:selectClient,
        onLoadSuccess:function(data){
            $('#clientName').combogrid('setValue', data.rows[1].fullName);
            initTT(data.rows[1].id);
        }
    });

    function selectClient(rowIndex, rowData) {
        setContentToInputById("clientId",rowData.id);
        setContentToInputById("clientName",rowData.fullName);
    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>