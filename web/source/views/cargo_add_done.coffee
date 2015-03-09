            
'use strict'
define [
    'backbone'
    'models/cargo'
    'text!templates/pages/cargo_add_done.html'
    'text!templates/modules/crumb.html'
], (Backbone, cargoModel, tmp_done, tmp_crumb) ->
    
    CargoAddDoneView = Backbone.View.extend

        initialize: (opts={})->

            @render()
        render: ->
            self = @
            $container = $('<div class="container"></div>')
            $container.append(@template(tmp_crumb, {urls:[{url:'cargos',name:'货源信息'}], current:'发布成功'}))
            $container.append(@template(tmp_done, {}))
            @$el.html($container)
            








