### 车辆 /cars

本应用没有车辆信息，套接口的数据全部从melink上获取，

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
1|[/cars](#car_list)					|GET|车辆列表
2|[/cars/:plate_number](#carinfo)	|GET|车辆详情
3|[/cars/status](#set_status)		|PUT |设置车辆状态
4|[/cars/status](#get_status)		|GET |获取车辆状态