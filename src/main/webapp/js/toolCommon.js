Date.prototype.toLocaleString = function() {
	return this.getFullYear() + "-" + (this.getMonth() + 1) + "-" + this.getDate() + " " + this.getHours() + ":" + this.getMinutes() + ":" + this.getSeconds();
};

/**
 * 根据id获取内容
 * @param {Object} id
 */
function getValueById(id){
    return document.getElementById(id).value;
}
function getContentsByCombobox(id) {
    return $("#"+id).combobox("getValues");
}
function  setTextareaValue(id,text) {
    $("#"+id).text(text);
}

/**
 * 小数转化为百分数
 * @param point
 * @returns {string}
 */
function toPercent(point){
    var str=Number(point*100).toFixed(1);
    str+="%";
    return str;
}

/**
 * 保留两位小数 四舍五入
 * @param num
 * @returns {string|*}
 */
function floatToVar2(num) {
    try{
        num = num.toFixed(2);  // 输出结果为 2.45
        return num;
    }catch (e){
        return num;
    }

}

/**
 * 保留四位小数 四舍五入
 * @param num
 * @returns {string|*}
 */
function floatToVar4(num) {
    num = num.toFixed(4);  // 输出结果为 2.45
    return num;
}

//去掉汉字  
function RemoveChinese(strValue) {
    if(strValue!= null && strValue != ""){
        var reg = /[\u4e00-\u9fa5]/g;
        return strValue.replace(reg, "");
    }
    else
        return "";
}

function getBatchNumber(content,number) {
    var batchNumber="";
    var code=RemoveChinese(content);
    code=code.replace("-","");
    number=number+1;
    if(number<10){
        batchNumber=code+"00"+number;
    }else if(number>9&&number<100){
        batchNumber=code+"0"+number;
    }else {
        batchNumber=code+""+number;
    }
    return batchNumber;
}

/**
 * 根据id获取select内容
 * @param id
 * @returns {jQuery}
 */
function getContentBySelect(id) {
    try {
        return $("#"+id).combobox("getValue");
    }catch (e){
        return "";
    }
}

function  getContentBycombogrid(id) {
    try {
        return $("#"+id).combogrid('getValue');
    }catch (e){
        return "";
    }

}

function  setContentToCombogrid(id,content) {
    $("#"+id).combogrid("setValue",content);
}

function  setContentToCombobox(id,content) {
    $("#"+id).combobox("setValue",content);
}

function  setContentsToCombobox(id,content) {
    $("#"+id).combobox("setValues",content.split(','));
}
/**
 *
 * 判断checkbox是否选中
 * @param id
 * @returns {*|jQuery}
 */
function checkBoxIsChecked(id) {
    return $("#"+id).is(':checked')
}

/**
 * 清空input内容
 */
function clearInput() {
    var $all = document.all;
    for(var i=0;i<$all.length;i++)
    {
        if($all[i].tagName=="INPUT")
            $all[i].value="";
    }
}

/**
 * 根据id向input赋值
 * @param {Object} id
 * @param {Object} content
 */
function setContentToInputById(id,content){
    if(getObjectById(id)!=null){
        getObjectById(id).value=content;
    }
}

function setContentToDivSpanById(id,content){
    if(getObjectById(id)!=null){
        getObjectById(id).innerText=content;
    }
}

/**
 * 根据name获取radio选中值
 * @param name
 * @returns {*|jQuery}
 */
function getRadioValueByName(name) {
    var val = $('input[name='+name+']:checked').val();
    return val;

}

/**
 * 根据id或content设置radio选值
 * @param id
 * @param content
 */
function setRadioCheckedByIdAndContent(id,content) {
    var length=document.getElementsByName(id).length;
    for(j=0;j<length;j++){
        var value=document.getElementsByName(id)[j].value;
        if(value==content){
            $("input[name='"+id+"']").eq(j).attr("checked","checked");
            $("input[name='"+id+"']").eq(j).click();
        }else{
            $("input[name='"+id+"']").eq(j).removeAttr("checked");
        }
    }
}

function setCheckBoxByIdAndContent(id,content) {
    if(content=="1"||content=="是"||content=="true"||content=="TRUE"){
        content=true;
    }else{
        content=false;
    }
    $("#"+id).attr("checked", content);
}
/*
 * 根据id获取对象
 */
function getObjectById(id){
    return document.getElementById(id);
}
function  setContentsToCombogrid(id,content) {
    $("#"+id).combogrid("setValues",content);
}
function changeToStringTime(time){
	return new Date(time).toLocaleString();
}

function  getContentsBycombogrid(id) {
    return $("#"+id).combogrid('getValues')
}

/**
 * 当list为空时 向id元素内加载noorder界面，清空指定
 * @param {Object} id
 * @param {Object} vue
 */
function loadNoMessageHtml(id,vue){
	ID="#"+id;
	$(ID).load("noorder.html");
    vue=undefined;
}

