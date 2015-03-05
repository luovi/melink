#### 车货匹配记录 /cargologs

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
  1|[/cargologs](#add_cargolog)		|POST |新增车货匹配记录
  2|[/cargologs](#cargolog_list)	|GET |匹配记录列表
  3|[/cargologs/:id/confirm](#cargolog_cfm)|POST |确认  
  
#### 1. <label id="add_cargolog">新增车货匹配记录</label>

|方法名称|/cargologs|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|carog_id		|N	|货单id|
|type			|N	|交易类型,1,车找货,2货找车|
|login_name		|N	|车方登录名|
|plate_number	|N	|车牌号|
|create_time	|	|匹配时间，默认当前时间|
|status			|	|状态，type=1时，默认20，type=2时默认为30|

说明:

	已成交的cargos(cargos.status==99)不能添加交易记录
	添加cargo_logs记录时要同步置cargos.status = cargo_logs.status
返回数据:
>
	{
		"result":1
	}
  
#### 2. <label id="cargolog_list">匹配记录列表</label>

|方法名称|/cargologs|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|
|carog_id		|	|货单id|
|type			|	|交易类型,1,车找货,2货找车|
|login_name		|	|车方登录名|
|plate_number	|	|车牌号|
|status			|	|状态|

返回数据:
>
	{
		"result":1,
		"data":[
			{
				"cargo_id":123,
				"title":"货源标题，cargos.title",
				"type":1,
				"plate_number":"浙A12345"，
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


#### 3. <label id="cargolog_cfm">确认匹配</label>

|方法名称|/cargologs/:id/confirm|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|type			|	|类型,1,车方确认,2货方确认|

说明:

	1.根据cargo_logs.id找到对应的货单，
	2.已完成的cargos不能确认
		if (cargos.status==99){
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
				status=99 //完成
			}
		}else if(cargo_logs.status==30){//货方已经确认
			if(type==1){//车方确认
				status=99 //完成
			}
		}
		读取cargo_logs的信息，修改状态为status,create_time 为当前时间 ，新加一条匹配记录
	5.如果新加的记录status==99，还要生成一条交易记录 trades
	
返回数据:
>
	{
		"result":1
	}
