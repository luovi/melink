# 旧 V3 dropMenu
# 支持顶部下拉菜单，以及表格里操作列下拉菜单
# 相关样式调整后则弃用
#
# 测试栗子：
# $('#header').dropMenu({name: 'header', activeClassName: 'nav-btn-active'})
# $._data($('#header')[0]).events
# $._data($(document)[0]).events
# $('#header').data('dropMenu').release()
#
# $('table').dropMenu({name: 'table', target: '.drop-down-link'})
# $._data($('table'[0])).events
# $._data($(document)[0]).events
# $('table').data('dropMenu').release()

define ['jquery'], ($) ->
  class DropMenu
    defaults:
      target: '.nav-btn'
      menus: '.drop-menu'
      namespaces: 'dropMenu'
      activeClassName: 'active'
      foot_h: 50 
      tip_h: 8


    $: (selector) -> $(selector, @$el)

    constructor: (options) ->
      options = $.extend {}, @defaults, options
      {@el, @target, @menus, @name, @namespaces, @activeClassName, @foot_h, @tip_h} = options
      throw new Error '错误，请输入 el, name' if not @el or not @name
      @$el = $(@el)
      @$target = @$(@target)
      @$menus = @$(@menus)
      @bindEvents()

    bindEvents: ->
      event = ['click', @name, @namespaces].join('.')
      @$el.on event, @target, @show
      $(document).on event, @hide

    show: (e) =>
      e.stopPropagation()
      
      if @name is 'header'
        
        @$target.not(
          $this = $(e.currentTarget).toggleClass(@activeClassName)
        ).removeClass(@activeClassName)

        @$menus.not($this.next().toggle()).hide()
      else
        menu = $(e.currentTarget).next().toggleClass(@activeClassName).css('top': 24 )
        menu.parent('.dropDown_box').css('zIndex','100')
        $('.arrow-bg',menu).css('top', -14)

        otherMenus = @$menus.not(
          menu.toggle()
        ).hide()

        otherMenus.parent('.dropDown_box').css('zIndex','50')

        ey = e.clientY
        cy = $(window).height()
        mh = menu[0].offsetHeight
        dh = @foot_h + @tip_h
        if mh + dh > cy - ey 
          menu.addClass('pos-top').css('top', parseInt(menu.css('top')) - (mh + @tip_h + 20) )
          $('.arrow-bg',menu).css('top', mh - @tip_h)
        else
          menu.removeClass('pos-top')

    hide: =>
      if @name is 'header'
        @$menus.hide() and @$target.removeClass(@activeClassName)
      else
        menu = @$menus.filter(".#{@activeClassName}")
        menu.removeClass(@activeClassName).hide()
        menu.parent('.dropDown_box').css('zIndex','50')


    release: ->
      event = if !!@name then ".#{@name}.#{@namespaces}" else ".#{@namespaces}"
      @$el.off(event)
      $(document).off(event)
      (data = @$el.data()) and delete data.dropMenu

  DropMenu
