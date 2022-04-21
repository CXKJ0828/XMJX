<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/toolOut/list.js"></script>
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
<div data-options="region:'center'" style="height:200px;">
    <table id="tt" style="width: 4000px"></table>
    <table id="ttselect" style="display: none"></table>
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        种类：
        <input  id="toolTypeId" class="inputShow">
        领取时间:<input  id="starttime" style="width: 100px" type="text">
        至
        <input id="endtime" style="width: 100px" type="text">
        员工:
        <select id="userId" style="width:100px" class="easyui-combogrid"  data-options="
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
        <input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <div id="sum">数量合计:</div>
    </div>
</div>
<script>
    initInputDataInput("starttime");
    initInputDataInput("endtime");
    var toolTypeId="";
    $('#toolTypeId').combobox({
        url:'${pageContext.request.contextPath}/toolType/toolTypeSelect.shtml',
        valueField:'id',
        textField:'name',
        width:100,
        onSelect: function(record){
            toolTypeId=record.id;
        }
    });
</script>
</body>