/**
 * Created by Administrator on 2018/11/9.
 */
function loadOprate(id,index){
    id="#"+id;
    $(id).datagrid('fixDetailRowHeight',index);
    $(id).datagrid('selectRow',index);
    $(id).datagrid('getRowDetail',index).find('form').form('load',row);
    $(id).datagrid('fixDetailRowHeight',index);
    $(id).datagrid('fixRowHeight', index);
}
function resizeOprate(id,index) {
    id="#"+id;
    $(id).datagrid('fixDetailRowHeight',index);
}
function loadSuccessOprate(id,index) {
    id="#"+id;
        $(id).datagrid('fixDetailRowHeight',index);
        $(id).datagrid('fixRowHeight',index);//防止出现滑动条
}
//此处是easyui的json格式
var tree = {
    id:'',
    text:'',
    url:'',
    state:'',
    // checked:'',
    attributes:'',
    children:''
}

/**
 * 根据数据菜单内容获取到tree格式json内容
 * @param data
 * @returns {Array}
 */
function getNavTreeJsonByResources(data) {
    var easyTree = new Array();
    $.each(data,function(index,item){
        if(item.type!=2){
            easyTree.push(blNav(item));
        }
    });
    return easyTree;
}

/**
 * 根据数据菜单内容获取到tree格式json内容
 * @param data
 * @returns {Array}
 */
function getTreeJsonByResources(data) {
    var easyTree = new Array();
    $.each(data,function(index,item){
        easyTree.push(bl(item));
    });
    return easyTree;
}

/**
 * datagrid 提交数据时判断content是否为空
 * @param id
 * @param content
 * @param showContent
 * @param index
 * @returns {boolean}
 */
function isNullDatagrid(id,content,showContent,index) {
    $("#"+id).datagrid('endEdit', index);
    var rows = $("#"+id).datagrid('getRows');
    var row = rows[index];
    if(row[content]==null||row[content]==""){
        showMessager("提示",showContent);
        $("#"+id).datagrid('beginEdit', index);
        return false;
    }else{
        return true;
    }
}

/**
 * 根据datagrid的index和id获取json字符串
 * @param id
 * @param index
 * @returns {*}
 */
function getDatagridJsonByIndexAndId(id,index) {
    $("#"+id).datagrid('endEdit', index);
    var rows = $("#"+id).datagrid('getRows');
    var row = rows[index];
    return row;
}

function getDatagridChanges(id) {
    var rows = $("#"+id).datagrid('getChanges');
    return rows;
}

function getDatagridSelections(id) {
    var rows = $("#"+id).datagrid('getSelections');
    return rows;
}
function getDatagridSelected(id) {
    var rows = $("#"+id).datagrid('getSelected');
    return rows;
}

function getDatagridChecked(id) {
    var rows = $("#"+id).datagrid('getChecked');
    return rows;
}

function getDatagridRows(id) {
    var rows = $("#"+id).datagrid('getRows');
    return rows;
}

function  clearDatagridSelections(id) {
    $("#"+id).datagrid('clearSelections');
}

/**
 * 获取某id的所有行内容
 * @param id
 * @returns {jQuery}
 */
function  getAllRowsContent(id) {
    var rows = $("#"+id).datagrid('getRows');
    return rows;
}

/**
 * 在最后新增行
 * @param id
 * @param json
 */
function  addRowInLast(id,json) {
    $("#"+id).datagrid('appendRow',json);
}

function detailFormatter(index, row) {//注意2
    myArray[index]=true;
    datagridDetailsArray[index]='ddv-'+index;
    return '<div style="padding:2px"><table id="ddv-' + index + '"></table></div>';
}

function detailFormatter2(index, row) {//注意2
    myArray2[index]=true;
    datagridDetailsArray2[index]='ddv2-'+index;
    return '<div style="padding:2px"><table id="ddv2-' + index + '"></table></div>';
}

