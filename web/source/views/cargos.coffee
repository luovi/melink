define [
    'backbone',
    'store',
    'dropMenu'
    'modal'
    '../collections/cargo'
    'text!templates/modules/crumb.html'
    'text!templates/pages/cargos.html'
    'text!templates/lists/cargos.html'
], (Backbone, Store, DropMenu, Modal, CargosCtrl, tmp_crumb, tmp_cargos, tmp_cargos_list) ->
    'use strict'
           
    IndexView = Backbone.View.extend
        initialize: ->
            @subViews ?= []
            @options = 
                data:{page_no:1}
            @cargos = new CargosCtrl
            @cargos.fetch @fetchOptions(@options, true)
            @$el.append @spinner().el
            @render()

            @listenTo @cargos, 'sync', @renderList


        render: ->
            $container = $('<div class="container"></div>')
            $container
                .append(@template(tmp_crumb, {urls:[],current:''}))
                .append(@template(tmp_cargos, {}))
            @$el.html($container)
            
        renderList: ->
            $('.J_cargo_list').empty().append @template(tmp_cargos_list, {cargos:@cargos})
            @dropMenu = new DropMenu(el: @$('table'), name: 'table', target: '.drop-down-link')
            @createPage @cargos, {target: $('.J_cargos_box')}
            
        remove: ->
            @_super('remove')
