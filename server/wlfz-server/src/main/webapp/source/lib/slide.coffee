# 旧v3 登录页首页轮播
# 如果不用 clearinterval 不知道是否会内存溢出
# $.remove + $.cleanData 默认会做这些事情，但是还是查看 devTools 之后再下结论
# TODO
# - [ ] 改为递归 setTimeout
# - [ ] 清除方法 根据 setTimeout ID 来清除
#
  #wait: (duration, fn)->
    #if "function" is typeof duration
      #fn = duration
      #duration = 0
    #return setTimeout fn, duration

  #killWait:(id)->
    #clearTimeout id if id
    #return null
require ['jquery'], ($) ->
  $.fn.autoSlide = ->
    container = $('>div', @)
    cell = container.find('div')
    cellWidth = @width()
    cellNum = cell.length
    container.css(width: cellWidth * cellNum)
    slide = ->
      current = $('div:first', container)
      current.animate 'margin-left': -cellWidth, 3000, ->
        current.appendTo(container).css 'margin-left': ''
    interval = setInterval(slide, 4000)
    interval
