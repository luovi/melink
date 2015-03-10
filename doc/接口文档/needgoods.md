### 司机要货 /needgoods

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
1|[/needgoods](#add_needgoods)		|POST 	|添加要货记录
2|[/needgoods](#list_needgoods)		|GET 	|获取要货记录


### 1 <label id="add_needgoods">添加要货记录</label>

|方法名称|/needgoods|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:----|:--------|:--------|
|login_name		|N	|登录名|
|origin			|	|出发地|
|destin			|	|途经地|
|publisth_time	|	|发布时间，默认当前时间|
|begin_time		|	|要货时段的开始时间，默认当前时间|
|end_time		|	|要货时段的结束时间，默认当前时间的24小时后|
|status			|	|1要货，999已给货|
|memo			|	|备注信息|


返回数据:
>
	{
		"result":1
	}
	
### 2 <label id="list_needgoods">获取要货记录</label>

|方法名称|/needgoods|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:----|:--------|:--------|
|keyword		|	|从origin,destin,memo中模糊查找|
|login_name		|	|登录名|
|origin			|	|出发地，like|
|destin			|	|途经地,like|
|end_time_from	|	|结束时间|
|end_time_to	|	|默认end_time_to为当前时间,即未结束|
|status			|	|1要货，999已给货|
|memo			|	|备注信息|

说明：
	
	返回的数据以时间降序排列

返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":{
			"login_name":"13812345678",
			"origin":"杭州",
			"destin":"上海",
			"publish_time":"2015-03-02 12:30:00",
			"begin_time":"2015-03-02 12:30:00",
			"end_time":"2015-03-03 12:30:00",
			"status":1,
			"memo":"其它信息"
		}
	}