function  initExpandRow(index,row,url,columns,parentDatagridId,isRightClick,editId,successBack) {
    if(row.id!=undefined){
        if(myArray[index]){
            myArray[index]=false;
            $('#ddv-' + index).datagrid({
                url:url,
                fitColumns: true,
                singleSelect: true,
                height: 'auto',
                columns:columns,
                onResize: function () {
                    $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
                },
                onLoadSuccess: function () {
                    setTimeout(function () {
                        $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
                    }, 0);
                    successBack(index);
                }
            });
            $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
            if(isRightClick){
            }
        }
    }
}
function  initExpandRow2(index,row,url,columns,parentDatagridId,isRightClick,editId,successBack) {
    if(row.id!=undefined){
        if(myArray2[index]){
            myArray2[index]=false;
            $('#ddv2-' + index).datagrid({
                url:url,
                fitColumns: true,
                singleSelect: true,
                height: 'auto',
                columns:columns,
                onResize: function () {
                    $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
                },
                onLoadSuccess: function () {
                    setTimeout(function () {
                        $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
                    }, 0);
                    successBack(index);
                }
            });
            $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
            if(isRightClick){
            }

        }
    }

}



function  initExpandRowContainState(index,row,url,columns,parentDatagridId,isRightClick,editId,successBack,DblClick) {
    if(myArray[index]){
        myArray[index]=false;
        $('#ddv-' + index).datagrid({
            url:url,
            fitColumns: true,
            singleSelect: true,
            height: 'auto',
            columns:columns,
            onResize: function () {
                $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
            },
            onLoadSuccess: function () {
                setTimeout(function () {
                    $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
                }, 0);
                successBack(index);
            },
            onDblClickRow:function (rowIndex, rowData) {
                DblClick(rowIndex,rowData);
        },
            rowStyler: function(index,row){
                if (row.state =='未完成'){
                    return 'background-color:#ff0030;color:#fff;';
                }else if (row.state =='异常'){
                    return 'background-color:#ffa800;color:#fff;';
                }else{
                    return 'background-color:#36ff00;color:#fff;';
                }
            }
        });
        $('#'+parentDatagridId).datagrid('fixDetailRowHeight', index);
        if(isRightClick){
            rightClickDatagrid('ddv-' + index,RowMeanu,HeaderMeanu,editId);
        }

    }
}

/**
 * 可输入内容dialog
 * @param title
 * @param content
 * @param back
 */
function showInputDialog(title,content,back) {
    $.messager.prompt(title, content, function(r){
        back(r);
    });
}


/**
 * 移除某id的index行
 * @param id
 * @param index
 */
function deleteOneRowById(id, index) {
    $("#"+id).datagrid('deleteRow', index);
}


function clearComboBoxSelect(id) {
    $("#"+id).combobox('clear');
}

function clearDataBox(id) {
    $("#"+id).combo('setText','');
}


/**
 * 刷新datagrid数据
 * @param id
 */
function reloadDatagridMessage(id) {
    $("#"+id).datagrid('reload');//刷新
}

function reloadTreegridMessage(id) {
    $("#"+id).treegrid('reload');//刷新
}

function reloadTreeMessage(id) {
    $("#"+id).tree('reload');//刷新
}

/**
 * radio获取选择值并放置到editor
 * @param row
 * @param id
 * @param index
 */
function  radioSelectSetEditor(row,id,index) {
    var val = getRadioValueByName(row);
    setContentToDatagridEditorText(id,index,row,val);
}

/**
 * 确定框
 */
function showconfirmDialog(title,content,back){
    $.messager.confirm(title,content, function(r){
        back(r);
    });
}

/**
 * 将datagrid的text格式的editor赋值
 * @param id
 * @param index
 * @param name
 * @param content
 */
function setContentToDatagridEditorText(id,index,name,content) {
    var ed = $("#"+id).datagrid('getEditor', {index:index,field:name});
    if(ed!=null&&ed.target!=null){
        $(ed.target).val(content);
    }
}


