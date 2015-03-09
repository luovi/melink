'use strict'
define [
    'backbone'
    '/component/models/cargo/cargo_orbits.js'
], (Backbone, orbitsModel) ->

    CargoModel = Backbone.Model.extend
        validation:
            "title": "short_title"
            "origin_city": "origin"
            "destin_city": "origin"
            "receiver_name": "value_length"
            "receiver_phone": 'contact_number'
            "sender_phone": 'contact_number'
            "delivery_name": 'short_title'
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
            
        parse: (response) ->
            self = @
            # if not response.item
                # response['status_name'] = self.getStatusName response.status
            if response.item
                item = response.item
                # item['status_name'] = self.getStatusName item.status
                # if item.user_id == @_current_user().id or item.carrier_user_id == @_current_user().id
                #     self.has_right = true
                # if item.user_id == @_current_user().id
                #     self.is_owner = true
            self.set(item:item)
            if response.item then response.item else response

        
        
        initialize: (opts={}) ->
            self = @
            opts.uid ?= @_current_user().id
            @urlRoot = "/api/cargos/#{ opts.uid }"
        
        requires:["login_name", "title","type"]
        
       
        # valueLength:
        #     "carrier_contact_person":
        #         "max": 20
