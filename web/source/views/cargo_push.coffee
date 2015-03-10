define [
    'backbone',
    'store',
    'modal'
    '../collections/cars'
    'text!templates/modules/crumb.html'
    'text!templates/pages/cargo_push.html'
    'text!templates/lists/car_list.html'
], (Backbone, Store, Modal, CarCtrl, tmp_crumb, tmp_push, tmp_car_list) ->
    'use strict'
           
    IndexView = Backbone.View.extend
        initialize: ->
            @subViews ?= []
            @options = 
                data:{page_no:1}
            @cars = new CarCtrl
            @cars.fetch @fetchOptions(@options, true)
            @$el.append @spinner().el
            @render()

            @listenTo @cars, 'sync', @renderList


        render: ->
            $container = $('<div class="container"></div>')
            $container
                .append(@template(tmp_crumb, {urls:[],current:'推送货源'}))
                .append(@template(tmp_push, {}))
            @$el.html($container)
            
        renderList: ->
            $('.J_car_list').empty().append @template(tmp_car_list, {cars:@cars})
            @createPage @cars, {target: $('.J_cars_box')}
            
        remove: ->
            @_super('remove')
