### 上传文件 /uploads

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
1|[/uploads/](#add_uploads)			|POST|生成上传附件
2|[/uploads/](#list_uploads)		|GET |获取附件列表
3|[/uploads/:id](#update_uploads)	|PUT |修改附件信息
4|[/uploads/:id](#uploads_info)		|GET |获取附件信息

#### 1. <label id="add_uploads">上传附件</label>

|方法名称|/uploads|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|:--------|
|type			|N	|文件分类，0附件，1图片，2语音|
|login_name		|N	|上传者的登录名|
|upload_time	|	|上传时间，默认为当前时间|
|path			|N	|文件的绝对物理路径，不包括文件名|
|name			|	|文件全名|
|url			|N	|相对url，包括文件名(重命名后的文件名)|
|size			|N	|文件大小，单位为字节|
|memo			|	|备注说明

说明:

	name:
		上传文件的原文件名	
		
	上传文件的文件名与存放位置:
		文件上传后，修改文件名，
		以当前时间的毫秒数作为文件名，扩展名不变(假设为 1425953988802.jpg)
		
		从配置文件中读取上传文件的物理对绝目录(假设为 /opt/uploads)，
		根据当前日期创建文件夹(如果文件夹不存在的话)，文件夹名如`20150302`。
		将上传的文件1425953988802.jpg 存在此目录/opt/uploads/20150302 下。
		
		将 /opt/uploads/20150302 作为path		
	
	url:		
		从配置文件中读取上传文件访问的url相对路径(假设为 /uploads)
		将 /uploads/20150302/1425953988802.jpg 作为url
	
返回数据:
>
	{
		"result":1,
		"data":{
			"type":1,
			"url":"/uploads/20150302/1425953988802.jpg",
			"size":1024
		}
	}

#### 2. <label id="list_uploads">获取附件列表</label>

|方法名称|/uploads|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|:--------|
|keyword			|	|从name,memo中like查找|
|type				|	|文件分类，0附件，1图片，2语音|
|login_name			|	|上传者的登录名|
|upload_time_from	|	|上传时间|
|upload_time_to		|	|上传时间|
|name				|	|文件名 like|
|size_from			|	|文件大小|
|size_to			|	|文件大小|

	
返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":[
			{
				"id":123,
				"type":1,
				"login_name":"13812345678",
				"upload_time":"2015-03-02 12:30:00",
				"size":1024,
				"name":"图片.jpg",
				"url":"/uploads/20150302/1425953988802.jpg"
			},
			{
				...
			},
			...
		]
	}
	
#### 3. <label id="update_uploads">修改附件信息</label>

|方法名称|/uploads/:id|
|:----|:--------|
|请求方式|PUT|

|参数名称|可空|说明|
|:--|:--------|:--------|
|name			|	|文件名|
|memo			|	|文件大小|

	
返回数据:
>
	{
		"result":1
	}
	
#### 4. <label id="uploads_info">获取附件信息</label>

|方法名称|/uploads/:id|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|:--------|
|name			|	|文件名|
|memo			|	|文件大小|

返回数据:
>
	{
		"result":1,
		"data":{
				"id":123,
				"type":1,
				"login_name":"13812345678",
				"upload_time":"2015-03-02 12:30:00",
				"name":"图片.jpg",
				"size":1024,
				"url":"/uploads/20150302/1425953988802.jpg",
				"memo":""
			}
	}
