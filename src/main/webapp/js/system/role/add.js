var listbutton;
$(function() {
    var url = rootPath + '/resources/reslists.shtml';
    var data = CommnUtil.ajax(url, null,"json");
    if (data != null) {
        var h = "<option value='0'>------顶级目录------</option>";
        for ( var i = 0; i < data.length; i++) {
            h+="<option value='" + data[i].id + "'>"+ data[i].name + "</option>";
        }
        $("#parentId").html(h);
    } else {
        layer.msg("获取菜单信息错误，请联系管理员！");
    }
});
function but(v){
    if(v.value==2){
        showBut();
    }else{
        $("#divbut").css("display","none");
    }
}
function toBut(b){
    var thisObj=$(b);//js对象转jquery对象
    $("#description").val(b.id);
}
function showBut(){
    document.getElementById("divbut").setAttribute("style","");
}

function submitForm(){
    $('#ff').form({
        url:rootPath + '/resources/addEntity.shtml',
        onSubmit: function(){
            return $(this).form('validate');
        },
        success:function(data){
            var jsonObj = eval( '(' + data + ')' ); // eval();方法
            if(jsonObj=="success"){
                $('#add').window('close');
                window.parent.reload();
            }else{
                alert("系统异常，请联系管理员");
            }
        }
    });
// submit the form
    $('#ff').submit();
}
function clearForm(){
    $('#ff').form('clear');
}