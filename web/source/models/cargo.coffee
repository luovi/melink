'use strict'
define [
    'backbone'
    # 'models/cargo_orbits.js'
], (Backbone) ->

    CargoModel = Backbone.Model.extend
        validation:
            "title": "short_title"
            "origin_city": "origin"
            "destin_city": "origin"
            "receiver_name": "value_length"
            "receiver_phone": 'contact_number'
            "sender_phone": 'contact_number'
            "delivery_name": 'short_title'
        # 状态,货单状态,0已取消,10未成交(默认),20待成交(车找货),30待成交(货找车),99已成交
        _status:
            0: '已取消'
            10: '已成交'
            20: '待成交'
            30: '待成交'
            99: '已成交'
        
        getStatusName: (_status) ->
            @_status[_status]
        
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
        
        requires:["title","origin_city","destin_city"]
        
       
        # valueLength:
        #     "carrier_contact_person":
        #         "max": 20
