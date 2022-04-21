<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/materialbackorder/list.js"></script>
<body class="easyui-layout">
<div data-options="region:'north'" style="height: 30px">
    <c:forEach items="${res}" var="key">
        ${key.description}
    </c:forEach>
    <div style="width:30%;float: left;">
        <form id="form" name="form"  method="post" enctype="multipart/form-data"
        >
            <input name="file" id="file" type="file" style="width:200px">
            <a href="#" class="easyui-linkbutton" onclick="upload()" title="导入" iconCls="page_go" plain="true">导入</a>
        </form>
    </div>
    <div style="float: left;">
        <form id="formMeters" name="formMeters"  method="post" enctype="multipart/form-data"
        >
            <input name="fileMeters" id="fileMeters" type="file" style="width:200px">
            <a href="#" class="easyui-linkbutton" onclick="uploadMeters()" title="导入(山东)" iconCls="page_go" plain="true">导入(山东)</a>
            <span id="sumShow">合计</span>
        </form>
    </div>
</div>
<div data-options="region:'west'" style="width:200px;">
    <ul id="tt" class="easyui-tree" style="margin-top: 10px;margin-left: 10px"></ul>
</div>

<div data-options="region:'center'" >
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
                    <th data-options="field:'id',checkbox:true,width:200,align:'center'">编号</th>
                    <%--<th data-options="field:'arrivalTime',width:200,align:'center'">到货日期</th>--%>
                    <th data-options="field:'materialQuality',width:200,align:'center'">材质</th>
                    <th data-options="field:'outerCircle',width:200,align:'center'">规格/Φ</th>
                    <th data-options="field:'amount',width:200,align:'center'">支数</th>
                    <th data-options="field:'weight',width:200,align:'center'">重量</th>
                    <th data-options="field:'length',width:200,align:'center'">回料米数</th>
                    <th data-options="field:'buyLength',width:200,align:'center'">订料米数</th>
                    <th data-options="field:'lackAmount',width:200,align:'center'">余料米数</th>
                    <%--<th data-options="field:'taxPrice',width:200,align:'center'">含税单价</th>--%>
                    <%--<th data-options="field:'taxMoney',width:200,align:'center'">含税金额</th>--%>
                    <th data-options="field:'materialBackOrderId',hidden:true,width:200,align:'center'">回料单id</th>
                    <th data-options="field:'materialBuyOrderDetailsId',hidden:true,width:200,align:'center'">订料明细id</th>
                </tr>
                </thead>
            </table>
            <script>
                function saveEntity() {
                    endEditDatagridByIndex('goodprocessList',editId);
                    var rows= $('#goodprocessList').datagrid('getChanges');
                    var url='${pageContext.request.contextPath}/materialbackorder/editEntity.shtml';
                    var data={
                        rows:rows,
                    }
                    communatePost(url,ListToJsonString(data),function back(data){
                        var url=rootPath+'/materialbackorder/findByMaterialQualityAndMaterialbackorderId.shtml?materialQualityAndMaterialbackorderId='+materialQualityAndMaterialbackorderId;
                        $('#goodprocessList').datagrid('options').url=url;
                        $("#goodprocessList").datagrid('reload');
                    })
                }
            </script>
            <div id="tboprate" style="height:auto">
                <a href="#" class="easyui-linkbutton" onclick="deleteEntity()" title="删除" iconCls="icon-cancel" plain="true">删除</a>
                <%--<a href="#" class="easyui-linkbutton" onclick="saveEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>--%>
            </div>
            <script>

                function deleteEntity() {
                    var rows=getDatagridSelections('goodprocessList');
                    var ids="";
                    var materialBackOrderId='';
                    for(i=0;i<rows.length;i++){
                        ids=ids+rows[i].id+",";
                        if(i==0){
                            materialBackOrderId=rows[i].materialBackOrderId;
                        }
                    }
                    if(ids!=""){
                        $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
                            if (r){
                                communateGet(rootPath +'/materialbackorder/deleteDetailsEntity.shtml?ids='+ids+'&password='+r+"&materialBackOrderId="+materialBackOrderId,function back(data){
                                    if(data=='success'){
                                        reloadDatagridMessage('goodprocessList')
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

                var editId;
                $("#goodprocessList").datagrid({
                    onDblClickRow:function (rowIndex, rowData) {
                        if(editId!=null){
                            endEditDatagridByIndex('goodprocessList',editId);
                        }else{
                        }
                        editId=rowIndex;
                        beginEditDatagridByIndex('goodprocessList',editId);
//                        editAndAddSetContentAndChange();
                    },
                    rowStyler: function(index,row){
                        var lackAmount=VarToFloat(row.lackAmount);
                        if (lackAmount>=0){
                            return 'color:#048133;font-weight:bold';
                        }else{
                            return 'color:#DC143C;font-weight:bold';//红色
                        }
                    },
                    onLoadSuccess: function (data) {
                        var amountSum=0;
                        var lengthSum=0;
                        var buyLengthSum=0;
                        var lackAmountSum=0;
                        var rows=data.rows;
                        for(j=0;j<rows.length;j++){
                            var row=rows[j];
                            var lackAmount=VarToFloat(row.lackAmount);
                            if(lackAmount<0){
                                lackAmountSum=lackAmountSum+lackAmount;
                            }
                            var amount=VarToFloat(row.amount);
                            amountSum=amountSum+amount;
                            var length=VarToFloat(row.length);
                            lengthSum=lengthSum+length;
                            var buyLength=VarToFloat(row.buyLength);
                            buyLengthSum=buyLengthSum+buyLength;
                        }
                        lackAmountSum=-lackAmountSum;
                        setContentToDivSpanById("sumShow","支数合计:"+amountSum+" 回料米数合计:"+floatToVar4(lengthSum)+" 订料米数合计:"+floatToVar4(buyLengthSum)+" 欠料米数合计:"+floatToVar4(lackAmountSum));

                    }
                });
                function computeByState(id,colName) {
                    var rows = $('#'+id).datagrid('getRows');
                    var total = 0;
                    for (var i = 0; i < rows.length; i++) {
                        if(rows[i].state!='已订料'){
                            total += StrToFloat(rows[i][colName]);
                        }

                    }
                    total=Math.round(total*100)/100
                    return total;
                }
                function editAndAddSetContentAndChange() {
                    var weight=getDatagridRows("goodprocessList")[editId].weight;
                    setEditorOnChange("goodprocessList",1,function onChangeBack(index,content) {
                        var stockWeight=getContentByEditor("goodprocessList",0);
                        if(VarToFloat(stockWeight)>0){
                            setContentToDatagridEditorText("goodprocessList",editId,'stockWeight',VarToFloat(stockWeight)-VarToFloat(content));
                        }

                    });
                }
            </script>
</div>
</body>