function  StrToFloat(content) {
    if(content==null||content==""){
        return 0;
    }else{
        return parseFloat(content);
    }
}

//指定列求和
function compute(id,colName) {
    var rows = $('#'+id).datagrid('getRows');
    var total = 0;
    for (var i = 0; i < rows.length; i++) {
        total += StrToFloat(rows[i][colName]);
    }
    total=Math.round(total*100)/100
    return total;
}

function  getDatagridEditorObj(id,index,name) {
    try
    {
        var ed = $("#"+id).datagrid('getEditor', {index:index,field:name});
        return ed.target;
    }
    catch(err)
    {
        return null;
    }

}

/**
 *
 * @param id
 * @param index 小角标
 * @param onChangeBack
 */
function setEditorOnChange(id,index,onChangeBack) {
    var editors =$("#"+id).datagrid('getEditors', editId);
    var priceEditor = editors[index];
    priceEditor.target.bind('change',function () {
        onChangeBack(index,priceEditor.target.val());
    });
}

function  getContentByEditor(id,index) {
    var editors =$("#"+id).datagrid('getEditors', editId);
    var priceEditor = editors[index];
    return priceEditor.target.val();
}

function  getComboboxContentByEditor(id,name) {
    var ed = $('#'+id).datagrid('getEditor', {index:editId,field:name});
    var value = $(ed.target).combobox('getValue');
    return value;
}

/**
 * 初始化data类型input 和onchange方法
 * @param row
 * @param id
 */
function  initDataInput(row,id) {
    $("#"+row).datebox({
        onChange: function(date){
            setContentToDatagridEditorText(id,editId,row,date);
        }
    });
}

function  initAddressSelectInput(id,selectArea) {
    var parentId=1;
    var initialAddressSelect=rootPath + '/area/areaSelect.shtml?parentId='+parentId;
    $('#province'+id).combobox({
        url:initialAddressSelect,
        valueField:'name',
        textField:'name',
        onSelect:function(area){
            $('#city'+id).combobox('clear');
            $('#area'+id).combobox('clear');
            $('#city'+id).combobox('reload',rootPath + '/area/areaSelect.shtml?parentId='+area.id),
            $('#area'+id).combobox('reload',rootPath + '/area/areaSelect.shtml?parentId='+parentId)}
    });
    $('#city'+id).combobox({
        url:initialAddressSelect,
        valueField:'name',
        textField:'name',
        onSelect:function(area){
            $('#area'+id).combobox('clear');
            $('#area'+id).combobox('reload',rootPath + '/area/areaSelect.shtml?parentId='+area.id)}
    });
    $('#area'+id).combobox({
        url:initialAddressSelect,
        valueField:'name',
        textField:'name',
        onSelect:function(area){
            selectArea(id,area)}
    });
}

function initInputMonthInput(id) {
    $("#"+id).datebox({
        onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
            span.trigger('click'); //触发click事件弹出月份层
            if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                tds = p.find('div.calendar-menu-month-inner td');
                tds.click(function (e) {
                    e.stopPropagation(); //禁止冒泡执行easyui给月份绑定的事件
                    var year = /\d{4}/.exec(span.html())[0]//得到年份
                        , month = parseInt($(this).attr('abbr'), 10); //月份，这里不需要+1
                    $("#"+id).datebox('hidePanel')//隐藏日期对象
                        .datebox('setValue', year + '-' + month); //设置日期的值
                });
            }, 0)
        },
        parser: function (s) {
            if (!s) return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        formatter: function (d) {
            var month=d.getMonth()+1;
            if(month<10){
                month="0"+month;
            }
            return d.getFullYear() + '-' + month;/*getMonth返回的是0开始的，忘记了。。已修正*/ }
    });
    var p =  $("#"+id).datebox('panel'), //日期选择对象
        tds = false, //日期选择对象中月份
        span = p.find('span.calendar-text'); //显示月份层的触发控件
}

