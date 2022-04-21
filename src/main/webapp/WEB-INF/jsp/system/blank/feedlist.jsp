<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/feedlist.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt"  data-options="fit:true" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <a href="#" style="display: none" class="easyui-linkbutton" onclick="selectAll()" title="打印下料单" iconCls="icon-sum" plain="true">打印下料单</a>
        <a href="#" class="easyui-linkbutton" onclick="print()" title="打印下料单" iconCls="icon-print" plain="true">打印下料单</a>
        <a href="#" class="easyui-linkbutton" onclick="printTicket()" title="打印小票" iconCls="icon-print" plain="true">打印小票</a>
        <a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>
        <a href="javascript:void(0)" onclick="buyMaterial()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'">生成订料单</a>
        <br>
        打印日期：
        <input  id="printTime">
        材质：
        <input  id="materialQuality" class="inputShow">
        状态:
        <select class="easyui-combobox" id='state' name="state" style="width:80px;">
            <option value="不限"></option>
            <option value="未订料">未订料</option>
            <option value="已订料">已订料</option>
            <option value="不限">不限</option>
        </select>
        客户：
        <input  id="clientId" class="inputShow">
        <input id="blankSize" placeholder="请输入下料尺寸" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
        <input id="makeTime" placeholder="请输入来单日期" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
        <input id="deliveryTime" placeholder="请输入交货日期" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
        <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>
    <script>
        function printTicket() {
            var rows=getDatagridSelections('tt');
            var ids="";
            for(i=0;i<rows.length;i++){
                ids=ids+rows[i].id+",";
            }
            $("#printOrder").window({
                width:1000,
                title:'小票',
                modal: true,
                top:10,
                href: rootPath + '/blank/ticketPrintUI.shtml?rows=&ids='+ids,
                onClose:function () {
                }
            });
        }

        $('#clientId').combobox({
            url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
            valueField:'id',
            textField:'text',
            width:130,
            onSelect: function(record){
                clientId=record.id;
            }
        });
    </script>
</div>
<div id="printOrder">

</div>
<script>
    function selectMaterialQuality(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.id);
            setContentToDatagridEditorText('tt',editId,"materialQualityName",rowData.name);
        }
    }

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

    function buyMaterial() {
        var isOprate=true;
        var message="";
        var rows=getDatagridSelections("tt");
        for(i=0;i<rows.length;i++){
            var state=rows[i].state;
            if(state=='已订料'){
                isOprate=false;
                message=message+"【"+rows[i].mapNumber+"】,";
            }
        }
        if(isOprate){
            var url='${pageContext.request.contextPath}/materialbuyorder/buyMaterial.shtml';
            var data={
                rows:rows,
            }
            communatePost(url,ListToJsonString(data),function back(data){
                reloadDatagridMessage("tt");
            })
        }else{
            showErrorAlert("警告","所选择图号"+message+"已订料");
            clearDatagridSelections('tt');
        }

    }

    function print() {
        var rows=getDatagridSelections('tt');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
        }
        $("#printOrder").window({
            width:1000,
            title:'下料单',
            modal: true,
            top:10,
            href: rootPath + '/blank/blankorderPrintUI.shtml?rows=&ids='+ids,
            onClose:function () {

            }
        });
    }


    $('#printTime').combobox({
        url:'${pageContext.request.contextPath}/blank/printTimeSelect.shtml',
        valueField:'id',
        textField:'text',
        width:100,
        onSelect: function(record){
            printTime=record.text;
        }
    });

    $('#materialQuality').combobox({
        url:'${pageContext.request.contextPath}/blank/materialQualitySelect.shtml',
        valueField:'id',
        textField:'text',
        width:100,
        onSelect: function(record){
            materialQuality=record.text;
        }
    });

    function selectGood(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"goodId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"goodMapNumber",rowData.mapNumber);
            setContentToDatagridEditorText('tt',editId,"goodName",rowData.name);
            setContentToDatagridEditorText('tt',editId,"goodSize",rowData.goodSize);
            setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.materialQuality);
        }
    }
    function editAndAddSetContentAndChange() {
        setEditorOnChange("tt",0,function onChangeBack(index,content) {
            var rows = $('#tt').datagrid('getRows');
            var amount=getContentByEditor('tt',1);;
            var blankSize=content;
            var blankWeight=rows[editId].blankWeight;
            var outside=blankSize.split("*")[0].replace("Φ","");
            var inside=blankSize.split("*")[1];
            if(isContain(inside,"Φ")){
                inside=inside.replace("Φ","");
                length=blankSize.split("*")[2];
                length=length.replace(/[^\d.]/g,'')
            }else{
                if(content.split("*").length==2){
                    length=inside;
                    inside="0";
                }else{
                    length=content.split("*")[2];
                }
            }

            outside=VarToFloat(outside);
            inside=VarToFloat(inside);
            length=VarToFloat(length);
            length=VarToFloat(amount)*length/1000;
            length=floatToVar4(length);
            setContentToDatagridEditorText("tt",editId,'length',length);

            weight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
            weight=floatToVar4(weight);
            setContentToDatagridEditorText("tt",editId,'weight',weight);
        });

        setEditorOnChange("tt",1,function onChangeBack(index,content) {
            var rows = $('#tt').datagrid('getRows');
            var blankSize=getContentByEditor('tt',0);
            var blankWeight=rows[editId].blankWeight;
            var outside=blankSize.split("*")[0].replace("Φ","");
            var inside=blankSize.split("*")[1];
            if(isContain(inside,"Φ")){
                inside=inside.replace("Φ","");
                length=blankSize.split("*")[2];
                length=length.replace(/[^\d.]/g,'')
            }else{
                if(blankSize.split("*").length==2){
                    length=inside;
                    inside="0";
                }else{
                    length=blankSize.split("*")[2];
                }
            }

            outside=VarToFloat(outside);
            inside=VarToFloat(inside);
            length=VarToFloat(length);
            length=VarToFloat(content)*length/1000;
            length=floatToVar4(length);
            setContentToDatagridEditorText("tt",editId,'length',length);

            weight=outside*outside*length*(0.00617/1000)-inside*inside*length*(0.00617/1000);
            weight=floatToVar4(weight);
            setContentToDatagridEditorText("tt",editId,'weight',weight);
        });
    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>