/**
								 * 向同级元素添加element
								 */
								function addElementToSameLevel(objAddEd,element){
									var div=objAddEd;
										var ndiv=element;
											if(div.nextSibling){
												div.parentNode.insertBefore(ndiv,div.nextSibling);
											}else{
												div.parentNode.appendChild(ndiv);
											}
								}


/**
 * 防止input获取焦点时内容上窜
 */
function controlInputKeyBoardCss(){
							//获取原始窗口的高度
var originalHeight=document.documentElement.clientHeight || document.body.clientHeight;

window.onresize=function(){

    //软键盘弹起与隐藏  都会引起窗口的高度发生变化
    var  resizeHeight=document.documentElement.clientHeight || document.body.clientHeight;

    if(resizeHeight*1<originalHeight*1){ //resizeHeight<originalHeight证明窗口被挤压了

            plus.webview.currentWebview().setStyle({
                height:originalHeight
            });

      }
}
}


/**
 * 获取当前时间
 */
function getNowTime(){
	 var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}

/**
 * 获取当前日期
 */
function getNowDate(){
	 var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}

/**
 * 获取当前月份
 */
function getNowMonth(){
	 var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    var currentdate = date.getFullYear() + seperator1 + month ;
    return currentdate;
}

/**
 * 获取当前时间无符号
 */
function getNowTimeNoCode(){
	 var date = new Date();
    var seperator1 = "";
    var seperator2 = "";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}

//读取文件
        function readParseFiles(file,callback) {
            var data = [];
            var files = [];
//          files.push(document.getElementById('file0').files[0], document.getElementById('file1').files[0],document.getElementById('file2').files[0],document.getElementById('file3').files[0]);
            var index = 0;
            ~function () {
                var reader = new FileReader();
//              reader.readAsText(files[index],'UTF-8');
 				reader.readAsText(file,'UTF-8');
                index++;
                var context = arguments.callee;
                //读取完文件之后，执行下面这个回调函数：
                reader.onloadend = function (evt) {
                	alert(evt.target.readyState);
                    if(evt.target.readyState == FileReader.DONE){
                        data.push(csv2array(reader.result.toString()));
                        if(index == files.length){
                            callback && callback(data);
                        }else {
                            context();
                        }
                    }
                };
            }();
        };

function downfileByUrl(url) {
    try {
        var elemIF = document.createElement("iframe");
        elemIF.src =url;
        elemIF.style.display = "none";
        document.body.appendChild(elemIF);
    } catch (e) {
        alert("下载异常！");
    }
}

/**
 * 拨打电话
 */
function call(){
				plus.device.dial("13400000000");
			}
/**
 * 判断内容是否为空 为空返回true
 * @param {Object} cotnent
 */
function isNull(content){
	if(content==undefined||content==null||content==''||content=='undefined'){
		return true;
	}else{
		return false;
	}
}

/**
 * 在界面上打印内容
 * @param {Object} content
 */
function system(content){
	// if(isSystem){
		console.log(content);
	// }
}

/**
 * 将var内容传换成float
 * @param {Object} content
 */
function VarToFloat(content){
    content=content+"";
    if(isNull(content)){
        content=0;
    }else{
        content=content.replace(/,/g, "");//取消字符串中出现的所有逗号
    }
    return parseFloat(content);
}

/**
 * list对象传变成json字符串
 * @param {Object} list
 */
function ListToJsonString(list){
	var str = JSON.stringify(list);
	return str;
}

/**
 * 将var内容传化成int
 * @param {Object} content
 */
function VarToInt(content){
	if(isNull(content)){
		content=0;
	}
	return parseInt(content);
}
		/**
		 * 将图片压缩到指定大小
		 * 将图片压缩转成base64 
		 * @param {Object} img
		 */
		function getBase64Image(img) {
			var canvas = document.createElement("canvas");
			var width = img.width;
			var height = img.height;
			// calculate the width and height, constraining the proportions 
			if(width > height) {
				if(width > 500) {
					height = Math.round(height *= 500 / width);
					width = 500;
				}
			} else {
				if(height > 500) {
					width = Math.round(width *= 500 / height);
					height = 500;
				}
			}
			canvas.width = width; /*设置新的图片的宽度*/
			canvas.height = height; /*设置新的图片的长度*/
			var ctx = canvas.getContext("2d");
			ctx.drawImage(img, 0, 0, width, height); /*绘图*/
			var dataURL = canvas.toDataURL("image/png", 0.8);
			return dataURL.replace("data:image/png;base64,", "");
		}
			
function isJSON(str) {
    if (typeof str == 'string') {
        try {
            var obj=JSON.parse(str);
            if(typeof obj == 'object' && obj ){
                return true;
            }else{
                return false;
            }

        } catch(e) {
            console.log('error：'+str+'!!!'+e);
            return false;
        }
    }
    console.log('It is not a string!')
}