function submitForm(){
    $('#ff').form({
            url:rootPath + '/good/addEntity.shtml',
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