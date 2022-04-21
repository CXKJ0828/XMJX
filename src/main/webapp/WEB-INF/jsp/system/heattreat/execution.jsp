<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/heattreat/execution.js"></script>
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
<div data-options="region:'north'">
    <form id="form">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <span id="all">合计:</span>
        <br>
        产品名称:
        <select class="easyui-combobox" id='goodName' name="heatTreatFormMap.goodName" style="width:80px;">
            <option value="不限"></option>
            <option value="轴">轴</option>
            <option value="套">套</option>
            <option value="垫/片">垫/片</option>
            <option value="其他">其他</option>
            <option value="不限">不限</option>
        </select>
        客户：
        <input  id="clientId" style="width:100px" name="heatTreatFormMap.clientId" class="inputShow">
        材质：
        <input  id="materialQuality" style="width:100px" name="heatTreatFormMap.materialQuality" class="inputShow">
        员工:
        <select id="oprateUserId" style="width:200px" class="easyui-combogrid" name="heatTreatFormMap.oprateUserId"  data-options="
                        panelWidth: 200,
                        idField: 'id',
                        textField: 'userName',
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/userSelect.shtml',
                        method: 'get',
                        columns: [[
                            {field:'id',hidden:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                        fitColumns: true"></select>
        状态:
        <select class="easyui-combobox" id='state' name="state" style="width:80px;">
            <option value="不限"></option>
            <option value="未完成">未完成</option>
            <option value="已完成">已完成</option>
            <option value="不限">不限</option>
        </select>
        交货日期:<input  id="starttime"  type="text">
        至
        <input id="endtime"  type="text">

        接收时间:<input  id="starttimeGet"  type="text">
        至
        <input id="endtimeGet"  type="text">
        完成时间:<input  id="starttimeComplete"  type="text">
        至
        <input id="endtimeComplete"  type="text">
        关键字:<input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <script>
            $('#materialQuality').combobox({
                url:'${pageContext.request.contextPath}/materialQualityType/materialQualityTypeSelect.shtml',
                valueField:'id',
                textField:'name',
                width:100
            });

            $('#clientId').combobox({
                url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
                valueField:'id',
                textField:'text',
                width:130
            });
            initInputDataInput('starttime');
            initInputDataInput('endtime');
            initInputDataInput('starttimeGet');
            initInputDataInput('endtimeGet');
            var endtimeGet=getNowDate();
            starttimeGet=getNowMonth()+"-01";
            setDataToDateBox("starttimeGet",starttimeGet);
            setDataToDateBox("endtimeGet",endtimeGet);
            initInputDataInput('starttimeComplete');
            initInputDataInput('endtimeComplete');

        </script>
    </form>
</div>
<div data-options="region:'west'" style="width:220px;">
    <ul id="tt1"
        data-options="
            method:'get',animate:true"
    ></ul>
    <script>
        $('#tt1').tree({
            url:'${pageContext.request.contextPath}/heattreat/findExecutionClientByPage.shtml',
            onBeforeLoad: function (node,params) {
                params.content=getValueById('content');
                params.oprateUserId=getContentBySelect("oprateUserId");
                params.goodName=getContentBySelect("goodName");
                params.clientId=getContentBySelect("clientId");
                params.materialQuality=getContentBySelect("materialQuality");
                params.state=getContentBySelect("state");
                params.startTime=getDataboxValue("starttime");
                params.endTime=getDataboxValue("endtime");
            },onBeforeSelect:function (node) {
                var clientId=node.id;
                setContentToCombobox("clientId",clientId);
               reloadDatagridMessage("tt");
               clearDatagridSelections("tt");
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
    </script>
</div>
<div data-options="region:'center'">
    <table id="tt" data-options="fit:true"></table>
    <table id="ttselect" style="display: none"></table>
</div>
<div id="printOrder">

</div>
<div id="printDistribution">

</div>
<div id="distributionWindow" title="分配">
</div>
<div id="progressWindow" class="easyui-window"
     data-options="modal:true,closed:true,iconCls:'icon-save'"
     title="进度详情"
     style="width:500px;height:200px;padding:10px;">
    <div id="progressContent"></div>
</div>
</body>