function  initInputDataInput(id) {
    $("#"+id).datebox({
    });
}

function setDataToDateBox(id,day) {
    $("#"+id).datebox('setValue',day);
}

function  getDataboxValue(id) {
    try {
        var v =  $("#"+id).datebox('getValue');
        return v
    }catch (e){
        return "";
    }

}

/**
 * 判断是否有有编辑行
 * @returns {boolean}
 */
function  checkIsSelectEd(editId) {
    if(editId==null){
        showMessager('提示','请选择操作行!');
        return false;
    }else{
        return true;
    }
}

/**
 * 将json字符串内容放置在以json字符集key指为id的input
 * @param obj
 */
function  setContentToInputByJson( obj) {
    console.log(ListToJsonString(obj));
    var j=0;
    for(i in obj)
    {
        var className = $("#"+i).attr("class");
        console.log(i+":"+className);
        if(obj[i]!=null&&obj[i]!=""){
            if(className!=undefined){
                if(isContain(className,'datebox')&&!isContain(className,'inputShow')){
                    console.log(i);
                    $("#"+i).datebox('setValue', obj[i]);
                }else if(isContain(className,'radioSpan')){
                    setRadioCheckedByIdAndContent(i,obj[i]);
                    // var length=document.getElementsByName(i).length;
                    // for(j=0;j<length;j++){
                    //     var value=document.getElementsByName(i)[j].value;
                    //     if(value==obj[i]){
                    //         $("input[name='"+i+"']").eq(j).attr("checked","checked");
                    //         $("input[name='"+i+"']").eq(j).click();
                    //     }else{
                    //         $("input[name='"+i+"']").eq(j).removeAttr("checked");
                    //     }
                    // }
                }else{
                    setContentToInputById(i,obj[i]);
                }
            }
        }
        j=j+1;
    }
}

/**
 * 根据id和index开始编辑datagrid
 * @param id
 * @param index
 */
function beginEditDatagridByIndex(id,index) {
    $("#"+id).datagrid('beginEdit', index);
}

/**
 * 根据id和index结束编辑datagrid
 * @param id
 * @param index
 */
function endEditDatagridByIndex(id,index) {
    try{
        $("#"+id).datagrid('endEdit', index);  //异常的抛出
    }catch(e){
    system("异常:"+ListToJsonString(e));//异常的捕获与处理
    }finally{
       //结束处理
    }
}

function addUIToDatagrid(id,url,onClose) {
    $("#"+id).window({
        width:800,
        maximized:false,
        modal: true,
        height: 400,
        top:50,
        href: rootPath + url,
        onClose:onClose
    });
}

function  reloadDataGridByUrl(id,url) {
    $('#'+id).datagrid('options').url=rootPath + url;
    $('#'+id).datagrid('reload');
}

function initDataGridSingleSelect(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:true,
        pagination:true,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}
function initDataGridNotFit(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}

function initDataGrid(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        fit:true,
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        pagination:true,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}

function initDataGridPaginationFalse(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}

function initDataGridPaginationFalseToolbarFalse(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}

function initDataGridFitColumnsFalse(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:false,
        singleSelect:false,
        pagination:true,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}


function initDataGridFitColumnsFalseNoToolBar(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:false,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}

function initDataGridsingleSelectTrue(id,url,columns,onDblClickRow,onClickRow) {
    $('#'+id).datagrid({
        url:rootPath + url,
        title:'数据展示',
        iconCls:'icon-ok',
        toolbar: '#tb',
        idField:'id',
        //是否显示行号
        rownumbers:true,
        fitColumns:true,
        singleSelect:true,
        pagination:true,
        onDblClickRow:onDblClickRow,
        onClickRow:onClickRow,
        columns:columns,
        onLoadSuccess:function(data){
        },
    });
}

/**
 * 获取指定id改变的内容
 * @param id
 * @returns {jQuery}
 */
