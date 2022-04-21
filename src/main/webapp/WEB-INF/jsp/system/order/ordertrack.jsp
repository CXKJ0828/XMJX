<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/ordertrack.js"></script>
<body class="easyui-layout">
<div data-options="region:'north'">
    <input style="display: none" id="origin" value="${origin}">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-sum',plain:true" onclick="selectAll()">全选</a>
        <a href="#" class="easyui-linkbutton" onclick="clearselectAll()" title="取消全选" iconCls="icon-undo" plain="true">取消全选</a>
        客户：
        <input  id="clientId" style="width:100px"  class="inputShow">
        来单日期:
        <input  id="makeTimestart" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
        至
        <input id="makeTimeend" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
        交货日期:
        <input  id="deliveryTimestart" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
        至
        <input id="deliveryTimeend"  type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
        产品名称:
        <select class="easyui-combobox" id='goodName' name="goodName" style="width:80px;">
            <option value="不限"></option>
            <option value="轴">轴</option>
            <option value="套">套</option>
            <option value="垫/片">垫/片</option>
            <option value="其他">其他</option>
            <option value="不限">不限</option>
        </select>
        材质：
        <input  id="materialQuality" style="width:100px"  class="inputShow">

        下料状态:
        <select class="easyui-combobox" id='blankIsFinish'  style="line-height:22px;width:100px;">
            <option value=""></option>
            <option value="未接收">未接收</option>
            <option value="已接收未完成">已接收未完成</option>
            <option value="已完成">已完成</option>
            <option value="不限">不限</option>
        </select>
        车工状态:
        <select class="easyui-combobox" id='turnerIsFinish'  style="line-height:22px;width:100px;">
            <option value=""></option>
            <option value="未接收">未接收</option>
            <option value="已接收未完成">已接收未完成</option>
            <option value="已完成">已完成</option>
            <option value="不限">不限</option>
        </select>
        渗碳状态:
        <select class="easyui-combobox" id='carburizationIsFinish'  style="line-height:22px;width:100px;">
            <option value=""></option>
            <option value="未接收">未接收</option>
            <option value="已接收未完成">已接收未完成</option>
            <option value="已完成">已完成</option>
            <option value="不限">不限</option>
        </select>
        装炉状态:
        <select class="easyui-combobox" id='feedIsFinish'  style="line-height:22px;width:100px;">
            <option value=""></option>
            <option value="未接收">未接收</option>
            <option value="已接收未完成">已接收未完成</option>
            <option value="已完成">已完成</option>
            <option value="不限">不限</option>
        </select>
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    <a href="javascript:void(0)" onclick="turnerWarning()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">车后预警</a>
    <span id="amountSum">合计:</span>
</div>
<div data-options="region:'west',split:true" style="width:150px;">
    <ul id="tt1"
        data-options="
            method:'get',animate:true"
    ></ul>
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" style="width:auto"></table>
    <div style="display: none">
        <table id="ttselect" style="width:auto;"></table>
    </div>
</div>
<script>
    
    function turnerWarning() {
        isWarning=true;
        reloadDatagridMessage('tt');
        reloadTreeMessage('tt1');
    }
    var treeObjext=$('#tt1');
    $('#tt1').tree({
        url:'${pageContext.request.contextPath}/order/clientSimpleNameSelectTwoContentTrack.shtml',
        onBeforeLoad: function (node,params) {
            params.isWarning='false';
            params.makeTimestart=getDataboxValue("makeTimestart");
            params.makeTimeend=getDataboxValue("makeTimeend");
            params.deliveryTimestart=getDataboxValue("deliveryTimestart");
            params.deliveryTimeend=getDataboxValue("deliveryTimeend");
            params.goodName=getContentBySelect("goodName");
            params.materialQuality=getContentBySelect("materialQuality");
            params.blankIsFinish=getContentBySelect("blankIsFinish");
            params.turnerIsFinish=getContentBySelect("turnerIsFinish");
            params.carburizationIsFinish=getContentBySelect("carburizationIsFinish");
            params.feedIsFinish=getContentBySelect("feedIsFinish");
            params.content=getValueById('content');
        },onBeforeSelect:function (node) {
            var clientId=node.id;
            setContentToCombobox("clientId",clientId);
            isWarning=false;
            reloadDatagridMessage("tt");
        },
        onSelect:function(node){
            var childrenNodes = treeObjext.tree('getChildren',node.target);
            var lengthChild=childrenNodes.length;
            if(lengthChild>0){
                $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
                node.state = node.state === 'closed' ? 'open' : 'closed';
            }
        },
    });

    function clearselectAll() {
        clearDatagridSelections('tt');
    }

    function selectAll() {

        //获取数据列表中的所有数据
        var rows = $("#tt").datagrid("getRows");
        //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
        for(var i=0;i<rows.length;i++){
            var index = $("#tt").datagrid("getRowIndex",rows[i])
                $("#tt").datagrid("checkRow",index);
        }
    }
    $('#materialQuality').combobox({
        url:'${pageContext.request.contextPath}/materialQualityType/materialQualityTypeSelect.shtml',
        valueField:'id',
        textField:'name',
        width:100
    });

    $('#clientId').combobox({
        url:'${pageContext.request.contextPath}/client/clientSimpleNameSelectTwoContent.shtml',
        valueField:'id',
        textField:'text',
        width:130
    });

    initInputDataInput("makeTimestart");
    initInputDataInput("makeTimeend");
    initInputDataInput("deliveryTimestart");
    initInputDataInput("deliveryTimeend");
//    var endtime=getNowDate();
//    starttime=getNowMonth()+"-01";
//    setDataToDateBox("deliveryTimestart",starttime);
//    setDataToDateBox("deliveryTimeend",endtime);
</script>
</body>