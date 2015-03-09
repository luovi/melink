'use strict'
define [
    'backbone'
    '/component/models/cargo/cargo_orbits.js'
], (Backbone, orbitsModel) ->

    CargoModel = Backbone.Model.extend
        # validation:
        #     "short_title": "short_title"
        #     "city_origin": "origin"
        #     "city_destination": "origin"
        #     "carrier_contact_person": "value_length"
        #     "delivery_contact_number": 'contact_number'
        #     "take_delivery_contact_number": 'contact_number'
        #     "carrier_contact_number": 'contact_number'
        #     "loading_contact": 'telephone'
        #     "unloading_contact": 'telephone'
        #     "delivery_name": 'short_title'
        #     "take_delivery_name": 'short_title'
        #     "carrier_name": 'short_title'
        #     "delivery_contact_person": 'short_title'
        #     "take_delivery_contact_person": 'short_title'
        # _status:
        #     100: '已正式'
        #     200: '已发布'
        #     300: '已接单'
        #     400: '已转包'
        #     500: '已启运'
        #     600: '已转运'
        #     800: '已收货'
        
        # getStatusName: (_status) ->
        #     @_status[_status]
        
        # getFlagIndex: (flagName) ->
        #     item = _.find  @get('item').locate_methods, (item) ->
        #         if flagName in item
        #             item
        #     _.indexOf @get('item').locate_methods, item
            
        # parse: (response) ->
        #     self = @
        #     if not response.item
        #         response['status_name'] = self.getStatusName response.status
        #     if response.item
        #         item = response.item
        #         item['status_name'] = self.getStatusName item.status
        #         if item.user_id == @_current_user().id or item.carrier_user_id == @_current_user().id
        #             self.has_right = true
        #         if item.user_id == @_current_user().id
        #             self.is_owner = true
        #     self.set(item:item)
        #     if response.item then response.item else response

        
        
        initialize: (opts={}) ->
            self = @
            opts.uid ?= @_current_user().id
            @urlRoot = "/api/cargos/#{ opts.uid }"
        
        # requires:["short_title", "city_origin","city_destination"]
        
       
        # valueLength:
        #     "carrier_contact_person":
        #         "max": 20
