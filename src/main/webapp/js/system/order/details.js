var editId;


function selectProcess(rowIndex, rowData) {
    if(checkIsSelectEd(editId)){
        var parentRowNum=editDatagrid.replace("ddv-","");
        var parentDetailsAmount=getDatagridJsonByIndexAndId('orderdetails',parentRowNum).amount;
        setContentToDatagridEditorText(editDatagrid,editId,"processId",rowData.id);
        setContentToDatagridEditorText(editDatagrid,editId,"time",VarToFloat(rowData.oneTime)*VarToFloat(parentDetailsAmount));
        setContentToDatagridEditorText(editDatagrid,editId,"name",rowData.simpleName);
        setContentToDatagridEditorText(editDatagrid,editId,"amount",parentDetailsAmount);
        setContentToDatagridEditorText(editDatagrid,editId,"code",rowData.organization);
    }
}
var editId=null;
var editDatagrid=null;
function RowMeanu(rowIndex,x,y,datagridId){
    // alert(editId+":"+editDatagrid);
    if(editId!=null||editId!=""){
        if(isContain(editDatagrid,'ddv')){
            var row=getDatagridJsonByIndexAndId(editDatagrid,editId);
            if(row.code==undefined||row.amount==''){
                showErrorAlert("提示","请选择上一条工序或输入数量，再进行操作");
                beginEditDatagridByIndex(editDatagrid,editId);
            }else{
                endEditDatagridByIndex(editDatagrid,editId);
                editDatagrid=datagridId;
                clearSelectAndSelectRowDatagrid(datagridId,rowIndex);
                editId=rowIndex;
                showMenu("rightMenu",x,y);
            }
        }else{
            editDatagrid=datagridId;
            clearSelectAndSelectRowDatagrid(datagridId,rowIndex);
            editId=rowIndex;
            showMenu("rightMenu",x,y);
        }

    }else{
        editDatagrid=datagridId;
        clearSelectAndSelectRowDatagrid(datagridId,rowIndex);
        editId=rowIndex;
        showMenu("rightMenu",x,y);
    }

}
function HeaderMeanu(x,y,datagridId){
    // alert(editId+":"+editDatagrid);
    //editDatagrid为上一个编辑的datagrid
    // if(editId!=null||editId!=""){
    if(editId!=null||editId!=""){
        editId=0;
    }
        if(isContain(editDatagrid,'ddv')){
            var row=getDatagridJsonByIndexAndId(editDatagrid,editId);
            if(row.code==undefined||row.amount==''){
                showErrorAlert("提示","请选择上一条工序或输入数量，再进行操作");
                beginEditDatagridByIndex(editDatagrid,editId);
            }else{
                endEditDatagridByIndex(editDatagrid,editId);
                editDatagrid=datagridId;
                showMenu("rightMenuHeader",x,y);
            }
        }else{
            if(editDatagrid!=undefined){
                endEditDatagridByIndex(editDatagrid,editId);
            }
            editDatagrid=datagridId;
            showMenu("rightMenuHeader",x,y);
        }

    // }

}

$("#orderdetails").datagrid({
    onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
        e.preventDefault(); //阻止浏览器捕获右键事件
        if(editId!=null){
            endEditDatagridByIndex("orderdetails",editId)
        }
        RowMeanu(rowIndex,e.pageX,e.pageY,"orderdetails");
        e.preventDefault();  //阻止浏览器自带的右键菜单弹出
    },
    onHeaderContextMenu: function (e, field){ //右键时触发事件                      
        e.preventDefault(); //阻止浏览器捕获右键事件
        if(editId!=null){
            endEditDatagridByIndex("orderdetails",editId)
        }
        HeaderMeanu(e.pageX,e.pageY,"orderdetails");
        e.preventDefault();  //阻止浏览器自带的右键菜单弹出
    }
})

function delEntity() {
    var rowData=getDatagridJsonByIndexAndId(editDatagrid,editId);
    var url="";
    if(isContain(editDatagrid,'ddv')){
        url=rootPath+'/order/deleteDetailsEntity.shtml?orderdetailsprocessId='+rowData.id+"&orderdetailsId=";
    }else{
        url=rootPath+'/order/deleteDetailsEntity.shtml?orderdetailsId='+rowData.id+"&orderdetailsprocessId=";
    }
    if(editId!=null){
        communateGet(url,function back(data){
            deleteOneRowById(editDatagrid,editId);
        });
    }else{
        $.messager.alert('警告','不存在可以删除数据','warning');
    }
}
function addContent() {
    alert(editDatagrid);
    var rows = getAllRowsContent(editDatagrid);
    var row=rows[editId];
    if(isContain(editDatagrid,'ddv')){
        addRowInLast(editDatagrid,{state:"未完成"});
    }else{
        var batchNumber=getBatchNumber(getValueById('orderId'),rows.length);
        addRowInLast(editDatagrid,{state:"未完成",batchNumber:batchNumber});
    }
    if(rows.length==0){
        editId=0;
    }else{
        editId=rows.length-1;
    }
    beginEditDatagridByIndex(editDatagrid,editId);
}
function edit() {
    beginEditDatagridByIndex(editDatagrid,editId);
}
function printEntity(rowIndex, rowData){
    var code=rowData.batchNumber;
    $("#codeShow").window({
        width:250,
        title:'二维码展示',
        modal: true,
        height: 240,
        top:50,
        href: rootPath + '/order/codeUI.shtml?code='+code,
        onClose:function () {

        }
    });
}



