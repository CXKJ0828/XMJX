<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/materialbackexcel/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'north'" style="height: 30px">
    <c:forEach items="${res}" var="key">
        ${key.description}
    </c:forEach>
    <div style="float: left;width:100%">
        <div style="float: left;">
            <form id="form" name="form"  method="post" enctype="multipart/form-data"
            >
                <input name="file" id="file" type="file" style="width:200px">
                <a href="#" class="easyui-linkbutton" onclick="upload()" title="数据导入" iconCls="page_go" plain="true">数据导入</a>
            </form>
        </div>
        <div style="float: left;margin-left: 30%">
            <form id="formBack" name="formBack"  method="post" enctype="multipart/form-data"
            >
                <input name="fileBack" id="fileBack" type="file" style="float: left">
                <a href="#" class="easyui-linkbutton" onclick="uploadBack()" title="回料导入" iconCls="page_go" plain="true">回料导入</a>
            </form>
        </div>
    </div>
</div>
<div data-options="region:'west'" style="width:200px;">
    <ul id="tt" class="easyui-tree" style="margin-top: 10px;margin-left: 10px"></ul>
</div>

<div data-options="region:'center'" style="height:100%;" >
    <div style="height: 70%;">
        <table id="goodprocessList"
               class="easyui-datagrid"
               style="width:auto;"
               data-options="rownumbers:true,
                   toolbar:'#tboprate',
                   fit:true,
                   title:'回料明细',
                   view:detailview,
            detailFormatter:detailFormatter,
            onExpandRow:onExpandRow,
            fitColumns:true">
            <thead>
            <tr>
                <th data-options="field:'id',hidden:true,width:200,align:'center'">编号</th>
                <th data-options="field:'materialQuality',width:200,align:'center'">材质</th>
                <th data-options="field:'size',width:200,align:'center'">规格/Φ</th>
                <th data-options="field:'length',width:200,align:'center'">订料米数</th>
                <th data-options="field:'weight',width:200,align:'center'">订料重量</th>
                <th data-options="field:'price',width:200,align:'center'">含税单价</th>
                <th data-options="field:'money',width:200,align:'center'">订料金额</th>
                <th data-options="field:'backLength',width:200,align:'center'">回料米数</th>
                <th data-options="field:'backWeight',width:200,align:'center'">回料重量</th>
                <th data-options="field:'backMoney',width:200,align:'center'">回料金额</th>
                <th data-options="field:'lackLength',width:200,align:'center'">余料米数</th>
                <th data-options="field:'lackMoney',width:200,align:'center'">余料金额</th>
                <th data-options="field:'remarks',hidden:true,width:200,align:'center'">remarks</th>
            </tr>
            </thead>
        </table>
    </div>
    <div style="height: 30%">
        <table id="sumShow"
               class="easyui-datagrid"
               style="width:auto;"
               data-options="rownumbers:true,
                   title:'材质统计',
                   toolbar:'#tbsum',
                   fit:true,
                    onLoadSuccess: function (data) {
                        setContentToDivSpanById('moneysumAll', '订料金额:'+floatToVar2(computeAllMoney('sumShow'))+'  回料金额:'+floatToVar2(computeMoney('sumShow','backMoney'))+'  余料金额:'+floatToVar2(computeMoney('sumShow','lackMoney')));
                    },
            fitColumns:true">
            <thead>
            <tr>
                <th data-options="field:'materialQuality',width:200,align:'center',editor:'text'">材质</th>
                <th data-options="field:'length',width:200,align:'center'">订料米数</th>
                <th data-options="field:'weight',width:200,align:'center'">订料重量</th>
                <th data-options="field:'money',width:200,align:'center',editor:'text'">订料金额</th>
                <th data-options="field:'backLength',width:200,align:'center'">回料米数</th>
                <th data-options="field:'backWeight',width:200,align:'center'">回料重量</th>
                <th data-options="field:'backMoney',width:200,align:'center',editor:'text'">回料金额</th>
                <th data-options="field:'lackLength',width:200,align:'center',editor:'text'">余料米数</th>
                <th data-options="field:'lackMoney',width:200,align:'center',editor:'text'">余料金额</th>
            </tr>
            </thead>
        </table>
    </div>

            <div id="tboprate" style="height:auto">
                 <span style="margin-left: 10px">
                   <span style="font-size: 14px;color: red" id="moneyAll"></span>
                 </span>
                状态:
                <select class="easyui-combobox" id='state' style="width:120px;">
                    <option value="不限">不限</option>
                    <option value="回料未完成">回料未完成</option>
                    <option value="回料已完成">回料已完成</option>
                </select>
                <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
                <a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>
                <a href="#" class="easyui-linkbutton" onclick="clearselectAll()" title="取消全选" iconCls="icon-undo" plain="true">取消全选</a>
                <a href="#" class="easyui-linkbutton" onclick="deleteEntity()" title="删除" iconCls="icon-cancel" plain="true">删除</a>
            </div>
    <div id="tbsum" style="height:auto">
                 <span style="margin-left: 10px">
                   <span style="font-size: 14px;color: red" id="moneysumAll"></span>
                 </span>
    </div>
            <script>
                function clearselectAll() {
                    clearDatagridSelections('goodprocessList');
                }
                function selectAll() {
                    //获取数据列表中的所有数据
                    var rows = $("#goodprocessList").datagrid("getRows");
                    //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
                    for(var i=0;i<rows.length;i++){
                        var index = $("#goodprocessList").datagrid("getRowIndex",rows[i])
                        $("#goodprocessList").datagrid("checkRow",index);
                    }
                }

                function find() {
                    reloadDatagridMessage('goodprocessList');
                }
                function deleteEntity() {
                    var rows=getDatagridSelections('goodprocessList');
                    var ids="";
                    for(i=0;i<rows.length;i++){
                        ids=ids+rows[i].id+",";
                    }
                    if(ids!=""){
                        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
                            if (r){
                                communateGet(rootPath +'/materialbackexcel/deleteEntity.shtml?ids='+ids+'&password='+r,function back(data){
                                    if(data=='success'){
                                        reloadDatagridMessage('goodprocessList');
                                        reloadTreeMessage('tt');
                                    }else{
                                        showErrorAlert("操作",data);
                                    }
                                });
                            }
                        });
                    }else{
                        $.messager.alert('警告','不存在可以删除数据','warning');
                    }
                }
                $("#goodprocessList").datagrid({
                    url:rootPath + '/materialbackexcel/findByRemarks.shtml',
                    onDblClickRow:function (rowIndex, rowData) {
                        $("#backWindow").window({
                            width: 400,
                            modal: true,
                            height: 200,
                            top:100,
                            href: rootPath + '/materialbackexcel/backUI.shtml?id='+rowData.id,
                            onClose:function () {
                                reloadDatagridMessage("goodprocessList");
                            }
                        });
                    },
                    onLoadSuccess:function(data){
                        setContentToDivSpanById('moneyAll', '订料金额:'+floatToVar2(computeAllMoney('goodprocessList'))+'  回料金额:'+floatToVar2(computeMoney('goodprocessList','backMoney'))+'  余料金额:'+floatToVar2(computeMoney('goodprocessList','lackMoney')));
                        $('#sumShow').datagrid('loadData', data.rowsSum);
                    },
                    onBeforeLoad: function (params) {
                        params.pageNumber = params.page;
                        params.sortName = params.sort;
                        params.remarks=remarks;
                        params.state=getContentBySelect('state');
                    },
                    rowStyler: function(index,row){
                        var lackLength=VarToFloat(row.lackLength);
                        if (lackLength>=0){
                            return 'color:#048133;font-weight:bold';
                        }else{
                            return 'color:#DC143C;font-weight:bold';//红色
                        }
                    }
                });
                function computeMoney(id,content) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        if(content=='backMoney'){
                            backMoney=row.backMoney;
                        }else if(content=='lackMoney'){
                            backMoney=row.lackMoney;
                        }

                        money=floatToVar2(backMoney);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function computeAllMoney(id) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        money=floatToVar2(row.money);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function computeSumAllMoney(id) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        money=row.money;
                        money=floatToVar2(money);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function computeSumBuyMoney(id) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        row=rows[i];
                        money=row.buyMoney;
                        money=floatToVar2(money);
                        total += StrToFloat(money);
                    }
                    total=Math.round(total*100)/100
                    return total;
                }
            </script>
</div>
<div id="backWindow" title="回料操作">
</div>
</body>