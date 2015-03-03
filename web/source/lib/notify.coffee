define ['jquery'], ($) ->
    'use strict'

    class Notify

        defaults:
            position: 'top-right'
            speed: 'fast'
            delay: 5000
            debug: true
        
        constructor: (options) ->
            self = @
            @settings = $.extend {}, @defaults, options
            @cached ?= []
            @init()

        init: ->
            self = @
            @queue = $('<div class="notify-queue"></div>')
            @queue.addClass @settings.position
            @counter = $('<div class="notify-counter" style=""></div>')
            @counter.addClass @settings.position
            @showCounter()
            @bindCounterEvent()
            # clear before create
            if @el
                @el.remove()
            # create
            @el = $('<div id="notify" style=""></div>')
            @tools = $('<div class="notify-tools">清除全部</div>')
            @el.append(@counter).append(@queue)
            @el.prependTo 'body'

        bindToolsEvent: ->
            self = @
            @tools.on 'click', ->
                self.clear()
                self.tools.remove()

        bindCounterEvent: ->
            self = @
            @counter.on 'click', ->
                self.showMessage(self.tools, self.queue)
                self.bindToolsEvent()
                $.each self.cached.slice(0,9), (i, message) ->
                    self.showMessage(message)
                    self.bindCloseEvent(message)

        bindCloseEvent: (message) ->
            self = @
            $('.notify-close', message).on 'click', ->
                self.cached.pop(message)
                message.remove()
                self.showCounter()

        showCounter: ->
            length = if @cached.length < 10 then @cached.length else '9+'
            @counter.html length
            if length is 0 then @counter.hide() else @counter.show()

        showMessage: (message, target) ->
            self = @
            #message.slideDown(@settings.speed).delay(@settings.delay).slideUp(@settings.speed)
            @queue.prepend message
            message.slideDown(@settings.speed)
            clearTimeout(message.timeout)
            @timer(message)

            target ?= message
            target.off()

            target.on 'mouseenter', ->
                clearTimeout(message.timeout)
            target.on 'mouseleave', ->
                self.timer(message)

        timer: (message) ->
            self = @
            message.timeout = setTimeout ->
                message.slideUp(self.settings.speed, ->
                    message.remove()
                )
            , self.settings.delay

        show: (msg, status) ->
            self = @
            message = $("""
            <div class="notify border-#{ @settings.position } notify-#{ status }">
                <img src="/static/v/v2/images/close.png" class="notify-close" title="Close">
                <div class="notify-note">#{ msg }</div>
            </div>
            """)
            @cached.push message
            #message.status = status
            @showCounter()
            @showMessage(message)
            @bindCloseEvent(message)
            
            if @settings.debug
                console?.log @cached

        clear: ->
            self = @
            if @cached.length > 0
                $.each @cached, (i, message) ->
                    message.remove()
            @cached = []
            @showCounter()

        remove: ->
            @el.remove()
            
        release: ->
            @clear()
            @remove()