function getChangesContent(id) {
    try{
        return $("#"+id).datagrid('getChanges');  //异常的抛出
    }catch(e){
        system("异常:"+ListToJsonString(e));//异常的捕获与处理
        return [{}];
    }finally{
        //结束处理
    }

}


/**
 * 关闭所有编辑状态的行
 * @param id
 */
function endEditDatagrid(id) {
    try{
        var eaRows = $("#"+id).datagrid('getRows');
        for(var ii=0;ii<eaRows.length;ii++){
            $("#"+id).datagrid('endEdit',ii);
        }
    }catch(e){
        system("异常:"+ListToJsonString(e));//异常的捕获与处理
    }finally{
        //结束处理
    }

}

/**
 * 取消所有选中行并选中某行
 * @param id
 */
function clearSelectAndSelectRowDatagrid(id,rowIndex) {
    $("#"+id).datagrid("clearSelections"); //取消所有选中项
    $("#"+id).datagrid("selectRow", rowIndex); //根据索引选中该行
}

function rightClickTreegrid(id,RowMeanu,HeaderMeanu) {
    $("#"+id).treegrid({
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            if(editId!=null){
                endEditDatagridByIndex(id,editId)
            }
            RowMeanu(rowIndex,e.pageX,e.pageY);
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        }
    })
}

function rightClickDatagrid(id,RowMeanu,HeaderMeanu) {
    $("#"+id).datagrid({
        onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            if(editId!=null){
                endEditDatagridByIndex(id,editId)
            }
            RowMeanu(rowIndex,e.pageX,e.pageY,id);
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        },
        onHeaderContextMenu: function (e, field){ //右键时触发事件                      
            e.preventDefault(); //阻止浏览器捕获右键事件
            if(editId!=null){
                endEditDatagridByIndex(id,editId)
            }
            HeaderMeanu(e.pageX,e.pageY,id);
            e.preventDefault();  //阻止浏览器自带的右键菜单弹出
        }
    })
}

/**
 * 显示memu
 * @param id
 * @param e
 */
function showMenu(id,x,y) {
    $("#"+id).menu('show', {
        left:x,//在鼠标点击处显示菜单
        top:y
    });
}

function DatagridRow(field,title,width,align){
    this.field=field;
    this.title=title;
    this.width=width;
    this.align=align;
}

/**
 * 初始化datagrid中editor=combogrid类型
 */
function initComboGridEditor(){
    //放到easyui 对应的扩展datagrid 的js中
    $.extend($.fn.datagrid.defaults.editors, {
        combogrid: {
            init: function (container, options) {
                var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
                input.combogrid(options);
                return input;
            },
            destroy: function (target) {
                $(target).combogrid('destroy');
            },
            getValue: function (target) {
                return $(target).combogrid('getValue');
            },
            setValue: function (target, value) {
                $(target).combogrid('setValue', value);
            },
            resize: function (target, width) {
                $(target).combogrid('resize', width);
            }
        }
    });
}

/**
 * 显示提示框 固定时间消失
 * @param title
 * @param msg
 */
function showMessager(title,msg) {
    $.messager.show({
        title:title,
        msg:msg,
        timeout:5000,
        showType:'slide'
    });
}

function showMessagerCenter(title,msg){
    $.messager.show({
        title:title,
        msg:msg,
        showType:'fade',
        style:{
            right:'',
            bottom:''
        }
    });
}

function  showErrorAlert(title,msg) {
    $.messager.alert(title,msg,'error');
}


//此处是把后台传过来的json数据转成easyui规定的格式
function blNav(item){
    var tree = new Object();
    // tree.id = item.resUrl+"?id="+item.id;
    tree.text = item.name;
    tree.url=item.resUrl;
    // tree.checked = 'false';
    if(item.children.length != 0){
        if(item.type==0){
            tree.id = "no:"+item.resUrl+"?id="+item.id;
            tree.state = 'closed';
        }else{
            tree.state = 'open';
            tree.id = "yes:"+item.resUrl+"?id="+item.id;
        }

        tree.children = getNavTreeJsonByResources(item.children);
    }else{
        tree.children = new Array();
        tree.state = 'open';
        tree.id = "yes:"+item.resUrl+"?id="+item.id;
    }
    return tree;
}

