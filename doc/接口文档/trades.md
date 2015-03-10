### 交易订单 /trades

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
 1|[/trades](#add_trade)			|POST 	|生成交易
 2|[/trades](#trade_list)			|GET 	|交易列表
 3|[/trades/:id](#view_trade)		|GET 	|交易详情
 4|[/trades/:id/cancel](#trades_cancel)	|DELETE|取消订单信息
 5|[/trades/logs](#trade_log)		|POST	|添加交易记录
 6|[/trades/logs](#add_trade_log)	|GET	|获取指定交易的交易记录 
 

#### 1. <label id="add_trade">生成交易订单</label>

|方法名称|/trades|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:------|:--------|:--------|
|cargo_id		|N	|货单id|
|login_name		|N	|车方登录名,货方的信息在货单中已经存在|
|trade_time		|	|交易达成时间,默认为当前服务器时间|
|carriage		|	|运费，默认为0|
|pre_pay		|	|预付款,默认为0|
|pay_type		|	|支付方式,0未知(默认),1月结,2提付,3到付,4回单付,999其它|
|status			|	|状态,状态,0取消,10待装货(默认),80支付,90评价,999完成|
|memo			|	|备注|

说明:

	根据login_name查找到车方的plate_number写入数据库
	
返回数据:
>
	{
		"result":1
	}
 
#### 2. <label id="trade_list">交易订单列表</label>

|方法名称|/trades|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:------|:--------|:--------|
|cargo_id		|	|货单id|
|login_name		|	|车方登录名,货方的信息在货单中已经存在|
|trade_time_from|	|交易达成时间,默认为当前服务器时间|
|trade_time_to	|	|交易达成时间,默认为当前服务器时间|
|carriage_from	|	|运费|
|carriage_to	|	|运费|
|status			|	|状态,默认为10|
|pay_type		|	|支付方式|

说明:

	排序：cargo_id desc,trade_time desc,status desc

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
				"login_name":"13812345678",
				"plate_number":"浙A12345",
				"trade_time":"2015-03-02 12:30:00",
				"carriage":150.00,
				"status":10
			},
			{
				...
			},
			...
		]
	}
 
#### 3. <label id="view_trade">交易订单详情</label>

|方法名称|/trades:id|
|:----|:--------|
|请求方式|GET|

返回数据:
>
	{
		"result":1,
		"data":{
				"cargo_id":123,
				"login_name":"13812345678",
				"plate_number":"浙A12345",
				"trade_time":"2015-03-02 12:30:00",
				"carriage":150.00
				"pre_pay":0,
				"pay_type":0,
				"status":10,
				"memo":""
			}
	}

#### 4. <label id="trades_cancel">取消交易订单</label>

|方法名称|/trades:id/cancel|
|:----|:--------|
|请求方式|POST|

说明:
	
	确认交易只有未启运的交易订单(status=10,)可以取消
	只有交易双方可以进行取消操作
	
返回数据:
>
	{
		"result":1
	}
#### 5. <label id="add_trade_log">交易状态变更</label>

|方法名称|/trades/logs|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:------|:--------|:--------|
|trade_id		|N	|订单id|
|login_name		|	|变更用户|
|type			|	|变更类型,默认为0|
|change_time	|	|变更时间,默认为服务器当前时间|
|status			|N	|变更状态|
|memo			|	|备注，变动原因|

说明:

	变量要同时修改 trades.status	
	
返回数据:
>
	{
		"result":1
	}
		
#### 6. <label id="trade_log">交易状态变更记录</label>

|方法名称|/trades/logs|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:------|:--------|:--------|
|trade_id		|	|订单id|
|cargo_id		|	|货单id|
|login_name		|	|变更用户|
|type			|	|变更类型|
	
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
				"trade_id":345,
				"type":"0",//变更类型,1为车方发起,2为货方发起,0为系统自动变更
				"login_name":"13812345678",//当type=1时,为车方,type=2时为货方,type=0时,此值为空
				"status":20,
				"change_time":"2015-03-02 12:30:00"
				"memo":"变动原因"
			},
			{
				...
			}
			...
		]
	}
	