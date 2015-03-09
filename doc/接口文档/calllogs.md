### 通话记录 /calllogs

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
 1|[/calllogs](#add_call)			|POST 	|添加通话记录
 2|[/calllogs](#call_list)			|GET 	|通话记录列表
 
	
#### 1. <label id="add_call">添加通话记录</label>

|方法名称|/calllogs/:id|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|type			|N	|通话类型,1,车找货,2货找车,默认1|
|login_name	|N	|拔出用户登录名|
|to_login_name	|N	|拔入用户登录名|
|cargo_id		|N	|货单id|
|hold_time		|N	|通话时长,单位为秒|
|end_time		|	|通话结束时间,默认为当前时间|
|result			|	|通话结果,0未达成交易,1达成交易,默认为0|

说明:
	
	result==0时,只添加通话记录,
	当result==1 && cargos.status!=99 需要生成或者修改交易记录
	规则 如下:
		当type==1 && result==1 && cargos.status!=99 时
			根据cargos_id对应的货单,生成一条交易记录 trades,状态为20
			
		当type==2 && result==1 && cargos.status!=99
			根据cargos_id对应的货单生成一条交易记录,状态为30

	
	考虑到客户端的时间可能与服务端时间不一致,所以不采用传入开始时间,直接由服务器当前时间作为结束时间,减去通话时长可以得到开始时间.
	end_time参数的功能为某些特殊需求(如假设数据)预留
	
返回数据:
>
	{
		"result":1
	}
	
### 2. <lable id="call_list">通话记录列表</label>

|方法名称|/calllogs|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:---------|
|type			|	|通话类型,1,车找货,2货找车,默认1|
|login_name	|	|拔出用户登录名|
|to_login_name	|	|拔入用户登录名|
|cargo_id		|	|货单id|
|result			|	|通话结果,0未达成交易,1达成交易,默认为0|

返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":{
			"type":1,
			"login_name":13812345678,
			"to_login_name":"13087654321",
			"cargo_id":123,
			"start_time":"2015-03-02 12:30",
			"end_time":"2015-03-02 12:31:30",
			"hold_time":90,
			"result":0
		}
	}