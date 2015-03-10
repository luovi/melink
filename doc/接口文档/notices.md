### 通知公告 /notices

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
 1|[/notices](#add_notices)			|POST 	|添加公告
 2|[/notices](#list_notices)		|GET 	|公告列表
 3|[/notices/:id](#update_notices)	|PUT	|修改公告
 4|[/notices/:id](#del_notices)		|DELETE	|删除公告
 
 
#### 1. <label id="add_notices">添加公告</label>

|方法名称|/notices|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:------|:--------|:--------|
|login_name		|N	|创建人登录名|
|title			|N	|标题|
|create_time	|	|创建时间，默认当前时间|
|expired_time	|	|过期时间，空为永不过期|
|content		|	|内容|
|status			|	|0草稿，1正常|

说明:
	
	添加通知公告为后台管理员功能
	token.role==999才能操作，否则要报权限不足
	
返回数据:
>
	{
		"result":1
	}
 
#### 2. <label id="list_notices">公告列表</label>

|方法名称|/notices|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:------|:--------|:--------|
|title			|	|标题|
|content		|	|内容|

说明：
	
	访问此接口时判断 token.role
	role < 999 时，指定status=1
	非管理员，只允许读取正常发布的公告


返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":[
			{
				"title":"标题",
				"content":"通知公告内容",
				"create_time":"创建时间",
				"expired_time":"过期时间",
				"status":1
			},
			{
				...
			},
			...
		]
	}
 
#### 3. <label id="update_comment">修改公告</label>

|方法名称|/notices:id|
|:----|:--------|
|请求方式|PUT|

|参数名称|可空|说明|
|:------|:--------|:--------|
|title		|	|标题|
|content	|	|内容|
|status		|	|状态|

说明：

	只有管理员才可以修改公告
	token.role==999
	
返回数据:
>
	{
		"result":1
	}
	
#### 4. <label id="del_comment">删除公告</label>

|方法名称|/notices:id|
|:----|:--------|
|请求方式|DELETE|

说明：

	只有管理员才可以删除公告
	role==999

返回数据:
>
	{
		"result":1
	}