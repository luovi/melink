### 消息推送 /message

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
1|[/message](#push)				|POST|推送信息
2|[/message](#list)				|GET |获取推送信息列表

#### 1. <label id="push">推送短信</label>

|方法名称|/message/sms|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:------|:--------|:--------|
|category		|N	|消息分类，1 sms,2 weixin,3 app|
|type			|	|999系统,100车,200货,300交易,0广告,默认999|
|login_name		|	|发起用户|
|content		|	|信息内容|

说明:

	根据login_name查找到车方的plate_number写入数据库
	
返回数据:
>
	{
		"result":1
	}
	
#### 2. <label id="list">推送信息列表</label>

|方法名称|/message|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:------|:--------|:--------|
|category		|	|消息分类，1 sms,2 weixin,3 app|
|type			|	|999系统,100车,200货,300交易,0广告,默认999|
|login_name		|	|发起用户|
|content		|	|信息内容,like|

说明:

	排序：messages.send_time desc

返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":[
			{
				"category":1,
				"type":99,
				"plate_number":"浙A12345",
				"send_time":"2015-03-02 12:30:00",
				"login_name":"1381234567",
				"content":"信息内容"
			},
			{
				...
			},
			...
		]
	}
