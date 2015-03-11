(function() {
  'use strict';
  define(['backbone'], function(Backbone) {
    var CargoModel;
    return CargoModel = Backbone.Model.extend({
      validation: {
        "title": "short_title",
        "origin_city": "origin",
        "destin_city": "origin",
        "receiver_name": "value_length",
        "receiver_phone": 'contact_number',
        "sender_phone": 'contact_number',
        "delivery_name": 'short_title'
      },
      _status: {
        0: '已取消',
        10: '已成交',
        20: '待成交',
        30: '待成交',
        99: '已成交'
      },
      getStatusName: function(_status) {
        return this._status[_status];
      },
      parse: function(response) {
        var item, self;
        self = this;
        if (response.item) {
          item = response.item;
        }
        self.set({
          item: item
        });
        if (response.item) {
          return response.item;
        } else {
          return response;
        }
      },
      initialize: function(opts) {
        var self;
        if (opts == null) {
          opts = {};
        }
        self = this;
        if (opts.uid == null) {
          opts.uid = this._current_user().id;
        }
        return this.urlRoot = "/api/cargos/" + opts.uid;
      },
      requires: ["title", "origin_city", "destin_city"]
    });
  });

}).call(this);
