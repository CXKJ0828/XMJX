<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/progressSearch.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
        <table id="tt" data-options="fit:true" style="width:auto"></table>
    <table id="ttExport" >
    </table>
</div>
<div id="tb" style="height:auto">
    <form id="form" name="form"  method="post" enctype="multipart/form-data">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="#" class="easyui-linkbutton" onclick="upload()" title="导入" iconCls="page_go" plain="true">导入</a>
        模板：<input name="file" id="file" type="file">
        客户：
        <input  id="clientId" style="width:100px" name="heatTreatFormMap.clientId" class="inputShow">
        产品名称:
        <select class="easyui-combobox" id='goodName' name="heatTreatFormMap.goodName" style="width:80px;">
            <option value="不限">不限</option>
            <option value="轴">轴</option>
            <option value="套">套</option>
            <option value="垫/片">垫/片</option>
            <option value="其他">其他</option>
            <option value="不限">不限</option>
        </select>
        交货日期:<input  id="starttime" type="text">
        至
        <input id="endtime"  type="text">
        <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;width: 100px">
        <a href="javascript:void(0)" onclick="find('检索')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <a href="javascript:void(0)" onclick="find('全部')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">查看全部</a>
        <a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>
        <a href="#" id="distributionOprate" class="easyui-linkbutton" onclick="distributionEntity()" title="分配" iconCls="icon-redo" plain="true">分配</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="downLoad()">导出</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="downLoadAll()">全部导出</a>
        <span style="font-size: 18px;font-weight: bold">分配数量合计:</span>
        <span style="font-size: 18px;font-weight: bold" id="distributionAmountSum"></span>
    </form>
</div>
<script>
    function selectAll() {
        //获取数据列表中的所有数据
        var rows = $("#tt").datagrid("getRows");
        //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
        for(var i=0;i<rows.length;i++){
            var state = rows[i].state;
            if(state==undefined){
                var index = $("#tt").datagrid("getRowIndex",rows[i])
                $("#tt").datagrid("checkRow",index);
            }
        }
    }

    function distributionEntity() {
        var rows=getDatagridSelections('tt');
        var ids="";
        var length=rows.length;
        var isOprate=true;
        if(length==0){
            showErrorAlert("提示","请选择分配内容");
        }else{
            for(i=0;i<rows.length;i++){
                var id=rows[i].progressSearchId;
                ids=ids+id+",";
                var nowDistributionAmount=rows[i].nowDistributionAmount;
                    if(isNull(nowDistributionAmount)||VarToFloat(nowDistributionAmount)<=0){
                        isOprate=false;
                        i=rows.length;
                        showErrorAlert("提示","分配数量不可为空");
                    }else {
                        isOprate=true;
                    }
            }
            if(isOprate){
                $("#distributionWindow").window({
                    width: 400,
                    modal: true,
                    height: 200,
                    top:100,
                    href: rootPath + '/heattreat/distributionUI.shtml?ids='+ids+'&origin=进度查询',
                    onClose:function () {
                        $('#tt').datagrid("reload");
                    }
                });
            }

        }
    }


    initInputDataInput('starttime');
    initInputDataInput('endtime');

    $('#clientId').combobox({
        url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
        valueField:'id',
        textField:'text',
        width:130
    });

    function upload() {
        $.messager.progress({
            title:'Please waiting',
            msg:'Loading data...'
        });
        $('#form').form({
            url:rootPath + '/blank/uploadExcelSearchProgress.shtml',
            onSubmit: function(){
                return $(this).form('validate');
            },
            success:function(data){
                $.messager.progress('close');
                var jsonObj = eval( '(' + data + ')' ); // eval();方法
                if(jsonObj=="success"){
                    reloadDatagridMessage('tt');
                    showMessagerCenter("提示","导入成功");
                }else{
                    reloadDatagridMessage('tt');
                    showErrorAlert("警告",jsonObj);
                }
            }
        });
        $('#form').submit();
    }
</script>
<div id="distributionWindow" title="分配">
</div>
<div id="progressWindow" class="easyui-window"
     data-options="modal:true,closed:true,iconCls:'icon-save'"
     title="进度详情"
     style="width:500px;height:200px;padding:10px;">
    <div id="progressContent"></div>
</div>
</body>