//此处是把后台传过来的json数据转成easyui规定的格式
function bl(item){
    var tree = new Object();
    tree.id = item.id;
    tree.text = item.name;
    tree.state = 'open';
    // tree.checked = 'false';
    if(item.children.length != 0){
        tree.children = getTreeJsonByResources(item.children);
    }else{
        tree.children = new Array();
    }
    return tree;
}

/*
 *  datagrid 获取正在编辑状态的行，使用如下：
 *  $('#id').datagrid('getEditingRowIndexs'); //获取当前datagrid中在编辑状态的行id列表
 */
$.extend($.fn.datagrid.methods, {
    getEditingRowIndexs: function(jq) {
        var rows = $.data(jq[0], "datagrid").panel.find('.datagrid-row-editing');
        var indexs = [];
        rows.each(function(i, row) {
            var id=$(this).attr("node-id");
                indexs.push(id);
        });
        return indexs;
    }
});

//扩展datagrid:动态加入删除editor
$.extend($.fn.datagrid.methods, {
    addEditor : function(jq, param) {
        if (param instanceof Array) {
            $.each(param, function(index, item) {
                var e = $(jq).datagrid('getColumnOption', item.field);
                e.editor = item.editor; });
        } else {
            var e = $(jq).datagrid('getColumnOption', param.field);
            e.editor = param.editor;
        }
    },
    removeEditor : function(jq, param) {
        if (param instanceof Array) {
            $.each(param, function(index, item) {
                var e = $(jq).datagrid('getColumnOption', item);
                e.editor = {};
            });
        } else {
            var e = $(jq).datagrid('getColumnOption', param);
            e.editor = {};
        }
    }
});
/**
 * 将选中的row变成可编辑状态
 * @param tableId
 */
function beginEditByTableIdSelects(tableId) {
    id="#"+tableId;
    var rows =$(id).treegrid('getSelections');
    for(i=0;i<rows.length;i++){
        $(id).treegrid('beginEdit', rows[i].id);
    }
}

/**
 * 将选中的row取消可编辑状态和取消选中状态
 * @param tableId
 */
function endEditAndcancelSelectByTableIdSelects(tableId) {
    id="#"+tableId;
    var rows =$(id).treegrid('getSelections');
    for(i=0;i<rows.length;i++){
        $(id).treegrid('cancelEdit', rows[i].id);
        $(id).treegrid('unselect', rows[i].id);
    }
    var rows =  $(id).datagrid('getEditingRowIndexs');
    for(i=0;i<rows.length;i++){
        var row =  $(id).treegrid("find",rows[i]);
        $(id).treegrid('endEdit',row.id);
        $(id).treegrid('remove',row.id);
    }
}

/**
 * 向同级元素添加元素
 * @param tableId 表格id
 * @param isEdit 添加元素是否编辑
 * @param param 将要添加的元素变量
 */
function addSamelLevelElementTreegrid(tableId,isEdit,param) {
    id="#"+tableId;
    $(id).treegrid('insert',param);
    if(isEdit){
        $(id).treegrid('beginEdit', param.data.id);
    }
}
function TitleEntity(colkey,name) {
    this.colkey=colkey;
    this.name=name;
}
function getDonwExcelTitleData(id) {
    id="#"+id;
    var datagridTitle = new Array();
    var fields = $(id).datagrid('getColumnFields');
    for (var i = 0; i < fields.length; i++) {
        var option = $(id).datagrid('getColumnOption', fields[i]);
        if (option.field != "checkItem" && option.hidden != true) { //过滤勾选框和隐藏列
            datagridTitle.push(new TitleEntity(fields[i],option.title));
        }
    }
    return datagridTitle;
}
