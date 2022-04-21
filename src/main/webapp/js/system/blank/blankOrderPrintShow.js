var editId;
$(function() {
    initComboGridEditor();
    id='printRows';
    url='/blank/findPrintByPage.shtml?ids='+getValueById("ids");
    columns=[[
        {field:'id',hidden:true,title:'编号',width:100,align:'center'},
        {field:'clientFullName',title:'客户',width:100,align:'center'},
        {field:'contractNumber',title:'订单编号',width:100,align:'center'},
        {field:'makeTime',title:'来单日期',width:100,align:'center'},
        {field:'goodName',title:'产品名称',width:100,align:'center'},
        {field:'mapNumber',title:'图号',width:100,align:'center'},
        {field:'goodSize',title:'成品尺寸',width:100,align:'center'},
        {field:'orderAmount',title:'订货数量',width:100,align:'center'},
        {field:'blankSize',title:'下料尺寸',width:100,align:'center',editor:'text'},
        {field:'blankWeight',hidden:true,title:'下料重量',width:100,align:'center'},
        {field:'amount',title:'下料数量',width:100,align:'center',editor:'text'},
        {field:'deliveryTime',title:'交货日期',width:100,align:'center'},
        {field:'materialQuality',title:'材质',width:100,align:'center'},
        {field:'length',title:'米数',width:100,align:'center',editor:'text'},
        {field:'weight',title:'吨数',width:100,align:'center',editor:'text'},
        {field:'remarks1',title:'备注1',width:100,align:'center'},
        {field:'remarks2',title:'备注2',width:100,align:'center'},
        {field:'remarks3',title:'备注3',width:100,align:'center'},
    ]];
    initDataGridFitColumnsFalseNoToolBar(id,url,columns,function (rowIndex, rowData) {
    });
});
