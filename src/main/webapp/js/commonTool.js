/**
 * Created by Administrator on 2018/11/13.
 */
/**
 * ajax访问后台接口
 * @param {Object} content 访问接口名称及变量
 * @param {Object} name 当前操作名称 区分操作成功后回调函数的处理内容
 */
function communatePost(url,data,callback){
    $.ajax({
        url: url,
        type: "post",
        async: true,
        dataType: 'json',
        data: {'entity':data},
        success: function (data) {
            callback(data);
            //     alert(data.message);
            // $('#dg').datagrid('reload');
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            alert("error:"+XMLHttpRequest.responseText);
        }
    });
}

/**
 * 判断str是否包含content 包含返回true 不包含返回false
 * @param {Object} str
 * @param {Object} content
 */
function isContain(str,content){
    try {
        if(str==undefined||str==''){
            return false;
        }else if(str.indexOf(content)==-1){
            return false;
        }else{
            return true;
        }
    }catch (e){
        return false;
    }

}

function submitFormNow(id,url,back) {
    $("#"+id).form({
        url:rootPath +url,
        onSubmit: function(param){
            var state=$(this).form('enableValidation').form('validate');
            if(state){
                $.messager.progress({
                    title:'Please waiting',
                    msg:'Loading data...'
                });
                return state;
            }else{
                return state;
            }
        },
        success:function(data){
            $.messager.progress('close');
            back(data);

        }
    });
    $("#"+id).submit();
}


/**
 * 提交form无其他变量
 * @param id
 * @param url
 * @param back
 */
function submitForm(id,url,back) {
    document.getElementById(id).action=rootPath +url;
    $("#"+id).submit();
}

function initForm(id,back) {
    $("#"+id).form({
        onSubmit: function(param){
            var state=$(this).form('enableValidation').form('validate');
            if(state){
                $.messager.progress({
                    title:'Please waiting',
                    msg:'Loading data...'
                });
                return state;
            }else{
                return state;
            }
        },
        success:function(data){
            back(document.getElementById(id).action,data);
            $.messager.progress('close');
        }
    });
}



function communateGet(url,callback){
    $.messager.progress({
        title:'Please waiting',
        msg:'Loading data...'
    });
    $.ajax({
        url: url,
        async: true,
        dataType: 'json',
        success: function (data) {
            $.messager.progress('close');
            callback(data);
            //     alert(data.message);
            // $('#dg').datagrid('reload');
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            $.messager.progress('close');
            alert("error:"+XMLHttpRequest.responseText);
           // showErrorAlert("警告","登录过期，请重新登录");
        }
    });
}

function newGuid()
{
    var guid = "";
    for (var i = 1; i <= 32; i++){
        var n = Math.floor(Math.random()*16.0).toString(16);
        guid +=   n;
    }
    return guid;
}

function ListToJsonString(list){
    var str = JSON.stringify(list);
    return str;
}


Array.prototype.remove = function (val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }}
