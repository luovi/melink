### 车辆位置 /locations

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
1|[/locations](#add_location)		|POST 	|添加车辆位置
2|[/locations](#list_location)		|GET 	|获取车辆位置列表 


### 1 <label id="add_location">添加定位记录</label>

|方法名称|/locations|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|:--------|
|login_name		|N	|登录名|
|type			|N	|定位类型，1基站定位，2手机app定位|
|lng			|N	|位置经度|
|lat			|N	|位置纬度|
|device_id		|	|设备号,基站定位无此值,app定位时，用以区别同用户不同手机的定位记录|
|location		|	|具体位置，冗余用|
|locate_time	|	|定位时间，默认当前时间，指定时间可以事后添加定位记录|


返回数据:
>
	{
		"result":1
	}
	
### 2 <label id="list_location">定位记录列表</label>

|方法名称|/locations|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|:--------|
|login_name		|	|登录名|
|type			|	|定位类型，1基站定位，2手机app定位|
|device_id		|	|手机设备号，app定位时，用以区别同一用户不同手机的定位记录|
|locate_time_from|	|定位时间范围|
|locate_time_to|	|定位时间范围|

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
			"type":2,
			"decice_id":"xxxxxxxx",
			"locate_time":"2015-03-02 12:30:00",
			"location":"",
			"lng":123.123456
			"lat":23.543321
		}
	}