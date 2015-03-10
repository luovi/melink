define [
    'backbone',
    'store',
    'modal'
    '../collections/cars'
    'cars_filter'
    'text!templates/modules/crumb.html'
    'text!templates/pages/cargo_push.html'
    'text!templates/lists/car_list.html'
], (Backbone, Store, Modal, CarCtrl, carsFilter, tmp_crumb, tmp_push, tmp_car_list) ->
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

        events:
            'submit form': 'search'

        search: _.debounce (event) ->
            $target = $(event.target)
            _.extend @options.data, q:@$keyword.val()
            @cars.fetch @fetchOptions(@options, true)
        , 800, true

        render: ->
            $container = $('<div class="container"></div>')
            $container
                .append(@template(tmp_crumb, {urls:[],current:'推送货源'}))
                .append(@template(tmp_push, {}))
            @$el.html($container)
            @$keyword = @$('#J_qsearchvalue', @$el)
            console.log @carsfilter
            @carsfilter = new carsFilter @options, self.cars
            
        renderList: ->
            $('.J_car_list').empty().append @template(tmp_car_list, {cars:@cars})
            @createPage @cars, {target: $('.J_cars_box')}
            
        remove: ->
            @_super('remove')
