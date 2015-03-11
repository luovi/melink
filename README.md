### 货单 /cargos

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
  1|[/cargos](#add_cargo)			|POST 	|发布货单
  2|[/cargos](#cargo_list)			|GET 	|获取货单列表
  3|[/cargos/:id](#view_cargo)		|GET 	|获取货单详情
  4|[/cargos/:id](#update_cargo)	|PUT 	|修改货单
  5|[/cargos/:id](#del_cargo)		|DELETE	|删除货单
  6|[/cargos/logs](#add_cargolog)	|POST 	|新增车货匹配记录
  7|[/cargos/logs](#cargolog_list)	|GET 	|匹配记录列表
  8|[/cargos/logs/:id/confirm](#cargolog_cfm)|POST |确认 

#### 1. <label id="add_cargo">发布货单</label>

|方法名称|/cargos|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:-------------|:----|:---------------|
|login_name|N|发货用户的登录名|
|title|N|货货名称,长度不超过200个字符|
|type|N|货单来源，0本系统，1联联看，10以上对外接口，默认0|
|origin_city||启运地所在城市(地级市)|
|origin_addr||启运地详细地址|
|origin_lng||启运地经度(从百度地图API中获取)|
|origin_lat||启运地纬度(从百度地图API中获取)|
|destin_city||抵达地所在城市(地级市)|
|destin_addr||抵达地详细地址|
|destin_lng||抵达地经度(从百度地图API中获取)|
|destin_lat||抵达地纬度(从百度地图API中获取)|
|load_time||装货时间|
|ref_distance||参考货运距离(根据百度地图导航数据,获取默认线路的总距离)|
|ref_carriage||参考运费,根据距离与车型计算,具体计算方式另外给出|
|carriage||运费(即货方运费报价)|
|weight||重量|
|volume||体积|
|length||货长|
|car_length||所需车长|
|car_model||所需车型,平板,高栏等|
|sender_name||发货方联系人姓名|
|sender_phone||发货方联系电话|
|receiver_name||收货方联系人姓名|
|receiver_phone||收货方联系电话|
|status||货单状态,0已取消,10未成交(默认),20待成交(车找货),30待成交(货找车),999已成交|
|voice||语音文件路径与文件名|
|img||图片文件路径与文件名|
|attch||附件路径与文件名,只允许一个附件,多个附件建议压缩成一个包,留作以后扩展用|
|memo||所需车型,平板,高栏等|

说明:

	附件,图片,语音等,前端实现时,先调用上传的接口,上传成功返回相应的相对url

返回数据:
>
	{
		"result":1
	}
	
#### 2. <label id="cargo_list">货单列表</label>

|方法名称|/cargos|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|:--------|
|type||货单来源|
|login_name||发货用户的登录名|
|origin_city||启运地所在城市(地级市)|
|destin_city||抵达地所在城市(地级市)|
|publish_time_from||发布时间,查询参数 publish_time>={publish_time_from}|
|publish_time_to||发布时间,查询参数 publish_time<={publish_time_to}|
|weight_from||重量,查询参数|
|weight_to||重量,查询参数|
|volume_from||体积,查询参数|
|volume_to||体积,查询参数|
|length_from||长度,查询参数|
|legnth_to||长度,查询参数|
|car_length_from||车长,查询参数|
|car_legnth_to||车长,查询参数|
|car_model||所需车型|
|status||货单状态|
|keyword||关键字,从title,origin_addr,destin_addr,sender_name,receiver_name,memo中模糊查找|
|lng||指定位置的经度|
|lat||指定位置的纬度,与经度一起确定指定位置(一般为手机当前定位位置),计算启运地与当前位置的距离|
|orderby||排序字段,time(发布时间),distance(须传入(lng,lat),指定位置与货启运地的距离,ref_distance(参考运输距离),参数前加负号代码降序,无负号为升序,默认为-time,即发布时间降序|


说明:

	附件,图片,语音等,前端实现时,先调用上传的接口,上传成功返回相应的文件名
	排序方式为distance时,必须传入lng与lat,以计算指定位置与启运地的距离,如果启运地的位置由(origin_lng,origin_lat)确定,如果为空或者值错误,该信息将不会出现在结果中.
	为提高查询效率,建议前端指定默认的publish_time_from

返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":[
			{
				"type":0,
				"login_name":"13812345678",
				"title":"duang~duang~duang~",
				"origin_city":"杭州",
				"origin_addr":"石大物流园",
				"destin_city":"杭州"
				"destin_addr":"传化物流园",
				"load_time":"2015-03-02 12:00:00",
				"weight":5,
				"volume":"",
				"length":"",
				"car_length":4.6,
				"car_model":"CM02",
				"ref_distance":5.1,//参考货运距离
				"distance":10.3,//参考距离,只有传入指定的lng与lat,才会有值
				"ref_carriage":200,//参考运费
				"carriage":250,//运费报价
				"sender_name":"张三",
				"sender_phone":"13812345678",
				"receiver_name":"李四",
				"receiver_phone":"13087654321",
				"status":10,
				"voice":"voice/20150302/123.voc",
				"img":"img/20150302/123.jpg",
				"attch":"attch/20150302/123.zip"
			}
			...
			
		]
	}
	
#### 3. <label id="view_cargo">货单详情</label>

|方法名称|/cargos/:id|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|:--------|
|lng||指定位置的经度|
|lat||指定位置的纬度,与经度一起确定指定位置,计算启运地与当前位置的直线距离|

返回数据:
>
	{
		"result":1
		"data":{
				"type":0,
				"login_name":"13812345678",
				"title":"duang~duang~duang~",
				"origin_city":"杭州",
				"origin_addr":"石大物流园",
				"origin_lng":130.123456,
				"origin_lat":36.123456,
				"destin_city":"杭州"
				"destin_addr":"传化物流园",
				"destin_lng":130.123456,
				"destin_lat":36.123456,
				"load_time":"2015-03-02 12:00:00",
				"weight":5,
				"volume":"",
				"length":"",
				"car_length":4.6,
				"car_model":"CM02",
				"ref_distance":5.1,//参考货运距离
				"distance":10.3,//参考距离,只有传入指定的lng与lat,才会有值
				"ref_carriage":200,
				"carriage":250,
				"sender_name":"张三",
				"sender_phone":"13812345678",
				"receiver_name":"李四",
				"receiver_phone":"13087654321",
				"status":10,
				"voice":"voice/20150302.voc",
				"img":"img/20150302/123.amr",
				"attch":"attch/20150302/123.zip",
				"memo":"我是备注信息,乱七八糟的都可以写在这里"
			}
	}

#### 4. <label id="update_cargo">修改货单</label>

|方法名称|/cargos/:id|
|:----|:--------|
|请求方式|PUT|

|参数名称|可空|说明|
|:--|:--------|:--------|
|title||货物名称,长度不超过200个字符|
|origin_city||启运地所在城市(地级市)|
|origin_addr||启运地详细地址|
|origin_lng||启运地经度(从百度地图API中获取)|
|origin_lat||启运地纬度(从百度地图API中获取)|
|destin_city||抵达地所在城市(地级市)|
|destin_addr||抵达地详细地址|
|destin_lng||抵达地经度(从百度地图API中获取)|
|destin_lat||抵达地纬度(从百度地图API中获取)|
|load_time||装货时间|
|ref_distance||参考货运距离(根据百度地图导航数据,获取默认线路的总距离)|
|ref_carriage||参考运费,根据距离与车型计算,具体计算方式另外给出|
|carriage||运费(即货方运费报价)|
|weight||重量|
|volume||体积|
|length||货长|
|car_length||所需车长|
|car_model||所需车型,平板,高栏等|
|sender_name||发货方联系人姓名|
|sender_phone||发货方联系电话|
|receiver_name||收货方联系人姓名|
|receiver_phone||收货方联系电话|
|voice||语音文件路径与文件名|
|img||图片文件路径与文件名|
|attch||附件路径与文件名,只允许一个附件,多个附件建议压缩成一个包,留作以后扩展用|
|memo||所需车型,平板,高栏等|


说明:

	修改 cargos 中相应的字段
	修改前要判断权限,
		1.只有未成交的货单可以被修改
			cargos.status==10
			
		2. 只有管理员与发布人能修改,
			token.login_name==cargos.login_name or token.role==999
	

返回数据:
>
	{
		"result":1
	}
	
#### 5. <label id="del_cargo">删除货单</label>

|方法名称|/cargos/:id|
|:----|:--------|
|请求方式|DELETE|

说明:

	删除前要判断权限,
		1.只有未成交的货单可以被删除
			cargos.status==10
			
		2. 只有管理员与发布人能删除,
			token.login_name==cargos.login_name or token.role==999
	

返回数据:
>
	{
		"result":1
	}
	
#### 6. <label id="add_cargolog">新增车货匹配记录</label>

|方法名称|/cargos/logs|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|:--------|
|carog_id		|N	|货单id|
|type			|N	|交易类型,1,车找货,2货找车|
|login_name		|N	|车方登录名|
|create_time	|	|匹配时间，默认当前时间|
|status			|	|状态，type=1时，默认20，type=2时默认为30,999为已成交|

说明:

	已成交的cargos(cargos.status==999)不能添加交易记录
	添加cargo_logs记录时要同步置cargos.status = cargo_logs.status
	
	根据login_name在drivers表中找到plate_number
	
返回数据:
>
	{
		"result":1
	}
  
#### 7. <label id="cargolog_list">匹配记录列表</label>

|方法名称|/cargos/logs|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|:--------|
|carog_id		|	|货单id|
|title			|	|货单标题,cargos.title|
|type			|	|交易类型,1,车找货,2货找车|
|login_name		|	|车方登录名|
|plate_number	|	|车牌号，drivers.plate_number|
|status			|	|状态|
|create_time_from|	|创建时间|
|create_time_to	|	|创建时间|

说明：

	查询时要连接drivers, drivers.plate_number={plate_number}
	
返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":[
			{
				"cargo_id":123,
				"type":1,
				"plate_number":"浙A12345"，//drivers.plate_number
				"login_name":"13812345678",
				"status":20,
				"create_time":"2015-03-02 12:30:00"
			},
			{
				...
			},
			...
		]
	}


#### 8. <label id="cargolog_cfm">确认匹配</label>

|方法名称|/cargos/logs/:id/confirm|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:----|:--------|:--------|
|type	|	|类型,1,车方确认,2货方确认|

说明:

	1.根据cargo_logs.id找到对应的货单，
	2.已完成的cargos不能确认
		if (cargos.status==999){
			return error
		}
	3.只有交易双方可以确认，从token中找到当前操作用户login_name
		if (cargologs.login_name!=login_name || cargos.login_name!=login_name){
			return error;
		}
	
	4.根据cargo_logs.status 与传入的type判断后续的status
	
		if(cargo_logs.status==10){
			if(type==1){
				status = 20
			}else if(type==2){
				status = 30;
			}
		}else if(cargo_logs.status==20){//车方已经确认
			if(type==2){//货方确认
				status=999 //完成
			}
		}else if(cargo_logs.status==30){//货方已经确认
			if(type==1){//车方确认
				status=999 //完成
			}
		}
		读取cargo_logs的信息，修改状态为status,create_time 为当前时间 ，新加一条匹配记录
	4. 同步修改cargos.status ＝ status
	
	5.如果新加的记录status==999，还要生成一条交易记录 trades
	
返回数据:
>
	{
		"result":1
	}
