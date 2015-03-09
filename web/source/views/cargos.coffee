define [
    'backbone',
    'store',
    'dropMenu'
    'text!templates/pages/cargos.html'
], (Backbone, Store, DropMenu, tmp_cargos) ->
    'use strict'
           
    IndexView = Backbone.View.extend
        initialize: ->
            @render()

        enableTooltip: ->
            self = @
            elements = $('.ellipsis')
            elements.each (target) ->
                $this = $(this)
                if self.checkOverflow($this)
                    txt = $.trim($this.text())
                    $this.addClass("tips").attr("tip",txt)

        render: ->
            self = @
            $container = $('<div class="container"></div>')
            $container
                .append(@template(tmp_cargos, {}))
            @$el.html($container)
            @dropMenu = new DropMenu(el: @$('table'), name: 'table', target: '.drop-down-link')
            # @createPage @cargos, {target: $container}
            @enableTooltip()
            timeout = setTimeout(->
                #self.tooltips({length:12})
                self.tooltips({position:'follow'})
            ,0)
            
        remove: ->
            @_super('remove')
