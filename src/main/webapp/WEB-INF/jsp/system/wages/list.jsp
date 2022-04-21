<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/wages/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true" style="width:auto"></table>
    <div style="display: none">
        <table id="ttselect" style="width:auto;"></table>
        <table id="ttexport" style="width:auto"></table>
    </div>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
            <input style="display: none"   type="text" id="taxRate" value="${taxRate}" />
            <input  style="display: none"  type="text" id="defaulttaxRate" value="${taxRate}" />
            <c:forEach items="${res}" var="key">
                ${key.description}
            </c:forEach>
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
                        fitColumns: true,
                        onSelect:function(rowIndex, rowData){
                           setContentToInputById('user',rowData.userName);
                        }"></select>
            <input id="user" placeholder="请输入员工姓名" style="display: none">
            <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;margin-top:5px;width: 100px">
            <input  id="starttime" type="text">
            至
            <input id="endtime" type="text">
            <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
            <br>
            <span id="allMoney">合计:</span>
        </form>
    </div>
</div>
<script>

    var endtime=getNowDate();
    starttime=getNowMonth()+"-01";
    initInputDataInput("starttime");
    initInputDataInput("endtime");
    setDataToDateBox("starttime",starttime);
    setDataToDateBox("endtime",endtime);
    initComboGridEditor();

</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>