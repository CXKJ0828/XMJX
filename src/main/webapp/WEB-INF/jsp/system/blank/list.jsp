<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'north'">
    <form id="search">
        <div id="tb" style="height:auto">
            <c:forEach items="${res}" var="key">
                ${key.description}
            </c:forEach>
            <a href="#" id="drawing" class="easyui-linkbutton" onclick="printDrawingEntity()" title="打印图纸" iconCls="icon-print" plain="true">打印图纸</a>
            <a href="#" id="technology" class="easyui-linkbutton" onclick="pringTechnologyEntity()" title="打印工艺卡" iconCls="icon-print" plain="true">打印工艺卡</a>
            <a href="#" class="easyui-linkbutton" onclick="print()" title="打印下料单" iconCls="icon-print" plain="true">打印下料单</a>
            <a href="#" class="easyui-linkbutton" onclick="printConditioningOrder()" title="打印调质单" iconCls="icon-print" plain="true">打印调质单</a>
            <a href="#" class="easyui-linkbutton" onclick="printNormalizingOrder()" title="打印正火单" iconCls="icon-print" plain="true">打印正火单</a>
            <a href="#" class="easyui-linkbutton" onclick="printTicket()" title="打印小票" iconCls="icon-print" plain="true">打印小票</a>
            <a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>
            <a href="javascript:void(0)" onclick="buyMaterial()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'" id="btnExport">生成订料单</a>
            <a href="javascript:void(0)" style="display: none" onclick="makeStartCodes()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'">生成接收码</a>
            <a href="javascript:void(0)" onclick="makeCode()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'">生成编号</a>
            <span id="all">合计:</span>
            <br>
            编号：
            <input  id="code"  name="blankFormMap.code[]" class="inputShow">
            <script>
                var code="";
                $('#code').combobox({
                    url:'${pageContext.request.contextPath}/blank/codeSelect.shtml',
                    valueField:'id',
                    textField:'text',
                    multiple:true,
                    width:220,
                    onSelect: function(record){
                    }
                });
            </script>
            打印日期：
            <input  id="printTime" name="blankFormMap.printTime">
            产品名称:
            <select class="easyui-combobox" id='goodName' name="blankFormMap.goodName" style="width:80px;">
                <option value="不限"></option>
                <option value="轴">轴</option>
                <option value="套">套</option>
                <option value="垫/片">垫/片</option>
                <option value="其他">其他</option>
                <option value="不限">不限</option>
            </select>
            材质：
            <input  id="materialQuality" name="blankFormMap.materialQuality" class="inputShow">
            状态:
            <select class="easyui-combobox" id='state' name="blankFormMap.state" style="width:80px;">
                <option value="不限"></option>
                <option value="未订料">未生成订料单</option>
                <option value="已订料">已生成订料单</option>
                <option value="不限">不限</option>
            </select>
            下料是否完成:
            <input  id="isFinish" name="blankFormMap.isFinish" class="inputShow">
            <input  id="orderId" style="display: none" name="blankFormMap.orderId" class="inputShow">
            客户：
            <input  id="clientId" name="blankFormMap.clientId[]" class="inputShow">
            <input id="goodSize" name="blankFormMap.goodSize" placeholder="请输入成品尺寸" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
            <input id="blankSize" name="blankFormMap.blankSize" placeholder="请输入下料尺寸" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
            来单日期:
            <input  id="makeTimestart" name="blankFormMap.makeTimestart" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
            至
            <input id="makeTimeend" name="blankFormMap.makeTimeend" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
            交货日期:
            <input  id="deliveryTimestart" name="blankFormMap.deliveryTimestart" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
            至
            <input id="deliveryTimeend" name="blankFormMap.deliveryTimeend" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
            <input id="content" name="blankFormMap.content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
            <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        </div>
    </form>
    <script>
        initInputDataInput("makeTimestart");
        initInputDataInput("makeTimeend");
        initInputDataInput("deliveryTimestart");
        initInputDataInput("deliveryTimeend");
    </script>
</div>
<div data-options="region:'west'" style="width:220px;">
    <ul id="tt1"
        data-options="
            method:'get',animate:true"
    ></ul>
