<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/printCompleteCodeShow.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <div style="height: 70%">
        <table id="tt" data-options="nowrap:false,fit:true"  style="width:auto;height:300px"></table>
    </div>
    <div style="height: 30%">
        <table id="ttSelect" data-options="nowrap:false,fit:true"   style="width:auto"></table>
    </div>
    <div id="tb" style="height:auto">
        客户：
        <input  id="clientId" class="inputShow">
        <input id="contractNumber" placeholder="请输入订单号" style="line-height:20px;width:150px;border:1px solid #ccc;margin-left: 10px">
        <input id="mapNumber" placeholder="请输入图号" style="line-height:20px;width:150px;border:1px solid #ccc;margin-left: 10px">
        工序:
        <input  id="processId" class="inputShow">
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
                           setContentToInputById('userName',rowData.userName);
                        }"></select>
        <input id="userName" placeholder="请输入姓名" style="display: none">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
    <div id="tbSelect" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="#" class="easyui-linkbutton" onclick="printEntity()" title="打印" iconCls="icon-print" plain="true">打印</a>
    </div>
    <script>
        $('#clientId').combobox({
            url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
            valueField:'id',
            textField:'text',
            width:130,
            onSelect: function(record){
                clientId=record.id;
            }
        });
        $('#processId').combobox({
            url:'${pageContext.request.contextPath}/process/processSelectTwoContent.shtml',
            valueField:'id',
            textField:'text',
            width:130,
            onSelect: function(record){
                processId=record.id;
            }
        });
    </script>
</div>
<div id="codeShow">

</div>
<script>
    function printEntity() {
        var isOprate=true;
        var rows=getDatagridRows('ttSelect');
        var ids="";
        var codes="";
        var shows="";
        for(i=0;i<rows.length;i++){
            var completeAmount=rows[i].completeAmount;
            var complteAmountInt=VarToInt(completeAmount);
            var receiveAmount=rows[i].receiveAmount;
            var receiveAmountInt=VarToInt(receiveAmount);
            if(complteAmountInt<=0||complteAmountInt>receiveAmountInt){
                isOprate=false;
                i=rows.length;
            }else{
                codes=codes+rows[i].endQRCode+"workersubmitId"+rows[i].id+",";
            }
        }
        if(isOprate){
            $("#codeShow").window({
                title:'完成码',
                width:250,
                modal: true,
                height:240,
                top:50,
                href: rootPath + '/blank/codesUI.shtml?codes='+codes,
                onClose:function () {

                }
            });
        }else{
            showErrorAlert("提示",'不能生成总码<br>完成数量为空或者完成数量大于接收数量');
        }

    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>