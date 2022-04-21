<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/expectOrder.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <input id="startTimeShow" style="display: none" type="text" value="${startTimeShow}">
        <input id="endTimeShow" style="display: none" type="text" value="${endTimeShow}">
        <input id="clientId" style="display: none" type="text" class="inputShow easyui-validatebox">
          客户：<input id="clientName" style="width: 250px" type="text"   > </td>
        产品名称:
        <select class="easyui-combobox" id='goodName' style="width:80px;">
            <option value="不限"></option>
            <option value="轴">轴</option>
            <option value="套">套</option>
            <option value="垫/片">垫/片</option>
            <option value="其他">其他</option>
            <option value="不限">不限</option>
        </select>
        <input  id="starttime" type="text" value="${startTime}">
        至
        <input id="endtime" type="text" value="${endTime}">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <span id="all">合计:</span>
    </div>
</div>
<script>
    initInputDataInput("starttime");
    initInputDataInput("endtime");
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
        onShowPanel: function () {

        }
    });
    function selectClient(rowIndex, rowData) {
        setContentToInputById("clientId",rowData.id);
        setContentToInputById("clientName",rowData.fullName);
    }

    $("#tt").datagrid({
       rowStyler: function(index,row){
            var deliveryTime=row.deliveryTime;
            var startTime=getValueById("startTimeShow");
            var endTime=getValueById("endTimeShow");
            if (deliveryTime>=startTime&&deliveryTime<endTime){
                return 'color:#ff0030;';//红色
            }else{
                return 'color:#000;';
            }
        },
    });
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>