</div>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt"  data-options="fit:true" style="width:auto"></table>
    <table id="ttexport"  data-options="fit:true" style="width:auto"></table>
    <script>
        initForm('search',function (url,data) {
            if(isContain(url,"/blank/findClientByPage.shtml")){
                treeObjext.tree({data: JSON.parse(data)});
            }else  if(isContain(url,"/blank/findOrderByPage.shtml")){
                treeObjext.tree('append', {
                    parent: target,
                    data: JSON.parse(data)
                });
            }else  if(isContain(url,"/blank/findOrderDetailsByPage.shtml")){
                treeObjext.tree('append', {
                    parent: target,
                    data: JSON.parse(data)
                });
            }
        })

        function getLeftTreeClientMessage() {
            var id='search';
            var url='/blank/findClientByPage.shtml'
            submitForm(id,url);

        }

        function getLeftTreeOrderMessage(clientId) {
            var id='search';
            var url='/blank/findOrderByPage.shtml';
            submitForm(id,url);
        }

        function getLeftTreeOrderDetailsMessage() {
            var id='search';
            var url='/blank/findOrderDetailsByPage.shtml';
            submitForm(id,url);
        }

        function  reloadTT() {
            reloadDatagridMessage('tt');
        }

        var treeObjext=$('#tt1');
        getLeftTreeClientMessage();
        var clientId="";
        var goodId="";
        var orderId="";
        var target="";
        treeObjext.tree({
            onBeforeSelect:function (node) {
                var childrenNodes = treeObjext.tree('getChildren',node.target);
                var lengthChild=childrenNodes.length;
                var state=node.state;
                if(state=='closed'&&lengthChild==0){
                    if(isContain(node.id,"客户")){//根据获取获取订单
                        clientId=node.id.replace("客户","");
                        setContentsToCombobox("clientId",clientId);
                        setContentToInputById("orderId","")
                        reloadTT();
                        target=node.target;
                        getLeftTreeOrderMessage();
                    }
                    if(isContain(node.id,"订单")){//根据根据订单获取明细
                        orderId=node.id.replace("订单","");
                        setContentsToCombobox("clientId",clientId);
                        setContentToInputById("orderId",orderId)
                        reloadTT();
                        target=node.target;
                        getLeftTreeOrderDetailsMessage();
                    }
                }
                if(isContain(node.id,"明细")){
                    goodId=node.id.replace("明细","");
                    reloadTT();
                }
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
            multiple:true,
            width:130,
            onSelect: function(record){
            }
        });
    </script>
</div>
<div id="printOrder">

</div>
<div id="codeShow">

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
    function makeStartCodes(){
        var rows=getDatagridSelections('tt');
        var ids="";
        var codes="";
        for(i=0;i<rows.length;i++){
            codes = codes + rows[i].startQRCode + "amount" + rows[i].amount +"blankId"+rows[i].id+ ",";
        }
            $("#codeShow").window({
                width: 250,
                title: '接收码',
                modal: true,
                height: 240,
                top: 50,
                href: rootPath + '/blank/codesUI.shtml?codes=' + codes,
                onClose: function () {
                    codes="";
                }
            });
    }

    function makeCode(){
        var rows=getDatagridSelections('tt');
        var ids="";
        var codes="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
        }
        showInputDialog("提示","请输入下料单编号",function (content) {
            communateGet(rootPath +'/blank/makeCodeEntity.shtml?ids='+ids+'&code='+content,function back(data){
                if(data=='success'){
                    reloadDatagridMessage('tt');
                }else{
                    showErrorAlert("操作",data);
                }
            });
        })
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
        $("#btnExport").css("color", "#CCC");
        $('#btnExport').linkbutton('disable');
        setTimeout('$("#btnExport").linkbutton("enable");$("#btnExport").css("color","black");', 10000);//10秒后恢复
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

    //打印调质单
    function printConditioningOrder() {
        var rows=getDatagridSelections('tt');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
        }
        $("#printOrder").window({
            width:1000,
            title:'调质单',
            modal: true,
            top:10,
            href: rootPath + '/blank/blankconditioningOrderPrintUI.shtml?rows=&ids='+ids,
            onClose:function () {

            }
        });
    }

    //打印正火单
    function printNormalizingOrder() {
        var rows=getDatagridSelections('tt');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
        }
        $("#printOrder").window({
            width:1000,
            title:'正火单',
            modal: true,
            top:10,
            href: rootPath + '/blank/blanknormalizingOrderPrintUI.shtml?rows=&ids='+ids,
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
        url:'${pageContext.request.contextPath}/blank/materialQualityContainIdSelect.shtml',
        valueField:'id',
        textField:'text',
        width:100,
        onSelect: function(record){
            materialQuality=record.id;
        }
    });

    $('#isFinish').combobox({
        url:'${pageContext.request.contextPath}/blank/isFinishSelect.shtml',
        valueField:'id',
        textField:'text',
        width:100
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
<div id="printDrawing">

</div>
<div id="pringTechnology">

</div>
</body>