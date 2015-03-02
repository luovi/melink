define ['jquery'], ($) ->
  'use strict'
  defaults =
    debug: false
    round: 0
    encodeURI: false
  dataType = 'jsonp'
  convertUrl = 'http://api.map.baidu.com/geoconv/v1/'
  convertUrlParm = '?from=1&to=5&output=json&ak=EF6aee05088be9901b4b11fcab5da1d1'
  convertLimit = 20
  cache = true
  
  # 分割请求
  slicePoins = (points) ->
    return [points] if (len = points.length) <= 20
    results = []
    num = Math.ceil(len / convertLimit)
    cur = 0
    while (cur != num)
      min = cur * convertLimit
      cur++ # 先加1再算 max
      max = cur * convertLimit
      results.push points.slice min, max
    #console?.debug 'slicePoins: points results length and num', points.length, results.length, num
    results
  
  # 生成 url 参数
  generatePointsParam = (items, options) ->
    # console.log(items)
    listA = []
    listB = []
    for item in items.models
      { longitude, latitude } = item
      # 取最小精度
      if !!options.round
        longitude = parseFloat(longitude).toFixed(options.round)
        latitude = parseFloat(latitude).toFixed(options.round)
      listA.push longitude
      listB.push latitude
    x = listA.join ','
    y = listB.join ','
    # 编码 url , => %2C
    if options.encodeURI
      x = encodeURIComponent x
      y = encodeURIComponent y
    "&coords=#{x},#{y}"
  
  convertPromise = (points, options = {}) ->
    defer = $.Deferred()
    return defer.reject() unless points.length
    options = $.extend defaults, options
  
    # 所有请求按20个一组进行分割
    requests = slicePoins(points).map (items) ->
      url = convertUrl + convertUrlParm + generatePointsParam(items, options)
      $.ajax {url, dataType, cache}
  
    # 合并并发送所有转换请求
    # 所有请求完成后处理结果
    $.when(requests...).then (responses...)->
      results = []
      #console.log(responses)
      # 只有一个请求时候套一层
      if requests.length is 1 and responses.length is 3
        responses = [responses]
      console?.debug +new Date, 'all responses done.', responses if options.debug
  
      # 提取 response 里面的数据合并进 results
      responses.forEach (response) ->
          results = results.concat response[0]
  
      # 原有结果数组长度与 转换结果长度不相等时 warn
      if results.length isnt points.length
        console?.error +new Date, 'results.length isnt points.length',
          results.length, points.length
  
      # console.log(points)
      # console.log(results)
      # 替换原有数据
      for point, index in points
        { x, y } = results[index]
        unless (x and y)
          console?.error 'unless x, y?', results[index], point
          continue
        point.longitude = x
        point.latitude = y
  
      # 返回替换好的原数据
      defer.resolve points
  
    defer.promise()
