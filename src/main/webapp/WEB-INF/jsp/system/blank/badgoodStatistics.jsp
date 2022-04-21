<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/badgoodStatistics.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        状态:
        <select class="easyui-combobox" id='state' name="state" style="width:80px;">
            <option value="不限">不限</option>
            <option value="未补料">未补料</option>
            <option value="已补料">已补料</option>
        </select>
        交货日期:<input  id="starttime" style="width: 100px" type="text">
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
        <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <a href="javascript:void(0)" onclick="feed()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'">生成补料单</a>
    </div>
</div>
<script>
    initInputDataInput("starttime");
    initInputDataInput("endtime");

  function feed() {
      var list= getDatagridSelections("tt");
      var url='${pageContext.request.contextPath}/blank/feedEntity.shtml';
      var data={
          selectList:list,
      }
      communatePost(url,ListToJsonString(data),function back(data){
         reloadDatagridMessage("tt");
      })
  }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
<div id="rightMenu" class="easyui-menu" style="width: 50px; display: none;">
    <div   onclick="printShow('废品通知单')">废品通知单</div>
    <div   onclick="printShow('产品不合格单')">产品不合格单</div>
    <div   onclick="printShow('外协废品单')">外协废品单</div>   
</div>
<script>
    function printShow(origin) {
        var row=getDatagridJsonByIndexAndId("tt",editId);
        var workersubmitId=row.workdersubmitId;
        var orderdetailsId=row.orderdetailsId;
        $("#printShow").window({
            width:600,
            title:origin,
            modal: true,
            top:20,
            href: rootPath + '/blank/badprintorderShowUI.shtml?orderdetailsId='+orderdetailsId
            +"&workersubmitId="+workersubmitId
            +"&origin="+origin,
            onClose:function () {

            }
        });
    }
</script>
<div id="printShow">

</div>
<div id="unqualifiedOrder">

</div>
<div id="scrapOrder">

</div>
<script>
    function unqualifiedOrder() {
        var row=getDatagridJsonByIndexAndId("tt",editId);
        var workersubmitId=row.workdersubmitId;
        var orderdetailsId=row.orderdetailsId;
        $("#unqualifiedOrder").window({
            width:800,
            title:'产品不合格单',
            modal: true,
            top:10,
            href: rootPath + '/blank/unqualifiedOrderUI.shtml?orderdetailsId='+orderdetailsId+"&workersubmitId="+workersubmitId,
            onClose:function () {

            }
        });
    }

    function scrapOrder() {
        var row=getDatagridJsonByIndexAndId("tt",editId);
        var workersubmitId=row.workdersubmitId;
        var orderdetailsId=row.orderdetailsId;
        $("#scrapOrder").window({
            width:800,
            title:'外协废品单',
            modal: true,
            top:10,
            href: rootPath + '/blank/scrapOrderPrintShowUI.shtml?orderdetailsId='+orderdetailsId+"&workersubmitId="+workersubmitId,
            onClose:function () {

            }
        });
    }
</script>
</body>